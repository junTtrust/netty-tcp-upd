package org.atm.dc.app.tcpssl.tcpclient.handler;

import java.util.concurrent.TimeUnit;

import org.atm.dc.app.tcpssl.tcpclient.NettyClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

public class ConnectionListener implements ChannelFutureListener {

	private NettyClient client;

	public ConnectionListener(NettyClient client) {

		this.client = client;

	}

	@Override

	public void operationComplete(ChannelFuture channelFuture) throws Exception {

		if (!channelFuture.isSuccess()) {

			System.out.println("------------Reconnect-----------------------");

			final EventLoop loop = channelFuture.channel().eventLoop();

			loop.schedule(new Runnable() {

				@Override

				public void run() {
					
					System.out.println("----------------ConnectionListener链接服务器------------------");
					client.createBootstrap(new Bootstrap(), loop);

				}

			}, 1L, TimeUnit.SECONDS);

		}

	}
}
