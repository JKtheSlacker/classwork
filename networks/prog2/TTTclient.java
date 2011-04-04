/* Tic Tac Toe Client
 * Networks Program 2
 * Written by Joshua Wood
 * March 2011 */
import java.io.*; 
import java.net.*; 
class TTTclient { 

    public static void main(String argv[]) throws Exception 
    { 
        String serverResponse;
        String input; 

	// Yay user input
        BufferedReader inFromUser = 
          new BufferedReader(new InputStreamReader(System.in)); 

	// Connecting to the server
        Socket clientSocket = new Socket("localhost", 42134); // A nice round number

	// Control channel
	DataOutputStream outToServer = 
          new DataOutputStream(clientSocket.getOutputStream()); 
        
	// Lets the server talk to us
        BufferedReader inFromServer = 
          new BufferedReader(new
          InputStreamReader(clientSocket.getInputStream())); 
	
	// Outer loop handles the overall game
	while (!((serverResponse = inFromServer.readLine()).equals("EOG"))){
		// Inner loop handles each move
		while (!((serverResponse = inFromServer.readLine()).equals("EOF"))){ //
			System.out.println(serverResponse);
		}
		input = inFromUser.readLine(); // Input the chosen move
		outToServer.writeBytes(input + "\n");
	}

        clientSocket.close(); // Cleanup when you're done playing
                   
    } 
} 
