package boomerang_detective;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map.Entry;

public class Main 
{
	static double falsePositiveProbability = 0.1;
	static int expectedSize = 100000000;
	
	//GERMAN
	static BloomFilter<String> de_bloomFilter = new BloomFilter<String>(falsePositiveProbability, expectedSize);
	//ENGLISH
	static BloomFilter<String> eng_bloomFilter = new BloomFilter<String>(falsePositiveProbability, expectedSize);
	//FRENCH
	static BloomFilter<String> fr_bloomFilter = new BloomFilter<String>(falsePositiveProbability, expectedSize);
	//SPANISH
	static BloomFilter<String> es_bloomFilter = new BloomFilter<String>(falsePositiveProbability, expectedSize);
	//ITALIAN
	static BloomFilter<String> it_bloomFilter = new BloomFilter<String>(falsePositiveProbability, expectedSize);
	//PORTUGESE
	static BloomFilter<String> pt_bloomFilter = new BloomFilter<String>(falsePositiveProbability, expectedSize);
	//DUTCH
	static BloomFilter<String> nl_bloomFilter = new BloomFilter<String>(falsePositiveProbability, expectedSize);
	
	public static void main( String[] args ) throws URISyntaxException, IOException
    {

	
        
        //THIS SEGMENT IS FOR DYNAMICALLY LOCATING THE DIRECTORY, SO THE PROGRAM WORKS "OUT OF THE BOX"
/*******************************************************************************************************************************************/
    	//this holds all the dictionary files, i.e. word lists garners from language folders
        ArrayList<Path> dictionary_files = new ArrayList<Path>();
		
        
		File currentDir = new File( "." ); // Read current file location
        //System.out.println(currentDir.getAbsolutePath());
        
        File targetDir = null;
        if (currentDir.isDirectory()) 
        {
            targetDir = new File( currentDir, "ascii_word_lists" ); // Construct the target directory file with the right parent directory
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
        	
        	
        	
        	//BufferedReader br = new BufferedReader( new InputStreamReader(new FileInputStream( dir.toString() ), "UTF-8") );
        	
            BufferedReader br = new BufferedReader( new FileReader( dir.toString() ) );
            String line = null;
            while ((line = br.readLine()) != null)
            {
            	
            	//GERMAN
                if(word_holding_directory_path.toLowerCase().contains("/de/") )
                {
                	de_bloomFilter.add( line.toLowerCase().trim() );	
                }
                //ENGLISH
                if(word_holding_directory_path.toLowerCase().contains("/eng/") )
                {
                	eng_bloomFilter.add( line.toLowerCase().trim() );
                }
                //FRENCH
                if(word_holding_directory_path.toLowerCase().contains("/fr/") )
                {
                	fr_bloomFilter.add( line.toLowerCase().trim() );
                }
                //SPANISH
                if(word_holding_directory_path.toLowerCase().contains("/es/") )
                {
                	es_bloomFilter.add( line.toLowerCase().trim() );
                }
                //ITALIAN
                if(word_holding_directory_path.toLowerCase().contains("/it/") )
                {
                	it_bloomFilter.add( line.toLowerCase().trim() );
                }
                //PORTUGESE
                if(word_holding_directory_path.toLowerCase().contains("/pt/") )
                {
                	pt_bloomFilter.add( line.toLowerCase().trim() );
                }
                //DUTCH
                if(word_holding_directory_path.toLowerCase().contains("/nl/") )
                {
                	nl_bloomFilter.add( line.toLowerCase().trim() );
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
        
        
    	int total_de  = 0;
    	int total_eng = 0;
    	int total_es  = 0;
    	int total_fr  = 0;
    	int total_gk  = 0;
    	int total_it  = 0;
    	int total_nl  = 0;
    	int total_pt  = 0;

                	
        for(String word : input_text)
        {
        	
            if(de_bloomFilter.contains( word ) )
            {
    		    total_de++;
            }
            if(eng_bloomFilter.contains( word ) )
            { 
    		    total_eng++;
            }
            if(fr_bloomFilter.contains( word ) )
            {
    		    total_fr++;
            }
            if(es_bloomFilter.contains( word ) )
            {
    		    total_es++;
            }
            if(it_bloomFilter.contains( word ) )
            {
    		    total_it++;
            }
            if(pt_bloomFilter.contains( word ) )
            {
    		    total_pt++;
            }
            if(nl_bloomFilter.contains( word ) )
            {
    		    total_nl++;
            }
        }
	            
        
        
        //this has to be here. you need to add them here AFTER the totals 
        //have been calculated. 
        HashMap< String, Integer > most_hits_lang = new HashMap<String, Integer>();
        most_hits_lang.put( "Deutsch, (German)", total_de );
        most_hits_lang.put( "English, (English)", total_eng );
        most_hits_lang.put( "Español, (Spanish)", total_es );
        most_hits_lang.put( "Français, (French)", total_fr );
        most_hits_lang.put( "Ελληνική, (Greek)", total_gk );
        most_hits_lang.put( "Italiano, (Italian)", total_it );
        most_hits_lang.put( "Nederlandse, (Dutch)", total_nl );
        most_hits_lang.put( "Português, (Portugese)", total_pt );

        
        Entry<String,Integer> maxEntry = null;

        for(Entry<String,Integer> entry : most_hits_lang.entrySet()) 
        {
        	System.out.println( entry.getKey() + ", " +  entry.getValue() );
        	
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) 
            {
                maxEntry = entry;
            }
        }
        
        System.out.println("Language is: " + maxEntry.getKey() );

        
        
        
    }
}
