import java.io.*;
import java.util.*;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.InputStreamReader;

public class Client{
	private static BufferedReader inputReader;
	private static PrintWriter outputWriter;
	private static Socket myClient;
	public static Scanner scan;
	public static String response;
	
	public static void main(String [] args) throws Exception{
		try{
			myClient = new Socket("35.196.184.162", 12347);
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(myClient.getInputStream()));
			PrintWriter outputWriter = new PrintWriter(myClient.getOutputStream());
			scan = new Scanner(System.in);
			ObjectInputStream in  = new ObjectInputStream(myClient.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(myClient.getOutputStream());
			
			while((response = (String)in.readObject())!= null)
			{
				if(response.startsWith("Message"))
				{
					System.out.println(response.substring(8));
					if(scan.hasNextInt())
					{
						int chosenBid = scan.nextInt();	
						out.writeObject("BID"+chosenBid);
					}
					else
					{	
						while(!scan.hasNextInt())
						{	
							System.out.println("please enter a proper integer input");
							scan.next();
						}
						int chosenBid = scan.nextInt();	
						out.writeObject("BID"+chosenBid);
					}
				}
				else if(response.startsWith("CARDS"))
				{
					System.out.println("\n"+"Your cards: ");
					System.out.println(response.substring(5));
				}
				else if(response.startsWith("Message1"))
				{
					System.out.println("\n"+response.substring(8));
				}
				else if(response.startsWith("Valid"))
				{
					System.out.println(response);
					
				}
				else if(response.startsWith("TotalBid"))
				{
					System.out.println(response.substring(9));
					
				}
				else if(response.startsWith("SELECT"))
				{
					System.out.println(response.substring(7));
					String selectedCard = scan.next();
					String card = selectedCard.toUpperCase();
					out.writeObject("CARD"+card);
				}
				else if(response.startsWith("VALID"))
				{
					System.out.println(response);
				}
				else if(response.startsWith("**"))
				{
					System.out.println("\n"+response.substring(2));
				}
				else if(response.startsWith("TRICK"))
				{
					System.out.println("\n"+ " cards played by other players: " +"\n"+ response.substring(5)+"\n");
				}
				else if(response.startsWith("in"))
				{
					System.out.println(response);
				}
				else if(response.startsWith("WON_TRICK"))
				{
					System.out.println(response.substring(9));
				}
				else if(response.startsWith("TEAM"))
				{
					System.out.println(response.substring(8));
					break;
				}
				else if(response.startsWith("Team"))
				{
					System.out.println(response);
				}
			}out.writeObject("QUIT");
		}finally{
			myClient.close();
		}
	}
}
