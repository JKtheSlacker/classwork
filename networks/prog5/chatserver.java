/* Networks Program 5 - Server
 * Written by Joshua Wood
 * May 2011 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.io.*;

class chatserver extends Thread {
	static ServerSocket welcomeSocket;
	static LinkedList<clientThread> clientList;
	static boolean stillChatting = true;
	static boolean haveHadClients = false;

	public static void main(String[] args) throws Exception {
		chatserver server = new chatserver();
		server.start();
		server.join();
	}

	public chatserver() throws Exception {
		welcomeSocket = new ServerSocket(42134);
		welcomeSocket.setSoTimeout(1000);
		clientList = new LinkedList<clientThread>();
	}

	public synchronized void run() {
		try {
			while (stillChatting){
				if (clientList.size() == 0 && !haveHadClients){
					System.err.println("Ran out of clients.");
					System.err.println("Shutting down server.");
					stillChatting = false;
				}
				if ((Socket clientSocket = welcomeSocket.accept()) != null) {
					clientThread client = new clientThread(clientSocket, clientList);
					client.start();
					clientList.add(client);
					haveHadClients = true;
				}
			}
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
				if (next == null) {
					System.err.println("Next is null.");
				}
				if (next != this){
					next.outToClient.writeBytes(userName + ": " + message + "\n");
//					System.err.println("Wrote to client: " + next.userName);
				}
			}
		} catch (Exception e) {
		}
	}

	public synchronized void run() {
		String fromClient;
		try {
			outToClient.writeBytes("Welcome to the multithreaded chat room.\n");
			userName = inFromUser.readLine();
//			System.err.println(userName + " has connected.");
			writeToClients(" has connected");
			while (stillChatting) {
				fromClient = inFromUser.readLine();
				if (fromClient != null) {
					if (fromClient.endsWith("EOF")){
						stillChatting = false;
						// Notify the other clients of disconnection.
						writeToClients(" has disconnected.");
//						System.err.println(userName + " has disconnected.");
						clientList.remove(this);
//						System.err.println("Number of clients: " + clientList.size());
					} else {
						writeToClients(fromClient);								
//						System.err.println( userName + ": " + fromClient);
					}
				}
			}
		} catch (Exception e){

		}
	}
}
