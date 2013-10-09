import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;


public class WhoopsTest
{
    public final static InputStream STANDARD_IN = System.in;
    public final static PrintStream STANDARD_OUT = System.out;
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");


    @After
    public void tearDown()
    {
        System.setIn(STANDARD_IN);
        System.setOut(STANDARD_OUT);
    }

    @Test
    public void testExampleIn() throws IOException
    {
        System.setIn(new FileInputStream("example.in"));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        Whoops.main();
        System.setOut(STANDARD_OUT);

        Scanner scanner = new Scanner(new File("example.out"));
        String expected = scanner.useDelimiter("\\A").next();
        String actual = output.toString();
        Assert.assertEquals(expected, actual);
        scanner.close();
    }

    @Test
    public void testSampleIn() throws IOException
    {
        System.setIn(new FileInputStream("sample.in"));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        Whoops.main();
        Scanner scanner = new Scanner(new File("sample.out"));
        String expected = scanner.useDelimiter("\\A").next();
        String actual = output.toString();
        Assert.assertEquals(expected, actual);
        scanner.close();
    }

    @Test
    public void testEveryone() throws IOException
    {
        String input = "Brigid Michael JR Richard Tim Lyndsey Will";
        input += LINE_SEPARATOR;
        input += 5;
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Whoops.main();
    }

    @Test
    public void testSimple() throws IOException
    {
        String input = "Will Michael Brigid";
        input += LINE_SEPARATOR;
        input += 2;
        
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Whoops.main();
    }

    @Test
    public void testLong() throws IOException
    {
        String input = "Will Brigid Lyndsey Shemp Larry Curly Moe";
        input += LINE_SEPARATOR;
        input += 2;

        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Whoops.main();
    }

    @Test
    public void testSuperLong() throws IOException
    {
        int numberOfRandoms = 100;
        String input = "Player1";
        for (int i = 2; i < numberOfRandoms / 2; i++)
        {
            input += " Player" + i;
        }
        input += " Tim";
        for (int i = numberOfRandoms / 2; i < numberOfRandoms; i++)
        {
            input += " Player" + i;
        }
        input += LINE_SEPARATOR;
        input += 1;

        System.out.println(input);
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Whoops.main();
    }

}
