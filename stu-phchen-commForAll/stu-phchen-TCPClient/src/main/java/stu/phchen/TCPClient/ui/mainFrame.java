package stu.phchen.TCPClient.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import stu.phchen.TCPClient.callback.Callback;
import stu.phchen.TCPClient.thread.ClientThread;

public class mainFrame implements Callback{
    private JFrame frame;
    private JPanel panel;
    private JButton send;
    private JTextField to,sendMes;
    private JTextArea text;
    private JLabel label1,label2;
    private ClientThread clientThread;
    public mainFrame(String serverAddress, int serverPort, String name){
        clientThread=new ClientThread(this, name, serverAddress, serverPort);
        clientThread.start();
        frame=new JFrame("text communication"+name);
        panel=new JPanel();
        send=new JButton("send");
        to=new JTextField();
        sendMes=new JTextField();
        text=new JTextArea(200, 200);
        label1=new JLabel("To");
        label2=new JLabel("sendMes");
    }
    public void go(){
        frame.setSize(350, 500);
        frame.getContentPane().add(panel);
        panel.setLayout(null);
        panel.add(to);
        panel.add(label1);
        panel.add(sendMes);
        panel.add(label2);
        panel.add(send);
        panel.add(text);
        to.setBounds(5,5,200, 50);
        label1.setBounds(210,5,50,50);
        sendMes.setBounds(5,55,200, 50);
        label2.setBounds(210,55,70,50);
        send.setBounds(5, 110, 70, 25);
        text.setBounds(5, 145, 200, 200);
        frame.setVisible(true);
        
        send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clientThread.comm(sendMes.getText(), to.getText());
                sendMes.setText(null);
            }
        });
    }
    
    public void dealMessage(String friendName, String msg) {
        text.append(friendName+":"+msg+"\n");
    }
}
