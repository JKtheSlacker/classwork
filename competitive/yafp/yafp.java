// Written by JK Wood
// Finds the first 10-digit and 1000-digit Fibonacci numbers
import java.lang.*;
import java.math.*;

public class yafp {
	public static void main (String [] args){
		BigInteger minustwo = new BigInteger("1");
		BigInteger minusone = new BigInteger("1");
		BigInteger current = minustwo.add(minusone);
		
		int counter = 3;
		boolean tenfound = false;
		boolean thousandfound = false;
		while(!thousandfound){
			if (current.toString().length() == 10 && !tenfound){
				System.out.println("First 10 digit Fibonacci number: F" + counter + ": " + current.toString());
				tenfound = true;
			}
			if (current.toString().length() == 1000 && !thousandfound){
				System.out.println("First 1000 digit Fibonacci number: F" + counter + ": " + current.toString());
				thousandfound = true;
			}
			minustwo = minusone;
			minusone = current;
			current = minustwo.add(minusone);
			counter++;
				
		}

		
	}
}
