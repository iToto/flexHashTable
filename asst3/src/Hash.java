//TODO (alex): multithreading
//TODO (alex): implement open addressing (double hashing)
//TODO (alex): implement a real hash funciton (to reduce number of collisions and maybe improve speed)

//TODO: add interface. It will make testing and timing easier.
//TODO: optimize by setting table size to prime numbers ( easy and will reduce collisions by about 10%)
//TODO: minor optimization: return hash on first calculation instead of retrieving it
//TODO optimization: modify String class to reduce memory footprint
//TODO: reconsider predefining the input nodes before the timing begins
//TODO: remove temporary node creation (currentNode) to improve speed

//
import java.io.*;
import java.util.*;

public class Hash {

	public static int lastValue = 0, firstValue = 0; //input array positions
	private static InString[] values = new InString[128];

	private static int nInStrings = 0;


	public static void main(String[] args) {
		loadTextFile("hash_test_file1.txt");
		Table ht = new Table();
		//printHashTableStatistics();

        //ht = new Table(); dont need this
		timePutOperation(ht);
		timeGetOperation(ht);
		countCollisions(ht);
	}
	//puts the text file into an array in memory so I/O itself is not timed over and over
	private static void add (String s) {
		if (nInStrings == values.length)
			values = Table.resizeArray(values, values.length*2); //sloppy
		values[nInStrings] = new InString (s);
		++nInStrings;
	}

		public static void countCollisions (Table ht) {
		int totalCollisions = 0;
		int collisionNodes = 0;
		int maxCollisions = 0;
		for (int i=0; i < ht.table.length; ++i) {
			if (ht.table[i] !=null) {
				if (ht.table[i].next!=null) {
					++collisionNodes;
					InString currentNode = ht.table[i];
					int currentCollisions = 0;
					while (currentNode.next != null) {
						++totalCollisions;
						++currentCollisions;
						currentNode = currentNode.next;
					}
				if (currentCollisions > maxCollisions)
					maxCollisions = currentCollisions;
				}
			}
		}
		if (collisionNodes == 0)
			System.out.println("There were no collisions. ");
		else
			System.out.println("There were "
				 + totalCollisions + " total collisions" + " in " + collisionNodes + " nodes. "
				 + "\nThere were " +(double)totalCollisions/collisionNodes + " collisions on average per collision cell." );
			System.out.println("The highest number of collisions was " + maxCollisions);
	}
	/**performs get operation on all available strings and times it */
	public static void timeGetOperation(Table ht) {
				long startTime = System.nanoTime();
		for (int i = firstValue; i < nInStrings; ++i) {
			ht.get (values[i]);
		}
		System.out.println("Get operation on " + (nInStrings - firstValue ) + " strings took "
				+ (System.nanoTime() - startTime) /1000000 + " milliseconds.");
	}

	/**performs put operation on all available strings and times it */
	public static void timePutOperation (Table ht) {
		long startTime = System.nanoTime();
		for (int i = 0; i < nInStrings; ++i) {
			ht.put (values[i]);
		}
		System.out.println( "Put operation on " + nInStrings + " Strings took "
				+(System.nanoTime() - startTime) /1000000 + " milliseconds.");
	}
	public static void loadTextFile(String in) {
		FileReader input = null;
		try {
			input = new FileReader (in);
		}
		catch (Exception e) {
			System.out.println(e);
		}
		BufferedReader inputStream = new BufferedReader (input);
		try {
			// inserts the strings into the input array and converts into a Node
			String s = inputStream.readLine();
			while (s != null) {
				add (s);
				s = inputStream.readLine();
			}
		}
		catch (Exception e) { System.out.println(e);}
	}
}
