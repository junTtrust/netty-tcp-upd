package org.atm.dc.app;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.atm.dc.app.udp.UdpServer2;
import org.atm.dc.app.udp.UdpServer3;
import org.atm.dc.app.util.FileUtil;

public class UdpListener3 implements ServletContextListener {

	private Thread thread;

	// 线程销毁
	public void contextDestroyed(ServletContextEvent sce) {
		if (thread != null && thread.isInterrupted()) {
			thread.interrupt();
		}
	}

	// 线程启动
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext app= sce.getServletContext();
	    String port = (String) sce.getServletContext().getInitParameter("port");
	    String endsymbol = (String) sce.getServletContext().getInitParameter("endsymbol");
	    String filepath = (String) sce.getServletContext().getInitParameter("filepath");
	    String ipfilepath = (String) sce.getServletContext().getInitParameter("ipfilepath");
	    Map<String,String> map = new HashMap<String, String>();
	    map.put("port", port);
	    map.put("endsymbol", endsymbol);
	    map.put("filepath", filepath);
	    System.out.println("========================"+port);
	    app.setAttribute("readAccessIps", readAccessIps(ipfilepath));
		if (thread == null) {
			UdpServer3 receiveSyslog = new UdpServer3(map,app);
			thread = new Thread(receiveSyslog);
			thread.start();
		}
	}
	public Map<String,String> readAccessIps(String filepath){
		 Object obj =  FileUtil.readObjectFromFile(filepath);
		 Map<String,String>  map = (HashMap<String,String>)obj;
		 return map;
	}
	

}
