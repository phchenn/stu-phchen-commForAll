package stu.phchen.UDPClient.test;

import stu.phchen.UDPClient.ui.Mainframe;

public class test2 {
    public static void main(String[] argc) throws Exception{
        new Mainframe(10088,"127.0.0.1",8800,"big7").go();
    }
}