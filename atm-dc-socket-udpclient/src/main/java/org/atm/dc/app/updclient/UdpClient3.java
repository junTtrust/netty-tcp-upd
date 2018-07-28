package org.atm.dc.app.updclient;

import java.net.InetSocketAddress;

import org.atm.dc.app.updclient.handler.UdpClientHandler3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

public class UdpClient3 {

    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(UdpClient3.class);

    public void run(int port) {
    	 EventLoopGroup group = new NioEventLoopGroup();
         try {
             Bootstrap b = new Bootstrap();
             b.group(group)
                     .channel(NioDatagramChannel.class)
                     .option(ChannelOption.SO_BROADCAST, true)
                     .handler(new UdpClientHandler3());

             Channel ch = b.bind(0).sync().channel();

             ch.writeAndFlush(new DatagramPacket(
                     Unpooled.copiedBuffer("Searh:", CharsetUtil.UTF_8),
                     new InetSocketAddress("255.255.255.255", port))).sync();
             if (!ch.closeFuture().await(5000)) {
                 System.err.println("Search request timed out.");
             }
         }catch (Exception e){
             e.printStackTrace();
         }
         finally {
             group.shutdownGracefully();
         }
    }
    public static void main(String[] args) {
    	 new UdpClient3().run(10086);
	}
    
}