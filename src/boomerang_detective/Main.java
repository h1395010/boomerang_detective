package boomerang_detective;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

public class Main 
{
	static double falsePositiveProbability = 0.1;
	static int expectedSize = 100;
	
	static BloomFilter<String> de_bloomFilter = new BloomFilter<String>(falsePositiveProbability, expectedSize);
	static BloomFilter<String> eng_bloomFilter = new BloomFilter<String>(falsePositiveProbability, expectedSize);
	static BloomFilter<String> fr_bloomFilter = new BloomFilter<String>(falsePositiveProbability, expectedSize);
	static BloomFilter<String> ru_bloomFilter = new BloomFilter<String>(falsePositiveProbability, expectedSize);
	
	public static void main( String[] args ) throws URISyntaxException, IOException
    {

	
		BloomFilter<String> bloomFilter = new BloomFilter<String>(falsePositiveProbability, expectedSize);
	
		bloomFilter.add("foo");
	
		if (bloomFilter.contains("foo")) 
		{ // Always returns true
		    System.out.println("BloomFilter contains foo!"); 
		    System.out.println("Probability of a false positive: " + bloomFilter.expectedFalsePositiveProbability());
		}
	
		if (bloomFilter.contains("bar")) 
		{ // Should return false, but could return true
		    System.out.println("There was a false positive.");
		}
		
		

    	
        
        //THIS SEGMENT IS FOR DYNAMICALLY LOCATING THE DIRECTORY, SO THE PROGRAM WORKS "OUT OF THE BOX"
/*******************************************************************************************************************************************/
    	//this holds all the dictionary files, i.e. word lists garners from language folders
        ArrayList<Path> dictionary_files = new ArrayList<Path>();
		
        
		File currentDir = new File( "." ); // Read current file location
        //System.out.println(currentDir.getAbsolutePath());
        
        File targetDir = null;
        if (currentDir.isDirectory()) 
        {
            targetDir = new File( currentDir, "word_lists" ); // Construct the target directory file with the right parent directory
        }
        if ( targetDir != null && targetDir.exists() )
        {
            SearchDirectories.listDirectoryAndFiles( targetDir.toPath(), dictionary_files );
        }
/*******************************************************************************************************************************************/
       
        //this populates word presence data structs for each language
        for(Path dir : dictionary_files)
        {
        	
        	String word_holding_directory_path = dir.toString().toLowerCase();
        	
        	
            BufferedReader br = new BufferedReader( new FileReader( dir.toString() ) );
            String line = null;
            while ((line = br.readLine()) != null)
            {
            	
                //System.out.println(line);
                if(word_holding_directory_path.toLowerCase().contains("/de/") )
                {
                	//de_bloomFilter.add( line.toLowerCase().trim() );	
                	de_bloomFilter.add( line );	
                }
                if(word_holding_directory_path.toLowerCase().contains("/eng/") )
                {
                	eng_bloomFilter.add( line.toLowerCase().trim() );
                }
                if(word_holding_directory_path.toLowerCase().contains("/fr/") )
                {
                	fr_bloomFilter.add( line.toLowerCase().trim() );
                }
                if(word_holding_directory_path.toLowerCase().contains("/ru/") )
                {
                	ru_bloomFilter.add( line.toLowerCase().trim() );
                }
            }
        }
        
        
/*******************************************************************************************************************************************/ 
        //GET THE USER INPUT
        ArrayList<String> input_text = new ArrayList<String>();
        
        //Scanner in = new Scanner( System.in , "UTF-8");
        
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter a sentence: ");
         
        String [] tokens = in.nextLine().split("\\s");
         
        for (int i = 0; i < tokens.length; i++)
        {
        	input_text.add( tokens[i].toString() );
        }
        
       
        
/*******************************************************************************************************************************************/
        
        
        

                	
        for(String word : input_text)
        {
        	
            if(de_bloomFilter.contains( word ) )
            {
    		    System.out.println("Direct hit!"); 
    		    System.out.println("Probability of a false positive: " + bloomFilter.expectedFalsePositiveProbability());
            }
        }
	                

        	
        
        
        
    }
}
