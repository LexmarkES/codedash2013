package com.joshuarobinson;

import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Joshua Robinson
 * Crashing Robots Code Dash problem
 */
public class Main {

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:\\test_in_progress.txt"))));
        String line;
        String[] data = new String[3];
        int i = 0;

        while ((line = br.readLine()) != null)
        {
            // ignore comments ( the real test data does not have any! )
            if( !line.contains("#"))
            {
                data[i] = line;
                i++;
                if( i == 3 )
                {
                    //send it in
                    processGame(Integer.parseInt(data[0]), Integer.parseInt(data[1]), data[2] );
                    i = 0;
                }
            }
        }


    }

    public static void processGame(int start, int goal, String gameString )
    {


       BoardBuilder bb = new BoardBuilder();
       bb.buildBoard(gameString);

        bb.movesForPosition(start);
        Set<Integer> moves = (Set<Integer>) bb.possibleMoves.get(start);

       // for( Integer num : moves)
       // {
         //  System.err.println(num);
       // }
        // System.err.println("------");
        if( moves.contains(goal)){
            System.out.println("Path found.");
        }else{
            System.out.println("Path not found.");
        }
    }
}
