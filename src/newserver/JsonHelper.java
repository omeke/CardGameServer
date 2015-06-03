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
import org.json.simple.JSONValue;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Bekarys
 */
public class JsonHelper {
    public static boolean exception = true;
    private Map     json    =   null;

    public JsonHelper() {
        
    }
    
    public void init(String jsonText) {
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
          this.json = (Map)parser.parse(jsonText, containerFactory);
        }
        catch(ParseException pe){
            if (exception) LogWriter.write("BEGIN EXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(pe.getStackTrace().toString());
            if (exception) LogWriter.write("END EXCEPTION*********************");
        }        
    }
    
    public ArrayList<Card> convertToArray(LinkedList<Object> list) {
        ArrayList<Card> result = new ArrayList<Card>();
        try {
            for(Object card: list) {
                Map json = (Map)card;
                result.add(new Card(
                        ((Long)this.findByName(json, "Number")).intValue(),
                        ((Long)this.findByName(json, "Suit")).intValue()
                        ));
            }
        } catch (Exception ex) {
            if (exception) LogWriter.write("BEGIN EXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(ex.getStackTrace().toString());
            if (exception) LogWriter.write("END EXCEPTION*********************");
        }
        return result;
    }
    
    public Object findByName(String n_name) {
        try {
            Iterator iter = json.entrySet().iterator();
            while(iter.hasNext()){
              Map.Entry entry = (Map.Entry)iter.next();
              String name = entry.getKey().toString();
              if (name.equals(n_name)) {
                  return entry.getValue();
              }
            }
        } catch (Exception ex) {
            if (exception) LogWriter.write("BEGIN EXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(ex.getStackTrace().toString());
            if (exception) LogWriter.write("END EXCEPTION*********************");
        }        
        return null;
    }

    public LinkedList<Object> findListByName(String n_name) {
        try {
            Iterator iter = json.entrySet().iterator();
            while(iter.hasNext()){
              Map.Entry entry = (Map.Entry)iter.next();
              String name = entry.getKey().toString();       
              if (name.equals(n_name)) {
                  //System.out.println(entry.getKey() + "=>" + entry.getValue());
                  return (LinkedList<Object>)entry.getValue();
              }
            }
        } catch (Exception ex) {
            if (exception) LogWriter.write("BEGIN EXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(ex.getStackTrace().toString());
            if (exception) LogWriter.write("END EXCEPTION*********************");
        }            
        return null;
    }

    public Object findByName(Map json1, String n_name) {
        try {
            Iterator iter = json1.entrySet().iterator();
            while(iter.hasNext()){
              Map.Entry entry = (Map.Entry)iter.next();
              String name = entry.getKey().toString();
              if (name.equals(n_name)) {
                  return entry.getValue();
              }
            }
        } catch (Exception ex) {
            if (exception) LogWriter.write("BEGIN EXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(ex.getStackTrace().toString());
            if (exception) LogWriter.write("END EXCEPTION*********************");
        }
        return null;
    }

    public LinkedList<Object> findListByName(Map json1, String n_name) {
        try{
            Iterator iter = json1.entrySet().iterator();
            while(iter.hasNext()){
              Map.Entry entry = (Map.Entry)iter.next();
              String name = entry.getKey().toString();       
              if (name.equals(n_name)) {
                  //System.out.println(entry.getKey() + "=>" + entry.getValue());
                  return (LinkedList<Object>)entry.getValue();
              }
            }
        } catch (Exception ex) {
            if (exception) LogWriter.write("BEGIN EXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(ex.getStackTrace().toString());
            if (exception) LogWriter.write("END EXCEPTION*********************");
        }
        return null;
    }

    /*
     * made string like {"action": "put"}
     */
    public static String toJSON(Map obj) {
        StringWriter outt = new StringWriter();
        try {
            JSONValue.writeJSONString(obj, outt);
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
        return outt.toString();
    }
}
