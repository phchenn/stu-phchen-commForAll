package stu.phchen.TCPServer.handler;

import com.alibaba.fastjson.JSON;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import stu.phchen.TCPServer.constant.ConnectManager;
import stu.phchen.TCPServer.constant.MessageContent;
import stu.phchen.TCPServer.model.ClientMessage;
import stu.phchen.TCPServer.model.CommunicationBo;
import stu.phchen.TCPServer.model.ServerPushMessage;
import stu.phchen.TCPServer.model.ToMessageBo;

public class CommunicationHandler extends SimpleChannelInboundHandler<ClientMessage>{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClientMessage msg) throws Exception {
        if(msg.getType()==MessageContent.COMMUNICATION){
            CommunicationBo bo=JSON.parseObject(msg.getData(),CommunicationBo.class);
            Channel ch=ConnectManager.connectManager.get(bo.getOppisiteName());
            ToMessageBo data=new ToMessageBo(bo.getMyName(),bo.getMessageContent());
            ServerPushMessage serverPushMessage=new ServerPushMessage();
            serverPushMessage.setType(MessageContent.TO_COMMUNICATION);
            serverPushMessage.setData(JSON.toJSONBytes(data));
            ch.writeAndFlush(serverPushMessage);
        }else{
            ctx.fireChannelRead(msg);
        }
    }
}