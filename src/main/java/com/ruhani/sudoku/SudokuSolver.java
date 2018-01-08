/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruhani.sudoku;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ruhani
 */
public class SudokuSolver extends Agent {
    public SudokuSolver(GameBoard[][] sudokuGameBoard, int size) {
        super(sudokuGameBoard, size);
    }

    public SudokuSolver(int[][] sudokuGameBoard, int size, Vector<Integer>vector) {
        super(sudokuGameBoard, size, vector);
    }


    @Override
    public boolean isConsistant(GameBoard[][] sudokuGameBoard, int row , int col, int val){
        GameBoard[][] tempSudokuGameBoard = new GameBoard[this.size][this.size];
        tempSudokuGameBoard = GameBoard.classGameBoard2DArrayCopy(sudokuGameBoard, this.size);

        if (isRowColConsistant(tempSudokuGameBoard, row, col, val)) {
            if (isBlockConsistant(tempSudokuGameBoard, row, col, val)) {
                return true;
            }
        }
        return false;

    }

    public boolean isBlockConsistant(GameBoard[][] sudokuGameBoard, int row , int col, int val){
        GameBoard[][] tempSudokuGameBoard = new GameBoard[this.size][this.size];
        tempSudokuGameBoard = GameBoard.classGameBoard2DArrayCopy(sudokuGameBoard, this.size);

        int startRow = row - row%3;
        int startCol = col - col%3;

        for (int i = startRow,k=0; k < 3; i++,k++) {
            for (int j = startCol,l=0; l < 3; j++,l++) {
                if(i==row && j==col) continue;
                if (tempSudokuGameBoard[i][j].value == val) {
                    return false;
                }
                if(tempSudokuGameBoard[i][j].value == 0 && tempSudokuGameBoard[i][j].possibleValues.contains(val) && tempSudokuGameBoard[i][j].possibleValues.size()==1)
                    return false;
            }
        }
        return true;
    }

    @Override
    public GameBoard[][] updateGameStates(GameBoard[][] sudokuGameBoard, int row, int col, int val)
    {
        GameBoard[][] tempSudokuGameBoard = new GameBoard[this.size][this.size];
        tempSudokuGameBoard = GameBoard.classGameBoard2DArrayCopy(sudokuGameBoard, this.size);

        tempSudokuGameBoard[row][col].value = val;
        for (int i = 0; i < this.size; i++) {   //row
            tempSudokuGameBoard[row][i].possibleValues.removeElement(val);
        }

        for (int i = 0; i < this.size; i++) {   //row
            tempSudokuGameBoard[i][col].possibleValues.removeElement(val);
        }


        //
        int startRow = row - row%3;
        int startCol = col - col%3;

        for (int i = startRow,k=0; k < 3; i++,k++) {
            for (int j = startCol,l=0; l < 3; j++,l++) {
                tempSudokuGameBoard[i][j].possibleValues.removeElement(val);
            }
        }


        return tempSudokuGameBoard;

    }

    @Override
    public void showActualDemo(GameBoard[][] board) {
        for (int i = 1; i < this.size; i++) {
            System.out.print("___");
        }
        System.out.println("_");
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (j%3==0) {
                    System.out.print("| ");
                }
                if(board[i][j].value!=0)
                    System.out.print(board[i][j].value+" ");
                else System.out.print("  ");
            }
            System.out.println("|");
            if(i==this.size-1)  continue;
            if(i%3==2){
                System.out.print(" ");
                for (int k = 0; k < this.size/3; k++) {
                    System.out.print("_______ ");
                }
                System.out.println("");
            }
        }
        for (int i = 1; i < this.size; i++) {
            System.out.print("___");
        }
        System.out.println("_");
    }
}
