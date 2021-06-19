/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupchatserver;

import static groupchatserver.StartGroupCallServer.add_server;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author user
 */
public class GroupChatServer {
     public static boolean calling=false;
     public static DatagramSocket socket_name;
     public static DatagramSocket socket0;
     public static DatagramSocket socket1;
     public static DatagramSocket socket2;
     public static DatagramSocket socket_notice;
     public static int allPeople=3;
     public static int clients[]= new int[3];
     public static InetAddress clientsinetaddress[]= new InetAddress[3];

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         try {
             // TODO code application logic here
             for(int i=0; i<allPeople; i++){ //inetAddress 초기화 시켜줌
                     clientsinetaddress[i]=InetAddress.getByName(add_server);
             }
             socket_name = new DatagramSocket(StartGroupCallServer.port_name);
             socket0=new DatagramSocket(StartGroupCallServer.port);
             socket1=new DatagramSocket(StartGroupCallServer.port+1);
             socket2=new DatagramSocket(StartGroupCallServer.port+2);
             socket_notice = new DatagramSocket();
         } catch (SocketException | UnknownHostException ex ) {
             Logger.getLogger(GroupChatServer.class.getName()).log(Level.SEVERE, null, ex);
         }
        StartGroupCallServer fr=new StartGroupCallServer();
        fr.setVisible(true);
    }
    
}
