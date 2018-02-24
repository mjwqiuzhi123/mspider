package com.zouhx.crawler.main;

import java.io.File;
import java.lang.management.ManagementFactory;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;

import com.zouhx.crawler.task.SpiderAgentTask;


public class Application {

	public Application() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		try{
			//MccCfg.InitApp();
			//代理更新
			SpiderAgentTask.start();
			//startWebServer();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void startWebServer(){
		Server server = new Server(8081);
		// 设置 JMX
		MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
        server.addBean(mbContainer);
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        String path = new File("").getAbsolutePath()+"/src/main/resources/webapps/WEB-INF";
        webapp.setDescriptor(path+"/web.xml");    
        webapp.setResourceBase(path);
        Configuration.ClassList classlist = Configuration.ClassList
                .setServerDefault( server );
        classlist.addBefore(
                "org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
                "org.eclipse.jetty.annotations.AnnotationConfiguration" );
        webapp.setAttribute(
                "org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
                ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/[^/]*taglibs.*\\.jar$" );
        server.setHandler(webapp);
        try {
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
