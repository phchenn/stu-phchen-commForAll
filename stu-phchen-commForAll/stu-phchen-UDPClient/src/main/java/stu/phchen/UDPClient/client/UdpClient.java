package stu.phchen.UDPClient.client;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import stu.phchen.UDPClient.CallBack.CommCallBack;
import stu.phchen.UDPClient.handler.CommHandler;
import stu.phchen.UDPClient.handler.FrameHandler;

public class UdpClient implements CommCallBack{
    private String serverAddress;
    private int serverPort;
    
    private boolean isQuit=false;
    private int port;
    private EventLoopGroup group;
    private Boolean isComm=false;
    private ExecutorService threadPool;
    private Channel ch;
    
    public UdpClient(int port, String serverAddress, int serverPort){
        Loader.load(opencv_objdetect.class);
        this.port=port;
        this.serverPort=serverPort;
        this.serverAddress=serverAddress;
        threadPool=Executors.newFixedThreadPool(5);
    }
    //
    public void connect(String userName){
        group=new NioEventLoopGroup();
        final CommHandler commHandler=new CommHandler(this,threadPool);
        try{
            Bootstrap b=new Bootstrap();
            b.group(group)
            .channel(NioDatagramChannel.class)
            .handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(commHandler);
                    ch.pipeline().addLast(new FrameHandler(57600/2047, 2047));
                }
            });
            ch = b.bind(port).sync().channel();
            System.out.println("[udp client] connect success");
            //send a ip active to server
            String ipactive="ip:"+userName;
            ch.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(ipactive.getBytes()),
                    new InetSocketAddress(serverAddress, serverPort)));
            ch.closeFuture().await();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            System.out.print("线程失败");
            group.shutdownGracefully();
        }
    }
    //回调:开始传视频
    public void comm(final String dstAddress, final int dstPort){
        System.out.println("[comm] dstaddress: "+dstAddress+" dstport: "+dstPort);
        System.out.println("statr to send frame grab data");
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        try {
            grabber.setImageHeight(10);
            grabber.setImageWidth(10);
            grabber.start();
            IplImage grabberImage;
            //开始获取摄像头数据
            while(!isQuit){
                Frame f=grabber.grab();
                grabberImage = converter.convert(f);
                BytePointer bp=grabberImage.imageData();
                int size=grabberImage.imageSize();
                byte[] framedata=new byte[size];
                bp.get(framedata);
                //分包并发送出去 首字节是序号
                //57600 2048 0,1,2,...
                for(int i=0;i<(size/2047);i++){
                    //System.out.println(i);
                    byte[] buffer=new byte[2048];
                    buffer[0]=(byte) i;
                    System.arraycopy(framedata, i*2047, buffer, 1, 2047);
                    ByteBuf buf=Unpooled.copiedBuffer(buffer);
                    ch.writeAndFlush(new DatagramPacket(buf,
                            new InetSocketAddress(dstAddress, dstPort)));
                }
                //grabberImage.release();
                //bp.close();
                TimeUnit.MILLISECONDS.sleep(200);
            }
            grabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                grabber.stop();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            try {
                grabber.stop();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
    //开始尝试通信
    public void tryComm(String name){
        System.out.println("[tryComm] client name:"+name+" isComm"+isComm);
        if(isComm)
            return;
        isComm=true;
        String msg="ask:"+name;
        ByteBuf buf=Unpooled.copiedBuffer(msg.getBytes());
        ch.writeAndFlush(new DatagramPacket(buf, 
                new InetSocketAddress(serverAddress, serverPort)));
    }
    //结束通信
    public void stopComm(){
        System.out.println("[stopComm] isComm"+isComm);
        if(!isComm)
            return;
        isComm=false;
        isQuit=true;
        System.out.println("视频流线程结束");
    }
    
    public void shutdown(){
        stopComm();
        group.shutdownGracefully();
    }
}