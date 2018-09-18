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
import java.lang.Math;

public class CardGame{
	public int T1Score = 0;
    public int T2Score = 0;
    private static ArrayList<String> p1 = new ArrayList<String>();
    private static ArrayList<String> p2 = new ArrayList<String>();
    private static ArrayList<String> p3 = new ArrayList<String>();
    private static ArrayList<String> p4 = new ArrayList<String>();
    private static Hashtable<String,String> cards = new Hashtable<String,String>();
    private static Hashtable<String,Integer> rank1 = new Hashtable<String,Integer>();
	private static Hashtable<String, String> trick = new Hashtable<String, String>();
	Enumeration<String> keys = trick.keys();
	public static PrintWriter outputWriter;
	public static BufferedReader inputReader;
	public int teamAtotalBid;
	public int teamBtotalBid;
	public String team;
	public int trickRound;
	public String checkSuit;
	public String winner;
	public int numberOfTricks = 1;
	public int tricksWonA = 0;
	public int tricksWonB = 0;
	public String winningTeam;
	public int numberOfRound = 1, teamA_Score=0, teamB_Score=0;
	

	
	int bid1, bid2, bid3, bid4, count =0;
	
	
	Players currentPlayer;
	Players newRoundPlayer;
	int bid;
	
	/*public synchronized Players playerChange(Players player)
	{
		if(player == currentPlayer)
		{
			currentPlayer = player.nextPlayer;
		}
		return currentPlayer;
	}*/
	
	public class Players extends Thread{
		Socket socket;
		String name;
		Players player;
		ObjectInputStream in;
	    ObjectOutputStream out;
		String response;
		Players nextPlayer, nextRoundPlayer;
		private  ArrayList<String> deck = new ArrayList<String>();

		public Players(Socket socket, String name){
			BufferedReader inputReader;
			PrintWriter outputWriter;
			this.socket = socket;
			this.name = name;
			try{
				inputReader = new BufferedReader(new InputStreamReader (socket.getInputStream()));
				System.out.println("Started");	
			} catch(IOException e){
				System.out.println(e);
			}
		}
		
		public void nextPlayer(Players nextPlayer){
			this.nextPlayer = nextPlayer;	
		}
		public void nextRoundPlayer(Players nextRoundPlayer){
			this.nextRoundPlayer = nextRoundPlayer;
		}
		
		public void deck(ArrayList<String> deck)
		{
			this.deck = deck;
			
		}
		
		public void run()
		{
			try{
				out = new ObjectOutputStream(socket.getOutputStream());
				in  = new ObjectInputStream(socket.getInputStream());
				System.out.println(name);
				currentPlayer = newRoundPlayer;
				/*if(name== newRoundPlayer.name)
				{
					System.out.println("check");
					Distribution();
				}
				displayCard();*/
				if(name== currentPlayer.name)
				{
					currentPlayer.newRound();
				}
				while(true)
				{
					response = (String)in.readObject();
					if(response.startsWith("BID"))
					{
						int bid= Integer.parseInt(response.substring(3));
						if(checkBid(bid))
						{
							if(count<4){currentPlayer.enterBid();}
							else if(count == 4)
							{
								for(int k=0;k<4;k++)
								{
									currentPlayer.teamBID();
									currentPlayer=currentPlayer.nextPlayer;
								}
								count =0;
								currentPlayer.chooseCard();
							}
						}
					}
					else if(response.startsWith("CARD"))
					{
						String chosenCard = response.substring(4);
						if(checkCard(chosenCard))
						{
							count++;
							if(count<4)
							{
								currentPlayer= currentPlayer.nextPlayer;
								currentPlayer.printTrick();
								currentPlayer.chooseCard();
							}
							else if(count==4)
							{
								count = 0;
								trickTaken(trick);
								//roundWon();
							}
						}				
					}
					else if(response.startsWith("QUIT"))
					{
						return;
					}
				}
				
			}catch(Exception e){
				System.out.println("Test2 error1");
				System.out.println(e);
			}
		}
		public  void Distribution() 
		{
			String[] suits = {"S","D","C","H"};
			//String[] rank  = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
			String[] rank  = {"2","3","4","5"};
			int size = suits.length*rank.length;
			String[] deck = new String[size];
			/*for(int i =0 ; i< 52;i++)
			{
				deck[i] = rank[i%13]+suits[i/13];
				cards.put(deck[i],suits[i/13]);
				rank1.put(deck[i],(i%13));
				String suit1 = cards.get(deck[i]);
				int rank2 = rank1.get(deck[i]);
			}*/
			for(int i =0 ; i< 16;i++)
			{
				deck[i] = rank[i%4]+suits[i/4];
				cards.put(deck[i],suits[i/4]);
				rank1.put(deck[i],(i%4));
				String suit1 = cards.get(deck[i]);
				int rank2 = rank1.get(deck[i]);
			}
			int k = 0;
			/*for (int i = 0 ; i<52;i++)
			{
				int index = (int)(Math.random() * (deck.length-k));
				String temp = deck[51-k];
				deck[51-k] = deck[index];
				deck[index] = temp;
				k++;
			}*/
			for (int i = 0 ; i<16;i++)
			{
				int index = (int)(Math.random() * (deck.length-k));
				String temp = deck[15-k];
				deck[15-k] = deck[index];
				deck[index] = temp;
				k++;
			}
			p1.clear();
			p2.clear();
			p3.clear();
			p4.clear();
			int j = 1;
			//for(int i = 0; i < 52 ; i++)
				for(int i = 0; i < 16 ; i++)
			{
				int p = 0;
				if(j == 1)
				{
					p1.add(deck[i]);
					j++;p++;
					continue;
				}
				else if(j == 2)
				{
					p2.add(deck[i]);
					j++;p++;
					continue;
				}else if(j==3)
				{
					p3.add(deck[i]);
					j++;p++;
					continue;
				}else if(j == 4)
				{
					p4.add(deck[i]);
					j = 1;p++;
					continue;
				}
			}

			Collections.sort(p1);
			System.out.println(p1);


		}
		
		public void displayCard()throws Exception{
		
			if(currentPlayer.name == "player1")
            {
				//Message obj_test = new Message(p1);
				out.writeObject("CARDS" +p1);
			}
            else if(currentPlayer.name == "player2")
            {
				//Message obj_test = new Message(p2);
				out.writeObject("CARDS" +p2);
            }
            else if(currentPlayer.name == "player3")
            {
				//Message obj_test = new Message(p3);
				out.writeObject("CARDS" +p3);
            }
			else if(currentPlayer.name == "player4")
            {
				//Message obj_test = new Message(p4);
				out.writeObject("CARDS" +p4);
            }
			else
			{
				System.out.println("None of them");
			}
			

		}
	
		public void newRound() throws Exception 
		{
			tricksWonA = 0;
			tricksWonB = 0;
			Distribution();
			for(int i=0;i<4;i++)
		{
			currentPlayer.displayCard();
			currentPlayer=currentPlayer.nextPlayer;
			System.out.println("currentPlayer=  "+ currentPlayer);
		}
			currentPlayer.enterBid();
		}
		
		public void enterBid() throws Exception
		{
			
			out.writeObject("**"+"You belong to "+team(this)+"\n\n"+"**************** NEW ROUND ***********"+"\n");
			out.writeObject("Message1 please enter your bid");
		}
		
		public void teamBID() throws Exception
		{
			teamAtotalBid = bid1+bid3;
			teamBtotalBid = bid2+bid4;
			out.writeObject("TotalBid TeamA TotalBid = "+teamAtotalBid+"\n"+"TeamB TotalBid = "+ teamBtotalBid);
		}
		
		public boolean checkBid(int chosenBid) throws Exception
		{
			boolean done = false;
			boolean result=false;
			while(!done)
			{
				//if(chosenBid <= 13 && chosenBid >= 0)
					if(chosenBid <= 4 && chosenBid >= 0)
				{
					if(name == "player1")
					{	bid1= chosenBid;}
					else if(name == "player2"){ bid2= chosenBid;}
					else if(name == "player3"){ bid3= chosenBid;}
					else if(name == "player4") {bid4= chosenBid;}
					currentPlayer = currentPlayer.nextPlayer;
					count++;
					
					out.writeObject("Valid bid");
					
					done = true;
					result= true;
				}	
				else
				{
					out.writeObject("Message Please enter a valid bid number");
					done = true;
					result =false;
				}
			}
			return result;		
		}
		
		public void chooseCard() throws Exception
		{
			//if(numberOfTricks <14)
				if(numberOfTricks <5)
			{			
				out.writeObject("**"+"***** Trick "+ numberOfTricks + "*****");
				if(currentPlayer.name == "player1")
				{
					out.writeObject("SELECT please select a card from your cards" + " \n" + p1);
				}
				else if(currentPlayer.name == "player2")
				{
					out.writeObject("SELECT please select a card from your cards" + " \n" + p2);
				}
				else if(currentPlayer.name == "player3")
				{
					out.writeObject("SELECT please select a card from your cards" + " \n" + p3);
				}
				else if(currentPlayer.name == "player4")
				{
					out.writeObject("SELECT please select a card from your cards" + " \n" + p4);
				}
			
			}
			else 
			{
				numberOfTricks = 1;
				System.out.println("in choosecard methos");
				roundWon();
			}
		}
		
		
		public synchronized boolean checkCard(String card) throws Exception 
		{   boolean result = false; int l = card.length();
			if(currentPlayer.name == "player1")
			{
				if(p1.contains(card))
				{
					if(count == 0)
					{
						checkSuit = card.substring(l-1);
						System.out.println("checking siut= "+checkSuit);
						out.writeObject("VALID CARD");
						trick.put("player1",card);
						p1.remove(card);
						result =  true;
					}
					else
					{
						if(contains(checkSuit, p1))
						{
							if((card.substring(l-1)).equals(checkSuit))
							{
								out.writeObject("VALID CARD");
								trick.put("player1",card);
								p1.remove(card);
								result = true;	
							}
							else 
							{
								out.writeObject("SELECT please select a card from your cards" + " \n" + p1);
								result = false;			
							}
						}
						else 
						{
							out.writeObject("VALID CARD");
							trick.put("player1","invalid");
							System.out.println("check1:"+trick.get("player1"));
							p1.remove(card);
							result = true;
						}
					}
				}
				else
				{
					out.writeObject("SELECT please select a card from your cards" + " \n" + p1);
					result = false;
				}
			}
			else if(currentPlayer.name == "player2")
			{
				if(p2.contains(card))
				{
					if(count == 0)
					{
						checkSuit = card.substring(l-1);
						System.out.println("checking siut= "+checkSuit);
						out.writeObject("VALID CARD");
						trick.put("player2",card);
						p2.remove(card);
						result = true;
					}
					else
					{	
						if(contains(checkSuit, p2))
						{
							if((card.substring(l-1)).equals(checkSuit))
							{
								out.writeObject("VALID CARD");
								trick.put("player2",card);
								p2.remove(card);
								result = true;
							}
							else 
							{
								out.writeObject("SELECT please select a card from your cards" + " \n" + p2);
								result = false;
							}
						}
						else 
						{
							out.writeObject("VALID CARD");
							trick.put("player2","invalid");
							System.out.println("check2:"+trick.get("player2"));
							p2.remove(card);
							result = true;
						}
					}
				}
				else
				{
					out.writeObject("SELECT please select a card from your cards" + " \n" + p2);
					result = false;
				}
			}
			else if(currentPlayer.name == "player3")
			{
				if(p3.contains(card))
				{
					if(count == 0)
					{
						checkSuit = card.substring(l-1);
						out.writeObject("VALID CARD");
						trick.put("player3",card);
						p3.remove(card);
						result = true;
					}
					else
					{
						if(contains(checkSuit, p3))
						{
							if((card.substring(l-1)).equals(checkSuit))
							{
								out.writeObject("VALID CARD");
								trick.put("player3",card);
								p3.remove(card);
								result = true;
							}
							else 
							{
								out.writeObject("SELECT please select a card from your cards" + " \n" + p3);
								result = false;
							}
						}
						else 
						{
							out.writeObject("VALID CARD");
							trick.put("player3","invalid");
							System.out.println("check3:"+trick.get("player3"));
							p3.remove(card);
							result = true;
						}
					}
				}
				else
				{
					out.writeObject("SELECT please select a card from your cards" + " \n" + p3);
					result = false;
				}
			}
			else if(currentPlayer.name == "player4")
			{
				if(p4.contains(card))
				{
					if(count == 0)
					{
						checkSuit = card.substring(l-1);
						out.writeObject("VALID CARD");
						trick.put("player4",card);
						p4.remove(card);
						result = true;
					}
					else
					{
						if(contains(checkSuit, p4))
						{
							if((card.substring(l-1)).equals(checkSuit))
							{
								out.writeObject("VALID CARD");
								trick.put("player4",card);
								p4.remove(card);
								result = true;
							}
							else
							{	
								out.writeObject("SELECT please select a card from your cards" + " \n" + p4);
								result = false;
							}
						}
						else 
						{
							out.writeObject("VALID CARD");
							trick.put("player4","invalid");
							System.out.println("check4:"+trick.get("player4"));
							p4.remove(card);
							result = true;
						}
					}
				}
				else
				{
					out.writeObject("SELECT please select a card from your cards" + " \n" + p4);
					result = false;
				}
			}
			return result;
		}
		
		public String team(Players player)
		{
			if(player.name ==  "player1" || player.name == "player3")
			{
				 team = "TeamA";
			}
			else if(player.name == "player2" ||player.name == "player4")
			{
				team = "TeamB";
			}
			return team;
			
		}
		public boolean contains(String suit, ArrayList<String> p)
		{
			boolean result = false;
			for(String s : p)
			{	int length = s.length();
				if((s.substring(length-1)).matches(suit))
				{
					result = true;
					break;
				}
			}	
			return result;
		}
		public void printTrick() throws Exception
		{
			out.writeObject("TRICK" + trick);
		}
		
		public void trickTaken(Hashtable<String,String> hm) throws Exception
		{
            int size = hm.size();
            ArrayList<String> trickplayer = new ArrayList<String>();
            String[] cardPlayed = new String[size];
            Set<String> keys = trick.keySet();
            for(String key: keys)
            {
				trickplayer.add(key);
            }
            for(int i = 0 ; i<hm.size();i++)
            {
			    cardPlayed[i] = hm.get(trickplayer.get(i));
            }
            String LeadCard = "\0";
			int p = 0;
            for(int j = 0 ; j < cardPlayed.length-1 ; j++)
            {
				System.out.println("value:  " + j+ "card"+ cardPlayed[j]);
				if(cardPlayed[j] == "invalid")
				{
					rank1.put(cardPlayed[j],-1);
					System.out.println("erro check"+rank1.get(cardPlayed[j]));
					p++;
				}
				if(cardPlayed[j+1] == "invalid")
				{
					rank1.put(cardPlayed[j+1],-1);
					System.out.println("erro check"+rank1.get(cardPlayed[j+1]));
					p++;
				}
                if(rank1.get(cardPlayed[j]) > rank1.get(cardPlayed[j+1]))
                {
                    cardPlayed[j+1] = cardPlayed[j];
                    LeadCard = cardPlayed[j+1];
					System.out.println("leader card" + LeadCard);
                }
				else
				{
					LeadCard = cardPlayed[j+1];
				}
            }
			if(p > 0)
			{
				rank1.remove("invalid",-1);
			}
			
            for(int i = 0 ; i<hm.size();i++)
            {
                if(hm.get(trickplayer.get(i)) == LeadCard)
                {
					winner = trickplayer.get(i);
                    break;
                }
            }
			trick.clear();
			if(winner == "player1"|| winner == "player3")
			{
				winningTeam = "TeamA";
				tricksWonA++;
			}
			else if(winner == "player2" || winner == "player4")
			{
				winningTeam = "TeamB";
				tricksWonB++;
			}
			for(int i=0;i<4;i++)
			{
				currentPlayer.printWinner();
				currentPlayer = currentPlayer.nextPlayer;
			}
			for(int i=0; i<4; i++)
			{
				if(currentPlayer.name == winner)
				{
					numberOfTricks++;
					currentPlayer.chooseCard();
					break;
				}
				else
				{
					currentPlayer = currentPlayer.nextPlayer;
				}
				
			}
		}
		
		public void printWinner() throws Exception
		{
			//if(numberOfTricks <13)
				if(numberOfTricks <4)
			{
				out.writeObject("WON_TRICK"+ winner + " i.e " + winningTeam+ "Won the "+ " Trick: "+ numberOfTricks+"\n"+  "Tricks Won by TEAM A: " + tricksWonA + "\n" +"Tricks Won by TEAM B :" + tricksWonB );
			}
			else 
			{
				
				out.writeObject("WON_TRICK"+ winner + " i.e " + winningTeam+ "Won the "+ " Trick: "+ numberOfTricks+"\n"+  "Tricks Won by TEAM A: " + tricksWonA + "\n" +"Tricks Won by TEAM B :" + tricksWonB+"\n\n" );
				
				teamBID();
			}
		}
		
		public void roundWon() throws Exception
		{
			teamA_Score = ScoreCheck(teamAtotalBid, tricksWonA)+teamA_Score;
			teamB_Score = ScoreCheck(teamBtotalBid, tricksWonB)+teamB_Score;
			System.out.println("team a score"+ teamA_Score);
			System.out.println("team b score"+ teamB_Score);
			
			//if(teamA_Score >=250 || teamB_Score >= 250)
					if(teamA_Score >=80|| teamB_Score >= 80)
			{
				if(teamA_Score > teamB_Score)
				{
					for(int i=0;i<4;i++)
					{
					currentPlayer.printTeamWinner("Team_A");
					currentPlayer=currentPlayer.nextPlayer;
					}
				}
				else
				{
					for(int i=0;i<4;i++)
					{
					currentPlayer.printTeamWinner("Team_B");
					currentPlayer=currentPlayer.nextPlayer;
					}

				}
			}
			else 
			{
				//round++;
				for(int i=0;i<4;i++)
				{				
					if(currentPlayer == newRoundPlayer)
					{
						System.out.println("current round Player"+currentPlayer);
						currentPlayer = newRoundPlayer.nextRoundPlayer;
						System.out.println("next round player "+ currentPlayer);
						newRoundPlayer = currentPlayer;
					//	if(name== currentPlayer.name)
					//	{
							currentPlayer.newRound();
							break;
					//	}	
					}
					else
					{
						currentPlayer = currentPlayer.nextPlayer;
					}
				}
				
			}
			
		}
		
		public int ScoreCheck(int teamBid, int tricksWon) 
		{
			int score = 0;
			int X = teamBid;
			int Y = tricksWon;
			if(Y >= X)
			{
				score = ((X*10)+(Y-X));
			}
			else
			{
				System.out.println("x value:"+(-X));
				score = ((-X)*10);
				
			}
			return score;
		}
		
		public void printTeamWinner(String a) throws Exception
		{
			out.writeObject("TEAM WON"+a+"WON the game");
		}
	}		
	
}
