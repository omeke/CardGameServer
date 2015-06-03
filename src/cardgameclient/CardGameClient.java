/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cardgameclient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bekarys
 */
public class CardGameClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException, IOException {
        System.out.println((new Date()).toString());
        
//        System.out.println("Welcome to Client side");
//
//
//
//        //System.out.println("Connecting to... "+args[0]);
//
//        final Socket fromserver = new Socket("localhost",1985);
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                try {
//                    BufferedReader in  = new
//                     BufferedReader(new
//                      InputStreamReader(fromserver.getInputStream()));
//                    PrintWriter    out = new 
//                     PrintWriter(fromserver.getOutputStream(),true);
//                    
//                    while(true) {
//                        System.err.println(in.readLine());
//
//                    }
//                } catch (IOException ex) {
//                    Logger.getLogger(CardGameClient.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }).start();
//        
//        BufferedReader in  = new
//         BufferedReader(new 
//          InputStreamReader(fromserver.getInputStream()));
//        PrintWriter    out = new 
//         PrintWriter(fromserver.getOutputStream(),true);
//        BufferedReader inu = new 
//         BufferedReader(new InputStreamReader(System.in));
//
//        String fuser,fserver,temp = "";
//        
//        Scanner scanner = new Scanner(System.in);
//        while(true) {
//            String inLine = scanner.next();
//            if (inLine.equalsIgnoreCase("exit")) break;
//            if (inLine.equalsIgnoreCase("login")){
//                temp = "{\"action\": \"login\", \"name\": \"shrek\"}";
//            }
//            if (inLine.equalsIgnoreCase("create")) {
//                temp = "{\"action\": \"create-game\", \"game-size\": 2}";
//            }
//            if (inLine.equalsIgnoreCase("list")) {
//                temp = "{\"action\": \"list-game\"}";
//            }
//            if (inLine.equalsIgnoreCase("join")) {
//                temp = "{\"action\": \"join-game\", \"game-index\": 0}";
//            }
//            if (inLine.equalsIgnoreCase("start")) {
//                temp = "{\"action\": \"start-game\"}";
//            }
//            if (inLine.equalsIgnoreCase("return-turn")) {
//                temp = "{\"action\":\"player-start\", \"turn\":0}";
//            }
//            if (inLine.equalsIgnoreCase("put-card")) {
//                temp = "{\"action\":\"put-card\", \"card\":{\"Number\":"+scanner.next()+",\"Suit\":"+scanner.next()+"}}";
//            }
//            if (inLine.equalsIgnoreCase("get-card")) {
//                temp = "{\"action\":\"get-card\"}";
//            }
//            temp += "/r/n";
//          out.println(temp);
//        }
//
//        out.close();
//        in.close();
//        inu.close();
//        fromserver.close();
    }
}
