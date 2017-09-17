package stu.phchen.TCPClient.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import stu.phchen.TCPClient.constant.MessageContent;
/**
 * @ClassName: com.tiger.appcomm.dubbo.handler.LengthFieldAndHeartbeatDecoder
 * @Description: 这是一个复合解码器，包继承自
 *               {@link io.netty.handler.codec.LengthFieldBasedFrameDecoder}解码器，
 *               比父类解码器新增功能，当发现一个包的开始头部首字节符合特定包内容，则将头部首字节当作特定包解析出来，
 *               存储交给后续处理Handler, 此类解特定头部首字节内容(
 *               {@link com.tiger.appcomm.dubbo.constant.MessageType#HEARTBEAT}
 *               )已经固定，无需配置
 * @author xl
 * @date 2017年6月16日
 * 
 */
public class LengthFieldAndHeartbeatDecoder extends LengthFieldBasedFrameDecoder{
    
    /**
     * @Fields nextByteIsNewPackage : 判断下一个字节是否一个新包的开始，用来做标记，好区别何时介入摘取首字段进行判断
     */
    private boolean nextByteIsNewPackage = true;

    public LengthFieldAndHeartbeatDecoder() {
        super(MessageContent.DEFAULT_MAX_FRAME_LENGTH, MessageContent.DEFAULT_LENGTH_FIELD_OFFSET,
                MessageContent.DEFAULT_LENGTH_FIELD_LENGTH);
    }

    public LengthFieldAndHeartbeatDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    public LengthFieldAndHeartbeatDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
            int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    public LengthFieldAndHeartbeatDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
            int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }
    
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        System.out.println("开始分包......");
        Object decoded = null;
        if (nextByteIsNewPackage) {
            decoded = decodeInitialBytes(ctx, in);
        }
        if (decoded != null) {
            return decoded;
        }
        decoded = super.decode(ctx, in);
        if (decoded != null) {
            nextByteIsNewPackage = true;
        } else {
            nextByteIsNewPackage = false;
        }
        return decoded;
    }
    
    /**
     * <p>
     * @Title: decodeInitialBytes
     * </p>
     * <p>
     * @Description: 这里是获取第一个字节，如果符合 MessageType.HEARTBEAT
     * 心跳包则将单字节心跳包单独取出写入ByteBuf
     * </p>
     */
    protected Object decodeInitialBytes(ChannelHandlerContext ctx, ByteBuf in) {
        ByteBuf frame = null;
        if (in.readableBytes() <= 0) {
            return null;
        }
        int readerIndex = in.readerIndex();
        byte initialBytes = in.getByte(readerIndex);
        if (initialBytes == MessageContent.HEARTBEAT) {
            frame = extractFrame(ctx, in, readerIndex, 1);
            in.readerIndex(readerIndex + 1);
        }
        return frame;
    }
}
