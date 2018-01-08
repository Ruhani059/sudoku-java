package com.ruhani.sudoku;

import java.util.Iterator;
import java.util.Vector;

public class FutoshikiSolver extends Agent {
    Vector<Integer> lessThanPosition;
    Vector<Integer>greaterThanPosition;


    public FutoshikiSolver(GameBoard[][] board, int size, Vector<Integer> lessThanVector, Vector<Integer> greaterThanVector) {
        super(board, size);
        this.lessThanPosition = new Vector<Integer>();
        for (Iterator<Integer> iterator = lessThanVector.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();
            this.lessThanPosition.addElement(next);
        }
        this.greaterThanPosition = new Vector<Integer>();
        for (Iterator<Integer> iterator = greaterThanVector.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();
            this.greaterThanPosition.addElement(next);
        }
    }

    public FutoshikiSolver(int[][] board, int size, Vector<Integer> possibleValues, Vector<Integer> lessThanVector, Vector<Integer> greaterThanVector) {
        super(board, size, possibleValues);
        this.lessThanPosition = new Vector<Integer>();
        for (Iterator<Integer> iterator = lessThanVector.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();
            this.lessThanPosition.addElement(next);
        }
        this.greaterThanPosition = new Vector<Integer>();
        for (Iterator<Integer> iterator = greaterThanVector.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();
            this.greaterThanPosition.addElement(next);
        }
        int index=0;
        for (Iterator<Integer> iterator = this.greaterThanPosition.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();
//            System.out.println(this.greaterThanPosition.get(index)+">"+this.lessThanPosition.get(index));
            index++;
        }
    }

    @Override
    public boolean isConsistant(GameBoard[][] futoshikiGameBoard, int row, int col, int val) {
        GameBoard[][] tempSudokuGameBoard = new GameBoard[this.size][this.size];
        tempSudokuGameBoard = GameBoard.copy2dArray(futoshikiGameBoard, this.size);

        if (isRowColConsistant(tempSudokuGameBoard, row, col, val)) {
            if (isConstraintConsistant(tempSudokuGameBoard, row, col, val)) {
                return true;
            }
        }
        return false;
    }

    public boolean isConstraintConsistant(GameBoard[][] futoshikiGameBoard, int row , int col, int val){
        GameBoard[][] tempSudokuGameBoard = new GameBoard[this.size][this.size];
        tempSudokuGameBoard = GameBoard.copy2dArray(futoshikiGameBoard, this.size);

        return true;
    }

    public int getMaximum(Vector<Integer> vector){
        int max=0;
        for (Iterator<Integer> iterator = vector.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();
            if (max < next) {
                max = next;
            }
        }
        return max;
    }

    public int getMinimum(Vector<Integer> vector){
        int min=10;
        for (Iterator<Integer> iterator = vector.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();
            if (min > next) {
                min = next;
            }
        }
        return min;
    }

    public int getPosition(int row, int col){
        return row*this.size+col+1;
    }


    @Override
    public GameBoard[][] updateGameStates(GameBoard[][] futoshikiGameBoard, int row, int col, int val) {
//        System.out.println("updateGameStates");
        GameBoard[][] tempFutoshikiGameBoard = new GameBoard[this.size][this.size];
        tempFutoshikiGameBoard = GameBoard.copy2dArray(futoshikiGameBoard, this.size);

        tempFutoshikiGameBoard[row][col].value = val;
        for (int i = 0; i < this.size; i++) {   //row
            tempFutoshikiGameBoard[row][i].possibleValues.removeElement(val);
        }

        for (int i = 0; i < this.size; i++) {   //col
            tempFutoshikiGameBoard[i][col].possibleValues.removeElement(val);
        }


        //updateConstraints
        tempFutoshikiGameBoard = futoshikiConstraintUpdates(tempFutoshikiGameBoard);
        return tempFutoshikiGameBoard;
    }

    public GameBoard[][] futoshikiConstraintUpdates(GameBoard[][] futoshikiGameBoard)
    {
        GameBoard[][] tempFutoshikiGameBoard = new GameBoard[this.size][this.size];
        tempFutoshikiGameBoard = GameBoard.copy2dArray(futoshikiGameBoard, this.size);

        for (int index = 0; index < this.greaterThanPosition.size(); index++) {
            int lessPosition = this.lessThanPosition.get(index) -1;
            int greaterPosition = this.greaterThanPosition.get(index) -1;
            int i,j,row,col;
            row = greaterPosition/this.size;
            col = greaterPosition%this.size;
            i = lessPosition/this.size;
            j = lessPosition%this.size;

            //
            //row,col er value greater than i,j er value   [row][col]>[i][j]
            //so row,col er actual value the n porjonto sob delete kore dibo i,j er theke
            if (tempFutoshikiGameBoard[row][col].value !=0) {
                for (int k = tempFutoshikiGameBoard[row][col].value; k <= this.size; k++) {
                    tempFutoshikiGameBoard[i][j].possibleValues.removeElement(k);
                }
            }

            int min=0;

            //jodi [i][j] e kono value thake
            if(tempFutoshikiGameBoard[i][j].value!=0)
                min = tempFutoshikiGameBoard[i][j].value;
                //[i][j] e value na thakle minimum koto hote pare
            else
            {
                if(!tempFutoshikiGameBoard[i][j].possibleValues.isEmpty())
                {
                    min = getMinimum(tempFutoshikiGameBoard[i][j].possibleValues);
                }
            }

            //[i][j] e possible minimum value theke 1 porjonto sob delete kore dibo [row][col] theke
            for (int k = min; k > 0; k--) {
                tempFutoshikiGameBoard[row][col].possibleValues.removeElement(k);
            }
        }
//        System.out.println("Constraint 1");
//        showPossibleValues(tempFutoshikiGameBoard);
//        showValues(tempFutoshikiGameBoard);
        for (int index = 0; index < this.greaterThanPosition.size(); index++) {
            int lessPosition = this.lessThanPosition.get(index) -1;
            int greaterPosition = this.greaterThanPosition.get(index) -1;
            int i,j,row,col;
            i = greaterPosition/this.size;
            j = greaterPosition%this.size;
            row = lessPosition/this.size;
            col = lessPosition%this.size;
//            System.out.println("["+i+"]["+j+"]>["+row+"]["+col+"]");
            //
            //row,col er value greater than i,j er value   [row][col]<[i][j]
            //so row,col er actual value the 1 porjonto sob delete kore dibo i,j er theke
            if (tempFutoshikiGameBoard[row][col].value !=0) {
                for (int k = tempFutoshikiGameBoard[row][col].value; k > 0; k--) {
                    tempFutoshikiGameBoard[i][j].possibleValues.removeElement(k);
//                    System.out.println(getPosition(i, j)+" - "+k);
                }
            }

            int max=0;

            //jodi [i][j] e kono value thake
            if(tempFutoshikiGameBoard[i][j].value!=0)
                max = tempFutoshikiGameBoard[i][j].value;
                //[i][j] e value na thakle maximum koto hote pare
            else
            {
                if(!tempFutoshikiGameBoard[i][j].possibleValues.isEmpty())
                {
                    max = getMaximum(tempFutoshikiGameBoard[i][j].possibleValues);
                }
            }

            //[i][j] e possible maximum value theke n porjonto sob delete kore dibo [row][col] theke
            for (int k = max; k <= this.size; k++) {
                tempFutoshikiGameBoard[row][col].possibleValues.removeElement(k);
//                System.out.println(getPosition(row, col)+" : "+k);
            }
        }
//        System.out.println("Constraint 2");
//        showPossibleValues(tempFutoshikiGameBoard);
//        showValues(tempFutoshikiGameBoard);

        return tempFutoshikiGameBoard;
    }
}
