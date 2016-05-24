package container;

import java.io.FileNotFoundException;

public class Program 
{	
	private static boolean[] finish;
	private static int[] order;
	
	private static int[][] allocation;
	private static int[][] need;
	private static int[] available;
	
	private static boolean runAlgorithm()
	{
		// preparing algorithm's data structures
		allocation = MatrixOperations.allocation;
		need = MatrixOperations.need;
		available = MatrixOperations.available;
		int[] work = available.clone();
		
		finish = new boolean[allocation.length];
		order = new int[allocation.length];
		int j = 0;

		// running the algorithm
		for(int i = 0, n = finish.length; i < n; i++)
		{
			if(MatrixOperations.lessOrEqual(need[i], new int[need[i].length]) && MatrixOperations.lessOrEqual(allocation[i], new int[allocation[i].length]))
			{
				finish[i] = true;
				continue;
			}
			if(!finish[i] && MatrixOperations.lessOrEqual(need[i], work))
			{
				work = MatrixOperations.add1d(work, allocation[i]);
				finish[i] = true;
				order[j++] = i;
				i = -1;
			}
		}
			
		// checking the safe state
		boolean safe = true;
		for(int i = 0, n = finish.length; i < n; i++)
			safe = safe && finish[i];
		return safe;
	}
	
	public static void repair()
	{
		for(int i = 0, n = allocation.length; i < n; i++)
		{
			System.out.println("Releasing Process " + i + " ...");
			int[] t1 = allocation[i].clone();
			int[] t2 = need[i].clone();
			MatrixOperations.available = MatrixOperations.add1d(MatrixOperations.available, t1);
			
			MatrixOperations.allocation[i] = new int[allocation[i].length];
			MatrixOperations.need[i] = new int[need[i].length];
			
			if(runAlgorithm())
			{
				System.out.println("DONE :D");
				order[order.length - 1] = i;
				MatrixOperations.print1d(order);
				break;
			}
			else
			{
				System.out.println("Didn't Work :(");
				allocation[i] = t1;
				need[i] = t2;
				MatrixOperations.available = MatrixOperations.sub1d(MatrixOperations.available, t1);
			}
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException 
	{
		MatrixOperations.readMatrices("matrices3.in");			// reading the file containing algorithm's inputs
		MatrixOperations.calculateNeed();					// calculating need matrix
		MatrixOperations.calculateAvailable();				// calculating available instances

		if(runAlgorithm())
		{
			System.out.println("Safe State.");
			MatrixOperations.print1d(order);
		}
		else
		{
			System.out.println("Not Safe");
			repair();
		}
	}
}