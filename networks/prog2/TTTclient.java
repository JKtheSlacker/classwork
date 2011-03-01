import java.io.*; 
import java.net.*; 
class TTTclient { 

    public static void main(String argv[]) throws Exception 
    { 
        String serverResponse;
        String input; 

        BufferedReader inFromUser = 
          new BufferedReader(new InputStreamReader(System.in)); 

        Socket clientSocket = new Socket("localhost", 42134); 

        DataOutputStream outToServer = 
          new DataOutputStream(clientSocket.getOutputStream()); 
        
        BufferedReader inFromServer = 
          new BufferedReader(new
          InputStreamReader(clientSocket.getInputStream())); 

	while (!((serverResponse = inFromServer.readLine()).equals("EOG"))){
		while (!((serverResponse = inFromServer.readLine()).equals("EOF"))){ //
			System.out.println(serverResponse);
		}
		input = inFromUser.readLine();
		outToServer.writeBytes(input + "\n");
	}

        clientSocket.close(); 
                   
    } 
} 
