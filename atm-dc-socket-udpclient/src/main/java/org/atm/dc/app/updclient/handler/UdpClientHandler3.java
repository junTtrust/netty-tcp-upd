package org.atm.dc.app.updclient.handler;

import java.io.FileWriter;
import java.io.IOException;

import org.atm.dc.app.util.EncrypDES;
import org.atm.dc.app.util.EncryptUtil;
import org.atm.dc.app.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class UdpClientHandler3 extends SimpleChannelInboundHandler<DatagramPacket> {
	StringBuilder sb=new StringBuilder();
	int count = 0 ;
    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(UdpClientHandler3.class);

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
    	String resp = msg.content().toString(CharsetUtil.UTF_8);
    	String keyEncryptionStr = FileUtil.getEncryptionKey(FileUtil.getLocalIp());
		EncryptUtil des = new EncryptUtil(keyEncryptionStr, "utf-8");
		EncrypDES des2 = new EncrypDES(keyEncryptionStr);
		String xmlEncryption = des2.decrypt(resp);
		String  xmlEncryptionresult = des.decode(xmlEncryption);
		System.out.println(xmlEncryptionresult);
		if(xmlEncryptionresult.startsWith("!!!@@@###")){
			count++;
			if(count%2==0){
				count=0;
				try {
					FileWriter fw=new FileWriter("D:/test.xml");
					fw.write(sb.toString());
					fw.close();
					sb = new StringBuilder();
					ctx.close();
					} catch (IOException e) {
					e.printStackTrace();
					}
			}
		}else{
				sb.append(xmlEncryptionresult);
		}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
        logger.error(e.getMessage(),e);
        ctx.close();
    }
}