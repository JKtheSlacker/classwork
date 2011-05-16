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
	
		while (true) {
			System.out.println(inFromServer.readLine());
			userInput = inFromUser.readLine();
			outToServer.writeBytes (userInput + "\n");
		}
   
//    clientSocket.close (); This really doesn't matter without a way to force the client to die.
  }
}
