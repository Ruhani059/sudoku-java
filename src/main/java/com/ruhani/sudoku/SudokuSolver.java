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
        tempSudokuGameBoard = GameBoard.copy2dArray(sudokuGameBoard, this.size);

        if (isRowColConsistant(tempSudokuGameBoard, row, col, val)) {
            if (isBlockConsistant(tempSudokuGameBoard, row, col, val)) {
                return true;
            }
        }
        return false;

    }

    public boolean isBlockConsistant(GameBoard[][] sudokuGameBoard, int row , int col, int val){
        GameBoard[][] tempSudokuGameBoard = new GameBoard[this.size][this.size];
        tempSudokuGameBoard = GameBoard.copy2dArray(sudokuGameBoard, this.size);

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
        tempSudokuGameBoard = GameBoard.copy2dArray(sudokuGameBoard, this.size);

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
}
