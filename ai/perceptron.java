/* Perceptron.java
 * Written by: Joshua Wood
 * Imitates a single-layer perceptron
 * Attempts to learn to OR
 * May 15, 2011 */

import java.lang.*;
import java.math.*;

public class perceptron {
	public static void main(String[] args){
		boolean done = false;
		int iteration = 1;
		int input1;
		int input2;
		// Behold my clever truncation technique
		BigDecimal theta = new BigDecimal(String.format("%.1g", (Math.random() - 0.5)));
		BigDecimal alpha = new BigDecimal("0.1");
		int desiredOutput;
		int actualOutput;
		int error;
		BigDecimal weight1 = new BigDecimal(String.format("%.1g", (Math.random() - 0.5)));
		BigDecimal weight2 = new BigDecimal(String.format("%.1g", (Math.random() - 0.5)));
		BigDecimal weight3;
		BigDecimal weight4;

		System.out.println("Theta is " + theta);

		while (!done){
			// First row
			input1 = 0;
			input2 = 0;
			desiredOutput = 0;
			actualOutput = stepFunction(input1, weight1, theta, input2, weight2);
			error = desiredOutput - actualOutput;
			weight3 = weight1.add(alpha.multiply(new BigDecimal(input1).multiply(new BigDecimal(error))));
			weight4 = weight2.add(alpha.multiply(new BigDecimal(input2).multiply(new BigDecimal(error))));

			System.out.println(iteration + " " + input1 + " " + input2 + " " + 
					desiredOutput + " " + weight1 + " " + weight2 + " " +
					actualOutput + " " + error + " " + weight3 +
					" " + weight4);
			done = error == 0;
			// Second row
			weight1 = weight3;
			weight2 = weight4;
			input1 = 0;
			input2 = 1;
			desiredOutput = 1;
			actualOutput = stepFunction(input1, weight1, theta, input2, weight2);
			error = desiredOutput - actualOutput;
			weight3 = weight1.add(alpha.multiply(new BigDecimal(input1).multiply(new BigDecimal(error))));
			weight4 = weight2.add(alpha.multiply(new BigDecimal(input2).multiply(new BigDecimal(error))));

			System.out.println(iteration + " " + input1 + " " + input2 + " " + 
					desiredOutput + " " + weight1 + " " + weight2 + " " +
					actualOutput + " " + error + " " + weight3 +
					" " + weight4);
			done = done && error == 0;
			// Third row
			weight1 = weight3;
			weight2 = weight4;
			input1 = 1;
			input2 = 0;
			desiredOutput = 1;
			actualOutput = stepFunction(input1, weight1, theta, input2, weight2);
			error = desiredOutput - actualOutput;
			weight3 = weight1.add(alpha.multiply(new BigDecimal(input1).multiply(new BigDecimal(error))));
			weight4 = weight2.add(alpha.multiply(new BigDecimal(input2).multiply(new BigDecimal(error))));

			System.out.println(iteration + " " + input1 + " " + input2 + " " + 
					desiredOutput + " " + weight1 + " " + weight2 + " " +
					actualOutput + " " + error + " " + weight3 +
					" " + weight4);
			done = done && error == 0;
			// Fourth row
			weight1 = weight3;
			weight2 = weight4;
			input1 = 1;
			input2 = 1;
			desiredOutput = 1;
			actualOutput = stepFunction(input1, weight1, theta, input2, weight2);
			error = desiredOutput - actualOutput;
			weight3 = weight1.add(alpha.multiply(new BigDecimal(input1).multiply(new BigDecimal(error))));
			weight4 = weight2.add(alpha.multiply(new BigDecimal(input2).multiply(new BigDecimal(error))));

			System.out.println(iteration + " " + input1 + " " + input2 + " " + 
					desiredOutput + " " + weight1 + " " + weight2 + " " +
					actualOutput + " " + error + " " + weight3 +
					" " + weight4);
			done = done && error == 0;

			weight1 = weight3;
			weight2 = weight4;
			iteration++;
			if (iteration > 5){
				done = true;
			}

		}
		// Outside the while loop, so it's safe to assume we're done.
	}

	public static int stepFunction(int input1, BigDecimal weight1, BigDecimal theta, int input2, BigDecimal weight2){
		BigDecimal output = new BigDecimal(input1).multiply(weight1).subtract(theta).add(new BigDecimal(input2).multiply(weight2)).subtract(theta);

		if (output.compareTo(theta) >= 0){
			return 1;
		} else {
			return 0;
		}
	}
}
