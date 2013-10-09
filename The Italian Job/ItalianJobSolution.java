import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.io.InputStreamReader;

public class ItalianJobSolution_EasierVersion {
	
	public static void main(String[] args)
	{
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in)))
		{
			String line1 = br.readLine();
			String wLine = line1.trim().substring(2);
			int numberOfWheels = Integer.parseInt(wLine);
			
			String line2 = br.readLine();
			String nLine = line2.trim().substring(2);
			int numberOfNotches = Integer.parseInt(nLine);
			
			String line3 = br.readLine();
			String lcpLine = line3.trim().substring(4);
			String[] lcpsAsStrings = lcpLine.split(",");
			double[] lcps = getDoublesFromStrings(lcpsAsStrings);
			
			String line4 = br.readLine();
			String rcpLine = line4.trim().substring(4);
			String[] rcpsAsStrings = rcpLine.split(",");
			double[] rcps = getDoublesFromStrings(rcpsAsStrings);
			
			//keeps track of differences between LCP's and RCP's
			//the key is the difference, the value is the corresponding combination number (1,4,7,10,14,...n)
			//where n <= numberOfNotches
			List<DiffPair> allDiffs = new LinkedList<DiffPair>();
			
			int notchPos = 1;
			int arrayPos = 0;
			while(notchPos<=numberOfNotches)
			{
				double lcp = lcps[arrayPos];
				double rcp = rcps[arrayPos];
				
				double difference = (rcp>lcp) ? rcp-lcp: lcp-rcp;
				allDiffs.add(new DiffPair(difference, notchPos));
				
				notchPos = notchPos + 3;
				arrayPos++;
			}
			
			//sort the diffs List, which will order them from smallest to largest by their keys (the differences)
			//the values (notch positions) are just along for the ride
			Collections.sort(allDiffs);
			
			//get w of those notch positions
			//at this point, we have the candidate combo numbers and we no longer care about the differences
			List<Integer> candidateComboNumbers = getSortedCandidateComboNumbersFromAllDiffsMap(allDiffs, numberOfWheels);
			
			//from here on, assume candidateComboNumbers.size() == numberOfWheels
			
			System.out.println("Your combination has "+numberOfWheels+" numbers from 0-"+numberOfNotches);
			System.out.print("Points of convergence are: ");
						
			for(int i=0; i<candidateComboNumbers.size(); i++)
			{
				System.out.print(candidateComboNumbers.get(i));
				
				if(i < (candidateComboNumbers.size() - 1))
				{
					System.out.print(",");
				}
				else
				{
					System.out.print("\n");
				}
			}
			
			printCombinations(candidateComboNumbers);
						
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	//This is the top-level print function
	private static void printCombinations(List<Integer> candidateComboNumbers)
	{
		int howManyToPrint = candidateComboNumbers.size();
		
		if(howManyToPrint == 4)
		{
			print4Numbers(candidateComboNumbers);
		}
		else if (howManyToPrint == 3)
		{
			print3Numbers(candidateComboNumbers, null, false);
		}
		else if (howManyToPrint == 2)
		{
			print2Numbers(candidateComboNumbers, true);
			System.out.print("\n");
			print2Numbers(candidateComboNumbers, false);
		}
		else
		{
			print1Number(candidateComboNumbers.get(0), false);
		}
	}

	private static void print4Numbers(List<Integer> candidateComboNumbers)
	{
		for(int i=0; i<4; i++)
		{
			List<Integer> copyListWithPrintedNumberRemoved = copyList(candidateComboNumbers);
			copyListWithPrintedNumberRemoved.remove(i);
			
			print3Numbers(copyListWithPrintedNumberRemoved, candidateComboNumbers.get(i), i!=3);
		}
	}
	
	private static void print3Numbers(List<Integer> candidateComboNumbers, Integer intToPrintBefore, boolean printLastLineBreak)
	{
		for(int i=0; i<3; i++)
		{
			if(intToPrintBefore!=null)
			{
				print1Number(intToPrintBefore, true);
			}
			
			print1Number(candidateComboNumbers.get(i), true);
			
			List<Integer> copyListWithPrintedNumberRemoved = copyList(candidateComboNumbers);
			copyListWithPrintedNumberRemoved.remove(i);
			
			print2Numbers(copyListWithPrintedNumberRemoved, true);
			
			System.out.print("\n");
			
			if(intToPrintBefore!=null)
			{
				print1Number(intToPrintBefore, true);
			}
			
			print1Number(candidateComboNumbers.get(i), true);
			print2Numbers(copyListWithPrintedNumberRemoved, false);
			
			if(i==2 && !printLastLineBreak)
			{
				//do nothing
			}
			else
			{
				System.out.print("\n");
			}
		}
	}
	
	private static void print2Numbers(List<Integer> candidateComboNumbers, boolean inOrder)
	{
		if(inOrder)
		{
			print1Number(candidateComboNumbers.get(0), true);
			print1Number(candidateComboNumbers.get(1), false);
		}
		else
		{
			print1Number(candidateComboNumbers.get(1), true);
			print1Number(candidateComboNumbers.get(0), false);
		}
	}
	
	private static void print1Number(Integer candidateComboNumber, boolean includeDashAfter)
	{
		System.out.print(candidateComboNumber);
		
		if(includeDashAfter)
		{
			System.out.print("-");
		}
	}
	
	private static List<Integer> copyList(List<Integer> originals)
	{
		List<Integer> retVal = new LinkedList<Integer>();
		retVal.addAll(originals);
		return retVal;
	}
	
	private static List<Integer> getSortedCandidateComboNumbersFromAllDiffsMap(List<DiffPair> allDiffs, int numberOfWheels)
	{
		List<Integer> candidateComboNumbers = new LinkedList<Integer>();
		
		for(int i=0; i<allDiffs.size(); i++)
		{
			DiffPair diffPair = allDiffs.get(i);
			
			candidateComboNumbers.add(diffPair.position);
			
			//we only expect n elements, where n=numberOfWheels
			if(candidateComboNumbers.size() == numberOfWheels)
			{
				break;
			}
		}
		
		Collections.sort(candidateComboNumbers);
		
		return candidateComboNumbers;
	}
	
	private static double[] getDoublesFromStrings(String[] strs)
	{
		double[] retVal = new double[strs.length];
		
		for(int i=0; i<strs.length; i++)
		{
			retVal[i] = Double.parseDouble(strs[i].trim());
		}
		
		return retVal;
	}
	
	public static class DiffPair implements Comparable<DiffPair>
	{
		public Double diff;
		public Integer position;
		
		public DiffPair(double d, int pos)
		{
			diff = d;
			position = pos;
		}
		
		@Override
		public int compareTo(DiffPair diffPair) {
			return diff.compareTo(diffPair.diff);
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if(!(obj instanceof DiffPair))
			{
				return false;
			}
			
			DiffPair diffPair = (DiffPair) obj;
			
			if(diff.equals(diffPair.diff) && position.equals(diffPair.position))
			{
				return true;
			}
			
			return false;
		}
	}
}
