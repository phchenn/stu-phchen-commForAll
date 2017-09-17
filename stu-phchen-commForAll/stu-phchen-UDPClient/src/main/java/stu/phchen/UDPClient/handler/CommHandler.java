package stu.phchen.UDPClient.handler;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import stu.phchen.UDPClient.CallBack.CommCallBack;
public class CommHandler extends SimpleChannelInboundHandler<DatagramPacket>{
    private CommCallBack call;
    private ExecutorService threadPool;
    public CommHandler(CommCallBack call, ExecutorService threadPool){
        super();
        this.call=call;
        this.threadPool=threadPool;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        ByteBuf buf=msg.content();
        byte[] data=new byte[buf.readableBytes()];
        buf.getBytes(0,data);
        String res=new String(data, "UTF-8");
        if(res.contains("give:")){//这条是服务器发的
            System.out.println("[clientCommHandler] get msg:"+res);
            res=res.substring(5);
            String[] list=res.split("\\|");
            final String dstAddress=list[0];
            final int dstPort=Integer.valueOf(list[1]);
            System.out.println("[clientCommHandler] address:"+dstAddress+" port:"+dstPort);
            threadPool.execute(new Runnable(){
                public void run() {
                    call.comm(dstAddress, dstPort);
                }
            });
            //这一条是请求对方的视频流
            System.out.println("send request for requireing opposite side video");
            String mes="start";
            ByteBuf request=Unpooled.copiedBuffer(mes.getBytes());
            ctx.channel().writeAndFlush(new DatagramPacket(request, new InetSocketAddress(dstAddress, dstPort)));
        }else if(res.equals("start")){//这条是视频发起者发的
            final String address=msg.sender().getAddress().getHostAddress();
            final int port=msg.sender().getPort();
            threadPool.execute(new Runnable(){
                public void run() {
                    System.out.println(address+":::"+port);
                    call.comm(address, port);
                }
            });
        }else{
            msg.retain();
            ctx.fireChannelRead(msg);
        }
    }
}