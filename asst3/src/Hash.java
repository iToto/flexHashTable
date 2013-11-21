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
	private static double loadFactor = 0.5;
	public static int lastValue = 0, firstValue = 0; //input array positions
	private static InString[] values = new InString[128];
	private static InString[] table = new InString[101]; 
	private static int nInStrings = 0;
	public static int elementsInTable=0;
	private static double expansionCoefficient = 2.0;
	private static int expansionLength = 100;
	private static boolean expandByFactor = true;
	private static boolean separateChaining = true;
	private static char markerScheme = 'A';
	
	public static void main(String[] args) {
		
		loadTextFile("hash_test_file1.txt");
		
		//printHashTableStatistics();
		timePutOperation();
		//timeGetOperation();
		timeGetOperation();
		countCollisions();
	}
	//puts the text file into an array in memory so I/O itself is not timed over and over
	private static void add (String s) {
		if (nInStrings == values.length)
			values = resizeArray(values, values.length*2);
		values[nInStrings] = new InString (s);
		++nInStrings;
	}
	
	/** resizes the input array
	 * 
	 * @param a the array that needs resizing
	 * @param newSize	the new size. Must be greater than the initial size
	 * @return	a bigger array with the same values in the same indexes
	 */
	static InString[] resizeArray(InString[] a, int newSize) {
		InString[] newArray = new InString[newSize];
		System.arraycopy(a, 0, newArray, 0, a.length );
		return newArray;
	}
	/** Inserts a String node (InString) into the array. */
	public static void put (InString s) {
		if (elementsInTable+1 > table.length*loadFactor) {
			if (expandByFactor)
				table = resizeArray(table, (int) (table.length*expansionCoefficient));
			else 
				table = resizeArray(table, table.length+expansionLength);
		}
		if (s.hash == 0)
			s.hash();
		insert (s, (s.hash & 0x7fffffff) % table.length);
		}
	//puts the node where it belongs in the linked list of that index
	public static void insert (InString s, int i) {
		InString currentNode = null;
		if (table[i] == null)
			table [i] = s;
		else {
			currentNode = table [i];
			while (currentNode.next != null)
				currentNode = currentNode.next;
			currentNode.next = s; 
		}
		++elementsInTable;
	
	}
	static void rehash (int newSize) {
		table = new InString[newSize] ;
		for (int i = 0; i < nInStrings; ++i) { 
			put (values[i]);
		}
	}
	/**retrieves the wanted string */
	public static InString get(InString key) { //"key" variable name is for conformity
		int i = (key.hash & 0x7fffffff) % table.length; //it should never be null or zero because all strings are entered first
		InString currentNode = table[i];
		if (currentNode !=null) { //TODO: optimize the double check, especially since it returns null so often
			while (currentNode.next != null) //test null?
				currentNode = currentNode.next;
		}
		return currentNode;
	}
	/**sets the load treshhold for rehashing */
	public void setRehashThreshold (double loadFactor) {
		this.loadFactor = loadFactor;
	}
	/**sets the expansion coefficient or expansion length and the expansion mode */
	public void setRehashFactor(double factorOrNumber) {
		if (factorOrNumber == (int)factorOrNumber) {
		   expandByFactor = false;
		   expansionLength = (int) factorOrNumber;
		}
		else {
			expandByFactor = true; //may be removed later if not necessary
			expansionCoefficient = factorOrNumber;
		}
	} 
	/**sets the collision handling type */
	public static void setCollisionHandling(char type) {
		if (type == 'S') 
			separateChaining = true;
		else
			separateChaining = false;
	}
	/**sets the empty marker scheme for open addressing */
	public static void setEmptyMarkerScheme (char type) {
			markerScheme = type;
	}
	/** prints	the	 selected	 values	 or	 options	 for	 a)-d)	 above,	the	 size	 of	
	the	 table,	 the	 number	 of	 elements	 in	 the	 table,	 the	 load	percentage,	 the	 number	 of	all	
	collisions	accounted	so	far,	the	maximum	number	of	collisions	for	a	single	cell,	the	average	
	number	of	collisions	over	all	cells	with	collisions.
	*/
	
	public static void printHashTableStatistics() {
		System.out.print("The hash table uses ");
		if (separateChaining)
			System.out.print(" separate chaining."); 
		else 
			System.out.print(" double hashing open addressing scheme.");
		
		System.out.println (" The empty marker scheme is "); 
		if (markerScheme == 'R') 
			System.out.print ("removing the duplicate index");
		else if (markerScheme == 'N')
			System.out.print ("using negative value of removed key as empty marker.");
		else if (markerScheme =='A')
			System.out.print ("using A as empty marker."); 
		
		System.out.print(" The hash table is expanding by ");
		if (expandByFactor == true) 
			System.out.print("a factor of " + expansionCoefficient);
		else
			System.out.print("a constant number of " + expansionLength + " elements");
		System.out.print(" when a load factor of " + loadFactor + " is reached." );
		
		
	}
	
	public static void countCollisions () { 
		int totalCollisions = 0;
		int collisionNodes = 0;
		int maxCollisions = 0; 
		for (int i=0; i < table.length; ++i) {
			if (table[i] !=null) {
				if (table[i].next!=null) { 
					++collisionNodes;
					InString currentNode = table[i];
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
	public static void timeGetOperation() {
				long startTime = System.nanoTime();
		for (int i = firstValue; i < nInStrings; ++i) {
			get (values[i]);
		}
		System.out.println("Get operation on " + (nInStrings - firstValue ) + " strings took "
				+ (System.nanoTime() - startTime) /1000000 + " milliseconds.");
	}
	
	/**performs put operation on all available strings and times it */
	public static void timePutOperation ( ) {
		long startTime = System.nanoTime();
		for (int i = 0; i < nInStrings; ++i) {
			put (values[i]);
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
