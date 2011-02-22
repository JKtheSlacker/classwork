// encrypt.java
// Solution to Matrix Encryption
// Written by JK Wood
import java.io.*;
import java.lang.*;

public class encrypt {
	public static void main (String[] args) throws IOException{
		int dimension;
		String message;
		String encodedMessage = "";
		int [] encMatrix;
		int [] inVector;
		int [] outVector;
		BufferedReader in = new BufferedReader (new FileReader("encrypt.in"));
		String str;
		while ((str = in.readLine()) != null){
			if (str.equals("0")){
				System.exit(0);
			}
			dimension = Integer.parseInt(str);
			encMatrix = new int[dimension * dimension];
			inVector = new int[dimension];
			outVector = new int[dimension];
			for (int i = 0; i < dimension; i++){
				str = in.readLine();
				String[] line = str.split("\\s");
				for (int j = 0; j < dimension; j++){
					encMatrix[ dimension * i + j ] = Integer.parseInt(line[j]);
				}
			}
			str = in.readLine();
			message = padInput(str, dimension);
			for (int i = 0; i < message.length() / dimension; i++){
				str = message.substring(i*dimension, i*dimension + dimension);
				for (int j = 0; j < dimension; j++){
					inVector[j] = toNum(str.charAt(j));
					outVector[j] = inVector[j];
				}
				inVector = matrixTransform(encMatrix, outVector);
				outVector = modK(inVector);
				for (int l = 0; l < dimension; l++){
					encodedMessage = encodedMessage + toChar(outVector[l]);
				}
			}
			System.out.println("'" + encodedMessage + "'");
			encodedMessage = "";
		}
	}
	public static int toNum(char in){
		char [] alphabet = {'A','B','C','D','E','F',
			'G','H','I','J','K','L','M','N','O','P',
			'Q','R','S','T','U','V','W','X','Y','Z',
			' ',',','.','?'};
		int [] alphatrans = new int [30];
		for (int i = 0; i < alphatrans.length; i++){
			alphatrans[i] = i+1;
		}
		for (int i = 0; i < alphabet.length; i++){
			if (alphabet[i] == in){
				return alphatrans[i];
			}
		}
		return 0;
	}
	public static char toChar(int in){
		char [] alphabet = {'A','B','C','D','E','F',
			'G','H','I','J','K','L','M','N','O','P',
			'Q','R','S','T','U','V','W','X','Y','Z',
			' ',',','.','?'};
		int [] alphatrans = new int [30];
		for (int i = 0; i < alphatrans.length; i++){
			alphatrans[i] = i+1;
		}
		for (int i = 0; i < alphatrans.length; i++){
			if (alphatrans[i] == in){
				return alphabet[i];
			}
		}
		return 0;
	}
	public static int [] matrixTransform(int [] encode, int [] inVector){
		int dimension = inVector.length;
		int [] outVector = new int [dimension];
		for (int i = 0; i < dimension; i++){
			outVector[i] = 0;
		}
		for (int i = 0; i < dimension; i++){ // rows
			for (int j = 0; j < dimension; j++){ // columns
				outVector[i] = outVector[i] + (inVector[j] * encode[dimension * i + j]);
			}
		}
		return outVector;
	}
	public static String padInput(String input, int dimension){
		for (int i = 0; i < dimension; i++){
			if ((input.length() % dimension) != 0){
			    input = input.concat(" ");
			}
		}
		return input;
	}
	public static int [] modK(int [] vector){
		int k = 30;
		for (int i = 0; i < vector.length; i++){
			while (vector[i] > k){
				vector[i] = vector[i] - k;
			}
			while (vector[i] < 0) { 
				vector[i] = vector[i] + 30;
			}
		}
		return vector;
	}
}

