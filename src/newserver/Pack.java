/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newserver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Bekarys
 */
public class Pack {
    public static boolean output = true;
    public static boolean debug = false;
    public static boolean exception = true;
    
    public ArrayList<Card> cards;
    public Card trump;
    public Card after_trump;
    public int size;
    public int lastTrump;

    Pack() {
        this.size = 36;
	this.cards = new ArrayList<Card>();
        this.initPack(); // generate Pack
        this.lastTrump = 0;
    }
    
    public void initTrump(int n) {
        switch(this.cards.get(36 - 6*n - 1).getPos()) {
            case 0:
                this.after_trump = this.cards.get((36-6*n)/2 - 1);
                break;
            case 1: // 6
                this.after_trump = this.cards.get(0);
                break;
            case -1: // A
                this.after_trump = this.cards.get(36 - 6*n - 1);
                break;
            default:
                break;                
        }
    }
    
    public void initPack() {
        try {
            this.lastTrump = 0;
            this.size = 36;
            this.cards = new ArrayList<Card>();
            boolean[] use = new boolean[37];
            int x;
            for(int i = 1; i <= 36; i ++) use[i] = false;
            for(int i = 0; i < 36; i ++) {
                do {
                    x = (int) (Math.random()*36);
                    x = (x%36)+1;
                } while (use[x]);
                this.cards.add(new Card( (((x%9 == 0)?(9):(x%9))+5), (x+8)/9 ));
                use[x] = true;
            }
        } catch (Exception ex) {
            if (exception) LogWriter.write("BEGIN EXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(ex.getStackTrace().toString());
            if (exception) LogWriter.write("END EXCEPTION*********************");
        }
    }
    
    public Card getFirst() {
        /*if (debug) LogWriter.write("***************pack***************");
        for(int i = 0; i < this.cards.size(); ++ i) {
            if (debug) LogWriter.write(this.cards.get(i).toCardString()+" ");
        }
        if (debug) LogWriter.write("");
        if (debug) LogWriter.write("***************pack***************");
        if (this.trump != null && debug) System.out.println("trump: "+this.trump.toCardString());
        if (debug) System.out.println("***************trump***************");/**/
        
        try {
            Card first_card = this.cards.get(this.cards.size()-1);
            this.cards.remove(first_card);

            if (this.trump != null && this.trump.getNumber() != 14 && this.after_trump != null 
                    && this.after_trump.isEqual(first_card)) {
                this.trump = this.after_trump;
            }
            
            this.size --;
            return first_card;            
        } catch(Exception e) {                    
            if (exception) LogWriter.write("BEGIN EXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(e.getStackTrace().toString());
            if (exception) LogWriter.write("END EXCEPTION*********************");
        }
        
        
        /*if (this.lastTrump == 1) {
            this.trump = first_card;
            this.lastTrump++;
        }
        if (this.trump != null && this.trump.isEqual(first_card) && this.trump.getNumber() != 14) {
            this.lastTrump ++;
        }*/
        
        return null;
    }
    
    public ArrayList<Card> getCards(int n) {
        ArrayList<Card> list = new ArrayList<Card>();
        try {
            for (int i = 0; i < n; ++ i) {
                list.add(this.getFirst());
            }
        } catch (Exception ex) {
            if (exception) LogWriter.write("BEGIN EXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(ex.getStackTrace().toString());
            if (exception) LogWriter.write("END EXCEPTION*********************");
        }    
        return list;
    }
    
    public void putCard(Card card) {
        try {
            int pos = card.getPos();
            if (pos == 0)
                this.cards.add((this.cards.size()+1)/2, card);
            if (pos == 1) // 6
                this.cards.add(1, card);
            if (pos == -1) // A
                this.cards.add(this.cards.size(), card);
        } catch (Exception ex) {
            if (exception) LogWriter.write("BEGIN EXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(ex.getStackTrace().toString());
            if (exception) LogWriter.write("END EXCEPTION*********************");
        }    
        return;
    }
    
    public void setTrump(Card card) {
        this.trump = card;
    }
    
//    public void writeToFile(String text) {
//        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)))) {
//            out.println(text);
//            out.close();
//        }catch (IOException e) {
//            if (exception) LogWriter.write((new Date()).toString());
//            if (exception) LogWriter.write(e.getStackTrace().toString());
//        }        
//    }
}
