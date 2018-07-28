package org.atm.dc.app.updclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import org.atm.dc.app.updclient.handler.UdpClientHandler2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpClient2 {

    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(UdpClient2.class);

    public void run(int port) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new UdpClientHandler2());
            bootstrap.bind(port).sync().channel().closeFuture().await();


        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) {
    	 new UdpClient2().run(10086);
	}
}