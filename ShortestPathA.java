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
import java.lang.*;

//this program solves the shortest path problem with the A* algorithm
public class ShortestPathA
{
	//Node class (inner class)
	private class Node
	{
		private int id;			//vertex id
		private double gvalue;	//path cost
		private double hvalue;		//heuristic value
		private double fvalue;		//gvalue plus hvalue
		private Node parent;	//parent of vertex
		
		//constructor of node class
		private Node(int id)
		{
			this.id = id;					//assign vertex id
			this.gvalue = 0;				//path cost is zero
			this.hvalue = heuristic(this);	//heuristic value of the node
			this.fvalue = gvalue + hvalue;	//gvalue plus hvalue
			this.parent = null;
		}
	}
	
	private double[][] matrix;				//adjacency matrix
	private int size;						//number of verts
	private Node initial;					//initial node
	private Node goal;						//goal node
	private PrintWriter pW;					//printwriter for file output
	private long totTime;					//for timing info
	
	//constructor of ShortestPathA class
	public ShortestPathA(int vertices, int[][] edges, double[][] coords,
	PrintWriter pWrite, int initial, int goal)
	{
		size = vertices;				//assigns vertices
		
		matrix = new double[size][size];	//initialize adjacency matrix
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				matrix[i][j] = 0;
			
		for (int i = 0; i < edges.length; i++) //place distances in adj matrix
		{
			int target = edges[i][0];
			int destination = edges[i][1];
			matrix[target-1][destination-1] = matrix[destination-1][target-1] = 
			calcHypotenuse((target - 1), (destination - 1), coords);
		}
		
		this.initial = new Node(initial);
		this.goal = new Node(goal);
	}
	
	//method finds the hypotenuse of two coordinates
	public double calcHypotenuse(int coord1, int coord2, double[][] coordVals)
	{
		double coord1ValX = coordVals[coord1][0];
		double coord1ValY = coordVals[coord1][1];
		
		double coord2ValX = coordVals[coord2][0];
		double coord2ValY = coordVals[coord2][1];
		
		double xDist = coord1ValX - coord2ValX;
		double yDist = coord1ValY - coord2ValY;
		
		double aSquared = xDist * xDist;
		double bSquared = yDist * yDist;
		
		double cSquared = aSquared + bSquared;
		
		double hypLength = Math.sqrt(cSquared);
		
		return hypLength;
	}
	
	//method finds shortest path
	public void timedSolve()
	{
		//makes note of initial time before algo begins
		long startTime = System.currentTimeMillis();
		
		LinkedList<Node> openList = new LinkedList<Node>(); 	//open list
		LinkedList<Node> closedList = new LinkedList<Node>(); 	//closed list
		
		openList.addFirst(initial); //adding initial board to openlist

		while (!openList.isEmpty()) //continues as long as there are states left to search
		{
			int best = selectBest(openList);		//select best Node
			
			Node node = openList.remove(best); 		//remove Node

			closedList.addLast(node); 				//add node to closed list

		  if (complete(node)) 						//checks if goal node has been reached
		  {
			//makes note of end time after solve has completed
			long endTime = System.currentTimeMillis();
		
			//calculates total time taken
			totTime = endTime - startTime;
			
			displayPath(node, totTime);				//display path to goal
			return;									//stop search
		  }
		  else //if path not complete, create children
		  {
			//  System.out.println("need childrens");
			LinkedList<Node> children = generate(node); //generate children
			for (int i = 0; i < children.size(); i++)
			{
			  Node child = children.get(i); 			//pop children off list

			  //if it is not in open or closed list then add to end of open list
			  if (!exists(child, closedList)) 	//if not in closed list
			  {
				if (!exists(child, openList))	//if not in open list
					openList.addLast(child); 	//list add to open list
				else							//if it is already in open list
				{
					int index  = find(child, openList);
					if (child.fvalue < openList.get(index).fvalue)
					{							//if fvalue of new copy is less
						openList.remove(index);	//than fvalue of old copy
						openList.addLast(child);//replace old with new copy
					}
				}
			  }
			}
		  }
		}

	
		//makes note of end time after solve has completed
		long endTime = System.currentTimeMillis();
		
		//calculates total time taken
		totTime = endTime - startTime;
		
		//if no solution is found print no solution
		System.out.println("no solution");
		pW.println("no solution");
		
		System.out.println("Time taken: " + totTime);
		pW.println("Time taken: " + totTime);
	}

	//Method creates children of a node
	private LinkedList<Node> generate(Node node)
	{
		LinkedList<Node> children = new LinkedList<Node>();		//list of children
		
		for (int i = 0; i < size; i++)		//go through adjacency matrix
			if (matrix[node.id][i] != 0)	//and determine neighbors
			{
				Node child = new Node(i);	//create node for neighbor
											
											//parent path cost plus edge
				child.gvalue = node.gvalue + matrix[node.id][i];
											//heuristic value of child
				child.hvalue = heuristic(child);
											//gvalue plus hvalue
				child.fvalue = child.gvalue + child.hvalue;
				
				child.parent = node;		//assign parent to child
				
				children.addLast(child);	//add children list
			}
			
		return children;										//return children
	}

	
	//Method computes heuristic value of node
	private double heuristic(Node node)
	{
		return 0;
	}
  
			  /*
			  //Method computes heuristic value of board based on misplaced values
			  private int heuristic(Board board)
			  {
				  int value = 0;						//initial heuristic value
				  
				  for (int i = 0; i < size; i++)		//go thru board and
					for (int j = 0; j < size; j++)		//count misplaced values
						if (board.array[i][j] != goal.array[i][j])
							value += 1;
						
				  return value;							//return heuristic value
			  }
			  */
  
	//method locates the board with the minimum fvalue in a list of boards
	private int selectBest(LinkedList<Node> list)
	{
		double minValue = list.get(0).fvalue;		//initialize minimum
		int minIndex = 0;							//value and location

		for (int i = 0; i < list.size(); i++)
		{
		  double value = list.get(i).fvalue;
		  if (value < minValue)					//updates minimum if
		  {										//board with smaller
			  minValue = value;					//heuristic value is found
			  minIndex = i;
		  }
		}

		return minIndex;							//returns minimum location
	}

				  //Method creates copy of a board
			//	  private Board copy(Board board)
			//	  {
			//		return new Board(board.array, size);
			//	  }

	//Method decides whether a Node is complete
	private boolean complete(Node node)
	{
		return identical(node, goal);				//compare node with goal
	}

	//Method decides whether a Node exists in a list
	private boolean exists(Node node, LinkedList<Node> list)
	{
		for (int i = 0; i < list.size(); i++)		//compare node with each element in list
		if (identical(node, list.get(i)))
		return true;

		return false;
	}

	//method finds location of a node in a list
	private int find(Node node, LinkedList<Node> list)
	{
		for (int i = 0; i < list.size(); i++)		//compare node with each
			if (identical(node, list.get(i)))		//element of a list
				return i;
				
		return -1;
	}

	//Method decides whether two nodes are identical
	private boolean identical(Node p, Node q)
	{
		return p.id == q.id;		//compares vertex id of nodes
	}


	//Method displays path from initial to current node
	private void displayPath(Node node, long elapsedTime)
	{
		LinkedList<Node> list = new LinkedList<Node>();

		System.out.println("Sequence of nodes: ");
		pW.println("Sequence of nodes: ");

		Node pointer = node;						//start at current node

		while (pointer != null)						//go back towards initial node
		{
			list.addFirst(pointer);					//add boards to beginning of list

			pointer = pointer.parent;				//keep going back
		}

		for (int i = 0; i < list.size(); i++)		//print nodes in list
			displayNode(list.get(i));

		//printing number of swaps to find answer and elapsed time to screen
		System.out.println("Miles: " + list.getLast().gvalue);
		System.out.println("Run Time: " + elapsedTime + " Milliseconds");

		//printing number of swaps to find answer and elapsed time to external file
		pW.println("Miles: " + list.getLast().gvalue);
		pW.println("Run Time: " + elapsedTime + " Milliseconds");
	}

	//Method displays Node
	private void displayNode(Node node)
	{
		System.out.print((node.id + 1) + " "); //print vertex id of node
		pW.print((node.id + 1) + " "); 			//print vertex id of node
	}
}