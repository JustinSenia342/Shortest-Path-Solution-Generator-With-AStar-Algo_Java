/*
 * Name: Justin Senia
 * E-Number: E00851822
 * Date: 10/20/2017
 * Class: COSC 461
 * Project: #2
 */
import java.util.LinkedList;
import java.io.*;
import java.util.*;

public class ShortestPathATester
{

    //Main method for testing
    public static void main(String[] args) throws IOException
    {
      //creating buffered reader for getting user input
      java.io.BufferedReader keyIn = new BufferedReader(new InputStreamReader(System.in));

	  //message welcoming to the program/giving instructions
      System.out.println("*****************************************");
      System.out.println("*         Shortest Path Problem         *");
      System.out.println("*          (Using A* Algorithm)         *");
      System.out.println("*****************************************");
      System.out.println("*  Please enter in a filename to start  *");
      System.out.println("* or type quit to terminate the program *");
      System.out.println("*****************************************");

      //start a loop that continues querying for input as long as user
      //does not enter "quit" (without the quotes)
      while (true)
      {
        System.out.print("Please make your entry now: ");
        String userIn = ""; //used for file entry and/or quitting

        userIn = keyIn.readLine(); //reading user input

        if (userIn.equalsIgnoreCase("quit")) //if user typed quit, stops program
          break;
        else{
              try
              {
				//establishing working directory for file I/O
                String currentDir = System.getProperty("user.dir");
                File fIn = new File(currentDir + '\\' + userIn);
				
				//using scanner with new input file based on user input
                Scanner scanIn = new Scanner(fIn);

				//creating printwriter for file output
                File fOut = new File("output_" + userIn);
                PrintWriter PWOut = new PrintWriter(fOut, "UTF-8");

				//scanning external file for Board dimensions & number of possible verts
                int numOfVerts = scanIn.nextInt();
                int numOfEdges = scanIn.nextInt();
				int edgeCols = 2;	//controls file in data populating loop for edges
				

				//declaring multidimensional arrays to hold externally read data
				double[][] coords = new double[numOfVerts][edgeCols]; 
                int[][] edges = new int[numOfEdges][edgeCols];

				//populating coord array, coord name will be = array index + 1
                for (int i = 0; i < numOfVerts; i++)
                {
					//ignoring coord name file input since naming is sequential
					//coord name = coords[] index+1
					scanIn.nextInt();	
					
                  for (int j = 0; j < edgeCols; j++)
                  {
					coords[i][j] = scanIn.nextDouble();			//adding coordinates to array
					System.out.print(" " + coords[i][j]);
                  }
                  System.out.println(""); //used to see if arrays transferred correctly
                }
				
				//populating edges array, with data that will be used to create an adjacency matrix
                for (int i = 0; i < numOfEdges; i++)
                {
                  for (int j = 0; j < edgeCols; j++)
                  {
					edges[i][j] = scanIn.nextInt();			//adding edges to array
					System.out.print(" " + edges[i][j]);	//ease of array use
                  }
                  System.out.println(""); //used to see if arrays transferred correctly
                }
				
				int startingVert = scanIn.nextInt();
				int endingVert = scanIn.nextInt();
				
				System.out.println("SV: " + startingVert + " EV: " + endingVert);
				
				/*
				//skipping newline chars and "beam sizes " string
				System.out.println(scanIn.nextLine());
				System.out.println(scanIn.nextLine());
				scanIn.skip("[A-z]* [A-z]* ");

				//reading beamsizes from file
				beamSize[0] = scanIn.nextInt();
				beamSize[1] = scanIn.nextInt();
				beamSize[2] = scanIn.nextInt();
				
				//for verification of beamsizes
				//System.out.println(beamSize[0] + " " + beamSize[1] + " " + beamSize[2]);
				*/
				
				//printing labels so user will know it's output
                System.out.println("\n***Shortest Path A* Output for " + userIn + "***\n");
                PWOut.println("\n***Shortest Path A* Output for " + userIn + "***\n");

				//creating new TravelBeam object and calling timedSolve Method
                ShortestPathA s = new ShortestPathA(numOfVerts, edges, coords, PWOut, startingVert,
				endingVert);
                s.timedSolve();

				//closing printwriter and scanner objects to maintain file integrity
                scanIn.close();
                PWOut.close();
              }
              catch (IOException e) //catches if there were any fileIO exceptions
              {
                ;
              }
            }
      }
    }
}
