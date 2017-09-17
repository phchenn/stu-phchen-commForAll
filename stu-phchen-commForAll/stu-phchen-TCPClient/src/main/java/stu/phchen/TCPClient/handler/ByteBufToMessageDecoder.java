package stu.phchen.TCPClient.handler;

import java.util.List;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import stu.phchen.TCPClient.constant.MessageContent;
import stu.phchen.TCPClient.model.ServerMessage;

/**
 * JSON解码器，负责解码通知报文，当遇到心跳包时，返回一个心跳包回去
 * @author phchen
 *
 */
public class ByteBufToMessageDecoder extends MessageToMessageDecoder<ByteBuf>{
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        System.out.println("收到一帧报文，开始解码......");
        int readerIndex = msg.readerIndex();
        int readableBytes = msg.readableBytes();
        byte type = msg.getByte(readerIndex);
        if(!MessageContent.SERVER_CLIENT.contains(type)){
            ctx.close();
        }else{
            ServerMessage message=null;
            if(readableBytes>1){
                byte[] content=new byte[readableBytes-5];
                msg.getBytes(readerIndex+5,content);
                message=JSON.parseObject(content, ServerMessage.class);
            }else if(readableBytes==1){
                //心跳
                System.out.println("收到心跳报文");
                byte heartbeat=MessageContent.HEARTBEAT;
                ctx.writeAndFlush(Unpooled.wrappedBuffer(new byte[]{heartbeat}));
                return;
            }else{
                message=new ServerMessage();
            }
            message.setType(type);
            out.add(message);
        }
    }
}