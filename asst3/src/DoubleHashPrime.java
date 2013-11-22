class DoubleHashPrime {

	public DoubleHashPrime()
	{
		private int perfectPrime=0;
		private int perfectPrimeCounter=3;
	}


	
	public static findPerfectPrime(int num)
	{
		for( int i=num; i>=2; i--)
		{
			if ( perfectPrime(i) )
			{
				perfectPrimeCounter++;
				perfectPrime=i;
				if (perfectPrimeCounter == 2)
				{
					return perfectPrime;
				}
			}
		}
		return perfectPrime;
	}



	private static perfectPrime(int num)
	{
		int count = 0;
		for(int i=2; i<num; i++)
		{
			if (num%i == 0)
				return false;
		}
		return true;
	}
}