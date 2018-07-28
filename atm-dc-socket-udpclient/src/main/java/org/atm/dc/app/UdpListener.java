package org.atm.dc.app;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.atm.dc.app.udp.UdpServer2;

public class UdpListener implements ServletContextListener {

	private Thread thread;

	// 线程销毁
	public void contextDestroyed(ServletContextEvent sce) {
		if (thread != null && thread.isInterrupted()) {
			thread.interrupt();
		}
	}

	// 线程启动
	public void contextInitialized(ServletContextEvent sce) {
	    String port = (String) sce.getServletContext().getInitParameter("port");
	    String endsymbol = (String) sce.getServletContext().getInitParameter("endsymbol");
	    String TimeIntervalBetweenTrainingSending = (String) sce.getServletContext().getInitParameter("TimeIntervalBetweenTrainingSending");
	    String filepath = (String) sce.getServletContext().getInitParameter("filepath");
	    Map<String,String> map = new HashMap<String, String>();
	    map.put("port", port);
	    map.put("endsymbol", endsymbol);
	    map.put("filepath", filepath);
	    map.put("TimeIntervalBetweenTrainingSending", TimeIntervalBetweenTrainingSending);
	    System.out.println(port);
		if (thread == null) {
			UdpServer2 receiveSyslog = new UdpServer2(map);
			thread = new Thread(receiveSyslog);
			thread.start();
		}
	}

}
