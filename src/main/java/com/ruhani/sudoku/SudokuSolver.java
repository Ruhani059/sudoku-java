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
public class SudokuSolver {
    SudokuBoard[][] board;
    int size;
    int filled;
    int valueOrdering;
    int variableOrdering;    

    public SudokuSolver(SudokuBoard[][] board, int size) {
        this.board = new SudokuBoard[size][size];
        this.board = SudokuBoard.copy2dArray(board, size);
        this.size = size;
        this.filled=0;
        this.valueOrdering=0;
        this.variableOrdering=0;
    }

    public SudokuSolver(int[][] board, int size, Vector<Integer>vector) {
        this.board = new SudokuBoard[size][size];
        this.board = SudokuBoard.array2SudokuBoard(board, size,vector);
        this.size = size;
        this.filled=0;
        this.valueOrdering=0;
        this.variableOrdering=0;
        
    }
    
    public SudokuBoard[][] backtracing_search(SudokuBoard[][] board)
    {
        if(isCompleteAssignment()){ 
            System.out.println("Completed Assignment");
            showValues(board);
        }

        int selectedVariable;
        
        int variableOrderingSelection = 1;
        switch(variableOrderingSelection){
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
                

        //using minimum ramianing value and degree heuristic
//        selectedVariable = mrv(board);
        
        //selection of the first available variable
        selectedVariable = select1stValue(board);
        
        //random
//        selectedVariable = randomVariable(nodes,n);
        
        this.variableOrdering++;
        
        if(selectedVariable==-1) return board;
        
        selectedVariable--;
        int minRow = selectedVariable/this.size;
        int minCol = selectedVariable%this.size;
        
//        System.out.println(selectedVariable+" "+minRow+" "+minCol);
        
        int size = board[minRow][minCol].possibleValues.size();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i]=0;
        }

        //value ordering : selection of the first available value 
        for (int i = 0; i < size; i++) {
            array[i] = board[minRow][minCol].possibleValues.get(i);
        }

        for (int i = 0 ; i<size ; i++) {            
            this.valueOrdering++;

            int tempValue = array[i];

            if (isConsistant(board, minRow, minCol, tempValue)) {
                
//                showValues(board);
//                showPossibleValues(board);
                SudokuBoard[][] temp = new SudokuBoard[this.size][this.size];
                temp = SudokuBoard.copy2dArray(board, this.size);

                temp = updateStatus(temp, minRow, minCol, tempValue);
//                showValues(temp);
//                showPossibleValues(temp);
                this.filled++;
                temp = backtracing_search(temp);
                this.filled--;

                if (isSuccess(temp)) {
//                    System.out.println("Returned");
                    return temp;
                }
              
            }
            
        }        
        return board;
    }
    
    
    public int mrv(SudokuBoard[][] board)
    {
        Vector<Integer>minimunRemainingVector = new Vector<Integer>();
        int minremain = 10;
        int mrValue = -1;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if(board[i][j].value != 0)
                    continue;
                int size = board[i][j].possibleValues.size();
                if(size==0 && board[i][j].value == 0)
                {
                    return -1;
                }
                if(size>0 && minremain > size) 
                {
                    minremain = size;
                    mrValue = i*this.size+j+1;
                }
                
            }
        }
/*        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(nodes[i][j].possibleValues.size()== minremain && nodes[i][j].actualValue==0 )
                {
                    minimunRemainingVector.addElement(nodes[i][j].position);
                }
            }
        }
*/        
        return mrValue;
    }
    
    
    public int select1stValue(SudokuBoard[][] board)
    {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if(board[i][j].value==0){
                    return (i*this.size+j+1);
                }
                
            }
        }
        return -1;        
    }
    
    public boolean checkSinglePosition(SudokuBoard[][] board, int row, int col, int val)
    {
        for (int i = 0; i < this.size; i++) {
            if(col == i) continue;
            if(board[row][i].value == val) return false;
        }
        
        for (int i = 0; i < this.size; i++) {
            if(row == i) continue;
            if(board[i][col].value == val) return false;
        }
        
        return true;        
    }
   
    public boolean isSuccess(SudokuBoard[][] board)
    {
         for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if(!checkSinglePosition(board, i, j, board[i][j].value))
                {
                    return false;
                }
            }
        }
        return true;      
    }
    
    public boolean isCompleteAssignment()
    {
        if (this.filled == this.size*this.size) {
            return true;
        }
        return false;
    }

    
    public boolean isConsistant(SudokuBoard[][] board, int row , int col, int val){
        SudokuBoard[][] temp = new SudokuBoard[this.size][this.size];
        temp = SudokuBoard.copy2dArray(board, this.size);
        
        for (int i = 0; i < this.size; i++) {   //row
            if(i==col)  continue;
            if (temp[row][i].value == val) {
                return false;                
            }
            if(temp[row][i].value == 0 && temp[row][i].possibleValues.contains(val) && temp[row][i].possibleValues.size()==1)
                return false;
        }

        for (int i = 0; i < this.size; i++) {   //row
            if(i==row)  continue;
            if (temp[i][col].value == val) {
                return false;                
            }
            if(temp[i][col].value == 0 && temp[i][col].possibleValues.contains(val) && temp[i][col].possibleValues.size()==1)
                return false;
        }
        
        
        //
        int startRow = row - row%3;
        int startCol = col - col%3;
        
        for (int i = startRow,k=0; k < 3; i++,k++) {
            for (int j = startCol,l=0; l < 3; j++,l++) {
                if(i==row && j==col) continue;
                if (temp[i][j].value == val) {
                    return false;                
                }
                if(temp[i][j].value == 0 && temp[i][j].possibleValues.contains(val) && temp[i][j].possibleValues.size()==1)
                    return false;
            }
        }
        
        
        return true;
    }
    
    public SudokuBoard[][] initailizeSudokuBoard(SudokuBoard[][] board)
    {
        SudokuBoard[][] temp = new SudokuBoard[this.size][this.size];
        temp = SudokuBoard.copy2dArray(board, this.size);
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (temp[i][j].value!=0) {
                    this.filled++;
                    temp[i][j].possibleValues.removeAllElements();
                    temp = updateStatus(temp, i, j, temp[i][j].value);
                }
            }            
        }
        return temp;
    }
    
    public SudokuBoard[][] updateStatus(SudokuBoard[][] board, int row, int col, int val)
    {
        SudokuBoard[][] temp = new SudokuBoard[this.size][this.size];
        temp = SudokuBoard.copy2dArray(board, this.size);

        temp[row][col].value = val;
        for (int i = 0; i < this.size; i++) {   //row
            temp[row][i].possibleValues.removeElement(val);
        }

        for (int i = 0; i < this.size; i++) {   //row
            temp[i][col].possibleValues.removeElement(val);
        }
        
        
        //
        int startRow = row - row%3;
        int startCol = col - col%3;
        
        for (int i = startRow,k=0; k < 3; i++,k++) {
            for (int j = startCol,l=0; l < 3; j++,l++) {
                temp[i][j].possibleValues.removeElement(val);
            }
        }
        
        
        return temp;
                
    }
    
    public void showValues(SudokuBoard[][] board)
    {
        System.out.println("_____________________________________");
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                System.out.print("| ");
                if(board[i][j].value!=0)
                    System.out.print(board[i][j].value+" ");
                else System.out.print("  ");
            }
            System.out.println("|");
            if(i==this.size-1)  continue;
            System.out.println(" ___ ___ ___ ___ ___ ___ ___ ___ ___ ");
        }
        System.out.println("_____________________________________");
        
    }

    public void showPossibleValues(SudokuBoard[][] board)
    {
        System.out.println("____________________________________________________________________________________________________");
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                System.out.print("| ");
                if(board[i][j].possibleValues.size()==0)
                {
                    for (int k = 0; k <this.size ; k++) {
                        System.out.print(" ");
                    }
                }
                else
                {
                    for (Iterator<Integer> iterator = board[i][j].possibleValues.iterator(); iterator.hasNext();) {
                        Integer next = iterator.next();
                        System.out.print(next+"");
                    }
                    for (int k = 0; k < this.size - board[i][j].possibleValues.size(); k++) {
                        System.out.print(" ");
                    }
                }
            }
            System.out.println("|");
            if(i==this.size-1)  continue;
            System.out.println(" _________  _________  _________  _________  _________  _________  _________  _________  _________ ");
        }
        System.out.println("____________________________________________________________________________________________________");
        
    }
    
}
