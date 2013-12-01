// class where the hashing will be done

public class Table {

    public static int elementsInTable=0;
    private static double expansionCoefficient = 2.0;
    private static int expansionLength = 100;
    private static boolean expandByFactor = true;
    public static boolean separateChaining = false;
    private static char markerScheme = 'A';
    public static InString[] table = new InString[101];
    private static double loadFactor = 0.345;
    public int collisionCounter = 0;



    /** Inserts a String node (InString) into the array. */
    public void put (InString s) {
        if (elementsInTable+1 > table.length*loadFactor) {
            if (expandByFactor)
                table = resizeArray(table, (int) (table.length*expansionCoefficient));
            else
                table = resizeArray(table, table.length+expansionLength);
        }
        if (s.hash == 0){
            if ( separateChaining )
                insert (s, (s.hash() & 0x7fffffff) % table.length);
            else
                //System.out.println( "before doubleHash" );
                doubleHash(s);
        }
        else
            insert (s, (s.hash & 0x7fffffff) % table.length);
        }

    public void doubleHash(InString s) {
        //System.out.println( "inside doubleHash" );
        int key = s.hash() & 0x7fffffff;
        int hv1 = hv1(key);
        int hv2 = hv2(key);

        if ( table[hv1] == null)
        {
            //System.out.println( "inside if hv1" );
            insert (s, hv1);
        }
        else
        {   
            // System.out.println( "got a collision, before insert with collision function" );
            //System.out.println( hv1 + " - " + hv2 );
            insert (s, collision(key, hv2));
            //System.out.println( "finished insert with collision function" );
        }
    }

    

    private static int hv1(int i){
        return i % table.length;
    }

    

    private static int hv2(int i){
        DoubleHashPrime prime = new DoubleHashPrime();
        int primeNum1 = prime.findPerfectPrime(table.length);
        int primeNum2 = prime.findPerfectPrime(primeNum1);
        int num = (primeNum2*i)%primeNum1;
        //System.out.println(  "  ---> "+num );
        return num;
    }



    private int collision(int hv1, int hv2){
        int i = 0;
        int newIndex = 0;
        do{
            // System.out.println( i);
            collisionCounter++;
            ++i;
            newIndex = (hv1+(i*hv2)) % table.length;
        }while(table[newIndex] != null && i < table.length);
        return newIndex;
    }



    //puts the node where it belongs in the linked list of that index
    public static void insert (InString s, int i) {
        InString currentNode = null;
        if (table[i] == null){
            table [i] = s;
            // System.out.println( "inserted!!!  " +i);
        }
        else {
            currentNode = table [i];
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = s;
        }
        ++elementsInTable;
    }



    public void rehash (int newSize) {
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



    /** prints  the  selected    values  or  options     for     a)-d)   above, the  size    of
    the  table,  the     number  of  elements    in  the     table,  the     load   percentage,  the     number  of all
    collisions  accounted   so  far,    the maximum number  of  collisions  for a   single  cell,   the average
    number  of  collisions  over    all cells   with    collisions.
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
    * @param newSize    the new size. Must be greater than the initial size
    * @return   a bigger array with the same values in the same indexes
    */
    static InString[] resizeArray(InString[] a, int newSize) {
        InString[] newArray = new InString[newSize];
        System.arraycopy(a, 0, newArray, 0, a.length );
        return newArray;
    }
}
