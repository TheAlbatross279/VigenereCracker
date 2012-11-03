/**
 * Class which creates a frequency table for input file, 
 * counting only letters.
 *
 * This class can be used to crack the Vigenere cipher 
 * with ic.java and kasiski.java
 * 
 *@version Project 2 CSC 456
 *@author Kim Paterson
 */
import java.util.HashMap;
import java.lang.Math;
import java.lang.NumberFormatException;
import java.util.Map;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedHashMap;

public class ftable{

  private BufferedReader read = null;
  private BufferedWriter write = null;
  private int total_num; 
  private String shortened_input = "";

  public static void main(String[] args){
    boolean verbose = false;
    int num_to_skip = 0;
    int num_period = 1;
    String input_file = null;
    String output_file = null;

    ftable ftable_obj = new ftable();

    //command line arguments with no dashes for skipping
    //all arguments there
    if ( args.length == 7 ){
      if ( args[0].equals("-v") ){
        verbose = true;
      }
      else{
        ftable_obj.print_usage();
      }
      if ( args[1].equals("-s") ) {
        if ( args[2].equals("-p") ){
          ftable_obj.print_usage();
        }
        try{
          num_to_skip = Integer.parseInt(args[2]);
        } catch (NumberFormatException e){
          ftable_obj.print_usage();
        }
      }
      else{
        ftable_obj.print_usage();
      }
      if ( args[3].equals("-p") ) {
        try{
          num_period = Integer.parseInt(args[4]);
        } catch (NumberFormatException e){
          ftable_obj.print_usage();
        }
      } 
      else{
        ftable_obj.print_usage();
      }
      
      input_file = args[5];
      output_file = args[6];
    }
    /* Possible arguments of length 6: 
     * -v -s num -p num infile
     * -s num -p num infile outfile
     */
    else if ( args.length == 6 ) {
      if ( args[0].equals("-v") ){
        verbose = true;
        if ( args[1].equals("-s") ) {
          if ( args[2].equals("-p") ){
            ftable_obj.print_usage();
          }
          try{
            num_to_skip = Integer.parseInt(args[2]);
          } catch (NumberFormatException e){
            ftable_obj.print_usage();
          }
          if ( args[3].equals("-p") ) {
            try{
              num_period = Integer.parseInt(args[4]);
            } catch (NumberFormatException e){
              ftable_obj.print_usage();
            }
          }
          input_file = args[5];
        }
        else{
          ftable_obj.print_usage();
        }
      }
      else if ( args[0].equals("-s") ) {
         if ( args[1].equals("-p") ){
            ftable_obj.print_usage();
          }
          try{
            num_to_skip = Integer.parseInt(args[1]);
          } catch (NumberFormatException e){
            ftable_obj.print_usage();
          }
          if ( args[2].equals("-p") ) {
            try{
              num_period = Integer.parseInt(args[3]);
            } catch (NumberFormatException e){
              ftable_obj.print_usage();
            }
          }
          input_file = args[4];
          output_file = args[5];
      }
      else {
        ftable_obj.print_usage();
      }
    }
    /* Possible arguments of length 5: 
     * -v -s num -p num
     * -v -s num infile outfile
     * -v -p num infile outfile
     * -s num -p num infile 
     */
    else if ( args.length == 5 ) {
      if ( args[0].equals("-v") ) {
        verbose = true;
      }
      else if ( args[0].equals("-s") ){
        try{
          num_to_skip = Integer.parseInt(args[1]);
        } catch (NumberFormatException e){
          ftable_obj.print_usage();
        }
        if ( args[2].equals("-p") ) {
          try{
            num_period = Integer.parseInt(args[3]);
          } catch (NumberFormatException e){
            ftable_obj.print_usage();
          } 
        }
        else {
          ftable_obj.print_usage();
        }
        input_file = args[4];
      }
      if ( args[1].equals("-s") ) {
        try{
          num_to_skip = Integer.parseInt(args[2]);
        } catch (NumberFormatException e){
          ftable_obj.print_usage();
        }
        input_file = args[3];
        output_file = args[4];
      }
      else if ( args[1].equals("-p") ) {
        try{
          num_period = Integer.parseInt(args[2]);
        } catch (NumberFormatException e){
          ftable_obj.print_usage();
        }
        input_file = args[3];
        output_file = args[4];
      }
     if ( args[3].equals("-p") ) {
        try{
          num_period = Integer.parseInt(args[4]);
        } catch (NumberFormatException e){
          ftable_obj.print_usage();
        }
      }
    }
    /* Possible arguments of length 4:
     * -v -s num infile
     * -v -p num infile
     * -s num -p num
     * -s num infile outfile
     * -p num infile outfile
     */
    else if ( args.length == 4) {
      if ( args[0].equals("-v") ) {
        verbose = true;

        if ( args[1].equals("-s") ) {
          try{
            num_to_skip = Integer.parseInt(args[2]);
              } catch (NumberFormatException e){
            ftable_obj.print_usage();
          }
        }
        else if ( args[1].equals("-p") ) {
          try{
            num_period = Integer.parseInt(args[2]);
          } catch (NumberFormatException e){
            ftable_obj.print_usage();
          }
        }
        else { 
          ftable_obj.print_usage();
        }
        input_file = args[3];
      }
      else if ( args[0].equals("-s") ) {
         try{
           num_to_skip = Integer.parseInt(args[1]);
         } catch (NumberFormatException e){
            ftable_obj.print_usage();
          }
        if ( args[2].equals("-p") ) {
          try{
            num_period = Integer.parseInt(args[3]);
          } catch (NumberFormatException e){
            ftable_obj.print_usage();
          }
        }
        else {
          input_file = args[2];
          output_file = args[3];
        }
      }
    }
    /* Possible arguments of length 3:
     * -v -s num
     * -v -p num
     * -s num infile
     * -p num infile
     * -v infile outfile
     */
    else if ( args.length == 3 ) {
      if ( args[0].equals("-v") ) {
        verbose = true;

        if ( args[1].equals("-s") ) {
          try{
            num_to_skip = Integer.parseInt(args[2]);
              } catch (NumberFormatException e){
            ftable_obj.print_usage();
          }
        }
        else if ( args[1].equals("-p") ) {
          try{
            num_period = Integer.parseInt(args[2]);
          } catch (NumberFormatException e){
            ftable_obj.print_usage();
          }
        }
        else { 
          input_file = args[1];
          output_file = args[2];
        }
      }
      else if ( args[0].equals("-s") ) {
         try{
           num_to_skip = Integer.parseInt(args[1]);
         } catch (NumberFormatException e){
            ftable_obj.print_usage();
         }
         input_file = args[2];
      }
      else if ( args[0].equals("-p") ) {
         try{
           num_period = Integer.parseInt(args[1]);
         } catch (NumberFormatException e){
            ftable_obj.print_usage();
         }
         input_file = args[2];
      }
    }
    /* Possible arguments of length 2:
     * -v infile
     * -s num
     * -p num
     */
    else if ( args.length == 2 ) {
      if ( args[0].equals("-v") ) {
        verbose = true;
        input_file = args[1];
      }
      else if ( args[0].equals("-s") ) {
         try{
           num_to_skip = Integer.parseInt(args[1]);
         } catch (NumberFormatException e){
            ftable_obj.print_usage();
         }
      }
      else if ( args[0].equals("-p") ) {
         try{
           num_period = Integer.parseInt(args[1]);
         } catch (NumberFormatException e){
            ftable_obj.print_usage();
         }
      }
    }
    /* Possible arguments of length 1:
     * -v
     * infile
     */
    else if ( args.length == 1 ) {
      if ( args[0].equals("-v") ) {
        verbose = true;
      }
      else {
        input_file = args[0];
      }
    }

    if (verbose){
      System.out.println("verbose: " + verbose + " -s " + num_to_skip 
                         + " -p " + num_period + 
                       " input: " + input_file + " output: " + output_file);
    }

    //open and read files
    cipherio io = new cipherio();
    io.openRW(input_file, output_file);

    String input = io.readFile();
    LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();

    //process input, create linkedhashmap
    map = ftable_obj.fill_ftable(map, input, num_to_skip, num_period, verbose);
    
    //output the frequencues in Map
    ftable_obj.print_output(map);
    io.closeStreams();    
  }//end main

  /**
   * Computes the frequencies for characters in input and skips  
   * characters according to num_to_skip and num_period.
   *@param map is a character map of inputs
   *@param input is the input string read from the file
   *@param num_to_skip designates to skip the first n characters 
   *@param num_period designates the method to skip every n characters
   *@param verbose optional flag that prints extra output
   *@return returns a map of the characters 
   *
   */
  private LinkedHashMap<String, Integer> fill_ftable( LinkedHashMap<String, 
                                                     Integer> map, String input,
                                                     int num_to_skip, 
                                                     int num_period, 
                                                     boolean verbose ){

    if (num_period == 0 ){
      num_period = 1;
    }

    //sanity check on num_input and num_period
    if ( num_to_skip >= input.length() ){
      num_to_skip = 1;
    }
    if ( num_period >= input.length() ){
      num_period = 0;
    }
    String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
                         "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                         "W", "X", "Y", "Z"};
    input = input.toUpperCase();

    if ( verbose ) {
      System.out.println("Input: '" + input + "'");
    }

    //start count of characters
    total_num  = 0;

    //initialize map with all letters
    for ( String letter : alphabet ){
      map.put(letter, 0);
    }

    //Remove spaces
//    input = input.replaceAll("[ \n]", "");
    input = input.replaceAll("[^A-Za-z]", "");    
    
    for ( int i = num_to_skip; i < input.length(); i+=num_period  ){
      String temp = input.substring(i, i+1);
      shortened_input += temp;
      while (temp.equals(" ") && i < input.length()){
        i++;
      }
      if (map.get(temp) != null && !temp.equals(" ")){
        map.put(temp, (map.get(temp) + 1));
        total_num++;
      }
      else if(!temp.equals(" ")){
        map.put(temp, 1);
        total_num++;
      }  
    } 
    System.out.println("");
    return map;
  }


  /**
   * Prints usage for program and exits
   */
  private void print_usage(){
    System.out.println("Usage: ftable [-v] [-s sum]" 
                       + "[-p num] [infile [outfile]]");
    System.exit(1);
  }

  /**
   * Prints final output
   *@param table takes in map of character frequencies
   */
  private void print_output(LinkedHashMap<String, Integer> table){
    System.out.printf("Total chars: %d\n", total_num);

    if ( total_num == 0 ) {
      total_num = 1;
    }
    
    for ( Map.Entry entry : table.entrySet() ){
      double percent = ((double)(Integer)entry.getValue() / total_num);
      System.out.printf(" %s:%9d ( %5.2f" ,  entry.getKey(),  entry.getValue(), 
                        percent * 100);
      System.out.print("%)  ");
      for (int i=0; i < Math.ceil(percent*100); i++){
        System.out.print("*");
      }
      System.out.print("\n");
    }

    // print IC
    ic ic_gen = new ic();

    //input = input.replaceAll("[^A-Za-z]", "");

    double ic_val = ic_gen.calc_ic(shortened_input, shortened_input.length());

    System.out.printf("\nIndex of Coincidence: %5.4f \n", ic_val);

  }


}//end of class
