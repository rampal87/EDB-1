package com.acc.tools.ed.server.context;

import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.webapp.WebAppContext;


public class AppContextBuilder {
	


	public WebAppContext buildWebAppContext(){
		final WebAppContext webAppContext = new WebAppContext();
		webAppContext.setContextPath("/edb");
        ProtectionDomain domain = AppContextBuilder.class.getProtectionDomain();
        URL location = domain.getCodeSource().getLocation();
       	String path=location.getPath();
        if(path==null)
        	throw new RuntimeException("EDB War file not loaded properly!");
        else
           	path=path.substring(1, path.lastIndexOf("/"))+"/edb.war";        	
        
		webAppContext.setWar("C:\\Users\\murali\\git\\edboard\\com.acc.tools.ed\\com.acc.tools.ed.web\\target\\com.acc.tools.ed.web-0.0.1-SNAPSHOT.war");

		return webAppContext;
	}

}
