package org.atm.dc.app.tcpclient.handler;

import java.util.concurrent.TimeUnit;

import org.atm.dc.app.tcpclient.NettyClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyInboundHandler extends SimpleChannelInboundHandler {  
	   private NettyClient client;  
//	   private static Bootstrap bootstrap = new Bootstrap() ;
	   public MyInboundHandler(NettyClient client) {  
	     this.client = client;  
	   }  
	   @Override 
	   public void channelInactive(ChannelHandlerContext ctx) throws Exception {  
	     final EventLoop eventLoop = ctx.channel().eventLoop();  
	     eventLoop.schedule(new Runnable() {  
	       @Override 
	       public void run() {  
	    	   System.out.println("----------------MyInboundHandler链接服务器------------------");
//	         client.createBootstrap(bootstrap, eventLoop); 
	         client.createBootstrap(new Bootstrap(), eventLoop); 
	       }  
	     }, 1L, TimeUnit.SECONDS);  
	     super.channelInactive(ctx);  
	   }
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
	}  
	 }