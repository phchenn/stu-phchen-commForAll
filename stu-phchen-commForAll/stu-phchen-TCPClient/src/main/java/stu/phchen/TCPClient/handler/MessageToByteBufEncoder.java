package stu.phchen.TCPClient.handler;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import stu.phchen.TCPClient.model.ClientPushMessage;

public class MessageToByteBufEncoder extends MessageToByteEncoder<ClientPushMessage>{
    @Override
    protected void encode(ChannelHandlerContext ctx, ClientPushMessage msg, ByteBuf out) throws Exception {
        System.out.println("收到待发送的业务对象，开始编码成字节流......");
        out.writeByte(msg.getType()) ;
        byte[] dataBytes = JSON.toJSONBytes(msg);
        out.writeInt(dataBytes.length) ;
        out.writeBytes(dataBytes) ;
    }
}
