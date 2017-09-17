package stu.phchen.TCPClient.client;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSON;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import stu.phchen.TCPClient.callback.Callback;
import stu.phchen.TCPClient.constant.MessageContent;
import stu.phchen.TCPClient.handler.ByteBufToMessageDecoder;
import stu.phchen.TCPClient.handler.CertificationHandler;
import stu.phchen.TCPClient.handler.CommunicationHandler;
import stu.phchen.TCPClient.handler.LengthFieldAndHeartbeatDecoder;
import stu.phchen.TCPClient.handler.MessageToByteBufEncoder;
import stu.phchen.TCPClient.model.ClientPushMessage;
import stu.phchen.TCPClient.model.CommunicationBo;

public class NettyTCPClient {
    private Callback callback;
    private ExecutorService threadPool;
    private String userName;
    private EventLoopGroup group;
    ChannelFuture f;
    Channel ch;
    public NettyTCPClient(Callback callback, String userName){
        this.callback=callback;
        this.userName=userName;
        threadPool=Executors.newFixedThreadPool(5);
    }
    
    public void connect(String serverHost, int serverPort){
        group=new NioEventLoopGroup();
        try{
            Bootstrap b=new Bootstrap();
            b.group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY,true)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LengthFieldAndHeartbeatDecoder());
                    ch.pipeline().addLast(new ByteBufToMessageDecoder());
                    ch.pipeline().addLast(new CommunicationHandler(callback, threadPool));
                    ch.pipeline().addLast(new CertificationHandler(userName));
                    ch.pipeline().addLast(new MessageToByteBufEncoder());
                }
            });
            f=b.connect(new InetSocketAddress(serverHost,serverPort)).sync();
            ch=f.channel();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            group.shutdownGracefully();
        }
    }
    
    public void sendMsg(String content, String myName, String friendName){
        System.out.println("send msg:"+content+"friendName:"+friendName);
        CommunicationBo bo=new CommunicationBo();
        bo.setMessageContent(content);
        bo.setMyName(myName);
        bo.setOppisiteName(friendName);
        ClientPushMessage clientPushMessage=new ClientPushMessage();
        clientPushMessage.setType(MessageContent.COMMUNICATION);
        clientPushMessage.setData(JSON.toJSONBytes(bo));
        try {
            ch.writeAndFlush(clientPushMessage).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void shutdown(){
        group.shutdownGracefully();
    }
}