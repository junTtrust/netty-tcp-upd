package org.atm.dc.app.tcpssl.tcpclient.handler;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import org.atm.dc.app.tcpssl.tcpclient.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;

public class NettyClientHandler extends ChannelHandlerAdapter {

//	private ByteBuf firstMessage;//不粘包
	private NettyClient client;
	private Logger logger = LoggerFactory.getLogger(getClass());

	private int count = 0;


	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		/**
		 * 不粘包处理
		 */
		/*
		 * byte[] data = "链接服务器成功".getBytes();
		 * 
		 * firstMessage = Unpooled.buffer(); firstMessage.writeBytes(data);
		 * 
		 * ctx.writeAndFlush(firstMessage);
		 * System.out.println("---------------像服务器发送消息---------------");
		 */

		/**
		 * 粘包处理
		 */
		String msg = "hello from client我是客户端 " ;
		logger.info("client send message:{}", msg);
		for (int i = 0; i < 10; i++) {
			ctx.writeAndFlush(msg + System.getProperty("line.separator"));
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		
		/**
		 * 不粘包处理
		 */
		/*
		try {

			System.out.println("--------------ctx---------------" + ctx);
			ByteBuf buf = (ByteBuf) msg;
			String rev = getMessage(buf);
			System.out.println("客户端收到服务器数据:" + rev);

		} catch (Exception e) {
			System.out.println("-------------斷綫了-----------------");
			e.printStackTrace();
		}
	*/
		/**
		 * 粘包处理
		 */
		String body = (String) msg;
		System.out.println("----------------收到数据----------------------"+body);
        count++;
        logger.info("client read msg:{}, count:{}", body, count);
        
		}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {

		final EventLoop eventLoop = ctx.channel().eventLoop();
		
		System.out.println("-----------------------MyInboundHandler------------------");
		eventLoop.schedule(new Runnable() {

			@Override

			public void run() {

				System.out.println("----------------MyInboundHandler链接服务器------------------");
				client.createBootstrap(new Bootstrap(), eventLoop);
			}

		}, 1L, TimeUnit.SECONDS);
		
		super.channelInactive(ctx);

	}
	

	private String getMessage(ByteBuf buf) {

		byte[] con = new byte[buf.readableBytes()];
		buf.readBytes(con);
		try {
			return new String(con, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}