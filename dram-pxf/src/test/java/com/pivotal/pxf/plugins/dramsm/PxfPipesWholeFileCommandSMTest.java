package com.pivotal.pxf.plugins.dramsm;

import com.pivotal.pxf.PxfUnit;
import com.pivotal.pxf.api.Fragmenter;
import com.pivotal.pxf.api.ReadAccessor;
import com.pivotal.pxf.api.ReadResolver;
import com.pivotal.pxf.api.io.DataType;
import com.pivotal.pxf.api.utilities.InputData;
import org.apache.hadoop.fs.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class PxfPipesWholeFileCommandSMTest extends PxfUnit {

	private static final Logger LOG = Logger.getLogger(PxfPipesWholeFileCommandSMTest.class.getName());

	private static List<Pair<String, DataType>> columnDefs = null;
	private static List<Pair<String, String>> extraParams = new ArrayList<Pair<String, String>>();

	static {
		columnDefs = new ArrayList<Pair<String, DataType>>();
		columnDefs.add(new Pair<String, DataType>("file", DataType.TEXT));
		columnDefs.add(new Pair<String, DataType>("serial", DataType.BIGINT));
		columnDefs.add(new Pair<String, DataType>("result", DataType.TEXT));
	}

	@Before
	public void setup() {
	}

	@After
	public void cleanup() {
		extraParams.clear();
	}


	@Test
	public void testDram() throws Exception {

		setup(new Path(System.getProperty("user.dir")
				+ "/" + "src/test/resources/dramdata/rawdata.txt.sample.0"));

		for (InputData data : inputs) {
			ReadAccessor accessor = getReadAccessor(data);
			ReadResolver resolver = getReadResolver(data);

			getAllOutput(accessor, resolver);
		}

	}



	@Override
	public List<Pair<String, String>> getExtraParams() {
		return extraParams;
	}

	@Override
	public Class<? extends Fragmenter> getFragmenterClass() {
		return com.pivotal.pxf.plugins.dramsm.WholeFileFragmenter.class;
	}

	@Override
	public Class<? extends ReadAccessor> getReadAccessorClass() {
		return BlobAccessor.class;
	}

	@Override
	public Class<? extends ReadResolver> getReadResolverClass() {
		return BlobResolver.class;
	}

	@Override
	public List<Pair<String, DataType>> getColumnDefinitions() {
		return columnDefs;
	}
}
