package com.pivotal.pxf.plugins.dramsm;

import com.gopivotal.mapred.input.CombineWholeFileInputFormat;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;
import pivotal.io.batch.state.StateMachine;
import pivotal.io.batch.command.Command;
import pivotal.io.batch.command.CommandUndefined;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * A {@link FileInputFormat} implementation that passes the file name as the key
 * and the bytes of the file as the value. Generates one map task per file, but
 * the {@link CombineWholeFileInputFormat} could be used to batch them together
 * into a configurable number of map tasks.
 */
public class BlobFileInputFormatStateMachine extends FileInputFormat<Text, BytesWritable> {

	private static final Logger LOG = Logger.getLogger(BlobFileInputFormatStateMachine.class.getName());

	@Override
	protected boolean isSplitable(FileSystem fs, Path filename) {
		return false;
	}

	@Override
	public RecordReader<Text, BytesWritable> getRecordReader(InputSplit split,
															 JobConf conf, Reporter reporter) throws IOException {
		return new WholeFileRecordReader(split, conf);
	}

	public static class WholeFileRecordReader implements RecordReader<Text, BytesWritable> {

		private boolean readComplete = false;
		private FileSystem fs = null;
		private FileSplit fSplit = null;
		private String filename=null;
		FSDataInputStream inStream=null;
		BufferedInputStream buffStream =null;

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
			buffStream= new BufferedInputStream(inStream);
			//buffStream= new BufferedInputStream(fs.open(fSplit.getPath()));

			filename = fSplit.getPath().getName();

		}

		private StateMachine sm = new StateMachine();
		private String prevHex="";
		private String bigHex="";
		private String bits="";
		private String parsed="";
		private long serial=0;
		byte[] bufferFinal = new byte[4];

		Command command;
		Command undefinedCmd= CommandUndefined.getInstance();
		boolean isTransit=false;

		@Override
		public boolean next(Text key, BytesWritable value) throws IOException {

			if (readComplete) {
				LOG.info("BlobFileInputFormatStateMachine finish! ");
				return false;
			}

			long lStartTime = System.currentTimeMillis();

			key.set(filename);
			try {


				String result=null;

				while (buffStream.read(bufferFinal) >= 0) {
					serial++;

					if (serial % 10000000==0) {
						LOG.info(String.format("%s, %s", filename, serial));
					}

					if(sm.isIgnoreCommand(bufferFinal)){
						continue;
					}
					isTransit = sm.transit(bufferFinal);
					bits = Command.byteToBits(bufferFinal);
					bigHex = Command.bytesToHex(bufferFinal);
					parsed = Command.parse(bufferFinal);
					command= sm.getTrialValueHolder().triedCommand;

					if(undefinedCmd.equals(command)){
						result = String.format("%10s, %s, %s, %s, %s\n", serial, sm, sm.findStateCommand(bufferFinal).getName(), bits, parsed);
					}else {
						result = String.format("%10s, %s, %s, %s, %s\n", serial, sm, isTransit, bits, parsed );
					}
					value.set(result.getBytes(), 0, result.getBytes().length);
					return true;
				}

				readComplete = true;
				long lEndTime = System.currentTimeMillis() - lStartTime;
				LOG.info(String.format("COMPLETE %s, items: %s, elapsed:%s", filename, serial, GetFormattedInterval(lEndTime)));

				return false;

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}


		public String GetFormattedInterval(final long ms) {
			long millis = ms % 1000;
			long x = ms / 1000;
			long seconds = x % 60;
			x /= 60;
			long minutes = x % 60;
			x /= 60;
			long hours = x % 24;

			return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
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

