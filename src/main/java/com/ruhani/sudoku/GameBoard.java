package com.ruhani.sudoku;

import java.util.Iterator;
import java.util.Vector;

public class GameBoard {
    int value;
    Vector<Integer>possibleValues;

    public GameBoard(int value) {
        this.value = value;
    }



    public GameBoard(int value, Vector<Integer> possibleValues) {
        this.value = value;
        this.possibleValues = new Vector<Integer>();
        for (Iterator<Integer> iterator = possibleValues.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();
            this.possibleValues.addElement(next);
        }
    }

    public GameBoard(GameBoard gameBoard) {
        this.value = gameBoard.value;
        this.possibleValues = new Vector<Integer>();
        for (Iterator<Integer> iterator = gameBoard.possibleValues.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();
            this.possibleValues.addElement(next);
        }
    }

    public static GameBoard[][] classGameBoard2DArrayCopy(GameBoard[][] sudokuBoard, int size)
    {
        GameBoard[][] board = new GameBoard[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new GameBoard(sudokuBoard[i][j]);
            }
        }
        return board;
    }

    public static GameBoard[][] convertIntegerArray2GameBoardClass(int[][] array, int size)
    {
        GameBoard[][] board = new GameBoard[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new GameBoard(array[i][j]);
            }
        }
        return board;
    }

    public static GameBoard[][] convertIntegerArray2GameBoardClass(int[][] array, int size, Vector<Integer>vector)
    {
        GameBoard[][] board = new GameBoard[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new GameBoard(array[i][j], vector);
            }
        }
        return board;
    }

    public static int compareGameBoard2DArray(GameBoard[][] firstBoard, GameBoard[][] secondBoard)
    {
        for (int i = 0; i < firstBoard.length; i++) {
            for (int j = 0; j < firstBoard[i].length; j++) {
                if (firstBoard[i][j].value != secondBoard[i][j].value) {
                    return i*firstBoard.length+j+1;
                }
            }
        }
        return 0;
    }
}
