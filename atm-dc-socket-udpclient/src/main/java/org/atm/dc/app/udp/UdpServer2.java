package org.atm.dc.app.udp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.InetSocketAddress;
import java.util.Map;

import org.atm.dc.app.udp.handler.UdpServerHandler;
import org.atm.dc.app.util.EncrypDES;
import org.atm.dc.app.util.EncryptUtil;
import org.atm.dc.app.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.util.CharsetUtil;

public class UdpServer2 extends Thread{
	/**
	 * 日志
	 */
	private Logger logger = LoggerFactory.getLogger(UdpServer2.class);
	private Map<String,String> map ;
	public void dorun(int port,String keyEncryptionStr,String endsymbol,int TimeIntervalBetweenTrainingSending,String filepath) {
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventLoopGroup).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, true)
					.handler(new UdpServerHandler());
			// bootstrap.bind(port).sync().channel().closeFuture().await();
			
			// ChannelPipeline p = socketChannel.pipeline(); 
             //粘包设置
            // p.addLast(new LineBasedFrameDecoder(1024));
			
			
//			bootstrap.setPipelineFactory(new SecureChatServerPipelineFactory());
			Channel channel = bootstrap.bind(0).sync().channel();
			
			EncryptUtil des = new EncryptUtil(keyEncryptionStr, "utf-8");
			EncrypDES des2 = new EncrypDES(keyEncryptionStr);
			
			// 向网段内的所有机器广播
			while (true) {
				String  start = des.encode(endsymbol+System.getProperty("line.separator"));
				String startEncrtyption = des2.encrypt(start);
				StringBuffer startbuffer = new StringBuffer();
				startbuffer.append(startEncrtyption);
			    channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(startbuffer.toString(), CharsetUtil.UTF_8),
					new InetSocketAddress("255.255.255.255", port))).sync();
				
				
				
				String line = null;
				BufferedReader bufferedReader  = new BufferedReader(new FileReader(filepath ));
					while ((line = bufferedReader.readLine()) != null) {
						line=line+System.getProperty("line.separator");
						String  xmlEncryption = des.encode(line);
						String buf = des2.encrypt(xmlEncryption);
						StringBuffer buffer = new StringBuffer();
						buffer.append(buf);
						channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(buffer.toString(), CharsetUtil.UTF_8),
								new InetSocketAddress("255.255.255.255", port))).sync();
					}
					String  end = des.encode(endsymbol+System.getProperty("line.separator"));
					String endEncrtyption = des2.encrypt(end);
					StringBuffer endbuffer = new StringBuffer();
					endbuffer.append(endEncrtyption);
				    channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(endbuffer.toString(), CharsetUtil.UTF_8),
						new InetSocketAddress("255.255.255.255", port))).sync();
				Thread.sleep(TimeIntervalBetweenTrainingSending);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}
	public static void main(String[] args) throws FileNotFoundException {
//		String ip = "127.0.0.1";
//		String keyEncryptionStr = FileUtil.getEncryptionKey(ip);
		//new UdpServer2().dorun(17008,keyEncryptionStr);
	}
	public  void execute() throws FileNotFoundException {
		String ip = "127.0.0.1";
		String keyEncryptionStr = FileUtil.getEncryptionKey(ip);
		String port = map.get("port");
		String endsymbol = map.get("endsymbol");
		String filepath = map.get("filepath");
		String  TimeIntervalBetweenTrainingSending = map.get("TimeIntervalBetweenTrainingSending");
		dorun(Integer.valueOf(port),keyEncryptionStr, endsymbol,Integer.valueOf(TimeIntervalBetweenTrainingSending),filepath);
	}
	 public void run() {  
		 try {
			execute();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 } 
	 public UdpServer2(Map<String,String> map ){
		 this.map=map;
	 }
}