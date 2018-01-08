package com.ruhani.sudoku;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


public class SudokuSolverTest {

    public static Logger logger = Logger.getLogger(SudokuSolverTest.class);

    @Before
    public void setup() throws Exception {
        FutoshikiSolver testFutoshiki;
    }

    @Test
    public void testClass() throws Exception {
        logger.debug("testClass");
    }

    @Test
    public void testUpdateStates(GameBoard[][] board) {
//        for (int i = 0; i < testFutoshiki.size; i++) {
//            for (int j = 0; j < testFutoshiki.size; j++) {
//                board = testFutoshiki.updateGameStates(board, i, j, board[i][j].value);
//            }
//        }
//        testFutoshiki.showValues(board);
//        testFutoshiki.showPossibleValues(board);
    }
}
