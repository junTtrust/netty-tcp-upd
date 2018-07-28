package org.atm.dc.app.tcp.handler;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class NettyServerHandler extends ChannelHandlerAdapter {

	 private Logger logger = LoggerFactory.getLogger(getClass());
	 
	private int counter;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {

		/**
		 * 不粘包处理
		 */
		/*ByteBuf buf = (ByteBuf) msg;

		String recieved = getMessage(buf);
		System.out.println("服务器接收到消息：" + recieved);*/

		/*try {
			System.out.println("tcp服务端输入数据");
			Scanner scan = new Scanner(System.in);
			while (true) {
				String read = scan.nextLine();
				ctx.writeAndFlush(getSendByteBuf("你好"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}*/
		
		/**
		 * 粘包处理
		 */
		counter++;
		String body = (String) msg;
		logger.info("服务器接收到消息server read msg:{}, count:{}", body, counter);
		
		for(int i=0;i<10;i++){
			String response = "hello from server"+System.getProperty("line.separator");
			ctx.writeAndFlush(response);
		}

	}

	/*
	 * 从ByteBuf中获取信息 使用UTF-8编码返回
	 */
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

	private ByteBuf getSendByteBuf(String message) throws UnsupportedEncodingException {

		byte[] req = message.getBytes("UTF-8");
		ByteBuf pingMessage = Unpooled.buffer();
		pingMessage.writeBytes(req);

		return pingMessage;
	}
}