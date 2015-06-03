/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newserver;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 *
 * @author Bekarys
 */
public class ServerRunner {
     public static void main(String[] args) throws IOException {
        Server server = new Server(1985);
        new Thread(server).start();
     }
}
