/* Networks Program 3 - Server
 * Written by Joshua Wood
 * May 2011 */

import java.io.*;
import java.net.*;

class chatserver {
	private ServerSocket welcomeSocket; // Why private and class-wide? Because that makes it more awesome.

	public static void main(String[] args) throws Exception {
		ServerSocket welcomeSocket;
		String fromClient;

		// Establish the listener socket
		welcomeSocket = new ServerSocket(42134);

		// A pair of client sockets!
		Socket clientSocket1 = welcomeSocket.accept();
		Socket clientSocket2 = welcomeSocket.accept();
		
		// Look! Magic BufferedReaders!
		BufferedReader inFromUser1 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));
		BufferedReader inFromUser2 = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()));

		// I wonder what a DataOutputStream might do?
		DataOutputStream outToClient1 = new DataOutputStream(clientSocket1.getOutputStream());
		DataOutputStream outToClient2 = new DataOutputStream(clientSocket2.getOutputStream());

		// I'd write this to both, but it would mess up my chat client.
		outToClient1.writeBytes("Welcome to the Stop and Go Chat Server.\n");

		boolean stillChatting = true;

		while (stillChatting){
			fromClient = inFromUser1.readLine();
			outToClient2.writeBytes("Client 1: " + fromClient + "\n");

			if (fromClient.equals("EOF")) {
				stillChatting = false;
			} else {
				fromClient = inFromUser2.readLine();
				outToClient1.writeBytes("Client 2: " + fromClient + "\n");
				if (fromClient.equals("EOF")){
					stillChatting = false;
				}
			}

		}
		welcomeSocket.close();
	}

}
