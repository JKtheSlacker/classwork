// Simple program to calculate the number of digits
// in an entered number.
// Written by Joshua K. Wood, 1/26/11
#include <stdio.h>
#include <stdlib.h>
#include <limits.h>

int n, x;

int numdigit(int x){
	n++;
	if (x < 10) return n;
	else return numdigit(x/10);
}

int main (){
	n = 0;
/*	printf("Enter a number between 1 and %d: ", INT_MAX);
	scanf("%d", &x);
	printf("That number has %d digits.\n", numdigit(x));
*/
	printf("Sample test cases:\n");
	n=0;
	printf("%d has %d digits.\n",65613 ,numdigit(65613)); // 5
	n=0;
	printf("%d has %d digits.\n",123456789 ,numdigit(123456789)); //9
	n=0;
	printf("%d has %d digits.\n",97 ,numdigit(97)); // 2
	n=0;
	printf("%d has %d digits.\n",9001 ,numdigit(9001)); // 4

	return 0;
}
