/**
 * Modifications By: Samuel K. Gutierrez
 */
package pacman.entries.pacman;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MyPacMan_RL extends Controller<MOVE> {
    // a table used to represent the learned state-action values.
    // the first index is a node index, the second a move.toint()
    private double[][] tableQ;

    // number of trials to run
    private int trials;
    // the learning rate
    private double learning; // the learning rate
    // the discount rate
    private double discount; // the discount rate
    // the exploration rate
    private double exploration; // the exploration rate

    private MOVE myMove = MOVE.NEUTRAL;

    /* ////////////////////////////////////////////////////////////////////// */
    // constructs a new controller with an empty q table.
    public MyPacMan_RL(int trials, double learning,
                       double discount, double exploration) {
        this.trials = trials;
        this.learning = learning;
        this.discount = discount;
        this.exploration = exploration;
        // make sure that the supplied values are okay - throws
        this.paramSanity();

    }

    /* ////////////////////////////////////////////////////////////////////// */
    public MyPacMan_RL(int trials, double learning,
                       double discount, double exploration,
                       double[][] tableQ) {
        this(trials, learning, discount, exploration);
        this.tableQ = tableQ;
    }

    /* ////////////////////////////////////////////////////////////////////// */
    private void paramSanity() {
        if (this.learning <= 0.0 || this.learning >= 1.0 ||
            this.trials <= 0 || this.exploration <= 0.0 ||
            this.exploration >= 1.0 || this.discount <= 0.0 ||
            this.discount >= 1.0) {
            throw new IllegalArgumentException("*** invalid parameter ***");
        }
    }

    /* ////////////////////////////////////////////////////////////////////// */
    public void initializeTableQ(Game game) {
        tableQ = new double[game.getNumberOfNodes()][4];
    }

    /* ////////////////////////////////////////////////////////////////////// */
    public double[][] getTableQ() {
        return tableQ;
    }

    /* ////////////////////////////////////////////////////////////////////// */
    public void printTableQ() {
        for (int state = 0; state < tableQ.length; ++state) {
            for (int action = 0; action < 4; ++action) {
                System.out.print("" + state + ":"
                                 + MOVE.fromInt(action).toString() + ":"
                                 + tableQ[state][action] + "\n");
            }
        }
    }

    /* ////////////////////////////////////////////////////////////////////// */
    // save the q table to a text file.
    public void saveTableQ(String filename) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(filename));
        for (int state = 0; state < tableQ.length; ++state) {
            for (int action = 0; action < 4; ++action) {
                out.write("" + state + ":" + MOVE.fromInt(action).toString()
                          + ":" + tableQ[state][action] + "\n");
            }
        }
        out.close();
    }

    /* ////////////////////////////////////////////////////////////////////// */
    // replaces the current q table with one specified in the read file.
    public void loadTableQ(String filename) throws IOException {
        String line = null;
        int stateCount = tableQ.length;
        int actionCount = 4;
        tableQ = new double[stateCount][actionCount];

        BufferedReader in = new BufferedReader(new FileReader(filename));
        while (null != (line = in.readLine())) {
            String[] parts = line.split(":");
            tableQ[Integer.parseInt(parts[0])]
                  [MOVE.fromString(parts[1]).toInt()] =
                  Double.parseDouble(parts[2]);
        }
        in.close();
    }

    /* ////////////////////////////////////////////////////////////////////// */
    // Your code for following the Epsilon-Greedy Q-Learning Policy goes here.
    public MOVE getMove(Game game, long timeDue) {
        int currentState = game.getPacmanCurrentNodeIndex();
        MOVE[] possibleMoves = game.getPossibleMoves(currentState);

        if (game.rnd.nextDouble() <= exploration) {
            // do random exploration
            int move_choice = game.rnd.nextInt(possibleMoves.length);
            myMove = possibleMoves[move_choice];
        }
        else {
            // Greedy Policy Exploitation

            //
            // Implement Arg-Max Action Selection Here
            //
        }
        return myMove;
    }

    /* ////////////////////////////////////////////////////////////////////// */
    // Q-Learning back-propagation here.
    public void performLearning(Game game, int previous_state,
            MOVE previous_move, double reward) {
        //
        // Implement Q-Learning Back-Propagation here
        //
    }
}