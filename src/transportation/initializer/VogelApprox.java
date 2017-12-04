package transportation.initializer;

import java.util.ArrayList;

import transportation.model.Penalty;
import transportation.model.Route;

public class VogelApprox
{
	private static double[] diff,used;
	
	public static Route[][] initiliaze(Route[][] table, final double[] supply, final double[] demand)
	{
		diff = supply.clone();
		used = demand.clone();
		
		ArrayList<Penalty> penalties = new ArrayList<Penalty>();
		
		for(int i = 0; i < table.length; i++)
		{
			Route[] s = new Route[table[i].length];
			for(int j = 0; j < table[i].length; j++)
			{
				s[j] = table[i][j];
			}
			penalties.add(new Penalty(s));
		}
		
		for(int i = 0; i < table[0].length; i++)
		{
			Route[] s = new Route[table.length];
			for(int j = 0; j < table.length; j++)
			{
				s[j] = table[j][i];
			}
			penalties.add(new Penalty(s));
		}
		
		do
		{
			sortPenalties(penalties);
			Penalty p = penalties.get(0);
			Route r = p.getRoutes()[0];
			int i = r.getX(),
				j = r.getY();
			System.out.print("Penalty: " + p.getPenalty());
			System.out.printf(" on (%d,%d)",i+1,j+1);
			if(diff[i] >= used[j])
			{
				System.out.println(" -> filled: " + used[j]);
				table[i][j].setSupply(used[j]);
				diff[i] -= used[j];
				used[j] = 0;
				for(int k = 0; k < diff.length; k++)
				{
					int size = penalties.size();
					for(int l = 0; l < size; l++)
					{
						penalties.get(l).removeRoute(table[k][j]);
						if(!penalties.get(l).hasRoute())
						{
							penalties.remove(l);
							l--;
							size--;
						}
					}
				}
			}
			else
			{
				System.out.println(" -> filled: " + diff[i]);
				table[i][j].setSupply(diff[i]);
				used[j] -= diff[i];
				diff[i] = 0;
				for(int k = 0; k < used.length; k++)
				{
					int size = penalties.size();
					for(int l = 0; l < size; l++)
					{
						penalties.get(l).removeRoute(table[i][k]);
						if(!penalties.get(l).hasRoute())
						{
							penalties.remove(l);
							l--;
							size--;
						}
					}
				}
			}
		}
		while(penalties.size() > 0);
		
		return table;
	}
	private static void sortPenalties(ArrayList<Penalty> penalties)
	{
		int size = penalties.size();
		for(int i = 0; i < size; i++)
		{
			Penalty s = penalties.get(i);
			for(int j = (i+1); j < size; j++)
			{
				if(penalties.get(j).getPenalty() > s.getPenalty())
				{
					penalties.set(i,penalties.get(j));
					penalties.set(j,s);
					s = penalties.get(i);
				}
			}
		}
	}
}
