/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newserver;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONValue;

/**
 *
 * @author Bekarys
 */
public class Game {
    public static boolean exception = true;
    
    public String       name        =   null;
    public int          size        =   0;
    public int          ID          =   -1;
    public int          magnify     =   1;
    public int          check       =   0;
    public int          state       =   0; // 0-created  1-run
    
    public Pack         pack        =   null;
    public PlayerList   playerList  =   null;
    public Ground       ground      =   null;
    public Player       lastRequest =   null;
    
    Game(String _name, int _size, int _ID) {
        ID = _ID;
        name = _name;
        size = _size;
        playerList = new PlayerList();
        pack = new Pack();
        ground = new Ground();
        state = 0;
    }
    
    public void init() {
        ground.clear();
        if(!playerList.clear()){
            LogWriter.write("Game--init playerList.clear() FALSE");
            return;
        }
        check = 0;
        pack = new Pack(); 
        pack.initTrump(playerList.inGame);
        playerList.deal(6, pack);
        pack.setTrump(pack.getFirst());
        pack.putCard(pack.trump);

        magnify = ((pack.trump.getNumber() == 14 || pack.trump.getNumber() == 6)?(2):(1));        
    }
    
    public void clearRound() {
        //playerList.calc(magnify);
    }       
    
    public void endRound() {
        while(playerList.turn_id != ground.winner_id) 
            playerList.nextTurn();
        Player player = playerList.get(playerList.turn_id);
        
        player.earn.addAll(ground.cards);
        ground.clear();
        
        LogWriter.write("Game(endRound): winner is: "+player.name);
        LogWriter.write("Game(endRound): count of card to send: "+(6-player.hand.size()));
        int n = 6 - player.hand.size();
        for (int i = 0; i < n; ++ i) {
            if (pack.cards.size() > 0) {
                playerList.deal(pack);
            }
        }
    }
    
    public boolean tryJoin(Player player) {
        return playerList.tryAdd(player, size);
    }

    public String toString() {
        Map obj=new LinkedHashMap();
        obj.put("Gamename", this.name);
        obj.put("Gameindex", this.ID);
        obj.put("Gamesize", this.size);
        obj.put("Connected", this.playerList.size());

        return JsonHelper.toJSON(obj);
    }

}
