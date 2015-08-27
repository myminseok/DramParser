/*
 * Copyright (c) 2004-2013 Tada AB and other contributors, as listed below.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the The BSD 3-Clause License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 *
 * Contributors:
 *   Tada AB
 *   Filip Hrbek
 */
package org.postgresql.dram;

import pivotal.io.batch.state.StateMachine;
import pivotal.io.batch.command.Command;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * @author Filip Hrbek
 */
public class ResultSetStateMachine {

	private static Logger logger=Logger.getAnonymousLogger();

	int fetchSize=10000;

	public static Iterator<String> executeSelect(String selectSQL) throws SQLException {

		if (!selectSQL.toUpperCase().trim().startsWith("SELECT ")) {
			throw new SQLException("Not a SELECT statement");
		}

		return new ResultSetStateMachine(selectSQL).iterator();
	}

	private ArrayList<String> m_results;
	PreparedStatement stmt=null;

	public ResultSetStateMachine(String selectSQL) throws SQLException {

		m_results = new ArrayList<String>();

		Connection conn = DriverManager.getConnection("jdbc:default:connection");
		logger.info("autoCommit\t" + conn.getAutoCommit());
		logger.warning("fetchSize\t" + fetchSize);

		stmt = conn.prepareStatement(selectSQL, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		stmt.setFetchSize(fetchSize);
		ResultSet rs = stmt.executeQuery();


		StateMachine sm = new StateMachine();
		boolean isTransit = false;
		String prevTransit="";
		String prevNoTransit="";
		String bigHex="";
		String bits="";
		String parsed="";
		byte[] buffer;
		double currSerial;
		logger.warning("running\t" + selectSQL);
		while (rs.next()) {

			currSerial = (Double)rs.getObject(2);
			buffer=(byte[]) rs.getObject(3);

			if( (currSerial % 1000000)==0){
				logger.warning("serial: " + currSerial);
			}

			bigHex = Command.bytesToHex(buffer);
			bits = Command.byteToBits(buffer);
			parsed = Command.parse(buffer);

			String result;
			isTransit = sm.transit(buffer);
			if(isTransit){
				if(prevTransit.equals(bigHex)){ // prevent dup
					continue;
				}
				result = String.format("%5s, %10s, %30s, %s, %s\n", isTransit, currSerial, sm, bits, parsed);
				prevTransit=bigHex;

			}else{
				if(prevNoTransit.equals(bigHex)){// prevent dup
					continue;
				}
//				result = String.format("invalid %10s, %15s, %15s, %s, %s\n", serial, sm, sm.getStateCommandType(buffer), bits, parsed);
				result = String.format("%5s, %10s, %30s, %s, %s\n", isTransit, currSerial, sm, bits, parsed);
				prevNoTransit=bigHex;

			}

			m_results.add(result);

		}
		rs.close();
	}


	public void close() {
		if(stmt!=null) {
			try {
				stmt.close();
				logger.warning("stmt.close");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	private Iterator<String> iterator() {
		return m_results.iterator();
	}
}
