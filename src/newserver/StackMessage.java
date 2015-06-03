/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newserver;

/**
 *
 * @author Bekarys
 */
public class StackMessage {
    public String   dump        =   null;
    public String   container   =   null;
    public String   currMess    =   null;

    StackMessage() {
        container = "";
        dump = "";
        currMess = "";
    }
    
    public void insert(String message) {
        container += message;
        dump += message;
    }
    
    public String nextMessage() {
        int k = 0;
        String message = "";
        for (int i = 0; i < container.length(); ++ i) {
            if (container.charAt(i) == '{') k++;
            if (k > 0) message = message+String.valueOf(container.charAt(i));
            if (container.charAt(i) == '}') k--;
            if (message.length() > 1 && k == 0) {
                container = container.substring(i+1, container.length());
                currMess = message;
                return message;
            }
        }
        return null;
    }
}
