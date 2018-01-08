/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruhani.sudoku;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Vector;

/**
 *
 * @author Ruhani
 */
public class SudokuInputProcess {
    public static void processInputFile(InputStream inputStream)
    {
        Scanner in = new Scanner(inputStream);
        int size = in.nextInt();
        int[][] array = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                array[i][j] = in.nextInt();
            }
        }
        Vector<Integer>possibleValueVector = new Vector<Integer>();
        for (int i = 0; i < size; i++) {
            possibleValueVector.addElement(i+1);
        }
        if (in.hasNextInt()) {
            int constraint = in.nextInt();
            Vector<Integer>large = new Vector<Integer>();
            Vector<Integer>small = new Vector<Integer>();
            for (int i = 0; i < constraint; i++) {
                large.addElement(in.nextInt());
                small.addElement(in.nextInt());
            }
            Agent game = new FutoshikiSolver(array, size, possibleValueVector, small, large);
            constructGame(game);
        }
        else
        {
            Agent game = new SudokuSolver(array, size, possibleValueVector );
            constructGame(game);
        }

    }

    public static void main(String[] args) {

        /** This segment was used for file name. But in jar, file path can not be specified by this process...
         * String string;
         * for(int i=1;i<14;i++)
         * {
         *      string = "inputFileSudoku\\input"+Integer.toString(i)+".txt";
         *      string = "inputFileFutoshiki\\input"+Integer.toString(i)+".txt";
         *      System.out.println("Processing Input File : "+string);
         *      processInputFile(string);
         * }
         **/

        try {
            new SudokuInputProcess().getAllFileName();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getAllFileName() throws IOException {
        for(int i=1;i<14;i++)
        {
            String inputFileName = "/sudokuInputFile/input" +String.valueOf(i)+".txt";
            InputStream is = this.getClass().getResourceAsStream(inputFileName);
            System.out.println("Processing Input File : " + inputFileName);
            processInputFile(is);
        }
    }

    public static void startGame(Agent game, int variableSelection, int valueSelection){
//        game.showValues(game.board);
        game.board = game.initailizeSudokuBoard(game.board);
        System.out.println("Total = "+game.filled);
        game.board = game.backtracing_search(game.board,variableSelection,valueSelection);
        System.out.println("Assignment Completed");
        System.out.println("Total = "+game.filled);
        game.showValues(game.board);
//        game.solutionBoard.show();
//        game.showActualDemo(game.board);
        System.out.println(""+game.variableOrdering);
        System.out.println(""+game.valueOrdering);
    }

    public static void constructGame(Agent game) {
    /*
        //variable selection method
        variableSelection_MinimumRemaingValue = 1;
        variableSelection_Random = 2;
        variableSelection_FirstAvailableVariable = 3;
        variableSelection_DegreeHeuristic = 4;

        //value selection methos
        valueSelection_LeastConstrainingValue = 1;
        valueSelection_Random = 2;
        valueSelection_FirstAvailableValue = 3;
    */
        int variable = 1;
        int value = 1;
        startGame(game, variable, value);
        for (variable = 1; variable <= 3; variable++) {
            for (value = 1; value <= 3; value++) {
//                startGame(game, variable, value);
            }
        }
    }

}
