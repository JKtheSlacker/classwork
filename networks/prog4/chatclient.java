/* chatclient.java
 * Written by Joshua K. Wood
 * May 2011 */
import java.io.*;
import java.net.*;

class chatclient extends Thread
{

	static Socket clientSocket;
	static DataOutputStream outToServer;
	static BufferedReader inFromUser;
	static BufferedReader inFromServer;
	static String serverResponse;
	static String userInput;
	static boolean stillChatting = true;

	public static void main (String argv[]) throws Exception 
	{
		// Set up the connection
		clientSocket = new Socket ("localhost", 42134);
		outToServer = new DataOutputStream (clientSocket.getOutputStream ());
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
		inFromServer = new BufferedReader (new 
				InputStreamReader (clientSocket.getInputStream ()));
		System.out.println("To exit chat at any time, type EOF and press enter.");

		new Thread (new chatclient()).start();
		

		while (stillChatting) {
			userInput = inFromUser.readLine();
			if (userInput.endsWith("EOF")){
				stillChatting = false;
			}
			outToServer.writeBytes(userInput + "\n");
		}
		clientSocket.close ();
	}

	// Handles reading from the server 
	public synchronized void run() {
		try {
			while (stillChatting){
				serverResponse = inFromServer.readLine();
				if(serverResponse.endsWith("EOF")){
					stillChatting = false;
				} else {
					System.out.println(serverResponse);
				}
			}
		} catch (Exception e){
			System.err.println(e);
		}
	}

}
