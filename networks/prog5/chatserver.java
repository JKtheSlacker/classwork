/* Networks Program 5 - Server
 * Written by Joshua Wood
 * May 2011 */

import java.io.*;
import java.net.*;
import java.util.*;

class chatserver extends Thread {
	static ServerSocket welcomeSocket;
	static LinkedList<clientThread> clientList;
	static boolean stillChatting = true;
	static boolean haveHadClients = false;
	static boolean gotConnection = true;
	static clientThread client;

	public static void main(String[] args) throws Exception {
		chatserver server = new chatserver();
		server.start();
		server.join();
	}

	// This lets me run the server in a separate thread.
	// Why? I thought it would let me get around 
	// ServerSocket.accept() blocking. Ah well.
	// Clean code is clean code.
	public chatserver() throws Exception {
		welcomeSocket = new ServerSocket(42134);
		// THIS is what lets me get around ServerSocket.accept()
		// blocking.
		welcomeSocket.setSoTimeout(1000);
		clientList = new LinkedList<clientThread>();
	}

	public synchronized void run() {
		while (stillChatting){
			if (clientList.size() == 0 && haveHadClients){
				// We naturally don't want this to run if we've never had clients.
				// But we definitely want it to run when everybody disconnects
				// if possible. It's possible.
				System.err.println("Ran out of clients.");
				System.err.println("Shutting down server.");
				stillChatting = false;
			}
			try {
				// Since welcomeSocket.accept() throws a socketTimeoutException
				// when it doesn't grab a client, we don't want to kill the
				// loop if we don't get one. So, try/catch INSIDE the loop.
				// This won't generate new clients if the accept() fails.
				client = new clientThread(welcomeSocket.accept(), clientList);
				client.start();
				clientList.add(client);
				// I feel no particular compulsion to check this every time.
				haveHadClients = true;
			} catch (Exception e) {
			}
		}
		try {
			// I'll even be nice and clean up after myself.
			welcomeSocket.close();
		} catch (Exception e) {
		}
	}
}

// May as well give every client its own thread.
class clientThread extends Thread {
	Socket clientSocket;
	BufferedReader inFromUser;
	DataOutputStream outToClient;
	boolean stillChatting = true;
	LinkedList<clientThread> clientList;
	String userName;

	// Awesome constructor, now with 130% more explosions.
	// This is the Michael Bay film of constructors.
	public clientThread (Socket clientSocket, LinkedList<clientThread> clientList){
		try {
			this.clientSocket = clientSocket;
			this.clientList = clientList;
			outToClient = new DataOutputStream(clientSocket.getOutputStream());
			inFromUser = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (Exception e) {
		}
	}

	// I do this often enough that it merits its own method.
	public void writeToClients(String message){
		try {
			Iterator<clientThread> iter = clientList.iterator();
			while (iter.hasNext()){
				clientThread next = iter.next();
				if (next == null) { // This won't happen, but it doesn't hurt to leave it.
					System.err.println("Next is null.");
				}
				if (next != this && !message.equals("null")){
					// For some reason, I keep seeing "null" from clients
					// who have just connected. Ah well.
					next.outToClient.writeBytes(userName + ": " + message + "\n");
				}
			}
		} catch (Exception e) {
		}
	}

	public synchronized void run() {
		String fromClient;
		try {
			outToClient.writeBytes("Welcome to the multithreaded chat room.\n");
			// This is a blocking call as well. However, there is no timeout
			// associated with it. And to be honest, I'm okay with that.
			// One unintended side effect is that the client queues up
			// messages from the other clients while waiting on user
			// input. I think this is kind of a neat feature.
			userName = inFromUser.readLine();
			writeToClients(" has connected");
			
			while (stillChatting) {
				fromClient = inFromUser.readLine();
				if (fromClient != null) {
					// An EOF indicates a client quit.
					if (fromClient.endsWith("EOF")){
						stillChatting = false;
						// Notify the other clients of disconnection.
						writeToClients(" has disconnected.");
						clientList.remove(this);
					} else {
						// No EOF, we have a message.
						writeToClients(fromClient);								
					}
				}
			}
		} catch (Exception e){
			// So far, I have not actually seen this happen.
			System.err.println("Something went wrong: " + e);
		}
	}
}
