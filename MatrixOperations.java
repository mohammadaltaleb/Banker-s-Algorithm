/*
 * Programmer: Mohammad Altaleb
 * e-mail: mohammadaltaleb@gmail.com
 * description: this class holds the matrices operations to help the (Program) class performing
 * 		some needed matrices manipulations.
*/

package container;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class MatrixOperations 
{
	public static int[][] allocation;    //Allocation Matrix
	public static int[][] max;           //Max Matrix
	public static int[][] need;   	     //Need Matrix
	public static int[] instances;       //Instances vector
	public static int[] available;       //Available vector
	
	// this method is responsible for instantiating the matrices and reading the file that contains allocation matrix, max matrix and instances count
	public static void readMatrices(String fileName) throws FileNotFoundException
	{
		Scanner input = new Scanner(new File("/home/mohammad/workspace/Bankers Algorithm/bin/container/" + fileName));
		int n = input.nextInt();
		int m = input.nextInt();
		allocation = new int[n][m];
		max = new int[n][m];
		need = new int[n][m];
		instances = new int[m];
		available = new int[m];
		
		for(int i = 0; i < n; i++)
			for(int j = 0; j < m; j++)
				allocation[i][j] = input.nextInt();
		
		for(int i = 0; i < n; i++)
			for(int j = 0; j < m; j++)
				max[i][j] = input.nextInt();
		
		for(int i = 0; i < m; i++)
			instances[i] = input.nextInt();
		input.close();
	}
	
	// this method subtracts allocation matrix from max matrix and store the result in need matrix
	public static void calculateNeed()
	{
		for(int i = 0, n = allocation.length; i < n; i++)
			for(int j = 0, m = allocation[i].length; j < m; j++)
				need[i][j] = max[i][j] - allocation[i][j];
	}
	
	// this method counts the available instances by subtracting allocated instances of a resource j from the total number of j's instances
	public static void calculateAvailable()
	{
		// counting the number of allocated instances
		int[] temp = new int[allocation[0].length];
		for(int i = 0, n = allocation.length; i < n; i++)
			for(int j = 0, m = allocation[i].length; j < m; j++)
				temp[j] += allocation[i][j];
		
		// calculating available instances
		available = sub1d(instances, temp);
	}
	
	// this method returns true if the matrix (a) is less than or equal to matrix (b), and false otherwise
	// Example: a = [2, 0, 4, 3], b = [2, 0, 5, 4] --> true
	//			a = [2, 0, 4, 3], b = [2, 0, 4, 3] --> true
	//			a = [2, 0, 4, 3], b = [1, 0, 4, 3] --> false
	public static boolean lessOrEqual(int[] a, int[] b)
	{
		for(int i = 0, n = a.length; i < n; i++)
			if(a[i] > b[i])
				return false;
		return true;
	}
	
	// this method adds two one-dimension matrices and return the resulted matrix
	public static int[] add1d(int[] a, int[] b)
	{
		int[] t = new int[a.length];
		for(int i = 0, n = a.length; i < n; i++)
			t[i] = a[i] + b[i];
		return t;
	}
	
	// this method adds two one-dimension matrices and return the resulted matrix
	public static int[] sub1d(int[] a, int[] b)
	{
		int[] t = new int[a.length];
		for(int i = 0, n = a.length; i < n; i++)
			t[i] = a[i] - b[i];
		return t;
	}
	
	// this method prints a given one-dimension matrix
	public static void print1d(int[] a)
	{
		for(int i = 0, n = a.length; i < n; i++)
			System.out.print(a[i] + " ");
	}
	
	// this method prints a given two-dimensions matrix
	public static void print2d(int[][] a)
	{
		for(int i = 0, n = a.length; i < n; i++)
		{
			for(int j = 0, m = a[i].length; j < m; j++)
				System.out.print(a[i][j] + " ");
			System.out.println();
		}
	}
}
