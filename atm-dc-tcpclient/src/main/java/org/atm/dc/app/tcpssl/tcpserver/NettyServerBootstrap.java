package org.atm.dc.app.tcpssl.tcpserver;

import javax.net.ssl.SSLEngine;

import org.apache.log4j.Logger;
import org.atm.dc.app.config.ContextSSLFactory;
import org.atm.dc.app.tcpssl.tcpserver.handler.NettyServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslHandler; 

public class NettyServerBootstrap {
//	@Value("${tcp.port}")
//	private int port;
	 private static Logger logger = Logger.getLogger(NettyServerBootstrap.class);  
	  
	    private int port;  
	  
	    public NettyServerBootstrap(int port) {  
	        this.port = port;  
	        bind();  
	    }  
	  
	    private void bind() {  
	  
	        EventLoopGroup boss = new NioEventLoopGroup();  
	        EventLoopGroup worker = new NioEventLoopGroup();  
	  
	        try {  
	  
	            ServerBootstrap bootstrap = new ServerBootstrap();  
	  
	            bootstrap.group(boss, worker);  
	            bootstrap.channel(NioServerSocketChannel.class);  
	            bootstrap.option(ChannelOption.SO_BACKLOG, 1024); //连接数  
	            bootstrap.option(ChannelOption.TCP_NODELAY, true);  //不延迟，消息立即发送  
	            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); //长连接  
	            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {  
	                @Override  
	                protected void initChannel(SocketChannel socketChannel)  
	                        throws Exception {  
	                    ChannelPipeline p = socketChannel.pipeline(); 
	                    
	                    SSLEngine engine = ContextSSLFactory.getSslContext().createSSLEngine();  
	                    engine.setUseClientMode(false);  
	                    engine.setNeedClientAuth(true);  //验证客户端
	                    p.addFirst("ssl", new SslHandler(engine)); 
	                    
	                    //粘包设置
	                    p.addLast(new LineBasedFrameDecoder(1024));
	                    p.addLast(new StringDecoder());
	                    p.addLast(new StringEncoder());
	                    
	                    //handler处理
	                    p.addLast(new NettyServerHandler()); 
	                    
	                }  
	            });  
	            ChannelFuture f = bootstrap.bind(port).sync(); 
	            if (f.isSuccess()) {  
	                logger.debug("启动Netty服务成功，端口号：" + this.port);  
	            }  
	            // 关闭连接  
	            f.channel().closeFuture().sync();  
	  
	        } catch (Exception e) {  
	            logger.error("启动Netty服务异常，异常信息：" + e.getMessage());  
	            e.printStackTrace();  
	        } finally {  
	            boss.shutdownGracefully();  
	            worker.shutdownGracefully();  
	        }  
	    }  
	  
	public static void main(String[] args) throws InterruptedException {  
	          
	        NettyServerBootstrap server= new NettyServerBootstrap(9999);  
	  
	    }  
	  
	}  
