/**
 *Class which opens files and reads from files and assists 
 * Vigenere cipher 
 *@version Project 2
 *@author Kim Paterson
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class cipherio {

  private BufferedReader read = null;
  private BufferedWriter write = null;

  /**
   * Open read and write files
   */ 
  public void openRW(String infile, String outfile){

    //open infile and outfile
    if( infile != null ){
      try{
        read = new BufferedReader(new FileReader(infile));
      }
      catch(FileNotFoundException e){
        System.out.println("Unable to locate file: " + infile);
        System.exit(1);
      }        

      if ( outfile != null ){
        try{
          write = new BufferedWriter(new FileWriter(outfile));
        }
        catch(IOException e){
          System.out.println("File exists but cannot be created or opened" +
                             "for unknown reasons.");             
          System.exit(1);
        }
      }
      else {
        write = new BufferedWriter(new OutputStreamWriter(System.out));
      }
    }
    else{
        read = new BufferedReader(new InputStreamReader(System.in));
        write = new BufferedWriter(new OutputStreamWriter(System.out));
    }
  }


  /**
   * Takes BufferedReader as input and reads file, 
   * returning a string of the file content.
   */
  public String readFile(){
    String input = "";
    int letter = -1;
    String temp = null;

    try{
      temp = read.readLine();
    }
    catch( IOException e){
        System.out.println("Unable to read from file.");
        System.exit(1);
    }
    while ( temp != null ){
      input += temp + "\n";
      try{
        temp = read.readLine();
      }
      catch(IOException e){
        System.out.println("Unable to read from file.");
        System.exit(1);
      }
    }
    input = input.toUpperCase();
    return input;
  }

  /**
   * Writes given output string to file. 
   */ 
  public void writeFile( String output){
    //write to output file
    for ( int m = 0; m < output.length(); m++ ){
      try{
        write.write(output.charAt(m));
      } catch (IOException e){
        System.out.println("Cannot write to file");
        System.exit(1);
      }
    }
  }


  /**
   * Attempts to close file streams. 
   */
  public void closeStreams(){

    try{
      write.close();
      read.close();
    }
    catch(IOException e){
      System.out.println("Unable to close files.");
      System.exit(1);
    }
  }

}//end of class
