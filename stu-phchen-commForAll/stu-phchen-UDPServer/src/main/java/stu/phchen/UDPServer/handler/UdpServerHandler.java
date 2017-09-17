package stu.phchen.UDPServer.handler;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket>{
    private Map<String, InetSocketAddress> connection;
    
    public UdpServerHandler(){
        super();
        connection=new HashMap<String, InetSocketAddress>();
    }
    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg)throws Exception{
        ByteBuf buf=msg.content();
        byte[] data=new byte[buf.readableBytes()];
        buf.getBytes(0,data);
        String req=new String(data, "UTF-8");
        System.out.println("[server] receive msg: '"+req+"'");
        if(req.contains("ask:")){
            req=req.substring(4);
            System.out.println("[server] deal msg: "+req);
            InetSocketAddress tmp=connection.get(req);
            if(tmp==null){
                System.out.println("no exist this people!");
                return;
            }
            String addr=tmp.getAddress().getHostAddress();
            int port=tmp.getPort();
            String message="give:"+addr+"|"+port;
            ctx.channel().writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer(message.getBytes()),msg.sender()));
            //if(req.equals("big7")){
            //    System.out.println("[server] send msg for: "+req);
            //    ctx.channel().writeAndFlush(new DatagramPacket(
            //            Unpooled.copiedBuffer("give:172.22.60.25|10086".getBytes()),msg.sender()));
            //}
        }else if(req.contains("ip:")){
            req=req.substring(3);
            System.out.println("[server] add ip for "+req);
            connection.put(req, msg.sender());
        }
    }
}