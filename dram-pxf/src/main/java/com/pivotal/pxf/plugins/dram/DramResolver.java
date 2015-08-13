package com.pivotal.pxf.plugins.dram;

import com.pivotal.pxf.PxfUnit;
import com.pivotal.pxf.api.OneField;
import com.pivotal.pxf.api.OneRow;
import com.pivotal.pxf.api.ReadResolver;
import com.pivotal.pxf.api.io.DataType;
import com.pivotal.pxf.api.utilities.InputData;
import com.pivotal.pxf.api.utilities.Plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * This Redis resolver for PXF will decode a given object from the
 *  RedisHashAccessor into a row for HAWQ.
 * 
 * It will also write key value pairs to a specific hash.
 */
public class DramResolver extends Plugin implements ReadResolver {
	private static final Logger LOG = Logger.getLogger(DramResolver.class.getName());

	private ArrayList<OneField> fields = new ArrayList<OneField>();

	public DramResolver(InputData inputData) throws Exception {
		super(inputData);
	}

	@Override
	public List<OneField> getFields(OneRow paramOneRow) throws Exception {
		fields.clear();

		// filename
		addFieldFromString(
				DataType.valueOf(inputData.getColumn(0).columnTypeName().toUpperCase()),
				paramOneRow.getKey().toString());

		Pair<Long, byte[]> data = (Pair<Long, byte[]>)paramOneRow.getData();

		//serial
		addFieldFromString(
				DataType.valueOf(inputData.getColumn(1).columnTypeName().toUpperCase()), String.valueOf(data.first));

		// data byte[]
		addFieldFromString(
				DataType.valueOf(inputData.getColumn(2).columnTypeName().toUpperCase()), Utils.byteToBits(data.second));

		return fields;
	}



	private void addFieldFromString(DataType type, String val)
			throws IOException {
		OneField oneField = new OneField();
		oneField.type = type.getOID();


		if (val == null) {
			oneField.val = null;
		} else {
			switch (type) {
			case BIGINT:
				oneField.val = Long.parseLong(val);
				break;
			case BOOLEAN:
				oneField.val = Boolean.parseBoolean(val);
				break;
			case BPCHAR:
			case CHAR:
				oneField.val = val.charAt(0);
				break;
			case BYTEA:
				oneField.val = val.getBytes();
				break;
			case FLOAT8:
			case REAL:
				oneField.val = Double.parseDouble(val);
				break;
			case INTEGER:
			case SMALLINT:
				oneField.val = Integer.parseInt(val);
				break;
			case TEXT:
			case VARCHAR:
				oneField.val = val;
				break;
			default:
				throw new IOException("Unsupported type " + type);
			}
		}

		//LOG.info("oneField type:"+oneField.type);
		fields.add(oneField);
	}
}
