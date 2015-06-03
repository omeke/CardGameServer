/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newserver;

/**
 *
 * @author Bekarys
 */
public class ActionTypes {
    public static final int         ACTION_LOGIN = 1;
    public static final String      ACTION_LOGIN_RESPONSE = "";
    public static final int         ACTION_EXIT = 2;
    public static final String      ACTION_EXIT_RESPONSE = "";
    public static final int         ACTION_PING = 3;
    public static final String      ACTION_PING_RESPONSE = "";

    public static final int         ACTION_GAME_EXIT = 4;
    public static final String      ACTION_GAME_EXIT_RESPONSE = "exit-received";
    public static final String      ACTION_GAME_EXIT_RESPONSE_BROADCAST = "exit-broadcast";
    
    //***************************** клиент просит список игр
    public static final int         ACTION_GAME_LIST = 5; 
    public static final String      ACTION_GAME_LIST_RESPONSE = "list-received";
    
    //***************************** клиент хочет присоединиться к игре
    public static final int         ACTION_GAME_JOIN = 6; 
    public static final String      ACTION_GAME_JOIN_RESPONSE = "join-received";
    public static final String      ACTION_GAME_JOIN_EXCEPT_RESPONSE = "join-player";
    
    //***************************** клиент начинает игру 
    public static final int         ACTION_GAME_START = 7; 
    public static final String      ACTION_GAME_START_RESPONSE = "pack-received";
    
    //***************************** клиент создает игру
    public static final int         ACTION_GAME_CREATE = 8; 
    public static final String      ACTION_GAME_CREATE_RESPONSE = "create-received";
    
    public static final int         ACTION_GAME_RECONNECT = 9;
    public static final String      ACTION_GAME_RECONNECT_RESPONSE = "";

    public static final int         ACTION_GAME_ROUND_END = 10;
    
    public static final int         ACTION_GAME_ROUND_NEXT = 11;

    //***************************** 
    public static final int         ACTION_GAME_ROUND_NEXT_BROADCAST = 17; 
    public static final String      ACTION_GAME_ROUND_NEXT_RESPONSE_BROADCAST = "ready-broadcast";

    
    //***************************** клиент говорит кто ходит
    public static final int         ACTION_GAME_ROUND_FIRST = 12;
    public static final String      ACTION_GAME_ROUND_FIRST_RESPONSE = "cards-received";
    
    //***************************** карты заработанные клиентами
    public static final int         ACTION_GAME_ROUND_RESULT = 13; 
    public static final String      ACTION_GAME_ROUND_RESULT_RESPONSE = "finish-card-received";
    
    // **************************** клиент просит карту
    public static final int         ACTION_GAME_ROUND_CARD_GET = 14;
    public static final String      ACTION_GAME_ROUND_CARD_GET_BROADCAST = "get-card-broadcast";
    
    // **************************** клиент ходит карту
    public static final int         ACTION_GAME_ROUND_CARD_PUT = 15; 
    public static final String      ACTION_GAME_ROUND_CARD_PUT_CALLBACK = "put-card-received";
    public static final String      ACTION_GAME_ROUND_CARD_PUT_RESPONSE = "enemy-card-received";
    
    //***************************** запрос на жизнь
    public static final int         ACTION_GAME_ASK_LIFE = 16; 
    public static final String      ACTION_GAME_ASK_LIFE_RESPONSE = "ask-life-received";
    
    //***************************** answer for request // give-life
    public static final int         ACTION_GAME_GIFE_LIFE = 17; 
    public static final String      ACTION_GAME_GIFE_LIFE_RESPONSE = "give-life-received";
    
    //***************************** broadcast // give-life
    public static final int         ACTION_GAME_GIFE_LIFE_BROADCAST = 17; 
    public static final String      ACTION_GAME_GIFE_LIFE_BROADCAST_RESPONSE = "give-life-broadcast";

    //***************************** broadcast // winner
    public static final String      ACTION_GAME_WINNER_BROADCAST = "winner-broadcast";
    
    
    public static int getType(String action) {
        if (action.equalsIgnoreCase("login")) 
        {
            return ACTION_LOGIN;
        }
        if (action.equalsIgnoreCase("ping")) 
        {
            return ACTION_PING;
        }
//-------------------------------------------------------------------//        
        if (action.equalsIgnoreCase("create-game")) 
        {
            return ACTION_GAME_CREATE;
        }
        if (action.equalsIgnoreCase("list-game")) 
        {
            return ACTION_GAME_LIST;
        }
        if (action.equalsIgnoreCase("join-game")) 
        {
            return ACTION_GAME_JOIN;
        }
        if (action.equalsIgnoreCase("start-game")) 
        {
            return ACTION_GAME_START;
        }
        if (action.equalsIgnoreCase("ask-life")) 
        {
            return ACTION_GAME_ASK_LIFE;
        }
        if (action.equalsIgnoreCase("give-life")) 
        {
            return ACTION_GAME_GIFE_LIFE;
        }
        if (action.equalsIgnoreCase("exit")) 
        {
            return ACTION_GAME_EXIT;
        }
//-------------------------------------------------------------------//
        if (action.equalsIgnoreCase("player-start")) 
        {
            return ACTION_GAME_ROUND_FIRST;
        }
        if (action.equalsIgnoreCase("start-next")) 
        {
            return ACTION_GAME_ROUND_NEXT;
        }
        if (action.equalsIgnoreCase("finish-card")) 
        {
            return ACTION_GAME_ROUND_RESULT;
        }
        if (action.equalsIgnoreCase("put-card")) 
        {
            return ACTION_GAME_ROUND_CARD_PUT;
        }
        if (action.equalsIgnoreCase("end-round")) 
        {
            return ACTION_GAME_ROUND_END;
        }
        if (action.equalsIgnoreCase("get-card")) 
        {
            return ACTION_GAME_ROUND_CARD_GET;
        }
        
        
        return -1;
    }
}
