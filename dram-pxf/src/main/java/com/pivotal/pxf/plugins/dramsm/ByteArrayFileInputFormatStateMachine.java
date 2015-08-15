package com.pivotal.pxf.plugins.dramsm;

import com.gopivotal.mapred.input.CombineWholeFileInputFormat;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import pivotal.io.batch.StateMachine;
import pivotal.io.batch.domain.StateCommand;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * A {@link FileInputFormat} implementation that passes the file name as the key
 * and the bytes of the file as the value. Generates one map task per file, but
 * the {@link CombineWholeFileInputFormat} could be used to batch them together
 * into a configurable number of map tasks.
 */
public class ByteArrayFileInputFormatStateMachine extends FileInputFormat<Text, BytesWritable> {

	private static final Logger LOG = Logger.getLogger(ByteArrayFileInputFormatStateMachine.class.getName());

	@Override
	protected boolean isSplitable(FileSystem fs, Path filename) {
		return false;
	}

	@Override
	public RecordReader<Text, BytesWritable> getRecordReader(InputSplit split,
															 JobConf conf, Reporter reporter) throws IOException {
		return new WholeFileRecordReader(split, conf);
	}

	public static class WholeFileRecordReader implements
			RecordReader<Text, BytesWritable> {

		private boolean readComplete = false;
		private FileSystem fs = null;
		private FileSplit fSplit = null;
		private String filename=null;
		FSDataInputStream inStream=null;


		public WholeFileRecordReader(InputSplit split, JobConf conf)
				throws IOException {
			readComplete = false;

			fSplit = (FileSplit) split;

			if (fSplit.getLength() > Integer.MAX_VALUE) {
				throw new IOException("Size of file is larger than max integer");
			}

			fs = FileSystem.get(conf);

			// get the bytes of the file for the value
			inStream = fs.open(fSplit.getPath());
			filename = fSplit.getPath().getName();

		}

		StateMachine sm = new StateMachine();

		String prevHex="";
		String bigHex="";
		String bits="";
		String parsed="";
		private long itemSerial=0;

		@Override
		public boolean next(Text key, BytesWritable value) throws IOException {

			if (readComplete) {
				LOG.info("ByteArrayFileInputFormatStateMachine finish! ");
				return false;
			}
			key.set(filename);
			try {

				byte[] buffer = new byte[4];

				while (inStream.read(buffer) >= 0) {
					itemSerial++;

					if (itemSerial % 100000==0) {
						LOG.info(String.format("%s, %s", filename, itemSerial));
					}
					String result = process(buffer);
					if (result == null) {
						continue;
					}
//					value=new BytesWritable(result.getBytes());
					value.set(result.getBytes(), 0, result.getBytes().length);
//					LOG.info("format: "+new String(value.getBytes()));
					return true;
				}
				readComplete = true;
				return false;

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}
		private String process(byte[] buf){

			byte[] buffer = Utils.getBigEndian(buf);
			bigHex = StateCommand.bytesToHex(buffer);
			bits = StateCommand.byteToBits(buffer);
			parsed = StateCommand.parse(buffer);

			if(prevHex.equals(bigHex)){ // prevent dup
				return null;
			}
			prevHex=bigHex;
			boolean isTransit = sm.transit(buffer);

//			return String.format("%30s, %5s, %10s, %15s, %s, %s", sm, isTransit, itemSerial, sm.getStateCommandType(buffer), bits, parsed);
			return String.format("%s, %s, %s, %s, %s", sm, isTransit, itemSerial, bits, parsed);
		}

		@Override
		public void close() throws IOException {
			if(inStream!=null){
				inStream.close();
			}
		}

		@Override
		public float getProgress() throws IOException {
			return readComplete ? 1 : 0;
		}

		@Override
		public Text createKey() {
			return new Text();
		}

		@Override
		public BytesWritable createValue() {
			return new BytesWritable();
		}

		@Override
		public long getPos() throws IOException {
			return 0;
		}
	}
}

