#include <stdio.h>
#include <stdlib.h>

int main (int argv, char * argc[]){
	FILE * inputfile, * outputfile;
	inputfile = fopen("example.txt", "r");
	outputfile = fopen("output.txt", "w");

	
	
	fclose(outputfile);
	fclose(inputfile);

	return 0;
}
