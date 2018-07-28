package org.atm.dc.app.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.Scanner;

import org.atm.dc.app.udp.handler.UdpServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpServer {

	/**
	 * 日志
	 */
	private Logger logger = LoggerFactory.getLogger(UdpServer.class);

	public void run(int port) {
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventLoopGroup).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, true)
					.handler(new UdpServerHandler());
			// bootstrap.bind(port).sync().channel().closeFuture().await();

			Channel channel = bootstrap.bind(0).sync().channel();
			
			StringBuffer buffer = new StringBuffer();
//			for(int i = 0 ; i < 10 ; i++) {
			for(int i = 0 ; i < 10000 ; i++) {
				buffer.append("a");
			}
			
			buffer.append("------");
			
			System.out.println(buffer.length());
			// 向网段内的所有机器广播
			while (true) {
				channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(buffer.toString(), CharsetUtil.UTF_8),
						new InetSocketAddress("255.255.255.255", port))).sync();
				
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		new UdpServer().run(17008);
	}
}