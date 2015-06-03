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
public class GameList {
    public ArrayList<Game>   games    =   null;
    
    GameList() {
        games = new ArrayList<Game>();
    }
    
    public void insert(Game newGame) {
        games.add(newGame);
    }
    
    public Game get(int ID) {
        for (Game game: games) {
            if (game.ID == ID) {
                return game;
            }
        }
        return null;
    }
    
    // first check all game clear it and return list as string
    public String toString() {
        ArrayList<Game> trush = new ArrayList<Game>();
        for (Game game: games) {
            if (!game.playerList.gameAlive()) {
                trush.add(game);
            }
        }
        for (Game game: trush) {
            games.remove(game);
        }
        ArrayList<Game> response = new ArrayList<Game>();
        for (Game game: games) {
            if (game.state == 0) {
                response.add(game);
            }
        }
        return response.toString();
    }
}
