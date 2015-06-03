/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newserver;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 *
 * @author Bekarys
 */
public class Card {
    public static boolean exception = true;
    public static boolean debug = false;
    public int number;
    public int suit;
    
    Card(int number, int suit) {
        this.number = number;
	this.suit = suit;
    }

    public String toString() {
        Map obj=new LinkedHashMap();
        obj.put("Number",this.getNumber());
        obj.put("Suit",this.getSuit());
        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(obj, out);
        } catch (IOException ex) {
            if (exception) LogWriter.write("BEGIN IOEXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(ex.getStackTrace().toString());
            if (exception) LogWriter.write("END IOEXCEPTION*********************");
        } catch (Exception e) {
            if (exception) LogWriter.write("BEGIN EXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(e.getStackTrace().toString());
            if (exception) LogWriter.write("END EXCEPTION*********************");
        }
        return out.toString();
    }
    
    public String toCardString() {
        String card = "";
        switch(this.number) {
            case 10:
                card = "J"; break;
            case 11:
                card = "D"; break;
            case 12:
                card = "K"; break;
            case 13:
                card = "10"; break;
            case 14:
                card = "A"; break;
            default :
                card = String.valueOf(this.number);
                break;
        }
        switch(this.suit) {
            case 1:
                card += "♥"; break;
            case 2:
                card += "♦"; break;
            case 3:
                card += "♠"; break;
            case 4:
                card += "♣"; break;
        }
        return card;
    }

    public boolean isBeat(Card card, Card trump) {
        if (card.getSuit() == trump.getSuit()) {
            if (this.getSuit() == trump.getSuit()) {
                return this.getNumber() < card.getNumber();
            } else {
                return true;
            }
        } else {
            if (this.getSuit() == card.getSuit()) {
                return this.getNumber() < card.getNumber();
            } 
        }
        
        return false;
    }

    public boolean isEqual(Card card) {
        return (this.getNumber() == card.getNumber() 
                && this.getSuit() == card.getSuit());
    }
    
    public int getPos() {
	if (this.getNumber() == 14)
            return -1;
	if (this.getNumber() == 6)
            return 1;
	return 0;
    }

    public int getNumber() {
        return this.number;
    }
    
    public int getSuit() {
        return this.suit;
    }

    public static Card toCard(String jsonText) {
        int number = 0, suit = 0;
        JSONParser parser = new JSONParser();
        ContainerFactory containerFactory = new ContainerFactory(){
          public List creatArrayContainer() {
            return new LinkedList();
          }

          public Map createObjectContainer() {
            return new LinkedHashMap();
          }

        };

        try{
          Map json = (Map)parser.parse(jsonText, containerFactory);
          Iterator iter = json.entrySet().iterator();
          while(iter.hasNext()){
            Map.Entry entry = (Map.Entry)iter.next();
            String name = entry.getKey().toString();
            String value = String.valueOf(entry.getValue());
            if (name.equals("Number")) {
                number = Integer.parseInt(value);
            } else if (name.equals("Suit")) {
                suit = Integer.parseInt(value);
            }
            if (debug) System.out.println(entry.getKey() + "=>" + entry.getValue());
          }
        }
        catch(ParseException pe){
            if (exception) LogWriter.write("BEGIN PARSEEXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(pe.getStackTrace().toString());
            if (exception) LogWriter.write("END PARSEEXCEPTION*********************");
        } catch (Exception e) {
            if (exception) LogWriter.write("BEGIN EXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(e.getStackTrace().toString());
            if (exception) LogWriter.write("END EXCEPTION*********************");
        }
        return new Card(number, suit);
    } 
  
    public static Card toCard(Map json) {
        int number = 0, suit = 0;
        try{
          Iterator iter = json.entrySet().iterator();
          while(iter.hasNext()){
            Map.Entry entry = (Map.Entry)iter.next();
            String name = entry.getKey().toString();
            String value = String.valueOf(entry.getValue());
            if (name.equals("Number")) {
                number = Integer.parseInt(value);
            } else if (name.equals("Suit")) {
                suit = Integer.parseInt(value);
            }
            if (debug) System.out.println(entry.getKey() + "=>" + entry.getValue());
          }
        }
        catch(Exception pe){
            if (exception) LogWriter.write("BEGIN EXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(pe.getStackTrace().toString());
            if (exception) LogWriter.write("END EXCEPTION*********************");
        }
        return new Card(number, suit);
    } 

    
    /*public static ArrayList<String> serializeJson(ArrayList<Card> list) {
        ArrayList<String> new_list = new ArrayList<String>();
        for (int i = 0; i < list.size(); ++ i) {
            new_list.add(list.get(i).toString());
        } 
        System.out.println("serializeJsonCard");
        return new_list;
    }/**/
}
