/* chatclient.java
 * Written by Joshua K. Wood
 * May 2011 */
import java.io.*;
import java.net.*;

class chatclient {
	static Socket clientSocket;
	static String userName;
	static DataOutputStream outToServer;
	static BufferedReader inFromUser;

	public static void main (String argv[]) throws Exception 
	{
		// Set up the connection
		clientSocket = new Socket ("localhost", 42134);
		System.out.println("To exit chat at any time, type EOF and press enter.");
		outToServer = new DataOutputStream(clientSocket.getOutputStream());
		inFromUser = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Please enter a username to continue:");
		userName = inFromUser.readLine();
		outToServer.writeBytes(userName + "\n");
		
		keyboardListener keyboard = new keyboardListener(clientSocket, outToServer, inFromUser);
		serverListener server = new serverListener(clientSocket);
		keyboard.start();
		server.start();
		keyboard.join();
		server.join();

	}
}

// Not exactly Ray Charles
class keyboardListener extends Thread {
	String userInput;
	BufferedReader inFromUser;
	DataOutputStream outToServer;
	Socket clientSocket;
	boolean stillChatting = true;

	public keyboardListener (Socket clientSocket, DataOutputStream outToServer, BufferedReader inFromUser){
		this.clientSocket = clientSocket;
		this.outToServer = outToServer;
		this.inFromUser = inFromUser;
	}

	public synchronized void run() {
		try {
			while (stillChatting) {
				userInput = inFromUser.readLine();
				if (userInput.endsWith("EOF")) {
					stillChatting = false;
				}
				outToServer.writeBytes(userInput + "\n");
			}
			clientSocket.close();
		} catch (Exception e) {
		}
	}
}

// Not the server whisperer, lest ye be confused.
class serverListener extends Thread {
	String serverResponse;
	BufferedReader inFromServer;
	boolean stillChatting = true;
	Socket clientSocket;

	public serverListener (Socket clientSocket){
		this.clientSocket = clientSocket;
	}

	// Handles reading from the server 
	public synchronized void run() {
		try {
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			while (stillChatting){
				serverResponse = inFromServer.readLine();
				System.out.println(serverResponse);
			}
		} catch (Exception e){
		}
	}
}
