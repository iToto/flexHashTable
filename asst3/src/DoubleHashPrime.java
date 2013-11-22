public class DoubleHashPrime {

	private int perfectPrime;
	private int perfectPrimeCounter;

	public void DoubleHashPrime()
	{
		this.perfectPrime=0;
		this.perfectPrimeCounter=3;
	}


	
	public int findPerfectPrime(int num)
	{
		for( int i=num; i>=2; i--)
		{
			if ( perfectPrime(i) )
			{
				perfectPrimeCounter++;
				perfectPrime=i;
				if (perfectPrimeCounter == 5)
				{
					return perfectPrime;
				}
			}
		}
		return perfectPrime;
	}



	private boolean perfectPrime(int num)
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