/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newserver;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Bekarys
 */
public class PlayerList {
    public int                  count       =   0; // counter
    public int                  get_card    =   0; // counter of get cards
    public int                  ready       =   0; // ready to the next battle
    public int                  turn_index  =   0; // index of player whos turn
    public int                  turn_id     =   0; // ID of player whos turn
    public int                  inGame      =   0; // count without loser
    public int                  maxScore    =   0; // needs to ask life
    public int                  bound       =   24; // max score to lose
    
    public ArrayList<Player>    players     =   null;
    public int                  round_win   =   -1;
    
    public boolean              debug       =   false; //comments;
    public static boolean       exception   =   true;
    
    PlayerList () {
        players = new ArrayList<Player>();
    }
     
    public void exit(Player player) {
        try {
            inGame--;
            if(player.ID == turn_id)
                nextTurn();
            players.remove(player);

            if (inGame == 1) {
                Map obj=new LinkedHashMap();
                obj.put("action", ActionTypes.ACTION_GAME_WINNER_BROADCAST);
                obj.put("player", getWinnerName()); 
                String json_response = JsonHelper.toJSON(obj);
                sendToAll(json_response);
            }
        } catch (Exception e) {
            if (exception) LogWriter.write("BEGIN SOCKETEXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(e.getStackTrace().toString());
            if (exception) LogWriter.write("END SOCKETEXCEPTION*********************");
            
        }
        
    }
    
    public String getWinnerName() {
        for (Player player: players) {
            if (player.state == Player.PLAY) {
                return player.name;
            }
        }
        return "";
    }
    
    public boolean clear() {
        if (inGame == 1) {
            Map obj=new LinkedHashMap();
            obj.put("action", ActionTypes.ACTION_GAME_WINNER_BROADCAST);
            obj.put("player", getWinnerName()); 
            String json_response = JsonHelper.toJSON(obj);
            sendToAll(json_response);
            return false;
        }
        for(Player player: players) {
            player.earn = new ArrayList<Card>();
            player.hand = new ArrayList<Card>();
            player.next = new ArrayList<Card>();
        }
        return true;
    }
    
    public void battleCards(ArrayList<Card> list, int player_id, boolean turn, Ground ground) {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.addAll(list);
        if (player_id != ground.winner_id) {
            cards.clear();
            for (int i = 0; i < list.size(); ++ i)
                cards.add(new Card(0,0));
        }
        if (turn) nextTurn();
        Map obj=new LinkedHashMap();
        obj.put("action", ActionTypes.ACTION_GAME_ROUND_CARD_PUT_RESPONSE);
        obj.put("card", cards.toString());
        String json_response = JsonHelper.toJSON(obj);

        Map obj2=new LinkedHashMap();
        obj2.put("action", ActionTypes.ACTION_GAME_ROUND_CARD_PUT_CALLBACK);
        obj2.put("card", cards.toString());
        String json_response2 = JsonHelper.toJSON(obj2);
        
        for(Player player: players) {
            if (player.ID != player_id) {
                player.sendToClient(json_response);
                LogWriter.write(player.name+" :"+json_response);
            } else {
                player.sendToClient(json_response2);
                LogWriter.write(player.name+" :"+json_response2);
            }
        }
    }
    
    public void calc(int x) {
        Player winner = null;
        int spas = ((this.players.size() == 2)?(41):(31));

        for (Player player: players) {
            player.calcPoint();
            
            if (winner == null) winner = player;
            else if (winner.point < player.point) winner = player;
            
            LogWriter.write("Player "+player.name+" points: "+player.point); // log for system
        }
        
        maxScore = 0;
        for (Player player: players) {
            maxScore = ((maxScore<player.score)?(player.score):(maxScore));
            if (player == winner) {
                player.point = 0;
                LogWriter.write("Player "+player.name+" score: "+player.score); // log for system
                continue;
            }
            if (player.point >= spas) {
                player.score += 2*x; // spas
            }
            else if (player.point < spas) {
                if (player.point == 0 && player.earn.size() != 0) {
                    player.score += 4*2*x; // bez spase i ochkov
                } else if (player.earn.size() > 0) {
                    player.score += 4*x; // bez spasa
                } else if (player.earn.size() == 0) {
                    player.score += 6*x; // bez spasa
                }
            }
            player.point = 0;
            LogWriter.write("Player "+player.name+" score: "+player.score); // log for system

            if (player.score > bound) {
                if (player.life <= 0) {
                    player.state = Player.LOSE;
                    inGame --;
                    LogWriter.write("Player "+player.name+" LOSE; inGame:"+inGame);
                }
            } else {
                maxScore = ((maxScore<player.score)?(player.score):(maxScore));               
            }
        } // score calculated
        
    }
    
    public void nextTurn() {
        for (int i = 1; i < 2*size(); ++ i) {
            if (players.get((turn_index+i)%size()).state != Player.LOSE) {
                if (debug) LogWriter.write("***Turn was "+players.get(turn_index).name+" -> "+players.get((turn_index+i)%size()).name);
                turn_index = (turn_index+i)%size();
                turn_id = players.get(turn_index).ID;
                return;
            }
        }
    }
    
    public String getEarnAll() {
        Map obj=new LinkedHashMap();
        obj.put("action", ActionTypes.ACTION_GAME_ROUND_RESULT_RESPONSE);
        for (Player _player: players) {
            obj.put(_player.name, _player.earn.toString());
        }
        return JsonHelper.toJSON(obj);
    }
    
    public void deal(Pack pack) {
        for (int i = 0; i < size(); ++ i) {
            Player player = get(turn_id);
            Card card = pack.getFirst();
            player.next.add(card);
            nextTurn();
        }
    }

    public void preCalc(int x) {
        Player winner = null;
        int spas = ((this.players.size() == 2)?(41):(31));

        for (Player player: players) {
            player.calcPoint();
            
            if (winner == null) winner = player;
            else if (winner.point < player.point) winner = player;
        }
        
        maxScore = 0;
        int sc = 0;
        for (Player player: players) {
            if (player == winner) {
                player.point = 0;
                maxScore = ((maxScore<player.score)?(player.score):(maxScore));
                continue;
            }
            sc = player.score;
            if (player.point >= spas) {
                sc += 2*x; // spas
            }
            else if (player.point < spas) {
                if (player.point == 0 && player.earn.size() != 0) {
                    sc += 4*2*x; // bez spase i ochkov
                } else if (player.earn.size() > 0) {
                    sc += 4*x; // bez spasa
                } else if (player.earn.size() == 0) {
                    sc += 6*x; // bez spasa
                }
            }
            player.point = 0;

            if (sc <= bound) {
                maxScore = ((maxScore<sc)?(sc):(maxScore));
            }
        } // score calculated
    }
        
    public void deal(int n, Pack pack) {
        for (Player player : players) {
            if (player.state == Player.PLAY) {
                player.hand.addAll(pack.getCards(6));

                Map obj=new LinkedHashMap();
                obj.put("action", ActionTypes.ACTION_GAME_ROUND_FIRST_RESPONSE);
                obj.put("cards", player.hand.toString());
                obj.put("trump", pack.cards.get(36 - 6*players.size() - 1).toString());
                obj.put("after-trump", pack.after_trump.toString());
                String json_response = JsonHelper.toJSON(obj);

                LogWriter.write("        sendCards to "+player.name+" "+player.hand.toString()+"; kozyr-"+
                        pack.cards.get(36 - 6*players.size() - 1).toString()+"; nextKozyr-"+pack.after_trump.toString());
                player.sendToClient(json_response);
            }
        }
    }
    
    public boolean isTurn(int _turn_id) {
        return (turn_id == _turn_id);
    }
    
    public void initTurn() {
        if (turn_index % count != 0)
            LogWriter.write("****** WRONG TURN CALCULATED "+turn_index+"%count!=0");
        
        turn_index /= count;
        turn_id = players.get(turn_index).ID;
        count = 0;
    }
    
    public boolean guesTurn(int _turn) {
        turn_index += _turn;
        count ++;
        return (this.size() == count);
    }
    
    public void insert(Player player, int size) {
        if (size == 2) player.life = 0;
        players.add(player);
        inGame ++;
        LogWriter.write("Player "+player.name+" join to game");
    }
    
    // send all player except new about joining the player
    public boolean tryAdd(Player player, int size) {
        if (size > players.size()) {
            if (debug) System.out.println("joined");
            // generate message to all players
            Map obj=new LinkedHashMap();
            obj.put("action", ActionTypes.ACTION_GAME_JOIN_EXCEPT_RESPONSE);
            obj.put("name", player.name);
            String json_response = JsonHelper.toJSON(obj);

            for (Player _player: players) {
                _player.sendToClient(json_response);
            }
            
            insert(player, size);
            return true;
        }
        return false;
    }
    
    public boolean gameAlive() {
        if (inGame == 0) return false;
        for (Player player: players) {
            if (player.ping()) {
                return true;
            }
        }
        return false;
    }
    
    public void sendToAll(String message) {
        for (Player player: players) {
            if (player.state == Player.PLAY){
                player.sendToClient(message);
            }
        }
    }
    
    public String toString() {
        return players.toString();
    }
    
    public Player get(int ID) {
        for(Player player: players) {
            if (player.ID == ID) {
                return player;
            }
        }
        return null;
    }

    public Player get(String name) {
        for(Player player: players) {
            if (player.name.equals(name)) {
                return player;
            }
        }
        return null;
    }
    
    public int size() {
        return players.size();
    }
}
