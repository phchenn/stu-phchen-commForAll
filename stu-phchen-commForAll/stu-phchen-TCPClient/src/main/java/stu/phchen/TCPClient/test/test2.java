package stu.phchen.TCPClient.test;

import stu.phchen.TCPClient.ui.mainFrame;

public class test2 {
    public static void main(String[] args){
        mainFrame frame=new mainFrame("127.0.0.1", 8800, "big7");
        frame.go();
    }
}
