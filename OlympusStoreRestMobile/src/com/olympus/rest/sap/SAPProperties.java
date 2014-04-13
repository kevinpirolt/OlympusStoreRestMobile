package com.olympus.rest.sap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

public class SAPProperties {
	
	public static HashMap<String,String> getSAPParameters(ServletContext context) throws NamingException {
		HashMap<String,String> parameters = new HashMap<String,String>();
		SAPProperties.fillParameters(parameters, context);
		return parameters;
	}

	private static void fillParameters(HashMap<String, String> parameters,
			ServletContext context) {
		parameters.put("user", context.getInitParameter("user"));
		parameters.put("password", context.getInitParameter("password"));
		parameters.put("ip", context.getInitParameter("ip"));
		parameters.put("mandant", context.getInitParameter("mandant"));
		parameters.put("language", context.getInitParameter("language"));
		parameters.put("systemNumber", context.getInitParameter("systemNumber"));
	}
	
}
