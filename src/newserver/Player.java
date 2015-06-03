/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newserver;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bekarys
 */
public class Player {
    public static boolean exception = true;
    
    public static final int NEW = 0;
    public static final int LOST = 1;
    public static final int LOSE = 2;
    public static final int PLAY = 3;
    
    public ClientList       clientList      =   null;
    public Socket           clientSocket    =   null;
    public String           name            =   null;
    public int              ID              =   -1;
    public int              score           =   0;
    public int              point           =   0;
    public int              life            =   1;
    public int              state           =   PLAY;
    
    public ArrayList<Card>  hand            =   null;
    public ArrayList<Card>  next            =   null;
    public ArrayList<Card>  earn            =   null;
    
    
    Player (Socket _clientSocket, String _name, int _ID, ClientList _clientList) {
        clientSocket = _clientSocket;
        clientList = _clientList;
        name = _name;
        ID = _ID;
        life = 1;
        hand = new ArrayList<Card>();
        earn = new ArrayList<Card>();
        next = new ArrayList<Card>();
        state = NEW;
        LogWriter.write("New player created. Name: '"+name+"'; ID: "+ID);                
    }

    public void sendCard() {
        try {
            Map obj=new LinkedHashMap();
            obj.put("action", "card-received");
            obj.put("card", next.toString());
            String json_response = JsonHelper.toJSON(obj);

            hand.addAll(next);
            next.clear();

            this.sendToClient(json_response);
            LogWriter.write("'"+this.name+"' sends cards: "+json_response);        
        } catch (Exception ex) {
            if (exception) LogWriter.write("BEGIN EXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(ex.getStackTrace().toString());
            if (exception) LogWriter.write("END EXCEPTION*********************");
        }
    }
    
    public void getCard(ArrayList<Card> _hand) {
        try {
            ArrayList<Card> trush = new ArrayList<Card>();
            for (Card card: _hand) {
                for(Card card1: hand) {
                    if (card1.isEqual(card)) {
                        trush.add(card1);
                        break;
                    }
                }            
            }
            if (trush.size() != _hand.size()) LogWriter.write("---Player, getCard: trush.size() != _hand.size() ");
            hand.removeAll(trush);
        } catch (Exception ex) {
            if (exception) LogWriter.write("BEGIN EXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(ex.getStackTrace().toString());
            if (exception) LogWriter.write("END EXCEPTION*********************");
        }
    }
    
    public void backToGame(int maxi) {
        life ++;
        state = Player.PLAY;
        score = maxi-2;
    }
    
    public void calcPoint() {
        this.point = 0;
        for (Card card: earn) {
            switch (card.getNumber()) {
                case 10:
                    this.point += 2;
                    break;
                case 11:
                    this.point += 3;
                    break;
                case 12:
                    this.point += 4;
                    break;
                case 13:
                    this.point += 10;
                    break;
                case 14:
                    this.point += 11;
                    break;
                default :
                    break;
            }
        }
    }

    
    public String toString() {
        return '"'+name+'"';
    }
    
    public boolean ping() {
        Map obj=new LinkedHashMap();
        obj.put("action", "ping");
        obj.put("ping", "ping");
        String json_response = JsonHelper.toJSON(obj);
        
        return this.sendToClient(json_response);
    }
    
    public boolean sendToClient(String jsonMessage) {
        try {
            jsonMessage += "\r\n";
            BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
            out.write(jsonMessage.getBytes());
            out.flush();
            LogWriter.write("Sent to("+name+"): "+jsonMessage);
            return true;
        } catch (SocketException e) {
            state = Player.LOST;   
            
            ClientServiceThread client = clientList.get(ID);
            if (client != null) {
                client.running = false;
                client.interrupt();
            }
            
            if (exception) LogWriter.write("BEGIN SOCKETEXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(e.getStackTrace().toString());
            if (exception) LogWriter.write("END SOCKETEXCEPTION*********************");
        } catch (IOException ex) {
            if (exception) LogWriter.write("BEGIN SOCKETEXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(ex.getStackTrace().toString());
            if (exception) LogWriter.write("END SOCKETEXCEPTION*********************");
        } catch (Exception e) {
            if (exception) LogWriter.write("BEGIN SOCKETEXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(e.getStackTrace().toString());
            if (exception) LogWriter.write("END SOCKETEXCEPTION*********************");
        }
        return false;
    }
}
