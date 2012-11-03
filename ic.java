/**
 *Class which calculates the expected index of
 *coincidence and the actual index of coincidence
 *of given input text.
 *
 *The class takes as input the length of the ciphertext
 *and a list of possible key lengths. For example,
 *a 1000 character ciphertext with possible key lengths
 *being 3, 4, 5, 6, 10, and 20 would have the following 
 *input: 
 * ./ic 1000 3 4 5 6 10 20
 * 
 *This class can be used as a supplimentary to
 *ftable.java and kasiski.java to crack the 
 * Vigenere cipher. 
 *@version Project 2 CSC 456
 *@author Kim Paterson
 */
import java.util.LinkedList;
import java.lang.NumberFormatException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ic {
  public static void main ( String args[] ) {
    ic ndxcoinc = new ic();

    if (args.length == 0 ){
      ndxcoinc.printUsage();
    }

    int message_len = 0;
    
    //get the length of the ciphertext from input
    try{
      message_len = Integer.parseInt(args[0]);
    }
    catch (NumberFormatException e ){
      ndxcoinc.printUsage();
    }

    LinkedList<Integer> list = new LinkedList<Integer>();
    
    //get list of integers (possible key lengths)
    for ( int i=1; i < args.length; i ++ ){
      try{
        list.add(Integer.parseInt(args[i]));
      }
      catch (NumberFormatException e ){
        ndxcoinc.printUsage();
      }
    }
    
    //create a map of the indecies of coincidence for each given key length
    LinkedHashMap<Integer, Double> ics = new LinkedHashMap<Integer, Double>();
    
    for (Integer key_len : list) {
      ics.put(key_len, ndxcoinc.calc_expected_ic(message_len, key_len));
    }

    ndxcoinc.printTable(message_len, ics);

  }//end of main

  /**
   * Calculates the expected index of coincidence using the following 
   * equation:
   * (1/d)*((N-d)/(N-1))*(0.066) + ((d-1)/d)* (N/(N-1))*(0.038)
   *@param int_N is the length of the ciphertext
   *@param int_d is the length of the key
   *@return a double of the expected index of coincidence
   */
  public Double calc_expected_ic(Integer int_N, Integer int_d){
    double N = int_N *1.0;
    double d = int_d *1.0;
    
    return ((1.0/d)*((N-d)/(N-1.0))*(0.066)) + (((d-1.0)/d)*(N/(N-1))*(0.038));
  }

  
  /**
   *Prints the final table given the Indecies of Coincidence
   *@param message_len is the length of the ciphertext
   *@param ics is the sotred indecies of coincidence
   */
  public void printTable(Integer message_len, 
                         LinkedHashMap<Integer, Double> ics ){
    System.out.printf("Key Expected IC (N=%d)\n", message_len);
    System.out.println("  ---- ------------------");
    for (Map.Entry<Integer, Double> entry : ics.entrySet()){
      System.out.printf("%4d %-5.4f\n", entry.getKey(), entry.getValue());
    }
  }

  /**
   * Calculates the IC value given an input string.
   * IC = (1/(N*(N-1)) * sum(Fi * (Fi-1)))
   *@param input is the ciphertext input
   *@param N is the length of the ciphertext
   *@return a double with the actual index of coincidence between characters
   */
  public Double calc_ic(String input, int N){
    //create hash table 
    LinkedHashMap<String, Integer> letter_freqs = 
      new LinkedHashMap<String, Integer>();

    String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
                         "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                         "W", "X", "Y", "Z"};

    //look at only letters
    input = input.replaceAll("[^A-Za-z]", "");

    //put all characters into the map with a frequency of 0
    for (String letter : alphabet){
      letter_freqs.put(letter, 0);
    }

    //take each letter in the input and put it into the map, incrementing count
    for ( int i=1; i < input.length()+1; i++){
      String sub = input.substring(i-1, i);

      if (letter_freqs.containsKey(sub)){
        int count = letter_freqs.get(sub)+ 1;
        letter_freqs.put(sub, count);
      }
    }
    
    //Calculate sum of frequencies
    int sum = 0;
    for(Map.Entry<String, Integer> entry : letter_freqs.entrySet()){
      sum += entry.getValue() * (entry.getValue() - 1);
      
    }
    
    //broken up into three lines for debugging
    double first = 1.0/(N*(N-1)) ;
    double ic_final = (first) * sum;
    return ic_final;
  }

  /**
   * Prints the correct usage for the system.
   */
  public void printUsage() {
    System.out.println("ic N l [12 [...] ]");
    System.exit(1);
  }

}//end of class
