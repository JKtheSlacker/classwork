/* Networks Program 4 - Server
 * Written by Joshua Wood
 * May 2011 */

import java.io.*;
import java.net.*;

class chatserver {
	private ServerSocket welcomeSocket; // Why private and class-wide? Because that makes it more awesome.
	private static Socket clientSocket1;
	private static Socket clientSocket2;
	private static clientThread clientThread1;
	private static clientThread clientThread2;

	public static void main(String[] args) throws Exception {
		ServerSocket welcomeSocket;

		// Establish the listener socket
		welcomeSocket = new ServerSocket(42134);

		boolean stillChatting = true;

		clientSocket1 = welcomeSocket.accept();
		clientSocket2 = welcomeSocket.accept();
		clientThread1 = new clientThread(clientSocket1);
		clientThread2 = new clientThread(clientSocket2);
		clientThread1.setOtherClient(clientThread2);
		clientThread2.setOtherClient(clientThread1);

		clientThread1.start();
		clientThread2.start();
		clientThread1.join();
		clientThread2.join();
	}		
}

// May as well give every client its own thread.
class clientThread extends Thread {
	Socket clientSocket;
	BufferedReader inFromUser;
	DataOutputStream outToClient;
	boolean stillChatting = true;
	clientThread otherClient;

	// Awesome constructor, now with 30% more explosions.
	public clientThread (Socket clientSocket, clientThread otherClient){
		this.clientSocket = clientSocket;
		this.otherClient = otherClient;
	}

	// OVERLOAD!
	public clientThread (Socket clientSocket){
		this.clientSocket = clientSocket;
	}

	// Apparently, my first constructor idea wasn't going to fly.
	// This fixes that problem.
	// Probably not elegantly.
	public void setOtherClient(clientThread otherClient){
		this.otherClient = otherClient;
	}

	public synchronized void run() {
		String fromClient;
		try {
			inFromUser = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToClient = new DataOutputStream(clientSocket.getOutputStream());
			outToClient.writeBytes("Welcome to the multithreaded chat server.\n");
			while (stillChatting) {
				fromClient = inFromUser.readLine();
				if (fromClient.endsWith("EOF")){
					stillChatting = false;
				} else {	
					otherClient.outToClient.writeBytes(fromClient + "\n");
				}
			}
			outToClient.writeBytes("Thanks for using this multithreaded chat server.\n");
			otherClient.outToClient.writeBytes("EOF\n");
		} catch (Exception e){

		}
	}
}
