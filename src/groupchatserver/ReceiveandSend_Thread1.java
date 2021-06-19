/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupchatserver;

import static groupchatserver.GroupChatServer.allPeople;
import static groupchatserver.GroupChatServer.clients;

import static groupchatserver.GroupChatServer.clientsinetaddress;
import static groupchatserver.GroupChatServer.socket0;
import static groupchatserver.GroupChatServer.socket2;
import static groupchatserver.StartGroupCallServer.clientsAnotherPlayerPort;
import java.sql.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author user
 */
public class ReceiveandSend_Thread1 extends Thread {
    DatagramSocket socket = null;
    int curNum = 0;
    byte[] buffer=new byte[1024];
   public ReceiveandSend_Thread1(DatagramSocket socket, int curNum){
       this.socket = socket;
       this.curNum = curNum;
   }
    @Override
    public void run(){
        DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
        while(GroupChatServer.calling){
            try{
                if(incoming.getLength()<=21){ // 클라이언트 id, password 체크
                   String idandPassword = new String(incoming.getData());
                   String[] arr = idandPassword.split(" ");
                   String clientId = arr[0];
                   String clientPassword = arr[1];
                   System.out.println(clientId);
                   System.out.println(clientPassword);
                   Class.forName("com.mysql.cj.jdbc.Driver");
                   java.sql.Connection con;
                   con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb?serverTimezone=UTC","root","alswn");
                   String query="select * from login where username=? and password=?";
                   PreparedStatement ps = con.prepareStatement(query);
                   ps.setString(1, clientId);
                   ps.setString(2, clientPassword);
                   ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        String sendMessage= "ok ";
                        DatagramPacket dp = new DatagramPacket(sendMessage.getBytes(), 0, sendMessage.length(), incoming.getAddress(), incoming.getPort() );
                        socket.send(dp);
                    } else {
                        String sendMessage= new String("no ");
                        DatagramPacket dp = new DatagramPacket(sendMessage.getBytes(), 0, sendMessage.length(), incoming.getAddress(), incoming.getPort() );
                        socket.send(dp);
                    }
               }
                socket.receive(incoming);
                buffer=incoming.getData();
                clients[curNum]=incoming.getPort();
                clientsinetaddress[curNum]=incoming.getAddress();
                DatagramPacket data0 = new DatagramPacket(buffer, 0, buffer.length, clientsinetaddress[0], clients[0]);
                socket0.send(data0);
               DatagramPacket data2 = new DatagramPacket(buffer, 0, buffer.length, clientsinetaddress[2], clientsAnotherPlayerPort[2]);
                socket2.send(data2);
                 System.out.println("!"+clients[0]+" "+clients[2]);
            }catch (IOException | ClassNotFoundException | SQLException ex) {
                Logger.getLogger(ReceiveandSend_Thread0.class.getName()).log(Level.SEVERE, null, ex);
            }    
        }
        System.out.println("stop");
    }
}
