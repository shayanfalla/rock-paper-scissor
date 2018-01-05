package client.view;

import common.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class GameInterpreter implements Runnable {

    private static final String PROMPT = "> ";
    private final Scanner input = new Scanner(System.in);
    private Game game;
    private PlayerDTO player = null;
    private boolean loggedIn = false;
    private NotificationHandler notif;
    String username;
    String move;
    private final ThreadSafeStdOut outMgr = new ThreadSafeStdOut();

    public void start(Game gm) {
        this.game = gm;
        try {
            this.notif = new NotificationHandler();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (loggedIn) {
            return;
        }
        loggedIn = true;

        new Thread(this).start();
    }

    @Override
    public void run() {
        outMgr.println("~~Welcome to the Game~~");
        while (loggedIn) {
            try {
                System.out.println();
                outMgr.println("Enter command: ('help' for list of commands)");
                CmdLine cmdLine = new CmdLine(readNextLine());
                //CmdLine cmdLine = new CmdLine(input.nextLine());

                switch (cmdLine.getCmd()) {
                    case HELP:
                        System.out.println("~~The commands are~~");
                        for (Command command : Command.values()) {
                            if (command == Command.ILLEGAL_COMMAND || command == Command.NO_COMMAND) {
                                continue;
                            }
                            System.out.println(command.toString().toLowerCase());
                        }
                        break;
                    case CONNECT:
                        outMgr.println("Choose a username: ");
                        username = readNextLine();
                        game.pickUsername(username);
                        //game.pickUsername(cmdLine.getArgument(0));
                        break;
                    case LOGIN:
                        outMgr.println("Give username: ");
                        username = readNextLine();
                        this.player = game.login(username);
                        break;
                    case LOGOUT:
                        this.player = null;
                        outMgr.println("You have logged out the game. Log in, to play again!");
                        break;
                    case PLAY:
                        if(this.player != null) {
                            outMgr.println("Make a move!");
                            move = readNextLine();
                            game.playGame(this.player, move, this.notif);
                        }
                        break;
                    case QUIT:
                        outMgr.println("Exitting the game...");
                        loggedIn = false;
                        game.deletePlayer(username);
                        System.exit(0);
                }
            } catch (Exception e) {
                outMgr.println("Operation failed");
                outMgr.println(e.getMessage());
            }
        }
    }

    private String readNextLine() {
        outMgr.print(PROMPT);
        return input.nextLine();
    }

    private class NotificationHandler extends UnicastRemoteObject implements MessageToPlayers {

        public NotificationHandler() throws RemoteException {
        }

        @Override
        public void recvMsg(String msg) throws RemoteException {
            outMgr.println(msg);
        }
    }
}
