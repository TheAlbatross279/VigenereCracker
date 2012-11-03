/**
 * Class which maps repeated substrings in ciphertext 
 * and calculates the distance between substrings. The 
 * class prints out a table with the substrings, the length
 * of the substrings, the number of times it appears, 
 * and the indecies of the substrings with the distance
 * between each substring printed in parenthesis. 
 *
 * This class can be used to crack the Vigenere cipher, 
 * which uses a keyword to create multiple shifted 
 * alphabets. By using a letter frequency table and
 * measuring the index of coincidence between characters, 
 * you can determine which of the ciphertext letters 
 * correspond to to an unshifted alphabet.
 *
 *@version Project 2 CSC 456 Security
 *@author Kim Paterson
 */
import java.lang.NumberFormatException;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Comparator;

public class kasiski {

  public static void main ( String args[] ) {
    kasiski kas = new kasiski();
    
    boolean verbose = false;
    //default minimum length of the substrings
    int length = 3; 

    String infile = null;
    String outfile = null;

    //Command line argument parsing with no dashes for
    //optional fields

    /* Possible arguments of length 5
     * -v -m length infile outfile
     */
    if ( args.length == 5 ) {
      if ( args[0].equals( "-v" ) ) {
        verbose = true;
      }
      else {
        kas.printUsage();
      }
      if ( args[1].equals( "-m")){
        try {
          length = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
          kas.printUsage();
        }
      }
      else {
        kas.printUsage();
      }
      infile = args[3];
      outfile = args[4];
    }
    /*
     * -v -m length infile
     * -m length infile outfile
     */
    else if ( args.length == 4 ) {
      if ( args[0].equals( "-v" ) ){
        verbose = true;
      }
      else if ( args[0].equals ( "-m") ){
        try {
          length = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
          kas.printUsage();
        } 
        infile = args[2];
        outfile = args[3];
      }
      else {
        kas.printUsage();
      }
      if ( args[1].equals ( "-m") ){
        try {
          length = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
          kas.printUsage();
        } 
        infile = args[3];
      }
    }
    /*
     * -v -m length
     * -v infile outfile
     * -m length infile
     */
    else if ( args.length == 3 ) {
      if ( args[0].equals( "-v" )) {
        verbose = true;
        if ( args[1].equals ( "-m") ){
          try {
            length = Integer.parseInt(args[2]);
          } catch (NumberFormatException e) {
            kas.printUsage();
          } 
        }
        else{
          infile = args[1];
          outfile = args[2];
        }
      }
      else if ( args[0].equals ( "-m") ){
        try {
          length = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
          kas.printUsage();
        } 
        infile = args[2];
      }
    }
    /*
     * -v infile
     * -m length
     * infile outfile
     */
    else if ( args.length == 2 ) {
      if ( args[0].equals("-v" )) {
        verbose = true;
        infile = args[1];
      }
      else if ( args[0].equals("-m") ) {
        try {
          length = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
          kas.printUsage();
        }
      }
      else {
        infile = args[0];
        outfile = args[1];
      }
    }
    /*
     * -v
     * infile
     */
    else if ( args.length == 1 ) {
      if ( args[0].equals( "-v" ) ){
        verbose = true;
      }
      else{ 
        infile = args[0];
      }
    }
    
    if (verbose){
      System.out.println("verbose: " + verbose + " -m " + length + 
                       " input: " + infile + " output: " + outfile);
    }

    //object to open and read from input file and print to output
    cipherio io = new cipherio();
    io.openRW(infile, outfile);
    String input = io.readFile();

    if(verbose){
      System.out.println("input: " + input);
    }

    //find the repeated substrings
    kas.findSubstr(input, length, verbose);
    
    io.closeStreams();
  }//end of main

  /**
   * Prints command line usage for program.
   */
  public void printUsage() {
    System.out.println("kasiski [-v] [-m length] [infile [outfile]]");
    System.exit(0);
  }

  /**
   * Finds the common substrings given a minimum length. The method
   * keeps track of the counts of the substring and keeps a second map
   * of the indecies of the substrings.
   *
   * @param length the minimum length substring to find (default 3) 
   * @param input is the enciphered text to analyze
   * @param verbose is a boolean value that when true, enables verbose printing
   */
  public void findSubstr(String input, int length, boolean verbose) {

    LinkedHashMap<String, Integer> counts = 
      new LinkedHashMap<String, Integer>();
    LinkedHashMap<String, LinkedList<Integer>> collisions = 
      new LinkedHashMap<String, LinkedList<Integer>>();

    //remove all non-alpha characters
    input = input.replaceAll("[^A-Za-z]", "");
    boolean foundSubStr = false;
    int orig_len = length;
    boolean found_any = false;

    //sliding window to check for repeated substrings.
    for ( int i=0, j=length; j <= input.length() ;) {
      String sub = input.substring(i, j);

      //if the substring is in the map
      if ( counts.containsKey(sub) && collisions.containsKey(sub) ){
        int count = counts.get(sub);
        counts.put(sub, count+1);
    
        //store the index of the substring
        LinkedList<Integer> temp = collisions.get(sub);
        temp.add(i);
        collisions.put(sub, temp);
        
        //set found to true
        foundSubStr = true;
        found_any = true;
      }
      //put it in the map with a count of 1
      else {
        counts.put(sub, 1);
        LinkedList<Integer> list = new LinkedList<Integer>();
        list.add(i);
        collisions.put(sub, list);
      }

      // condition that checks if it didn't any substrings of size 'length', 
      // then don't contiue to check length 'length + 1')
      if( j == input.length() && foundSubStr == true){
        foundSubStr = false;      
        i = 0;
        length += 1;
        j = length;
      }
      else {
        i++;
        j++;
      }
    }
    
    //The output must be sorted by length, then count, then alphabetically.
    // The comparator and treemap sort on theses three values
    TreeMap<String, Integer> counts_final =
      new TreeMap<String, Integer>(new LengthCompare(counts));

    //Sort elements by putting back in map
    for (Map.Entry<String, Integer> entry : counts.entrySet()){
      counts_final.put(entry.getKey(), entry.getValue());
    }

    if ( verbose ){
      for (Map.Entry<String, LinkedList<Integer>> entry: collisions.entrySet()){
        System.out.println( "Substr: " + entry.getKey() + " list: " 
                              + entry.getValue()) ;
      } 
      for (Map.Entry<String, Integer> entry: counts_final.entrySet() ){
        System.out.println( "Substr: " + entry.getKey() + " count: " 
                            + entry.getValue()) ;
      } 
    }
    //Don't print if we never found any!
    if( found_any ){
      printSubstrResults(counts_final, collisions, orig_len);
    }
  }
  
  /**
   * Prints the substring counts, the repeated substrings and the positions of
   * each substring in the ciphertext with a distance from the previous 
   * substring.
   *@param counts is a sorted map with the substrings as keys and counts as values
   *@param locations is a map with the substrings as keys and a linked list of the 
   * index of each substring.
   *@param orig_len is a copy of the original minimum length inputed by the user
   */
  public void printSubstrResults( TreeMap<String, Integer> counts, 
                                  LinkedHashMap<String, LinkedList<Integer>> 
                                  locations, int orig_len ){

    System.out.println("Length Count Word Location (distance)");
    System.out.println("====== ===== ==== =========");

    for (Map.Entry<String, Integer> entry : counts.entrySet() ) {
      if ( entry.getValue() >= 2 ) {
        LinkedList<Integer> list = locations.get(entry.getKey());
        System.out.printf("%6d %5d %4s ", (entry.getKey()).length(), 
                          entry.getValue(), entry.getKey() );
        int i = 0;
        for (; i < list.size(); i++){
          if (i > 0) {
            System.out.printf("%d (%d) ", list.get(i), 
                              list.get(i) - list.get(i-1));            
          }
          else{
            System.out.print(list.get(i) + " ");
          }
        }
        System.out.print("\n");
      }
    }
  }

  //comparator method for sorting treemap
  private class LengthCompare implements Comparator<String> {

    Map<String, Integer> base;
    public LengthCompare(Map<String, Integer> base){
      this.base = base;
    }

      @Override 
      public int compare(String s1, String s2){
      int return_val = 0;

      //sort by length
      if ( s1.length() < s2.length() ){
        return_val = 1;
      }
      else if ( s1.length() > s2.length() ){
        return_val = -1;
      }
      else { //sort by count
        if (base.get(s1) < base.get(s2)){
          return_val = 1;
        }
        else if (base.get(s1) > base.get(s2)){
          return_val = -1;
        }
        else{
          //Alphabetical
          return_val = s1.compareTo(s2);
        }
      }
      return return_val;
    }

  }//end of private class

}//end of class
