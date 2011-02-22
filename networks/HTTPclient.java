/* HTTPclient.java
 * Originally written by James Cain
 * Modified by Joshua K. Wood
 * February 6, 2011 */
import java.io.*;
import java.net.*;
class HTTPclient
{
  public static void main (String argv[]) throws Exception 
  {
    String getRequest;
    if (argv.length == 0){
        System.out.println("Usage: java HTTPclient url");
        System.out.println("    Retrieves the url passed to it on the command line.");
        System.out.println("    Prints the HTTP code and page content.");
        System.exit(0);
    }
    String url = argv[0];
    String hostname;
    String pathname = "/";
    String serverResponse;
    String[] tokens = url.split ("/");
    hostname = tokens[0];

    for (int i = 1; i < tokens.length; i++) {
        if (tokens[i].indexOf (".") >= 0){
	        pathname = pathname.concat (tokens[i]);
	    } else {
	        pathname = pathname.concat (tokens[i] + "/");
	    }
      }

    Socket clientSocket = new Socket (hostname, 80);

    DataOutputStream outToServer = 
        new DataOutputStream (clientSocket.getOutputStream ());

    BufferedReader inFromServer = new BufferedReader (new 
            InputStreamReader (clientSocket.getInputStream ()));

    getRequest = "GET " + pathname + " HTTP/1.1\n" + 
        "Host: " + hostname + "\n" + 
        "Connection: close \n\n\n";

    outToServer.writeBytes (getRequest);
   
    int guard = 1;
    while (guard != 0){
        serverResponse = inFromServer.readLine();
        if (serverResponse == null){
            guard = 0;
        } else if (serverResponse.equals("")){
            System.out.println("\n");
	} else {
	    System.out.println (serverResponse);
	}
    }
    clientSocket.close ();
  }
}
