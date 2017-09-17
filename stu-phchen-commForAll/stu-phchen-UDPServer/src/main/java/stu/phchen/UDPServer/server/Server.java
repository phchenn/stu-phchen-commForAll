package stu.phchen.UDPServer.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import stu.phchen.UDPServer.handler.UdpServerHandler;

public class Server {
    public static void initServer(int port){
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .handler(new UdpServerHandler());
            Channel channel = bootstrap.bind(port).sync().channel();
            channel.closeFuture().await();
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            group.shutdownGracefully();
        }
    }
    
    public static void main(String[] args){
        Server.initServer(8800);
    }
}