package transportation.model;

public class Route
{
	private int x,y;
	private double cost,supply;
	private boolean isFilled;
	
	public Route(int x, int y, double cost, double supply)
	{
		this.cost = cost;
		this.supply = supply;
		this.x = x;
		this.y = y;
		this.isFilled = false;
	}

	public Route(int x, int y, double cost)
	{
		this(x,y,cost,0);
	}
	
	public double getCost()
	{
		return cost;
	}

	public double getSupply()
	{
		return supply;
	}

	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setSupply(double supply)
	{
		this.supply = supply;
		if(supply == 0.0)
		{
			this.isFilled = false;
		}
		else
		{
			this.isFilled = true;
		}
	}
	public void setSupply(double supply, boolean used)
	{
		this.supply = supply;
		this.isFilled = used;
	}
	public boolean hasConstraints()
	{
		return isFilled;
	}
}
