package transportation.method;

import transportation.model.Route;

public class IterativeComputation
{
	private Route start;
	private Route[][] table;
	private double coefficient;
	private int x,y;
	private double[] u,v;
	public IterativeComputation(Route start, Route[][] table, double[] u, double[] v)
	{
		this.start = start;
		this.table = table;
		
		x = start.getX();
		y = start.getY();
		this.u = u;
		this.v = v;
		
		this.coefficient = u[x]+v[y]-start.getCost();
	}
	public String getEquation()
	{
		String out = String.format("x%d%d = u%d + v%d - c%d%d = %.1f + %.1f - %.1f = %.1f",
			x+1,y+1,x+1,y+1,x+1,y+1,u[x],v[y],start.getCost(),getCoefficient());
		return out;
	}
	public double getCoefficient()
	{
		return coefficient;
	}
	public void rotate()
	{
		SteppingStone s = new SteppingStone(table, start);
		s.rotate(table.length, table[0].length);
	}
	
}