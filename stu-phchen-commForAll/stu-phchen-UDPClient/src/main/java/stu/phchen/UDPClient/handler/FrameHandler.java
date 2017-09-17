package stu.phchen.UDPClient.handler;

import javax.swing.JFrame;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class FrameHandler extends SimpleChannelInboundHandler<DatagramPacket>{
    private int frameNum;
    private int packetSize=2047;
    
    private byte[] image;
    private CanvasFrame canvas;
    private OpenCVFrameConverter.ToIplImage converter;
    private boolean[] packetHasRead;
    public FrameHandler(int frameNum, int packetSize){
        super();
        Loader.load(opencv_objdetect.class);
        this.frameNum=frameNum;
        this.packetSize=packetSize;
        packetHasRead=new boolean[frameNum];
        for(int i=0;i<frameNum;i++)
            packetHasRead[i]=false;//未读取状态
        image=new byte[60000];//现在一帧图像是57600
        canvas = new CanvasFrame("摄像头");//新建一个窗口  
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.setAlwaysOnTop(true);
        converter=new OpenCVFrameConverter.ToIplImage();
    }
    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        //开头第一个字节是包的序号，0-26左右，所以从索引0开始读，一共2048字节的包，图像数据只有2047字节 
        int num=msg.content().getByte(0);
        if(num<frameNum){
            //System.out.println(num);
            if(!packetHasRead[num]){//如果是未读取状态
                packetHasRead[num]=true;
            }else {
                BytePointer cbp=new BytePointer(image);
                IplImage cimage=IplImage.create(160, 120, 8, 3);
                cimage.imageData(cbp);
                canvas.showImage(converter.convert(cimage));
                cimage.release();
                cbp.close();
                for(int i=0;i<frameNum;i++)
                    packetHasRead[i]=false;
            }
            msg.content().getBytes(1, image, num*packetSize, msg.content().readableBytes()-1);
        }
    }
}