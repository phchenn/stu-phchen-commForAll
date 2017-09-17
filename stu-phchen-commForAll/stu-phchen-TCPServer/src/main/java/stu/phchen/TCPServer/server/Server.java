package stu.phchen.TCPServer.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import stu.phchen.TCPServer.constant.Constant;
import stu.phchen.TCPServer.handler.ByteBufToMessageDecoder;
import stu.phchen.TCPServer.handler.CertificationHandler;
import stu.phchen.TCPServer.handler.CommunicationHandler;
import stu.phchen.TCPServer.handler.HeartbeatHandler;
import stu.phchen.TCPServer.handler.LengthFieldAndHeartbeatDecoder;
import stu.phchen.TCPServer.handler.MessageToByteBufEncoder;

public class Server {
    private ServerBootstrap serverBootstrap ;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private int tcpPort;
    
    private Server(int tcpPort){
        this.tcpPort=tcpPort;
        serverBootstrap=new ServerBootstrap();
        bossGroup=new NioEventLoopGroup();
        workerGroup=new NioEventLoopGroup();
    }
    
    public void initServer(){
        serverBootstrap.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .childHandler(new ChannelInitializer<SocketChannel>(){
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new IdleStateHandler(Constant.readerIdleTimeSeconds, Constant.writerIdleTimeSeconds, Constant.allIdleTimeSeconds));
                ch.pipeline().addLast(new HeartbeatHandler());
                ch.pipeline().addLast(new LengthFieldAndHeartbeatDecoder());
                ch.pipeline().addLast(new ByteBufToMessageDecoder());
                ch.pipeline().addLast(new CertificationHandler());
                ch.pipeline().addLast(new CommunicationHandler());
                ch.pipeline().addLast(new MessageToByteBufEncoder());
            }
        });
        try{
            ChannelFuture channelFuture = serverBootstrap.bind(tcpPort).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    
    public void shutdown(){
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
    
    public static void main(String[] args){
        Server server=new Server(8888);
        server.initServer();
    }
}