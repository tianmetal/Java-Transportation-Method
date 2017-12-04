package transportation.model;

import java.util.ArrayList;

public class Penalty
{
	private double penalty;
	private ArrayList<Route> routes;
	
	public Penalty(Route[] routes)
	{
		this.routes = new ArrayList<Route>();
		for(Route r : routes)
		{
			this.routes.add(r);
		}
		sort();
		penalty = findPenalty();
	}
	public double getPenalty()
	{
		return penalty;
	}
	public void removeRoute(Route r)
	{
		if(routes.contains(r))
		{
			routes.remove(r);
			sort();
			penalty = findPenalty();
		}
	}
	public boolean hasRoute()
	{
		return (routes.size() > 0);
	}
	public Route[] getRoutes()
	{
		Route[] r = new Route[routes.size()];
		routes.toArray(r);
		return r;
	}
	private double findPenalty()
	{
		double penalty = 0.0;
		System.out.print("Penalty from ");
		for(Route r : routes)
		{
			System.out.printf("(%d,%d) ",r.getX()+1,r.getY()+1);
		}
		if(routes.size() == 1)
		{
			penalty = routes.get(0).getCost();
		}
		else if(routes.size() != 0)
		{
			penalty = (routes.get(1).getCost()-routes.get(0).getCost());
		}
		System.out.println("= " + penalty);
		return penalty;
	}
	private void sort()
	{
		int size = routes.size();
		for (int i = 0; i < size; i++)
		{
			Route s = routes.get(i);
			for(int j = (i+1); j < size; j++)
			{
				if(routes.get(j).getCost() < s.getCost())
				{
					routes.set(i, routes.get(j));
					routes.set(j, s);
					s = routes.get(i);
				}
			}
		}
	}
}
