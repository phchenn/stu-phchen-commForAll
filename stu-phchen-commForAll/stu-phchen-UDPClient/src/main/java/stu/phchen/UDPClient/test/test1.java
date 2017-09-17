package stu.phchen.UDPClient.test;

import stu.phchen.UDPClient.ui.Mainframe;
//57600
//2048
public class test1 {
    public static void main(String[] args) throws Exception{
        new Mainframe(10086,"10.22.27.61",8800,"phchen").go();
    }
}
