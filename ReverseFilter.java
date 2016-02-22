import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileOutputStream;

public class ReverseFilter
{

    //Check for -- flag. Return index, if found. Return number of arguments, if not found.
    public static int ignoreFlag(String[] arguments) 
    {
	for (int k = 0; k < arguments.length; k++)
	{
	    if  (arguments[k].compareTo("--") == 0)
		return k; //Subsequent -- flags will be treated as file names.
	}
       	return arguments.length;
    }

    //Check for -o flag. Return index, if found. Return number of arguments, if not found.
    public static int outputFlag(String[] arguments)
    {
	for (int l = 0; l < arguments.length; l++)
	{
	    if (arguments[l].compareTo("-o") == 0)
		return l; //Subsequent -o flags will be treated as file names.
	}
	return arguments.length;
    }

    //Reverse standard in and write to standard out.
    public static void reverseKeyboard()
    {
	Scanner keyboard = null;
	String line = "";
	String reversedLine = "";

	keyboard = new Scanner(System.in);
	
	System.out.println("Enter the lines you would like reversed. Press Enter when finished.");
	
	//Is there a way to better way to stop reading from standard in?
	
	while (keyboard.hasNextLine() && !(line = keyboard.nextLine()).equals(""))
	{
	    String[] words = line.split(" ");
	    for (int j = words.length - 1; j >= 0; j--)
	    {
		reversedLine += words[j] + " ";
	    }
	    System.out.println(reversedLine);
	    reversedLine = "";
	} 

	//Is there a nice way to create a "reverse" function that can be called multiple times?
	
	keyboard.close();
    }

    public static void main (String[] args)
    {

	Scanner keyboard = null;
	Scanner inputStream = null;
	PrintWriter outputStream = null;
	String line = "";
	String reversedLine = "";
	int ignoreFlagsAfter = ignoreFlag(args);
        int outputFileIndex = outputFlag(args) + 1;

	//If there are no arguments, the filter reads from standard in and writes to standard out.
	if (args.length == 0)
	{
	    reverseKeyboard();
	}
	//If there are arguments, the filter checks for flags to determine where to read from and where to write to.
	//If outputFileIndex is greater than ignoreFlagsAfter, the output will be written to standard out.
	else if (outputFileIndex > ignoreFlagsAfter) //If the first -- flag follows immediately after the -o flag, it will be treated as a file name.
	{
	    for (int i = 0; i < args.length; i++)
	    {
		//The first -- is just a flag, not a file.
		if (args[i].compareTo("--") == 0 && i == ignoreFlagsAfter)
		{
		}
		//If the - flag comes before the -- flag, the filter should read from standard in.
		else if  (args[i].compareTo("-") == 0 && i < ignoreFlagsAfter)
		{
		    reverseKeyboard();
		}
		//All other arguments should be treated as file names and the filter should read from them.
		else
		{
		    try
		    {
			inputStream = new Scanner(new FileInputStream(args[i]));
		    }
		    catch(FileNotFoundException e)
		    {
			System.out.println("Problem opening files to write to standard output.");
			System.exit(0);
		    }

		    while (inputStream.hasNextLine())
		    {
			line = inputStream.nextLine();
			String [] words = line.split(" ");
			for (int j = words.length - 1; j >= 0; j--)
			{
			    reversedLine += words[j] + " ";
			}
			System.out.println(reversedLine);
			reversedLine = "";
		     } 
		    inputStream.close();
		}
	    }
      	}
	//If outputFileIndex is less than ignoreFlagsAfter, the output will be written to the file with index outputFileIndex.
	else
	{
	    String outputFileName = args[outputFileIndex];

	    for (int i = 0; i < args.length; i++)
	    {
		//The file at outputFileIndex should be written to, not read.
		if (i == outputFileIndex)
		{
		}
		//The first -- is just a flag, not a file.
		else if (args[i].compareTo("--") == 0 && i == ignoreFlagsAfter)
		{
		}
		//The first -o is just a flag, not a file.
		else if (args[i].compareTo("-o") == 0 && i == outputFileIndex - 1)
		{
		}
		//If the - flag comes before the -- flag, the filter should read from standard in.
		else if  (args[i].compareTo("-") == 0 && i < ignoreFlagsAfter)
		{
		    try
		    {
			outputStream = new PrintWriter(new FileOutputStream(outputFileName, true)); //Appends subsequent file reversals, instead of overwriting.
		    }
		    catch(FileNotFoundException e)
		    {
			System.out.println("Problem with output file.");
			System.exit(0);
		    }

		    keyboard = new Scanner(System.in);

		    System.out.println("Enter the lines you would like reversed. Press Enter when finished.");
	    
		    //Is there a way to better way to stop reading from standard in?

		    while (keyboard.hasNextLine() && !(line = keyboard.nextLine()).equals(""))
		    {
			String[] words = line.split(" ");
			for (int j = words.length - 1; j >= 0; j--)
			{
			    reversedLine += words[j] + " ";
			}
			outputStream.println(reversedLine);
			reversedLine = "";
		    } 

		    //Is there a nice way to create a "reverse" function that can be called multiple times?
		    
		    keyboard.close();
		}
		//All other arguments should be treated as file names and the filter should read from them.
		else
		{
		    try
		    {
			inputStream = new Scanner(new FileInputStream(args[i]));
			outputStream = new PrintWriter(new FileOutputStream(outputFileName, true)); //Appends subsequent file reversals, instead of overwriting.
		    }
		    catch(FileNotFoundException e)
		    {
			System.out.println("Problem opening files to write to output file.");
			System.exit(0);
		    }

		    while (inputStream.hasNextLine())
		    {
			line = inputStream.nextLine();
			String [] words = line.split(" ");
			for (int j = words.length - 1; j >= 0; j--)
			{
			    reversedLine += words[j] + " ";
			}
			outputStream.println(reversedLine);
			reversedLine = "";
		     } 
		    inputStream.close();
		}
		outputStream.close();
	    }
	}
    }
}