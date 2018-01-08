package com.ruhani.sudoku;

import java.util.Iterator;
import java.util.Vector;

public abstract class Agent {
    GameBoard[][] board;
    int size;
    int filled;
    int valueOrdering;
    int variableOrdering;
    final int variableSelection_MinimumRemaingValue;
    final int variableSelection_Random;
    final int variableSelection_FirstAvailableVariable;
    final int variableSelection_DegreeHeuristic;
    final int valueSelection_LeastConstrainingValue;
    final int valueSelection_Random;
    final int valueSelection_FirstAvailableValue;

    public Agent()
    {
        this.variableSelection_MinimumRemaingValue = 1;
        this.variableSelection_Random = 2;
        this.variableSelection_FirstAvailableVariable = 3;
        this.variableSelection_DegreeHeuristic = 4;
        this.valueSelection_LeastConstrainingValue = 1 ;
        this.valueSelection_Random = 2;
        this.valueSelection_FirstAvailableValue = 3;
    }

    public Agent(GameBoard[][] board, int size) {
        this();
        this.board = new GameBoard[size][size];
        this.board = GameBoard.copy2dArray(board, size);
        this.size = size;
        this.filled=0;
        this.valueOrdering=0;
        this.variableOrdering=0;
    }

    public Agent(int[][] board, int size, Vector<Integer> vector) {
        this();
        this.board = new GameBoard[size][size];
        this.board = GameBoard.array2SudokuBoard(board, size,vector);
        this.size = size;
        this.filled=0;
        this.valueOrdering=0;
        this.variableOrdering=0;
    }

    public GameBoard[][] backtracing_search(GameBoard[][] board, int variableSelectionMethod, int valueSelectionMethod)
    {
        if(isCompleteAssignment()){
//            System.out.println("Completed Assignment");
//            showValues(board);
            return board;
        }

        int selectedVariable = 1;

        switch(variableSelectionMethod){
            case 1:       //  variableSelection_MinimumRemaingValue
                selectedVariable = minimumRemainingValueSelect(board);
                break;
            case 2:       //    variableSelection_Random
                selectedVariable = randomVariableSelect(board);
                break;
            case 3:       //    variableSelection_FirstAvailableValue
                selectedVariable = firstAvailableVariableSelect(board);
                break;
            case 4:       //    variableSelection_Degree
                selectedVariable = degreeHeuristic(board);
                break;
            default:
                break;
        }
        if(selectedVariable==-1) return board;
        this.variableOrdering++;

        selectedVariable--;
        int minRow = selectedVariable/this.size;
        int minCol = selectedVariable%this.size;

        int size = board[minRow][minCol].possibleValues.size();

        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i]=0;
        }

        switch (valueSelectionMethod){
            case 1 :  //valueSelection_LeastConstrainingValue
                break;
            case 2 :  //valueSelection_Random
                break;
            case 3 :  //valueSelection_FirstAvailableVariable
                for (int i = 0; i < size; i++) {
                    array[i] = board[minRow][minCol].possibleValues.get(i);
                }
                break;
            default:
                break;
        }
        //value ordering : selection of the first available value

        for (int i = 0 ; i<size ; i++) {
            this.valueOrdering++;

            int tempValue = array[i];

            if (isConsistant(board, minRow, minCol, tempValue)) {
                GameBoard[][] temp = new GameBoard[this.size][this.size];
                temp = GameBoard.copy2dArray(board, this.size);
                temp = updateGameStates(temp, minRow, minCol, tempValue);
                this.filled++;
                temp = backtracing_search(temp,variableSelectionMethod,valueSelectionMethod);
                this.filled--;
                if (isSuccess(temp)) {
                    return temp;
                }

            }

        }
        return board;
    }

    public int minimumRemainingValueSelect(GameBoard[][] board)
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


    public int firstAvailableVariableSelect(GameBoard[][] board)
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

    public int randomVariableSelect(GameBoard[][] board) {
        return minimumRemainingValueSelect(board);
    }

    public int degreeHeuristic(GameBoard[][] board) {
        return minimumRemainingValueSelect(board);
    }

    public boolean checkSinglePosition(GameBoard[][] board, int row, int col, int val)
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

    public boolean isSuccess(GameBoard[][] board)
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


    public abstract boolean isConsistant(GameBoard[][] board, int row , int col, int val);

    public boolean isRowColConsistant(GameBoard[][] board, int row , int col, int val){
        GameBoard[][] temp = new GameBoard[this.size][this.size];
        temp = GameBoard.copy2dArray(board, this.size);

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

        return true;
    }


    public GameBoard[][] initailizeSudokuBoard(GameBoard[][] board)
    {
//        System.out.println("Initialization Begins");
        GameBoard[][] temp = new GameBoard[this.size][this.size];
        temp = GameBoard.copy2dArray(board, this.size);
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (temp[i][j].value!=0) {
                    this.filled++;
                    temp[i][j].possibleValues.removeAllElements();
//                    temp = updateGameStates(temp, i, j, temp[i][j].value);
                }
//                temp = updateGameStates(temp, i, j, temp[i][j].value);
            }
        }
//        System.out.println("Before Update");
//        showPossibleValues(temp);
//        showValues(temp);
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                temp = updateGameStates(temp, i, j, temp[i][j].value);
            }
        }
//        showPossibleValues(temp);
//        showValues(temp);
//        System.out.println("Initialization Ends Here");
        return temp;
    }

    public abstract GameBoard[][] updateGameStates(GameBoard[][] board, int row, int col, int val);

    public void showValues(GameBoard[][] board)
    {
        for (int i = 0; i < this.size; i++) {
            System.out.print("____");
        }
        System.out.println("_");
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                System.out.print("| ");
                if(board[i][j].value!=0)
                    System.out.print(board[i][j].value+" ");
                else System.out.print("  ");
            }
            System.out.println("|");
            if(i==this.size-1)  continue;
            System.out.print(" ");
            for (int k = 0; k < this.size; k++) {
                System.out.print("___ ");
            }
            System.out.println("");
        }
        for (int i = 0; i < this.size; i++) {
            System.out.print("____");
        }
        System.out.println("_");

    }

    public void showPossibleValues(GameBoard[][] board)
    {
        for (int i = 0; i < this.size; i++) {
            System.out.print("__");
            for (int j = 0; j < this.size; j++) {
                System.out.print("_");
            }
            System.out.print("_");
        }
        System.out.println("_");
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
                System.out.print(" ");
            }
            System.out.println("|");
            if(i==this.size-1)  continue;
            System.out.print(" ");
            for (int l = 0; l < this.size; l++) {
                System.out.print("_");
                for (int m = 0; m < this.size; m++) {
                    System.out.print("_");
                }
                System.out.print("_ ");
            }
            System.out.println("");
        }
        for (int i = 0; i < this.size; i++) {
            System.out.print("__");
            for (int j = 0; j < this.size; j++) {
                System.out.print("_");
            }
            System.out.print("_");
        }
        System.out.println("_");

    }
}
