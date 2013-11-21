
 class InString {
	public String str;
	public int hash;
	public InString next;
	
	public InString (String stri) {
		str = stri;
	}
	
	public int hash() {
		if (str.length() == 0)
			return 0; //TODO: throw exception?
		for (int i = 0; i < str.length(); i++) {
				hash = hash*31+ str.charAt(i);
		}
		return hash;
	}
	
	public String toString() {
		return str;
	}
}
