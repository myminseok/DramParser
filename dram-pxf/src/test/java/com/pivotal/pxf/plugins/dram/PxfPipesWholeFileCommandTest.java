package com.pivotal.pxf.plugins.dram;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.pivotal.pxf.api.utilities.InputData;
import com.pivotal.pxf.plugins.dramsm.BlobResolver;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.hadoop.fs.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.pivotal.pxf.PxfUnit;
import com.pivotal.pxf.api.Fragmenter;
import com.pivotal.pxf.api.ReadAccessor;
import com.pivotal.pxf.api.ReadResolver;
import com.pivotal.pxf.api.io.DataType;


public class PxfPipesWholeFileCommandTest extends PxfUnit {

	private static final Logger LOG = Logger.getLogger(PxfPipesWholeFileCommandTest.class.getName());

	private static List<Pair<String, DataType>> columnDefs = null;
	private static List<Pair<String, String>> extraParams = new ArrayList<Pair<String, String>>();

	static {
		columnDefs = new ArrayList<Pair<String, DataType>>();
		columnDefs.add(new Pair<String, DataType>("file", DataType.TEXT));
		columnDefs.add(new Pair<String, DataType>("serial", DataType.BIGINT));
		columnDefs.add(new Pair<String, DataType>("bits", DataType.BYTEA));
	}

	@Before
	public void setup() {
//		extraParams.add(new Pair<String, String>("MAPPER", System
//				.getProperty("user.dir")
//				+ "/src/test/resources/dram-mapper.py"));

//		extraParams.add(new Pair<String, String>("LINEBYLINE", "FALSE"));
	}

	@After
	public void cleanup() {
		extraParams.clear();
	}


	//@Test
	public void testDram() throws Exception {

		setup(new Path(System.getProperty("user.dir")
				+ File.separator + "src/test/resources/dramdata/rawdata.txt.sample.0"));

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
		return WholeFileFragmenter.class;
	}

	@Override
	public Class<? extends ReadAccessor> getReadAccessorClass() {
		return DramBlobAccessor.class;
	}

	@Override
	public Class<? extends ReadResolver> getReadResolverClass() {
		return DramResolver.class;
	}

	@Override
	public List<Pair<String, DataType>> getColumnDefinitions() {
		return columnDefs;
	}
}
