package stu.phchen.TCPClient.handler;

import java.util.concurrent.ExecutorService;

import com.alibaba.fastjson.JSON;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import stu.phchen.TCPClient.callback.Callback;
import stu.phchen.TCPClient.constant.MessageContent;
import stu.phchen.TCPClient.model.ServerMessage;
import stu.phchen.TCPClient.model.ToMessageBo;

public class CommunicationHandler extends SimpleChannelInboundHandler<ServerMessage>{
    ExecutorService threadPool;
    Callback callback;
    public CommunicationHandler(Callback callback, ExecutorService threadPool){
        this.callback=callback;
        this.threadPool=threadPool;
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ServerMessage msg) throws Exception {
        if(msg.getType()==MessageContent.TO_COMMUNICATION){
            ToMessageBo bo=JSON.parseObject(msg.getData(), ToMessageBo.class);
            final String name=bo.getName();
            final String content=bo.getMsg();
            System.out.println("receive msg from"+name+" content is:"+content);
            threadPool.execute(new Runnable(){
                public void run(){
                    callback.dealMessage(name, content);
                }
            });
        }else{
            ctx.fireChannelRead(msg);
        }
    }
}