package stu.phchen.TCPClient.test;

import stu.phchen.TCPClient.ui.mainFrame;

public class test1 {
    public static void main(String[] args){
        mainFrame frame=new mainFrame("10.22.27.61", 8888, "phchen");
        frame.go();
    }
}
