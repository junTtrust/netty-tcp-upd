package org.atm.dc.app.updclient.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(UdpClientHandler.class);

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        String resp = msg.content().toString(CharsetUtil.UTF_8);
        	System.out.println("----------收到服务端发送的消息-------------"+resp);
            logger.debug(resp);
            //接受到消息就关闭
            //ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
        logger.error(e.getMessage(),e);
        ctx.close();
    }
}