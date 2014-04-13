package com.olympus.rest.sap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.naming.NamingException;
import javax.servlet.ServletContext;

import com.sap.mw.jco.JCO;

public class JCOClientCreator {

	private static JCOClientCreator instance = null;
	private ServletContext context = null;
	
	private JCOClientCreator(ServletContext context) {
		this.context = context;
	}
	
	public static synchronized JCOClientCreator getInstanceOf(ServletContext context) {
		if(instance == null)
			instance = new JCOClientCreator(context);
		return instance;
	}
	
	public JCO.Client createJCOClient() throws FileNotFoundException, IOException, NamingException {
		/*String user = spp.getUser();
		String password = spp.getPassword();
		String ip = spp.getIP();
		String mandant = spp.getMandant();
		String language = spp.getLanguage();
		String systemNumber = spp.getSystemNumber();*/
		HashMap<String, String> parameters = SAPProperties.getSAPParameters(this.context);
		
		return JCO.createClient(parameters.get("mandant"), parameters.get("user").toUpperCase(),
								parameters.get("password").toUpperCase(), parameters.get("language"),
								parameters.get("ip"), parameters.get("systemNumber"));
		//return JCO.createClient(mandant, user, password, language, ip, systemNumber);
	}

}
