package stu.phchen.UDPClient.Thread;

import stu.phchen.UDPClient.client.UdpClient;

public class ClientThread extends Thread{
    private UdpClient client;
    private String userName;
    public ClientThread(int port, String serverAddress, int serverPort, String userName){
        this.userName=userName;
        client=new UdpClient(port, serverAddress, serverPort);
    }
    @Override
    public void run(){
        client.connect(userName);
    }
    
    public void shutdown(){
        client.shutdown();
    }
    
    public void stopComm(){
        client.stopComm();
    }
    public void tryComm(String name){
        client.tryComm(name);
    }
    //test
    private void comm(final String dstAddress, final int dstPort){
        client.comm(dstAddress, dstPort);
    }
}
