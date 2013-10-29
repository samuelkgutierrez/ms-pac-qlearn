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

final class MoveMax {
    private MOVE move;
    private double max;

    public MoveMax(MOVE move, double max) {
        this.move = move;
        this.max = max;
    }

    public void max(double max) {
        this.max = max;
    }

    public double max() {
        return this.max;
    }

    public void move(MOVE move) {
        this.move = move;
    }

    public MOVE move() {
        return this.move;
    }
}

public class MyPacMan_RL extends Controller<MOVE> {
    private static final int NACTIONS = 4;
    // a table used to represent the learned state-action values.
    // the first index is a node index, the second a move.toint()
    private double[][] tableQ;

    // number of trials to run
    private int trials;
    // the learning rate (alpha)
    private double learning; // the learning rate
    // the discount factor (gamma)
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
            for (int action = 0; action < NACTIONS; ++action) {
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
            for (int action = 0; action < NACTIONS; ++action) {
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
        int actionCount = NACTIONS;
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
    public MOVE getMove(Game game, long timeDue) {
        int currentState = game.getPacmanCurrentNodeIndex();
        MOVE[] possibleMoves = game.getPossibleMoves(currentState);

        if (game.rnd.nextDouble() <= exploration) {
            // do random exploration
            int moveChoice = game.rnd.nextInt(possibleMoves.length);
            myMove = possibleMoves[moveChoice];
        }
        else {
            // greedy policy exploitation - arg-max selection
            myMove = getMMFromState(currentState, possibleMoves).move();
        }
        return myMove;
    }

    /* ////////////////////////////////////////////////////////////////////// */
    private MoveMax getMMFromState(int currentState, MOVE[] possibleMoves) {
        double max = 0.0;
        boolean first = true;
        MOVE myMove = MOVE.NEUTRAL;

        for (int i = 0; i < possibleMoves.length; ++i) {
            int movei = possibleMoves[i].toInt();
            if (first) {
                max = this.tableQ[currentState][movei];
                first = false;
                myMove = possibleMoves[i];
            }
            else {
                double cVal = this.tableQ[currentState][movei];
                if (cVal > max) {
                    max = cVal;
                    myMove = possibleMoves[i];
                }
            }
        }
        return new MoveMax(myMove, max);
    }

    /* ////////////////////////////////////////////////////////////////////// */
    public void performLearning(Game game, int previousState,
                                MOVE previousMove, double reward) {
        // table: [Node ID][U][R][D][L]
        //        [Node ID][U][R][D][L]
        //        ...
        int currentState = game.getPacmanCurrentNodeIndex();
        // array of possible moves given my current position
        MOVE[] possibleMoves = game.getPossibleMoves(currentState);
        // max Q(s', a)
        double maxQsp = getMMFromState(currentState, possibleMoves).max();
        // Q(s, a)
        double qsa = tableQ[previousState][previousMove.toInt()];
        // table update
        tableQ[previousState][previousMove.toInt()] =
                ((1.0 - learning) * qsa) +
                (learning * (reward + (discount * maxQsp)));
    }
}