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

        SudokuSolver gameSolver = new SudokuSolver(array, size, possibleValueVector);
//            SudokuBoard[][] sudokuBoardArray = new SudokuBoard[size][size];
        gameSolver.board = gameSolver.initailizeSudokuBoard(gameSolver.board);
//            gameSolver.showValues((gameSolver.board));
        gameSolver.board = gameSolver.backtracing_search(gameSolver.board);
//            gameSolver.showValues((gameSolver.board));
        System.out.println("Variable Selected : "+gameSolver.variableOrdering);
        System.out.println("Value Selected    : "+gameSolver.valueOrdering);


    }
    public static void main(String[] args) throws IOException {
        new SudokuInputProcess().startGame();
    }

    public void startGame() throws IOException {
        for(int i=1;i<14;i++)
        {
            String inputFileName = "/inputfile/input"+String.valueOf(i)+".txt";
            InputStream is = this.getClass().getResourceAsStream(inputFileName);
            System.out.println("Processing Input File : " + inputFileName);
            processInputFile(is);
        }
    }
}
