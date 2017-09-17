package stu.phchen.TCPClient.thread;

import stu.phchen.TCPClient.callback.Callback;
import stu.phchen.TCPClient.client.NettyTCPClient;

public class ClientThread extends Thread{
    private NettyTCPClient client;
    private String serverAddr;
    private int serverPort;
    private String userName;
    public ClientThread(Callback callback, String userName, String serverAddr, int serverPort){
        super();
        this.userName=userName;
        this.serverAddr=serverAddr;
        this.serverPort=serverPort;
        client=new NettyTCPClient(callback, userName);
    }
    
    @Override
    public void run(){
        client.connect(serverAddr, serverPort);
    }
    
    public void comm(String content, String friendName){
        client.sendMsg(content, userName, friendName);
    }
    
    public void shutdown(){
        client.shutdown();
    }
}
