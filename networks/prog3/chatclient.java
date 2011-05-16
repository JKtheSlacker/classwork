/* chatclient.java
 * Written by Joshua K. Wood
 * May 2011 */
import java.io.*;
import java.net.*;

class chatclient
{
	public static void main (String argv[]) throws Exception 
	{
		String serverResponse;
		String userInput;

		Socket clientSocket = new Socket ("localhost", 42134);

		DataOutputStream outToServer = 
		new DataOutputStream (clientSocket.getOutputStream ());

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

		BufferedReader inFromServer = new BufferedReader (new 
				InputStreamReader (clientSocket.getInputStream ()));

		System.out.println("To exit chat at any time, type EOF and press enter.");
		boolean stillChatting = true;
		while (stillChatting) {
			serverResponse = inFromServer.readLine();
			if (serverResponse.endsWith("EOF")){
				stillChatting = false;
			} else {
				System.out.println(serverResponse);
				userInput = inFromUser.readLine();
				outToServer.writeBytes (userInput + "\n");
				if (userInput.equals("EOF")){
					stillChatting = false;
				}
			}
		}
		System.out.println("Thank you for using the Stop and Wait Chat Server.");
   	    clientSocket.close ();
  }
}
