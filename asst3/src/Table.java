// class where the hashing will be done

public class Table {
	
	public static int elementsInTable=0;

	private static double expansionCoefficient = 2.0;
	private static int expansionLength = 100;
	private static boolean expandByFactor = true;
	private static boolean separateChaining = true;
	private static char markerScheme = 'A';
	private static InString[] table = new InString[101]; 
	private static double loadFactor = 0.5;
	

	/** Inserts a String node (InString) into the array. */
	public static void put (InString s) {
		if (elementsInTable+1 > table.length*loadFactor) {
			if (expandByFactor)
				table = resizeArray(table, (int) (table.length*expansionCoefficient));
			else 
				table = resizeArray(table, table.length+expansionLength);
		}
		if (s.hash == 0)
			insert (s, (s.hash() & 0x7fffffff) % table.length);
		else 
			insert (s, (s.hash & 0x7fffffff) % table.length);
		}
	


	//puts the node where it belongs in the linked list of that index
	public static void insert (InString s, int i) {
		InString currentNode = null;
		if (table[i] == null)
			table [i] = s;
		else {
			currentNode = table [i];
			while (currentNode.next != null) {
				currentNode = currentNode.next;
			}
			currentNode.next = s; 
		}
		++elementsInTable;
	}

	

	static void rehash (int newSize) {
		InString[] temp = table ;
		table = new InString[newSize];
		for (int i = 0; i < temp.length; ++i) { 
			if (table[i] != null)
			put (temp [i]);
		}
	}



	/** retrieves the wanted string */
	public static InString get(InString key) { //"key" variable name is for conformity
		int i = (key.hash & 0x7fffffff) % table.length; //it should never be null or zero because all strings are entered first
		if (table[i] == null)
			return null;
		if (table[i].str.equals(key.str) ) //checks first value for match
			return table[i];
		else {
			InString currentNode = table[i];
			while (currentNode.next != null){
				if (!currentNode.str.equals(key.str)) 
					return currentNode.next;
				currentNode = currentNode.next;
			}
		}
		return null;
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
	


	/** resizes the input array
	* @param a the array that needs resizing
	* @param newSize	the new size. Must be greater than the initial size
	* @return	a bigger array with the same values in the same indexes
	*/
	static InString[] resizeArray(InString[] a, int newSize) {
		InString[] newArray = new InString[newSize];
		System.arraycopy(a, 0, newArray, 0, a.length );
		return newArray;
	}
}
