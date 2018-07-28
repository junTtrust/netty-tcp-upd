package org.atm.dc.app.udp;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

import javax.servlet.ServletContext;

import org.atm.dc.app.util.EncrypDES;
import org.atm.dc.app.util.EncryptUtil;
import org.atm.dc.app.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

/**
 * Created by Lovell on 9/29/16.
 */
public class UdpServer3 extends Thread{
    private static Logger logger = LoggerFactory.getLogger(UdpServer3.class);
    private static Map<String,String> map ;
    private static ServletContext app;
    /**
     * udp服务端，接受客户端发送的广播
     */
    public static void initServer(int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new MyUdpServerHandler());
            Channel channel = bootstrap.bind(port).sync().channel();
            channel.closeFuture().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    private static class MyUdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
		@Override
		protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
			 // 因为Netty对UDP进行了封装，所以接收到的是DatagramPacket对象。
			String endsymbol = map.get("endsymbol");
			String filepath = map.get("filepath");
			sendMessage(ctx,filepath,msg,endsymbol);
		}
    }
    private static void sendMessage(ChannelHandlerContext ctx,String filepath,DatagramPacket msg,String endsymbol) throws Exception{
    	String clientIP = msg.sender().getAddress().getHostAddress();
    	if(!checkips(clientIP)){
    		ctx.close();
    		return ;
    	};
    	String keyEncryptionStr = FileUtil.getEncryptionKey(clientIP);
    	EncryptUtil des = new EncryptUtil(keyEncryptionStr, "utf-8");
		EncrypDES des2 = new EncrypDES(keyEncryptionStr);
		String  start = des.encode(endsymbol+System.getProperty("line.separator"));
		String startEncrtyption = des2.encrypt(start);
		ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(startEncrtyption, CharsetUtil.UTF_8), msg.sender()));
    	String line = null;
		BufferedReader bufferedReader  = new BufferedReader(new FileReader(filepath ));
		while ((line = bufferedReader.readLine()) != null) {
			line=line+System.getProperty("line.separator");
			String  lineEncryption = des.encode(line);
			String result = des2.encrypt(lineEncryption);
			ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(result, CharsetUtil.UTF_8), msg.sender()));
       }
		String  end = des.encode(endsymbol+System.getProperty("line.separator"));
		String endEncrtyption = des2.encrypt(end);
		ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(endEncrtyption, CharsetUtil.UTF_8), msg.sender()));
   }
    public static void main(String[] args) {
    	//UdpServer3.initServer();
	}
    public static boolean checkips(String ip){
    	ip=ip+"_access";
    	Map<String,String> maptempip = (Map<String, String>) app.getAttribute("readAccessIps");
    	return "true".equals(maptempip.get(ip)) ;
    }
	 public void run() {  
		 try {
				String port = map.get("port");
			 UdpServer3.initServer(Integer.valueOf(port));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 } 
	 public UdpServer3(Map<String,String> map,ServletContext app ){
		 this.app=app;
		 this.map=map;
	 }
}
