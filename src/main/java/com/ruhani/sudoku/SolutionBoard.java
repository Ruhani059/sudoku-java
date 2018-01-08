package com.ruhani.sudoku;

import java.util.Iterator;
import java.util.Vector;

public class SolutionBoard {
    int size;
    //    Vector<Agent>solutionProcess;
    Vector<GameBoard[][]> solutionProcess;

    public SolutionBoard() {
//        solutionProcess = new Vector<Agent>();
        solutionProcess = new Vector<GameBoard[][]>();
    }
    public void add(GameBoard[][] board, int size)
    {
        this.size = size;
        GameBoard[][] hello = new GameBoard[size][size];
        hello = GameBoard.classGameBoard2DArrayCopy(board, size);
        solutionProcess.addElement(board);
    }
    public void remove (GameBoard[][] board)
    {
        solutionProcess.remove(solutionProcess.lastElement());
    }

    public void getRowCol(int position, int[] rowcol)
    {
        rowcol[0] = (position-1)/size;
        rowcol[1] = (position-1)%size;
    }
    public void show ()
    {
        int i = 0;
        GameBoard[][] prev =solutionProcess.firstElement();
        for (Iterator<GameBoard[][]> iterator = solutionProcess.iterator(); iterator.hasNext();) {
            GameBoard[][] next = iterator.next();
            int position = GameBoard.compareGameBoard2DArray(prev, next);
            System.out.println(i++);
            int[] rowcol = new int[2];
            getRowCol(position, rowcol);
            if(position!=0) System.out.println("At this board postion ["+rowcol[0]+"]["+rowcol[1]+"] got the new value "+next[rowcol[0]][rowcol[1]].value+".");
//            if(position!=0) System.out.println("At this board postion "+position+" got the new value "+next[(position-1)/this.size][(position-1)%this.size].value+".");
            Agent agent = new Agent(next, size) {

                @Override
                public boolean isConsistant(GameBoard[][] board, int row, int col, int val) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public GameBoard[][] updateGameStates(GameBoard[][] board, int row, int col, int val) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void showActualDemo(GameBoard[][] board) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
            agent.showValues(next);
            prev = GameBoard.classGameBoard2DArrayCopy(next, this.size);

        }
    }
}
