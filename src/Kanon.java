import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class Kanon {

	static int[][] errorNumbers;
	static HashMap<Integer, ArrayList<ArrayList<Integer>>> sets = new HashMap<>();
	static int anonymity = 4;
	static ArrayList<Integer> data;

	
	public static void main(String[] args) {
		//read the data from the  file and create the table
		data = readData(args[0]);
		errorNumbers = new int[data.size()][anonymity];
		
		//sort the data in ascending order
		Collections.sort(data);

		
		//initialize the first column which is trivial(1-anonymity)
		initalizeMap();
		
		
		//fill in the table with the columns
		calculateAnonimity();

		//print table
        printTableRows();
		
        
		//print the answer that achieves the anonymity with minimum error
		printSets();
        
 
        //replace the strings in the file according to the found k-anonymity
        modifyFile(args);
		   
	}

	private static void calculateAnonimity() {
		//start from the second column and loop through until the desired anonymity is reached
		for(int j = 1; j < anonymity; j++){
			//loop through the data to compute the minimum errors
			for(int i = anonymity - 1; i < data.size(); i++){
				//compute the lowest error values for the anonymity
				errorNumbers[i][j] = computeFinalError(i,j);
			}
		}
	}

	private static void initalizeMap() {
		for(int i = anonymity-1; i<data.size();i++){
			errorNumbers[i][0] = computerOneError(getValues(0,i));
			//initialize the keys of the map
			ArrayList<ArrayList<Integer>> temp = new ArrayList<>();
			temp.add(getValues(0,i));		
			sets.put(i, temp);
		}
	}

	private static void modifyFile(String[] args) {
		try {
        	String line;
			BufferedReader reader = new BufferedReader(new FileReader(args[0]));
			StringBuffer sb = new StringBuffer();
			while((line = reader.readLine()) != null){
				line = replaceLine(line);
				//System.out.println(line);
				sb.append(line);
				sb.append(System.getProperty("line.separator"));
			}
			reader.close();
			BufferedWriter writer =  new BufferedWriter(new FileWriter(args[0]));
			writer.write(sb.toString());
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	private static void printTableRows() {
		for(int[] row : errorNumbers) {
            printRow(row);
        }
	}

	private static void printSets() {
		System.out.println("\nThe sets I could derive are:");
		ArrayList<ArrayList<Integer>> answer = sets.get(data.size()-1);
		for(ArrayList<Integer> a: answer){
			System.out.print("<");
			for(Integer i: a){
				System.out.print(i+ " ");
			}
			System.out.println("> ");
		}
	}
	
	private static String replaceLine(String line) {
		String[] att = line.split(",");
		int value = Integer.parseInt(att[0]);
		ArrayList<ArrayList<Integer>> a = sets.get(data.size()-1);
		for(ArrayList<Integer> i : a){
			if(i.contains(value))
				value = i.get(i.size() / 2);
		}
		
		return value + "," + att[1];
	}

	private static void printRow(int[] row) {
        for (int i : row) {
            System.out.print(i);
            System.out.print("  ");
        }
        System.out.println();
    }
	
	
	private static int computeFinalError(int i, int j) {
		//list with possible answers for the smallest error
		ArrayList<Integer> candidates = new ArrayList<>();
		//list with the corresponding sets
		ArrayList<ArrayList<ArrayList<Integer>>> keeper = new ArrayList<>();
		//start from the anonymity index and loop until the last index - anonymity
		for(int k = anonymity - 1; k < i - anonymity+1; k++){
			//System.out.println("I am errorNumbers["+i+"]["+j+"]" + "and trying to access errorNumbers["+k+"]["+(j-1)+"]" + " and received " + errorNumbers[k][j-1]);
			//System.out.println(Integer.toString(computerOneError(getValues(k,i))));
			
			
			int currentError = errorNumbers[k][j-1] + computerOneError(getValues(k+1,i));
			candidates.add(currentError);
			
			
			ArrayList<ArrayList<Integer>> currentSet = new ArrayList<>();
			currentSet.addAll(sets.get(k));
			currentSet.add(getValues(k+1, i));
			keeper.add(currentSet);
		}
		candidates.add(errorNumbers[i][j-1]);
		keeper.add(sets.get(i));
		
//		System.out.print("\nThe candidates are: ");
//		for(Integer c: candidates){
//			System.out.print(c+" ");
//		}
//		System.out.println("\n");
		
		
		int[] a = getMin(candidates);
		sets.put(i, keeper.get(a[1]));
		
		return a[0];
	}

	private static int[] getMin(ArrayList<Integer> possibilities) {
		int[] a = new int[2];
		a[0] = possibilities.get(0);
		for(int p1: possibilities){
			if(p1 < a[0]){
				a[0] = p1;
				a[1] = possibilities.indexOf(p1);
			}
		}
		return a;
	}

	private static int computerOneError(ArrayList<Integer> values) {
		//get the median of the list
		int med = values.get(values.size()/2);
		int error = 0;
		//calculate the error
		for(Integer i: values){
			error = error + Math.abs(med - i);
		}
		return error;
	}


	private static ArrayList<Integer> getValues(int start,int end) {
		ArrayList<Integer> v = new ArrayList<>();
		//get  the values from the start until the end
		for(int i = start; i <= end; i++){
			v.add(data.get(i));
		}
		return v;
	}
	
	
	private static ArrayList<Integer> readData(String path){
		File file= new File(path);
		ArrayList<Integer> values = new ArrayList<>();
		try {
			Scanner inputStream = new Scanner(file);
			while(inputStream.hasNext()){
				String line = inputStream.next();
				String[] att = line.split(",");
				values.add(Integer.parseInt(att[0]));
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return values;
	}
	
}
