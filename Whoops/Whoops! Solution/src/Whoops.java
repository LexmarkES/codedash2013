import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Whoops
{
    Map<String, Player> players = new HashMap<>();
    {
        // doesn't respond after Whoops!
        Player richard = new Player("Richard")
        {
            @Override
            public String response(int previousNumber)
            {
                if (previousNumber != 0 && !isValid(previousNumber))
                {
                    return "";
                }
                return super.response(previousNumber);
            }
        };
        // forgets rule 1: to check each digit for 7
        Player michael = new Player("Michael")
        {
            @Override
            boolean containsSeven(int number)
            {
                return false;
            }
        };
        // forgets rule 2: divisible by 7
        Player tim = new Player("Tim")
        {
            @Override
            boolean divisibleBySeven(int number)
            {
                return false;
            }
        };
        // forgets rule 3: digits total 7
        Player lyndsey = new Player("Lyndsey")
        {
            @Override
            boolean totalsSeven(int number)
            {
                return false;
            }
        };
        // never says whoops
        Player jr = new Player("JR")
        {
            @Override
            public String response(int previousNumber)
            {
                return String.valueOf(previousNumber + 1);
            }
        };
        players.put(richard.name, richard);
        players.put(michael.name, michael);
        players.put(tim.name, tim);
        players.put(lyndsey.name, lyndsey);
        players.put(jr.name, jr);
    }

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String... args) throws IOException
    {
        // grab data from standard in
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (reader.ready())
        {
            String firstLine = reader.readLine();
            String[] playerNames = firstLine.split(" ");
            String secondLine = reader.readLine();
            int rounds = Integer.parseInt(secondLine);

            // create a double linked list of players
            Whoops whoops = new Whoops();
            Player firstPlayer = null;
            Player previousPlayer = null;
            for (String player : playerNames)
            {
                Player newPlayer = whoops.players.get(player);
                if (newPlayer == null)
                {
                    newPlayer = new Player(player);
                }
                if (firstPlayer == null)
                {
                    firstPlayer = previousPlayer = newPlayer;
                }
                else
                {
                    previousPlayer.nextPlayer = newPlayer;
                    newPlayer.previousPlayer = previousPlayer;
                    // always update the ends so we always have a circle
                    firstPlayer.previousPlayer = newPlayer;
                    newPlayer.nextPlayer = firstPlayer;
                }
                previousPlayer = newPlayer;
            }

            // play Whoops! for n rounds
            whoops.play(firstPlayer, rounds);
        }
        reader.close();

    }

    void play(Player firstPlayer, int rounds)
    {
        Player perfectPlayer = new Player("Perfect");
        Player player = firstPlayer;
        for (int j = 0; j < rounds; j++)
        {
            int i = 0;
            boolean isForward = true;
            while (true)
            {
                String response = player.response(i);
                System.out.println(player.name + "-" + response);
                if (!perfectPlayer.response(i).equals(response))
                {
                    System.out.println("END OF ROUND");
                    player.numberOfLosses++;
                    break;
                }

                boolean isWhoops = Player.WHOOPS.equals(response);

                if (isWhoops)
                {
                    isForward = !isForward;
                }
                player = isForward ? player.nextPlayer : player.previousPlayer;
                i++;
            }
        }
        player = firstPlayer;
        do
        {
            System.out.println(player.name + ": " + player.numberOfLosses);
            player = player.nextPlayer;
        }while (player != firstPlayer);
        System.out.println("--");
    }

}

class Player
{
    public final static String WHOOPS = "Whoops!";

    String name;
    int numberOfLosses;
    Player previousPlayer;
    Player nextPlayer;

    Player(String name)
    {
        this.name = name;
    }

    public String response(int previousNumber)
    {
        int number = previousNumber + 1;
        if (isValid(number))
        {
            return String.valueOf(number);
        }
        return WHOOPS;
    }

    boolean isValid(int number)
    {
        return !(containsSeven(number) || totalsSeven(number) || divisibleBySeven(number));
    }

    boolean containsSeven(int number)
    {
        for (int digit : toDigitArray(number))
        {
            if (digit == 7)
            {
                return true;
            }
        }
        return false;
    }

    boolean totalsSeven(int number)
    {
        int total = 0;
        for (int digit : toDigitArray(number))
        {
            total += digit;
        }
        return total == 7;
    }

    boolean divisibleBySeven(int number)
    {
        return number % 7 == 0;
    }

    /**
     * Take a number and return an int array of each digit
     * @param number
     * @return
     */
    int[] toDigitArray(int number)
    {
        char[] charArray = Integer.toString(number).toCharArray();
        int[] digitArray = new int[charArray.length];
        for (int i = 0; i < charArray.length; i++)
        {
            digitArray[i] = Integer.parseInt(String.valueOf(charArray[i]));
        }
        return digitArray;
    }

}