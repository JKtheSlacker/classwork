/* marcus.java
 * Written by Joshua Wood
 * 1/31/11 */
import java.io.*;
import java.lang.*;

public class marcus
{

  public static void main (String[]args) throws IOException
  {
    int numcols = 0;
    int numrows = 0;
    int numtestcases = 0;
    char[] patharray = new char[0];

    BufferedReader in = new BufferedReader (new FileReader ("marcus.in"));
    String str; // Temporary buffer string.  I normally disdain such lazy variable names
		// outside of loops, but from a competitive programming sense, reusability
		// and maintainability take a backseat to getting it working fast.
    if ((str = in.readLine ()) != null)
      {				// First line is always test cases
	numtestcases = Integer.parseInt (str.trim ()); // The trim() makes parseInt() happy.
      }
    for (int k = 0; k < numtestcases; k++)
      {				// This loop handles all our testcases in order
	str = in.readLine ();
	String[] line = str.split ("\\s"); 
	/* The split() method of the string classes will break
	 * a string up into individual parts, based on the
	 * delimiter used. In this case, it's a space, denoted
	 * by \\s. It returns an array. */
	numrows = Integer.parseInt (line[0]); // First token in the array
	numcols = Integer.parseInt (line[1]); // Second token in the array
	// And that pretty much sums up our initial data collection for the maze.

	// This particular bit populates our array.
	patharray = new char[numrows * numcols]; // You can reinstantiate a variable any time you want.

	for (int i = 0; i < numrows * numcols; i = i + numcols)
	  { // Note that i jumps by the number of columns - in this way, we can keep track of
	    // what segment of the array we're working on filling.  An alternative would be to
	    // use a two-dimensional array - either would work just fine.
	    str = in.readLine (); // This is important. We want to advance in reading the file for every
	    // line in the maze definition, but we want to prevent it happening too many times,
	    // or we'll miss the next maze definition line and screw up.
	    for (int j = 0; j < numcols; j++)
	      {
		patharray[i + j] = str.charAt (j); // Just simple array filling.
	      }
	  }
	findpath (patharray, numcols);	// The findpath function not only finds the path, but prints it
      }
    in.close ();		// It's polite to close files when you're done with them.
  }

  public static void findpath (char[]patharray, int numcols)
  {				// This does the magic
    String nameofgod = "IEHOVA#"; // Strings are easy to iterate.
    int currentlocation = 0; // This shows our current location in the array.

    // Find the starting location
    for (int i = 0; i < patharray.length; i++)
      { // I could probably do some fancy breaking here or something.
	// In a contest, we have 1 minute for our program to finish what it's doing.
	// Most of the time, we won't come anywhere near this limit, but this is
	// definitely one spot in the program where I could shave some excution time
	// off. As it is, we do one comparison for every location in the array, no
	// matter what.
	if (patharray[i] == '@') // Denotes the beginning of the path
	  {
	    currentlocation = i; // And now we know where to start from.
	  }
      }

    for (int j = 0; j < nameofgod.length (); j++)
      { // Overall, this is a pretty simple section of code, but I take advantage
	// of some interesting properties of how Java handles conditionals, and
	// how logic works in general. if statements evaluate for truth, and obey
	// the rules of logic. I take advantage of the fact that, if the first premise
	// in an AND statement is false, then the entire rest of the statement is false.
	// By checking for possible out of bounds conditions first, I make sure I'll
	// never hit them. The program will evaluate lookleft, see it's false, and move
	// on down the line without even trying to evalute the second half of the
	// condition.  Saves a bunch of try/catch blocks, too.
	if (lookleft (currentlocation)
	    && patharray[currentlocation - 1] == nameofgod.charAt (j))
	  {
	    System.out.print ("left ");
	    currentlocation = currentlocation - 1;
	  }
	else if (lookright (currentlocation, patharray.length)
		 && patharray[currentlocation + 1] == nameofgod.charAt (j))
	  {
	    System.out.print ("right ");
	    currentlocation = currentlocation + 1;
	  }
	else
	  { // Going forward is the only other possibility, so I don't even bother doing
	    // a comparison here. This is a unique property of this problem - there's
	    // guaranteed to be a solution for every test case.
	    System.out.print ("forth ");
	    currentlocation = currentlocation - numcols;
	  }
      }

    System.out.println ();	// This inserts a newline before the next potential test case.
  }

  public static boolean lookleft (int currentlocation)
  {
    // Checks whether we can safely look to the left
    // Avoids ArrayIndexOutOfBounds errors
    if (currentlocation == 0)
      {
	return false;
      }
    else
      {
	return true;
      }
  }

  public static boolean lookright (int currentlocation, int pathlength)
  {
    // Checks whether we can safely look to the right
    // Avoids ArrayIndexOutOfBounds errors
    if (currentlocation == pathlength)
      {
	return false;
      }
    else
      {
	return true;
      }
  }

}
