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
package org.postgresql.dummy;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Filip Hrbek
 */
public class ResultSetTest {
	public static Iterator<String> executeSelect(String selectSQL) throws SQLException {
		if (!selectSQL.toUpperCase().trim().startsWith("SELECT ")) {
			throw new SQLException("Not a SELECT statement");
		}

		return new ResultSetTest(selectSQL).iterator();
	}

	private ArrayList<String> m_results;
	private long serial=0;

	public ResultSetTest(String selectSQL) throws SQLException {
		Connection conn = DriverManager
				.getConnection("jdbc:default:connection");
		m_results = new ArrayList<String>();
		StringBuffer result;

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(selectSQL);
		ResultSetMetaData rsmd = rs.getMetaData();

		int cnt = rsmd.getColumnCount();
		result = new StringBuffer();
		for (int i = 1; i <= cnt; i++) {
			result.append((rsmd.getColumnName(i) + "("
					+ rsmd.getColumnClassName(i) + ")").replaceAll("(\\\\|;)",
					"\\$1") + ";;");
		}
		m_results.add(result.toString());

		while (rs.next()) {
			serial++;
			result = new StringBuffer();
			Object rsObject = null;
			for (int i = 1; i <= cnt; i++) {
				rsObject = rs.getObject(i);
				if (rsObject == null) {
					rsObject = "<NULL>";
				}
				result.append(rsObject.toString()
						.replaceAll("(\\\\|;)", "\\$1") + ";");
			}
			if(serial % 100000==0){
				System.out.println("debug "+result.toString());
			}
			m_results.add(result.toString());
		}
		rs.close();
	}

	public void close() {
	}

	private Iterator<String> iterator() {
		return m_results.iterator();
	}
}
