/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newserver;

import java.util.ArrayList;

/**
 *
 * @author Bekarys
 */
public class ClientList {
    public ArrayList<ClientServiceThread>   clients     =   null;
    
    ClientList() {
        clients = new ArrayList<ClientServiceThread>();
    }
    
    public void insert(ClientServiceThread newClient) {
        clients.add(newClient);
    }
    
    public void cleanGarbage() {
        for (ClientServiceThread client: clients) {
            if (client.player != null && !client.player.ping()) {
            }
        }
    }
    
    public ClientServiceThread get(int clientID) {
        for (ClientServiceThread client: clients) {
            if (client.clientID == clientID) {
                return client;
            }
        }
        return null;
    }
}
