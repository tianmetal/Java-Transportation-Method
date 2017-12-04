package transportation.initializer;

import transportation.model.Route;

public class NorthWest
{
	public static Route[][] initiliaze(Route[][] table, final double[] supply, final double[] demand)
	{
		Route[][] temp = table;
		double[] diff = supply.clone();
		double[] used = demand.clone();
		
		for (int i = 0; i < supply.length; i++)
		{
			for (int j = 0; j < demand.length; j++)
			{
				if(diff[i] >= used[j])
				{
					temp[i][j].setSupply(used[j]);
					diff[i] -= used[j];
					used[j] = 0;
				}
				else
				{
					temp[i][j].setSupply(diff[i]);
					used[j] -= diff[i];
					diff[i] = 0;
				}
			}
		}
		
		return temp;
	}
}
