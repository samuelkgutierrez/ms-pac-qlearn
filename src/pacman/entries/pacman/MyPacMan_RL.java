package pacman.entries.pacman;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MyPacMan_RL extends Controller<MOVE>
{
	// A table used to represent the learned state-action values.
	// The first index is a node index, the second a MOVE.toInt()
	private double[][] tableQ;
	
	private int trials; // how many trials to run
	private double learning; // the learning rate
	private double discount; // the discount rate
	private double exploration; // the exploration rate
	
	private MOVE myMove=MOVE.NEUTRAL;
	
	// Construct a new controller with an empty Q table.
	public MyPacMan_RL(int _trials, double _learning, 
			double _discount, double _exploration)
	{
		trials = _trials; assert trials >= 0;
		learning = _learning; assert learning >= 0.0; assert learning <= 1.0;
		discount = _discount; assert discount >= 0.0; assert discount <= 1.0;
		exploration = _exploration; assert exploration >= 0.0; assert exploration <= 1.0;		
	}
	
	// Construct a new controller with a value table passed in.
	public MyPacMan_RL(int _trials, double _learning, 
			double _discount, double _exploration, 
			double[][] _tableQ)
	{
		trials = _trials; assert trials >= 0;
		learning = _learning; assert learning >= 0.0; assert learning <= 1.0;
		discount = _discount; assert discount >= 0.0; assert discount <= 1.0;
		exploration = _exploration; assert exploration >= 0.0; assert exploration <= 1.0;
		tableQ = _tableQ;
	}
	
	// Initialize tableQ from Game object
	public void initializeTableQ(Game game)
	{
		int number_of_nodes = game.getNumberOfNodes();
		tableQ = new double[number_of_nodes][4];
	}

	// Returns the value table.
	public double[][] getTableQ()
	{
		return tableQ;
	}
	
	// Prints the Q Table to the console for debugging convenience.
	public void printTableQ()
	{
		for(int state = 0; state < tableQ.length; ++state)
		{
			for(int action = 0; action < 4; ++action)
			{
				System.out.print(""+state+":"+MOVE.fromInt(action).toString()+":"+tableQ[state][action]+"\n");
			}
		}
	}
	
	// Save the Q Table to a text file.
	public void saveTableQ(String filename) throws IOException
	{
		BufferedWriter out = new BufferedWriter(new FileWriter(filename));
		for(int state = 0; state < tableQ.length; ++state)
		{
			for(int action = 0; action < 4; ++action)
			{
				out.write(""+state+":"+MOVE.fromInt(action).toString()+":"+tableQ[state][action]+"\n");
			}
		}
		out.close();
	}
	
	// Replaces the current Q Table with one specified in the read file.
	public void loadTableQ(String filename) throws IOException
	{
		int state_count = tableQ.length;
		int action_count = 4;
		tableQ = new double[state_count][action_count];
		
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String line = null;
		while((line = in.readLine()) != null)
		{
			String[] parts = line.split(":");
			tableQ[Integer.parseInt(parts[0])][MOVE.fromString(parts[1]).toInt()]
					= Double.parseDouble(parts[2]);
		}
	}
	
	// Your code for following the Epsilon-Greedy Q-Learning Policy goes here.
	public MOVE getMove(Game game, long timeDue)
	{
		// the current node of Ms PacMan and her possible actions
		int current_state = game.getPacmanCurrentNodeIndex();
		MOVE[] possible_moves = game.getPossibleMoves(current_state);
		
		if(game.rnd.nextDouble() <= exploration)
		{
			// Random Exploration
			int move_choice = game.rnd.nextInt(possible_moves.length);
			myMove = possible_moves[move_choice];
		}
		else
		{
			// Greedy Policy Exploitation
			
			//
			// Implement Arg-Max Action Selection Here
			//
		}
		return myMove;
	}
	
	// Q-Learning back-propagation here.
	public void performLearning(Game game, int previous_state, 
			MOVE previous_move, double reward)
	{
		//
		// Implement Q-Learning Back-Propagation here
		//
	}
}