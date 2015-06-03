/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bekarys
 */
public class Server implements Runnable {
    protected int           id              =   0;
    protected int           serverPort      =   8080;
    protected ServerSocket  serverSocket    =   null;
    protected ClientList    clientList      =   null;
    protected GameList      gameList        =   null;

    public Server(int port) throws IOException{
        this.serverPort = port;
        this.serverSocket = new ServerSocket(this.serverPort);
        this.clientList = new ClientList();
        this.gameList = new GameList();
    }

    public void run(){
        int id = 0;
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientServiceThread cliThread = new ClientServiceThread(clientSocket, id++, clientList, gameList);
                clientList.insert(cliThread);
                cliThread.start();
                clientList.cleanGarbage();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
