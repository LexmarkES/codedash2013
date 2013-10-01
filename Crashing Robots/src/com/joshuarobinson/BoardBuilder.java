package com.joshuarobinson;



import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Joshua Robinson
 * Date: 9/10/13
 * Time: 9:20 PM
 *
 */
public class BoardBuilder {
    HashMap<Integer, BoardSpace> board = new HashMap<Integer, BoardSpace>();
    HashMap<Integer,Collection<Integer>> possibleMoves = new HashMap<Integer, Collection<Integer>>();
    int boardSize;

    public void buildBoard(String boardString)
    {
        String[] boards = boardString.split(" ");
        boardSize = (int)Math.sqrt(boards.length);

        double root = Math.sqrt((double)boards.length);
        // Assert.assertTrue("Board not square", root * root == boards.length);

        for(int i =0; i < boardSize*boardSize; i++)
        {
            board.put(i, findBoard(boards[i], i));
        }
        verifyBoard();
    }

    public HashSet<Integer> movesForPosition(int position)
    {
        HashSet<Integer> moves = new HashSet<Integer>();

        if(possibleMoves.containsKey(position))
        {
            // already did calc for this spot
            moves.addAll(possibleMoves.get(position));
        }else{
            possibleMoves.put(position, moves);

            moves.addAll(movesForPosition(moveUp(position)));
            moves.addAll(movesForPosition(moveDown(position)));
            moves.addAll(movesForPosition(moveLeft(position)));
            moves.addAll(movesForPosition(moveRight(position)));

        }
        moves.add(position);
        return moves;

    }

    protected BoardSpace findBoard(String boardString, int position)
    {

         BoardSpace spot = new BoardSpace(position);
         spot.mask = boardString;

         return spot;
    }

    protected String calcString( String boardString, int position)
    {
        int index = boardString.indexOf(position +"-");
        String description = boardString.substring(index+3,index+7 );
        return description;
    }

    public int moveUp(int position)
    {

        if(board.get(position).mask.substring(0,1).equals("1"))
        {
            return position;
        }else
        {
            return moveUp(position - boardSize );
        }
    }

    public int moveDown(int position)
    {
        if(board.get(position).mask.substring(2,3).equals("1"))
        {
            return position;
        }else
        {
            return moveDown(position + boardSize);
        }
    }

    public int moveLeft(int position)
    {
        if(board.get(position).mask.substring(3,4).equals("1"))
        {
            return position;
        }else
        {
            return moveLeft(position - 1);
        }
    }

    public int moveRight(int position)
    {
        if(board.get(position).mask.substring(1,2).equals("1"))
        {
            return position;
        }else
        {
            return moveRight(position + 1);
        }
    }

    /**
     * This is a sanity check for the boards I am building for the test
     * There would be no reason to do this for the problem
     */
    private void verifyBoard()
    {
       /* for(int i =0; i < boardSize*boardSize; i++)
        {
            // march at each spot and look
            // UP
            if(board.get(i).mask.substring(0,1).equals("1"))
            {
                //check for up wall
                int checkPosition = (i - boardSize);
                if(!(checkPosition < 0))
                {
                    // not outside
                    Assert.assertTrue("Missing wall at: "+ checkPosition ,board.get(checkPosition).mask.substring(2,3).equals("1"));
                }
            }
            // RIGHT
            if(board.get(i).mask.substring(1,2).equals("1"))
            {
                //check for up wall
                int checkPosition = (i + 1);
                if(!(checkPosition > (boardSize*boardSize) - 1 ))
                {
                    // not outside
                    Assert.assertTrue("Missing wall at: "+ checkPosition ,board.get(checkPosition).mask.substring(3,4).equals("1"));
                }
            }

            // DOWN
            if(board.get(i).mask.substring(2,3).equals("1"))
            {
                //check for up wall
                int checkPosition = (i + boardSize);
                if(!(checkPosition > (boardSize*boardSize) - 1 ))
                {
                    // not outside
                    Assert.assertTrue("Missing wall at: "+ checkPosition ,board.get(checkPosition).mask.substring(0,1).equals("1"));
                }
            }

            // LEFT
            if(board.get(i).mask.substring(3,4).equals("1"))
            {
                //check for up wall
                int checkPosition = (i - 1);
                if(!(checkPosition < 0 ))
                {
                    // not outside
                    Assert.assertTrue("Missing wall at: "+ checkPosition ,board.get(checkPosition).mask.substring(1,2).equals("1"));
                }
            }
        } */
    }
}