package stu.phchen.TCPServer.handler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import com.alibaba.fastjson.JSON;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import stu.phchen.TCPServer.constant.ConnectManager;
import stu.phchen.TCPServer.constant.MessageContent;
import stu.phchen.TCPServer.model.CertificationBo;
import stu.phchen.TCPServer.model.ClientMessage;

public class CertificationHandler extends SimpleChannelInboundHandler<ClientMessage>{
    private BufferedReader reader;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClientMessage msg) throws Exception {
        if(msg.getType()==MessageContent.CERTIFICATION){
            CertificationBo bo=JSON.parseObject(msg.getData(), CertificationBo.class);
            //开始验证
            try {
                reader=new BufferedReader(new InputStreamReader(
                        new FileInputStream("src/main/resources/user.text")));
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }
            String line;
            while((line=reader.readLine())!= null){
                if(bo.getName().equals(line)){
                    //这里最好加个锁，静态变量
                    ConnectManager.connectManager.put(bo.getName(), ctx.channel());
                    reader.close();
                    return;
                }
            }
            reader.close();
        }else{
            ctx.fireChannelRead(msg);
        }
    }
}
