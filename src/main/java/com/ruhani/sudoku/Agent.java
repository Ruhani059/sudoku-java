package com.ruhani.sudoku;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

public abstract class Agent {
    GameBoard[][] board;
    int size;
    int filled;
    int valueOrdering;
    int variableOrdering;
    SolutionBoard solutionBoard;
    final int variableSelection_MinimumRemaingValue;
    final int variableSelection_Random;
    final int variableSelection_FirstAvailableVariable;
    final int variableSelection_DegreeHeuristic;
    final int valueSelection_LeastConstrainingValue;
    final int valueSelection_Random;
    final int valueSelection_FirstAvailableValue;

    public Agent()
    {
        solutionBoard = new SolutionBoard();
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
        this.board = GameBoard.classGameBoard2DArrayCopy(board, size);
        this.size = size;
        this.filled=0;
        this.valueOrdering=0;
        this.variableOrdering=0;
    }

    public Agent(int[][] board, int size, Vector<Integer>vector) {
        this();
        this.board = new GameBoard[size][size];
        this.board = GameBoard.convertIntegerArray2GameBoardClass(board, size,vector);
        this.size = size;
        this.filled=0;
        this.valueOrdering=0;
        this.variableOrdering=0;
    }

    public GameBoard[][] backtracing_search(GameBoard[][] board, int variableSelectionMethod, int valueSelectionMethod)
    {
        if(isCompleteAssignment()){
            System.out.println("Completed Assignment");
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

        switch (valueSelectionMethod){
            case 1 :  //valueSelection_LeastConstrainingValue
                array = leastConstrainingValueSelect(board, board[minRow][minCol].possibleValues, getPosition(minRow, minCol));
                break;
            case 2 :  //valueSelection_Random
                array = randomValueSelect(board[minRow][minCol].possibleValues);
                break;
            case 3 :  //valueSelection_FirstAvailableVariable
                array = firstAvailableValueSelect(board[minRow][minCol].possibleValues);
                break;
            default:
                break;
        }
        //main backtrac
        for (int tempValue : array) {
            this.valueOrdering++;

            if (isConsistant(board, minRow, minCol, tempValue)) {
                GameBoard[][] temp = new GameBoard[this.size][this.size];
                temp = GameBoard.classGameBoard2DArrayCopy(board, this.size);
                temp = updateGameStates(temp, minRow, minCol, tempValue);
                this.filled++;
                solutionBoard.add(temp,this.size);
                temp = backtracing_search(temp,variableSelectionMethod,valueSelectionMethod);
                if (!isCompleteAssignment()) {
                    solutionBoard.remove(temp);
                    this.filled--;
                }
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
                    mrValue = getPosition(i, j);
                }
            }
        }

        //for same number of multiple value
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if(board[i][j].possibleValues.size()== minremain && board[i][j].value==0 )
                {
                    minimunRemainingVector.addElement(getPosition(i, j));
                }
            }
        }
        Random random = new Random();
        if (minimunRemainingVector.size()==0) {
            mrValue = -1;
        }
        else{
            mrValue = minimunRemainingVector.get(random.nextInt(minimunRemainingVector.size()));
        }

        return mrValue;
    }

    public int randomVariableSelect(GameBoard[][] board) {
        Vector<Integer>emptyPosition = new Vector<Integer>();
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if(board[i][j].value == 0){
                    emptyPosition.addElement(getPosition(i, j));
                }
            }
        }
        Random random = new Random();
        if (emptyPosition.isEmpty()) {
            return -1;
        }
        return emptyPosition.get(random.nextInt(emptyPosition.size()));
    }

    public int firstAvailableVariableSelect(GameBoard[][] board)
    {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if(board[i][j].value==0){
                    return getPosition(i, j);
                }
            }
        }
        return -1;
    }

    public int degreeHeuristic(GameBoard[][] board) {
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
                    mrValue = getPosition(i, j);
                }
            }
        }

        //for same number of multiple value
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if(board[i][j].possibleValues.size()== minremain && board[i][j].value==0 )
                {
                    minimunRemainingVector.addElement(getPosition(i, j));
                }
            }
        }
        Random random = new Random();
        mrValue = minimunRemainingVector.get(random.nextInt(minimunRemainingVector.size()));

        return mrValue;
    }


    //value selection functions
    public int[] leastConstrainingValueSelect(GameBoard[][] board, Vector<Integer>vector, int position) {
//        System.out.println("Hello");
        int size = vector.size();
        int row = (position-1)/this.size;
        int col = (position-1)%this.size;
        int[] possibleOrder = new int[size];
        if (size==1) {
            possibleOrder[0] = vector.get(0);
        }
        else{

            //copy vector to temp
            Vector<Integer>temp = new Vector<Integer>();
            for (Iterator<Integer> iterator = vector.iterator(); iterator.hasNext();) {
                Integer next = iterator.next();
                temp.addElement(0);
            }
            Vector<Integer>tempVector = new Vector<Integer>();
            for (Iterator<Integer> iterator = vector.iterator(); iterator.hasNext();) {
                Integer next = iterator.next();
                tempVector.addElement(next);
            }
/*
            showPossibleValues(board);
            System.out.println("Position " + position);
            for (Iterator<Integer> iterator = vector.iterator(); iterator.hasNext();) {
                Integer next = iterator.next();
                System.out.print(next+" ");
            }
            System.out.println();
            for (Iterator<Integer> iterator = temp.iterator(); iterator.hasNext();) {
                Integer next = iterator.next();
                System.out.print(next+" ");
            }
            System.out.println();
*/
            for (int i = 0; i < this.size; i++) {
                for (Iterator<Integer> iterator = board[row][i].possibleValues.iterator(); iterator.hasNext();) {
                    Integer next = iterator.next();
                    if(vector.contains(next))
                    {
                        temp.set(vector.indexOf(next), temp.get(vector.indexOf(next))+1);
                    }
                }
            }



            for (int i = 0; i < this.size; i++) {
                for (Iterator<Integer> iterator = board[i][col].possibleValues.iterator(); iterator.hasNext();) {
                    Integer next = iterator.next();
                    if(vector.contains(next))
                    {
                        temp.set(vector.indexOf(next), temp.get(vector.indexOf(next))+1);
                    }
                }
            }

            for (Iterator<Integer> iterator = temp.iterator(); iterator.hasNext();) {
                Integer next = iterator.next();
//                System.out.print(next+" ");
            }
//            System.out.println();
            for (int i = 0; i < size; i++) {
                int min = getMinimum(temp);
//                System.out.print("min = "+min);
                int index = temp.indexOf(min);
//                System.out.print(" index = "+index);
                int a = tempVector.get(index);
//                System.out.println(" a = "+a);
                possibleOrder[i] = tempVector.get(temp.indexOf(getMinimum(temp)));
                temp.remove(index);
                tempVector.removeElement(possibleOrder[i]);
                if(tempVector.size()!=temp.size()) System.out.println("Gojob poreche");
//                System.out.print(possibleOrder[i]+" ");
            }
//            System.out.println("");
        }
        return possibleOrder;
    }

    public int[] randomValueSelect(Vector<Integer>vector) {
        int size = vector.size();
        int[] possibleOrder = new int[size];
        //copy vector to temp
        Vector<Integer>temp = new Vector<Integer>();
        for (Iterator<Integer> iterator = vector.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();
            temp.addElement(next);
        }
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int randomPosition = random.nextInt(size - i);
            possibleOrder[i] = temp.get(randomPosition);
            temp.removeElementAt(randomPosition);
        }
        return possibleOrder;
    }

    public int[] firstAvailableValueSelect(Vector<Integer>vector)
    {
        int size = vector.size();
        int[] possibleOrder = new int[size];
        for (int i = 0; i < size; i++) {
            possibleOrder[i] = vector.get(i);
        }
        return possibleOrder;
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
        temp = GameBoard.classGameBoard2DArrayCopy(board, this.size);

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
        temp = GameBoard.classGameBoard2DArrayCopy(board, this.size);
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
        int min=99999999;
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

    public abstract void showActualDemo(GameBoard[][] board);

}
