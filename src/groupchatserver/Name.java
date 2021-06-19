/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupchatserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Name extends Thread{
    byte[] buffer=new byte[512];
    public HashMap <Integer, InetAddress> infos = new HashMap<Integer, InetAddress>();
    public HashMap <Integer, String> names = new HashMap<Integer, String>();
    @Override
    public void run(){
        DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
        while(GroupChatServer.calling){
            try {
                GroupChatServer.socket_name.receive(incoming);
                InetAddress incomingAddress=incoming.getAddress();
                int incomingPort= incoming.getPort();
                String clientName = new String(incoming.getData());
                String[] arr = clientName.split(" ");
                String theclientName = arr[0];
                String ifNo = arr[1];
                System.out.println(ifNo);
                if("no".equals(ifNo)){
                    infos.remove(incomingPort);
                    names.remove(incomingPort);
                    System.out.println("removed");
                }
                else{
                    infos.put(incomingPort, incomingAddress);
                    names.put(incomingPort, theclientName);
                }
                String allNames = "";
                for(int namesKey : names.keySet() ){
                    String thename = names.get(namesKey);
                    allNames = allNames + thename +" ";
                }
                for(int infosKey : infos.keySet() ){
                    InetAddress address = infos.get(infosKey);
                    System.out.println(allNames);
                    DatagramPacket dp = new DatagramPacket(allNames.getBytes(), 0, allNames.length(), address, infosKey );
                    System.out.println(address +" "+infosKey);
                    GroupChatServer.socket_name.send(dp);
                }
            } catch (IOException ex) {
                Logger.getLogger(Name.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

