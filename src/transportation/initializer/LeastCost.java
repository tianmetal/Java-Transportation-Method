package transportation.initializer;

import transportation.model.Route;

public class LeastCost
{
	private static Route[][] temp;
	private static double[] diff,used;
	public static Route[][] initiliaze(Route[][] table, final double[] supply, final double[] demand)
	{
		temp = table;
		diff = supply.clone();
		used = demand.clone();
		
		Route next = null;
		while((next = findLeastCost()) != null)
		{
			int i = next.getX(),
				j = next.getY();
			if(diff[i] >= used[j])
			{
				System.out.println(" -> filled: " + used[j]);
				temp[i][j].setSupply(used[j]);
				diff[i] -= used[j];
				used[j] = 0;
			}
			else
			{
				System.out.println(" -> filled: " + diff[j]);
				temp[i][j].setSupply(diff[i]);
				used[j] -= diff[i];
				diff[i] = 0;
			}
		}
		
		return temp;
	}
	private static Route findLeastCost()
	{
		Route lowest = null;
		for (int i = 0; i < diff.length; i++)
		{
			if(diff[i] == 0) continue;
			for (int j = 0; j < used.length; j++)
			{
				if(used[j] == 0) continue;
				if(lowest == null || temp[i][j].getCost() < lowest.getCost())
				{
					lowest = temp[i][j];
				}
			}
		}
		if(lowest != null) System.out.printf("Lowest = (%d,%d) = %.1f",lowest.getX(),lowest.getY(),lowest.getCost());
		return lowest;
	}
}
