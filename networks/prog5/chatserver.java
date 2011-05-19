/* Networks Program 5 - Server
 * Written by Joshua Wood
 * May 2011 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.io.*;

class chatserver {
	public static void main(String[] args) throws Exception {
		ServerSocket welcomeSocket;
		PrintStream debug = System.out;

		// Establish the listener socket
		welcomeSocket = new ServerSocket(42134);
		
		LinkedList<clientThread> clientList = new LinkedList<clientThread>();

		// This is not entirely true, but it's more true than 
		// boolean haveClients = true.
		boolean stillChatting = true;

		while (stillChatting){
			Socket clientSocket = welcomeSocket.accept();
			clientThread client = new clientThread(clientSocket, clientList);
			client.start();
			clientList.add(client);
			debug.println("Number of clients: " + clientList.size());
			if (clientList.size() == 0){
				System.out.println("Ran out of clients.");
				System.out.println("Shutting down server.");
				stillChatting = false;
			}
		}
		welcomeSocket.close();
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
		this.clientSocket = clientSocket;
		this.clientList = clientList;
		outToClient = new DataOutputStream(clientSocket.getOutputStream());
		inFromUser = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}

	public synchronized void run() {
		String fromClient;
		try {
			outToClient.writeBytes("Welcome to the multithreaded chat room.\n");
			userName = inFromUser.readLine();
			System.out.println(userName + " has connected.");
			Iterator<clientThread> iter = clientList.iterator();
			while (iter.hasNext()){
				iter.next().outToClient.writeBytes(userName + " has connected.\n");
			}
			while (stillChatting) {
				fromClient = inFromUser.readLine();
				if (fromClient != null) {
					if (fromClient.endsWith("EOF")){
						stillChatting = false;
						// Notify the other clients and delist this one.
						iter = clientList.iterator();
						while (iter.hasNext()){
							if (iter.next() != this){
								iter.next().outToClient.writeBytes(userName + " has disconnected.\n");
								System.out.println(userName + " has disconnected.");
							} else {
								clientList.remove(iter.next());
							}
						}
					} else {
						iter = clientList.iterator();
						while (iter.hasNext()){
							if (iter.next() != this){
								iter.next().outToClient.writeBytes(userName + ": " + fromClient +  "\n");
								System.out.println( userName + ": " + fromClient);
							}
						}
					}
				}
			}
		} catch (Exception e){

		}
	}
}
