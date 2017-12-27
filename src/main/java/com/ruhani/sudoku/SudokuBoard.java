/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruhani.sudoku;

import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author Ruhani
 */
public class SudokuBoard {
    int value;
    Vector<Integer>possibleValues;    

    public SudokuBoard(int value) {
        this.value = value;
    }
    
    
        
    public SudokuBoard(int value, Vector<Integer> possibleValues) {
        this.value = value;
        this.possibleValues = new Vector<Integer>();
        for (Iterator<Integer> iterator = possibleValues.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();
            this.possibleValues.addElement(next);
        }
    }        

    public SudokuBoard(SudokuBoard sudokuBoard) {
        this.value = sudokuBoard.value;
        this.possibleValues = new Vector<Integer>();
        for (Iterator<Integer> iterator = sudokuBoard.possibleValues.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();
            this.possibleValues.addElement(next);
        }
    }
    
    public static SudokuBoard[][] copy2dArray(SudokuBoard[][] sudokuBoard, int size)
    {
        SudokuBoard[][] board = new SudokuBoard[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new SudokuBoard(sudokuBoard[i][j]);
            }
        }
        return board;
    }

    public static SudokuBoard[][] array2SudokuBoard(int[][] array, int size)
    {
        SudokuBoard[][] board = new SudokuBoard[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new SudokuBoard(array[i][j]);
            }
        }
        return board;
    }

    public static SudokuBoard[][] array2SudokuBoard(int[][] array, int size, Vector<Integer>vector)
    {
        SudokuBoard[][] board = new SudokuBoard[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new SudokuBoard(array[i][j], vector);
            }
        }
        return board;
    }
    
}
