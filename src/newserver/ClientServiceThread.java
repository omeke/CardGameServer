/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author Bekarys
 */
class ClientServiceThread extends Thread {
    public Socket           clientSocket    =   null;
    public int              clientID        =   -1;
    public boolean          running         =   true;

    public ClientList       clientList      =   null;
    public GameList         gameList        =   null;
    public StackMessage     message         =   null;
    public JsonHelper       jsonHelper      =   null;

    public Game             game            =   null;
    public Player           player          =   null;
    public boolean          debug           =   false;
    public boolean          exception       =   true;
    public boolean          output          =   true;
    
    ClientServiceThread(Socket socket, int ID, ClientList _clientList, GameList _gameList) {
        clientSocket = socket;
        clientID = ID;
        clientList = _clientList;
        gameList = _gameList;
        message = new StackMessage();
        jsonHelper = new JsonHelper();
    }

    public void run() {
        try {
            BufferedReader   in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter     out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            while (running) {
                message.insert(in.readLine());                
                
                while (message.nextMessage() != null) { 
                    if (message.currMess.equalsIgnoreCase("ping")) LogWriter.write("Client Says :" + message.currMess);
                    if (debug) System.out.println("Client("+((player==null)?("unknouwn"):(player.name))+") Says :" + message.currMess);
                    if (output) LogWriter.write("Client("+((player==null)?("unknouwn"):(player.name))+") Says :" + message.currMess);
                    doAction(message.currMess);
                }
            }
        } catch (SocketException e) {
            if (game != null) game.playerList.inGame --;
            else if (exception) LogWriter.write("SocketException:: game is null");

            if (player != null) player.state = Player.LOST;
            else if (exception) LogWriter.write("SocketException:: player is null");
               
            if (exception) LogWriter.write("BEGIN SOCKETEXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(e.getStackTrace().toString());
            if (exception) LogWriter.write("END SOCKETEXCEPTION*********************");
        } catch (Exception e) {            
            if (exception) LogWriter.write("BEGIN EXCEPTION*******************");
            if (exception) LogWriter.write((new Date()).toString());
            if (exception) LogWriter.write(e.getStackTrace().toString());
            if (exception) LogWriter.write("END EXCEPTION*********************");
        }
    }
    
    // авторизация клиента
    public void actionLogin() {
        player = new Player(
                        clientSocket, 
                        (String)jsonHelper.findByName("name"),
                        clientID, clientList);
        LogWriter.write("actionLogin: "+player.name+" id:"+player.ID);
        player.sendToClient(message.currMess); // send back request as response
    }
    
    // клиент создает игру
    public void actionCreateGame() {
        int isCreated = 0; // 1 true || 0 false
        int game_size = 0;
        if (game == null && player != null) {
            game_size = ((Long) jsonHelper.findByName("game-size")).intValue(); // на сколько играков // tested 
            game = new Game(player.name, game_size, player.ID); // Новая игра с именем Игрока
            gameList.insert(game); // Добовляем игру в общии лист
            game.playerList.insert(player, game_size); // Добавляем игрока в список игры
            isCreated = 1; // Игра создана
        }
        
        // Отправляем ответ клиенту
        Map obj=new LinkedHashMap();
        obj.put("action", ActionTypes.ACTION_GAME_CREATE_RESPONSE);
        obj.put("message", isCreated);
        obj.put("game-size", game_size);
        String json_response = JsonHelper.toJSON(obj); // get message converted to json
        
        LogWriter.write("Game '"+player.name+"': game for "+game_size+" "+((isCreated==1)?("created"):("NOT created")));
        player.sendToClient(json_response); // Отправляем клиенту о содании или нет
    }
    
    // клиент просит список игр
    public void actionListGame() {
        Map obj=new LinkedHashMap();
        obj.put("action", ActionTypes.ACTION_GAME_LIST_RESPONSE);
        obj.put("games", gameList.toString());
        String json_response = JsonHelper.toJSON(obj); // get message converted to json

        //LogWriter.write("'"+player.name+"' ListGame: "+json_response); // log for system
        player.sendToClient(json_response); // sends message to client        
    }
    
    // клиент хочет присоединиться к игре
    public void actionJoinGame() {
        int game_index = ((Long)jsonHelper.findByName("game-index")).intValue(); // get index of game // tested
        int isJoined = 0; // 0 - false || 1 - true

        Game targetGame = this.gameList.get(game_index); // find in gameList game with ID
        if (targetGame != null && targetGame.tryJoin(player)) { // game exist with ID and player joined
            game = targetGame;
            isJoined = 1;
        }

        Map obj=new LinkedHashMap();
        obj.put("action", ActionTypes.ACTION_GAME_JOIN_RESPONSE);
        obj.put("message", isJoined);
        String json_response = JsonHelper.toJSON(obj);

        if (debug) LogWriter.write("actionJoinGame: '"+player.name+"' "+((isJoined==1)?("joined"):("NOT joined"))+((game==null)?(""):(" to game '"+game.name+"'"))); // log for system
        player.sendToClient(json_response); // sends message to client        
    }
    
    // клиент начинает игру 
    public void actionStartGame() {
        Map obj=new LinkedHashMap();
        obj.put("action", ActionTypes.ACTION_GAME_START_RESPONSE);
        obj.put("pack", game.pack.cards.toString()); 
        obj.put("players", game.playerList.toString());
        String json_response = JsonHelper.toJSON(obj);
        
        game.state = 1; // state of game "run"

        if (debug) LogWriter.write("actionStartGame: Game '"+game.name+"' started: pack-"+json_response); // log for system
        game.playerList.sendToAll(json_response); // sends message to all client
    }
   
    // клиент говорит кто ходит
    public void actionStartRound() {
        int gues_turn = ((Long)jsonHelper.findByName("turn")).intValue(); // tested
        if (game.playerList.guesTurn(gues_turn) ) {
            game.playerList.initTurn();
            game.init();
//            LogWriter.write("Game("+game.name+") first round begins with turn of "+game.playerList.get(game.playerList.turn_id).name);
        }
        
    }
    
    // клиент ходит карты
    public void actionPutCard() {
//        if (debug) LogWriter.write("actionPutCard(turn): "+game.playerList.get(game.playerList.turn_id).name);
        if (game.playerList.isTurn(player.ID) && game.playerList.get_card == 0) { // his turn its ok and all players gets cards
            LinkedList<Object> list = (LinkedList<Object>) jsonHelper.findByName("card");
            ArrayList<Card> received_card = jsonHelper.convertToArray(list);
            
            if (received_card == null) {
                if (debug) LogWriter.write("actionPutCard: from player "+player.name+"received_card is NULL!!!");
            } else if (received_card.size() == 0) { // received empty means end round
                game.endRound();
                game.playerList.battleCards(received_card, player.ID, false, game.ground);
//                LogWriter.write("now turn of "+game.playerList.get(game.playerList.turn_id).name);
            } else {
                player.getCard(received_card);
                game.ground.addCards(received_card, game.pack.trump, player.ID, (game.playerList.inGame == 2)); // add card to ground
                game.playerList.battleCards(received_card, player.ID, true, game.ground);
                if (game.playerList.inGame == 2) {
                    if (game.ground.round%2 == 0) {
                        game.playerList.nextTurn(); // opiat' hodit tot kto srezal
                    }
                    if (game.ground.cards.size() == 12 || (game.ground.first_turn_id == game.ground.winner_id && game.ground.round%2 == 0)) {
                        //game.endRound();
                    }
                } else {
                }
            }
        } else if(game.playerList.get_card != 0) {
            LogWriter.write("ERRORR WITH ACTION PUT CARD: action put card received eirly");
            message.container += message.currMess;
            //client_id = this; needs some idea
        } else {
            if (debug) LogWriter.write("ERRORR WITH ACTION PUT CARD: wrong turn expect:"+game.playerList.get(game.playerList.turn_id).name+" receive:"+player.name);
        }
        
    }

    // клиент хочет карты
    public void actionGetCard() {
        game.playerList.get_card ++;
        if (game.playerList.get_card == game.playerList.inGame) {
            game.playerList.get_card = 0;
        }        
        player.sendCard(); // send card which prepared
             
        Map obj=new LinkedHashMap();
        obj.put("action", ActionTypes.ACTION_GAME_ROUND_CARD_GET_BROADCAST);
        obj.put("player", player.name); 
        String json_response = JsonHelper.toJSON(obj);
        game.playerList.sendToAll(json_response);
        
        if (debug) LogWriter.write(json_response);
    }
    
    // карты заработанные клиентами
    public void actionRoundResult() {
        if (game.ground.cards.size() != 0) {
            game.endRound();
        }
        if(game.check == 0) {
            game.playerList.calc(game.magnify);
        }
        game.check++;
        String json_response = game.playerList.getEarnAll();

        LogWriter.write("    Resut:"+json_response); // log for system
        player.sendToClient(json_response); // sends message to client
    }
    
    // следующи раунд
    public void actionRoundNext() {
        game.playerList.ready ++;
        
        Map obj=new LinkedHashMap();
        obj.put("action", ActionTypes.ACTION_GAME_ROUND_NEXT_RESPONSE_BROADCAST);
        obj.put("player", player.name); 
        String json_response = JsonHelper.toJSON(obj);
        game.playerList.sendToAll(json_response);
        
        if (debug) LogWriter.write("    RoundNext(): game.playerList.ready="+game.playerList.ready+" game.playerList.inGame="+game.playerList.inGame);
        if (game.playerList.ready == game.playerList.inGame) {
            LogWriter.write("nextRound: "+game.playerList.ready +" == "+game.playerList.inGame);
            game.playerList.ready = 0;
            if (game.playerList.get(game.playerList.turn_id).state != Player.PLAY) {
                game.playerList.nextTurn();
            }
            //game.clearRound();
            game.init();
        }                    
    }
    
    // запрос жизнь
    public void actionAskLife() {
        game.lastRequest = player;
        Player targetPlayer = game.playerList.get((String) jsonHelper.findByName("player"));
        if (targetPlayer != null) {
            // begin send request ask life
            Map obj1=new LinkedHashMap();
            obj1.put("action", ActionTypes.ACTION_GAME_ASK_LIFE_RESPONSE);
            obj1.put("player", player.name);
            String json_response1 = JsonHelper.toJSON(obj1);
            targetPlayer.sendToClient(json_response1);
            // end send request ask life
            if (debug) LogWriter.write(targetPlayer.name+": "+json_response1);
        } else {
            if (debug) LogWriter.write("Aks life: Player NOT found");
        }
    }
    
    // ответ жизнь
    public void actionGiveLife() {
        String answer = (String) jsonHelper.findByName("answer");
        
        if (answer.equalsIgnoreCase("yes")) { // proverki na validnost jizni netu nujno dopisat
            game.lastRequest.sendToClient(message.currMess);
            player.life --;
            game.lastRequest.backToGame(Math.max(game.playerList.maxScore, 0));
            //game.playerList.inGame++;            

            Map obj=new LinkedHashMap();
            obj.put("action", ActionTypes.ACTION_GAME_GIFE_LIFE_BROADCAST_RESPONSE);
            obj.put("looser", game.lastRequest.name); 
            obj.put("helper", player.name);
            obj.put("points", game.lastRequest.score);
            String json_response = JsonHelper.toJSON(obj);
            
            game.playerList.sendToAll(json_response);
        }
        Map obj1=new LinkedHashMap();
        obj1.put("action", ActionTypes.ACTION_GAME_GIFE_LIFE_RESPONSE);
        obj1.put("answer", answer);
        obj1.put("player", player.name);
        String json_response1 = JsonHelper.toJSON(obj1);
        game.lastRequest.sendToClient(json_response1);        
    }
    
    public void actionExitGame() {
        Map obj=new LinkedHashMap();
        obj.put("action", ActionTypes.ACTION_GAME_EXIT_RESPONSE);
        String json_response = JsonHelper.toJSON(obj);
        player.sendToClient(json_response);
        
        game.playerList.exit(player);

        obj=new LinkedHashMap();
        obj.put("action", ActionTypes.ACTION_GAME_EXIT_RESPONSE_BROADCAST);
        obj.put("player", player.name);
        json_response = JsonHelper.toJSON(obj);
        game.playerList.sendToAll(json_response);
    }
    
    // accept messages like {"action": "put-card"}
    public void doAction(String actionJson) {
        jsonHelper.init(actionJson);
        
        switch (ActionTypes.getType((String)jsonHelper.findByName("action"))) {
            case ActionTypes.ACTION_LOGIN:
                actionLogin();
                break;
            case ActionTypes.ACTION_GAME_EXIT: // клиент exit from game
                actionExitGame();
                break;
            case ActionTypes.ACTION_GAME_CREATE: // клиент создает игру
                actionCreateGame();
                break;
            case ActionTypes.ACTION_GAME_LIST: // клиент просит список игр
                actionListGame();
                break;
            case ActionTypes.ACTION_GAME_JOIN: // клиент хочет присоединиться к игре
                actionJoinGame();
                break;
            case ActionTypes.ACTION_GAME_START: // клиент начинает игру
                actionStartGame();
                break;
            case ActionTypes.ACTION_GAME_ROUND_FIRST: // клиент говорит кто ходит
                actionStartRound();
                break;
            case ActionTypes.ACTION_GAME_ROUND_NEXT: // следующи раунд
                actionRoundNext();
                break;
            case ActionTypes.ACTION_GAME_ROUND_CARD_GET: // клиент хочет карты
                actionGetCard();
                break;
            case ActionTypes.ACTION_GAME_ROUND_CARD_PUT: // клиент ходит карты
                actionPutCard();
                break;
            case ActionTypes.ACTION_GAME_ROUND_RESULT: // карты заработанные клиентами
                actionRoundResult();
                break;
            case ActionTypes.ACTION_GAME_ASK_LIFE: // запрос жизнь
                actionAskLife();
                break;
            case ActionTypes.ACTION_GAME_GIFE_LIFE: // ответ жизнь
                actionGiveLife();
                break;
        }
    }
}

