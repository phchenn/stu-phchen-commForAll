package stu.phchen.TCPClient.handler;

import com.alibaba.fastjson.JSON;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import stu.phchen.TCPClient.constant.MessageContent;
import stu.phchen.TCPClient.model.CertificationBo;
import stu.phchen.TCPClient.model.ClientPushMessage;

/**
 * 当建立起连接，发送验证报文
 * @author phchen
 *
 */
public class CertificationHandler extends ChannelInboundHandlerAdapter{
    String name;
    
    public CertificationHandler(String name){
        super();
        this.name=name;
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("tcp连接已建立，开始发送验证报文......");
        CertificationBo bo=new CertificationBo();
        bo.setName(name);
        ClientPushMessage clientPushMessage=new ClientPushMessage();
        clientPushMessage.setType(MessageContent.CERTIFICATION);
        clientPushMessage.setData(JSON.toJSONBytes(bo));
        ctx.channel().writeAndFlush(clientPushMessage);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        System.out.println("收到异常，关闭连接");
        //不能用ctx.close();
        ctx.channel().close();        
    }
}
