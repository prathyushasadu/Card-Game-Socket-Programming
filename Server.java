import java.io.*;
import java.util.*;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;


public class Server {

	
	public static void main (String [] args) throws Exception{
		ServerSocket serverSocket = new ServerSocket(12347);
		System.err.println("Waiting for new connection " );
		try{
			
			while(true){
				CardGame game = new CardGame();
				CardGame.Players playerA = game.new Players(serverSocket.accept(), "player1");
				CardGame.Players playerB = game.new Players(serverSocket.accept(),"player2");
				CardGame.Players playerC = game.new Players(serverSocket.accept(), "player3");
				CardGame.Players playerD = game.new Players(serverSocket.accept(), "player4");
				
				
				
				
				//nextPlayer
				
				playerA.nextPlayer(playerB);
				playerB.nextPlayer(playerC);
				playerC.nextPlayer(playerD);
				playerD.nextPlayer(playerA);
				
							
			// current player
				//game.currentPlayer = playerA;
				
			// nextround player
				game.newRoundPlayer = playerA;
				
				playerA.nextRoundPlayer(playerB);
				playerB.nextRoundPlayer(playerC);
				playerC.nextRoundPlayer(playerD);
				playerD.nextRoundPlayer(playerA);
				
				
				
				// starting client thread
				playerA.start();
				playerB.start();
				playerC.start();
				playerD.start();				
			}
		}finally{
			serverSocket.close();
		}
	}	
}
