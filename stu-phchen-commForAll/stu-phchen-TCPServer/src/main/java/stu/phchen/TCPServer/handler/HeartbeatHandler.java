package stu.phchen.TCPServer.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import stu.phchen.TCPServer.constant.MessageContent;

public class HeartbeatHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;  
            if (event.state() == IdleState.READER_IDLE) {
                System.out.println("readerIdal:====");
                ctx.close() ; //发现在是读空超时，直接关闭客户端
            } else if (event.state() == IdleState.WRITER_IDLE) {
                //发送失败则直接关闭客户端
                System.out.println("writerIdal:====");
                ctx.channel().writeAndFlush(Unpooled.copiedBuffer(new byte[]{MessageContent.HEARTBEAT}))
                .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}