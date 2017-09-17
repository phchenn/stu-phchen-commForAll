package stu.phchen.TCPServer.handler;

import java.util.List;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import stu.phchen.TCPServer.constant.MessageContent;
import stu.phchen.TCPServer.model.ClientMessage;

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
        if(!MessageContent.CLIENT_SERVER.contains(type)){
            ctx.close();
        }else{
            ClientMessage message=null;
            if(readableBytes>1){
                byte[] content=new byte[readableBytes-5];
                msg.getBytes(readerIndex+5,content);
                message=JSON.parseObject(content, ClientMessage.class);
            }else if(readableBytes==1){
                //心跳
                System.out.println("收到心跳报文");
                //byte heartbeat=MessageContent.HEARTBEAT;
                //ctx.writeAndFlush(Unpooled.wrappedBuffer(new byte[]{heartbeat}));
                return;
            }else{
                message=new ClientMessage();
            }
            message.setType(type);
            out.add(message);
        }
    }
}