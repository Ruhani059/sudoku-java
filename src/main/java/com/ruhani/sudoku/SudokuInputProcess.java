/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruhani.sudoku;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            String inputFileName = "/inputfile/input"+String.valueOf(i)+".txt";
            InputStream is = this.getClass().getResourceAsStream(inputFileName);
            System.out.println("Processing Input File : " + inputFileName);
            processInputFile(is);
        }
    }

    public static void startGame(Agent game, int variableSelection, int valueSelection){
        game.board = game.initailizeSudokuBoard(game.board);
        game.board = game.backtracing_search(game.board,variableSelection,valueSelection);
        System.out.println("Assignment Completed");
        game.showValues(game.board);
        System.out.println(""+game.variableOrdering);
        System.out.println(""+game.valueOrdering);
    }

    public static void constructGame(Agent game) {
    /*
        final int variableSelection_MinimumRemaingValue = 1;
        final int variableSelection_Random = 2;
        final int variableSelection_FirstAvailableVariable = 3;
        final int variableSelection_DegreeHeuristic = 4;
        final int valueSelection_LeastConstrainingValue = 1;
        final int valueSelection_Random = 2;
        final int valueSelection_FirstAvailableValue = 3;
    */
        int variable = 1;
        int value = 3;
        startGame(game, variable, value);
        for (variable = 1; variable <= 4; variable++) {
            for (value = 1; value <= 3; value++) {
//                startGame(game, variable, value);
            }
        }

    }


}
