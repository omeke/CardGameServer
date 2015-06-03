/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newserver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Bekarys
 */
public class LogWriter {
    public static void write(String text) {
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)))) {
            out.println(text);
            out.close();
        }catch (IOException e) {
            e.printStackTrace();
        } 
    }
}
