package stu.phchen.UDPClient.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import stu.phchen.UDPClient.Thread.ClientThread;

public class Mainframe {
    private JFrame frame;
    private JPanel panel;
    private JButton connect,stop;
    private JTextField textName;
    private JLabel label1;
    
    private ClientThread thread;
    public Mainframe(int port, String serverAddress, int serverPort, String userName){
        frame=new JFrame("video communication:"+port);
        panel=new JPanel();
        connect=new JButton("connect");
        stop=new JButton("stop");
        textName=new JTextField();
        label1=new JLabel("用户名");
        
        thread=new ClientThread(port, serverAddress, serverPort, userName);
        thread.start();
    }
    public void go(){
        frame.setSize(300, 200);
        frame.getContentPane().add(panel);
        panel.setLayout(null);
        panel.add(textName);
        panel.add(label1);
        panel.add(connect);
        panel.add(stop);
        textName.setBounds(5,5,200, 50);
        label1.setBounds(210,5,50,50);
        connect.setBounds(5, 110, 70, 25);
        stop.setBounds(80, 110, 60, 25);
        frame.setVisible(true);
        
        connect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                thread.tryComm(textName.getText());
            }
        });
        stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                thread.stopComm();
                //thread.shutdown();
            }
        });
    }
}