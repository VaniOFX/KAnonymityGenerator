import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class KTest {

	public static void main(String[] args) {
        File file= new File(args[0]);

        // this gives you a 2-dimensional array of strings
       	HashMap<String,Integer> map = new HashMap<>();
        Scanner inputStream;

        try{
            inputStream = new Scanner(file);
            while(inputStream.hasNext()){
                String line= inputStream.next();
                System.out.println(line);
                String[] values = line.split(",");
                Object counter = map.get(values[0]);
                if(counter == null){
                	map.put(values[0], 1);
                	System.out.println("Putting in the map: " + values[0]);
                }
                else{
                	map.put(values[0],(Integer) counter+1);
                	System.out.println("Incrementing tha value for: " + values[0] + ", "+ ((Integer) counter+1));
                }
            }
            inputStream.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        int min = Integer.MAX_VALUE;
        for(HashMap.Entry<String,Integer> entry: map.entrySet()){
        	if(min>entry.getValue()){
        		min = entry.getValue();
        		System.out.println("The new min is " + min+ " for : " + entry.getKey());
        	}
        }
        
        System.out.println("The final min is: " + min);
        System.exit(min);
	}

}
