package org.atm.dc.app.tcpclient;

import org.atm.dc.app.tcpclient.handler.ConnectionListener;
import org.atm.dc.app.tcpclient.handler.MyInboundHandler;
import org.atm.dc.app.tcpclient.handler.NettyClientHandler;
import org.springframework.context.annotation.Configuration;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

@Configuration
public class NettyClient {

	private EventLoopGroup loop = new NioEventLoopGroup();

	//@Value("${tcp.port}")
	private int port=9999;
	static Bootstrap bootstrap = new Bootstrap();
	//@Value("${tcp.ip}")
	private String host="192.168.3.55";
//	private String host="192.168.1.163";
	
	public static void main(String[] args)

	{

		while(true){
				new NettyClient().run();
				bootstrap=null;
		}

	}

	public Bootstrap createBootstrap(Bootstrap bootstrap, EventLoopGroup eventLoop) {

		if (bootstrap != null) {

			final NettyClientHandler nehandler = new NettyClientHandler();
			final MyInboundHandler myhandler = new MyInboundHandler(this);

			bootstrap.group(eventLoop);

			bootstrap.channel(NioSocketChannel.class);

			bootstrap.option(ChannelOption.SO_KEEPALIVE, true);//长连接
	        bootstrap.option(ChannelOption.TCP_NODELAY, true);  //不延迟，消息立即发送  
			bootstrap.handler(new ChannelInitializer<SocketChannel>() {

				@Override

				protected void initChannel(SocketChannel socketChannel) throws Exception {

					
					
					//粘包处理
					socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
					socketChannel.pipeline().addLast(new StringDecoder());
					socketChannel.pipeline().addLast(new StringEncoder());
					//handler处理
					socketChannel.pipeline().addLast(nehandler);
					socketChannel.pipeline().addLast(myhandler);
					

				}

			});

			bootstrap.remoteAddress(host, port);
			
			ChannelFuture addListener = bootstrap.connect().addListener(new ConnectionListener(this));
			  // 关闭连接  
			try {
				addListener.channel().closeFuture().sync();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}

		return bootstrap;

	}

	public void run() {

//		createBootstrap(new Bootstrap(), loop);
		createBootstrap(bootstrap, loop);

	}
}