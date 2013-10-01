import java.util.Scanner;

class LookAndSay
{
    /**
     * appendDigits
     * Appends count and digit to old sequence member
     *
     * @param oldMember - Old sequence member
     * @param count - Count of old digit
     * @param digit - Old digit
     * @return old sequence plus new digits
     */
    public static String appendDigits(String oldMember, int count, char digit)
    {
        return oldMember + count + digit;
    }

    /**
     * nextMember
     * Calculates the next member of the sequence
     * 
     * @param currentMember - Current sequence member
     * @return nextMember - Next member of the sequence
     */
    public static String nextMember(String currentMember)
    {
        String nextMember = "";

        if (currentMember.length() == 0)
        {
            return nextMember;
        }

        int count = 1;
        char previousDigit = currentMember.charAt(0);
        int len = currentMember.length();

        for (int i = 1; i < len; i++)
        {
            if (currentMember.charAt(i) == previousDigit)
            {
                count++;
            }
            else
            {
                nextMember = appendDigits(nextMember, count, previousDigit);

                previousDigit = currentMember.charAt(i);
                count = 1;
            }
        }
        nextMember = appendDigits(nextMember, count, previousDigit);

        return nextMember;
    }

    /**
     * main
     */
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        while (input.hasNext())
        {
            // get starting member
            String sequenceMember = input.next();
            if (input.hasNext())
            {
                // get next n members to calculate
                int stop = Integer.parseInt(input.next());
                if (sequenceMember.equals("0") && stop == 0)
                {
                    // 0 0 -> Done
                    return;
                }
                else
                {
                    for (int i = 0; i < stop; i++)
                    {
                        sequenceMember = LookAndSay.nextMember(sequenceMember);
                        // System.out.println(sequenceMember);
                    }
                    System.out.println(sequenceMember);
                }
            }
        }
    }
}
