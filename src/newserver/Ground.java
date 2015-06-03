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

/**
 *
 * @author Bekarys
 */
public class Ground {
    public int size;
    public int winner_id;
    public int first_turn_id;
    public int round;
    public ArrayList<Card> cards;
    public ArrayList<Card> winner_cards;
    public ArrayList<Card> pocket;
    public boolean debug = false;
    
    Ground() {
        cards = new ArrayList<Card>();
        winner_cards = new ArrayList<Card>();
        pocket = new ArrayList<Card>();
        winner_id = -1;
        first_turn_id = -1;
        size = 0;
        round = 0;
    }
    
    public void clear() {
        String output =  "";
        for (int i = 0; i < cards.size(); ++ i) {
            if (i%size == 0) output += "\n";
            output += cards.get(i).toCardString();
        }
        LogWriter.write(output);
        cards = new ArrayList<Card>();
        winner_cards = new ArrayList<Card>();
        pocket = new ArrayList<Card>();
        winner_id = -1;
        round = 0;
        size = 0;
    }
    
    public void addCards(ArrayList<Card> ground, Card trump, int player_id, boolean is2player) {
        if (round%2 == 0 && is2player) {
            winner_cards.clear();
        }
        if (winner_cards.size() == 0) {
            winner_cards.addAll(ground);
            first_turn_id = winner_id = player_id;
            size = ground.size();
        } else if (ground.size() != 0) {
            boolean beat = true;
            for (int i = 0; i < size; ++ i) {
                if (!winner_cards.get(i).isBeat(ground.get(i), trump)) {                    
                    beat = false;
                }
            }
            if (beat) {
                winner_cards = ground;
                winner_id = player_id;
                //System.out.println("---"+ground.toString());
            }            
        }
        cards.addAll(ground);
        round++;
        LogWriter.write("round:"+round+"; first_turn_id:"+first_turn_id+"; winner_id:"+winner_id);
    }
    
//    public void writeToFile(String text) {
//        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)))) {
//            out.println(text);
//            out.close();
//        }catch (IOException e) {
//            e.printStackTrace();
//        }        
//    }
}
