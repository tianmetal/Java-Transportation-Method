package transportation.method;

import java.util.ArrayList;

import transportation.model.Route;

public class SteppingStone
{
	private Route[][] table;
	private ArrayList<Route> lap,blacklist;
	public SteppingStone(Route[][] table, Route start)
	{
		super();
		int startX = 0,
			startY = 0,
			currentX = 0,
			currentY = 0;
		this.table = table;
		lap = new ArrayList<Route>();
		blacklist = new ArrayList<Route>();
		
		currentX = startX = start.getX();
		currentY = startY = start.getY();
		
		int method = 2,
			sizeY = this.table[0].length,
			sizeX = this.table.length;
		
		do
		{
			int nextX = currentX,
				nextY = currentY;
			if(!lap.contains(this.table[currentX][currentY])) lap.add(this.table[currentX][currentY]);
			System.out.printf("(%d,%d) -> ",(currentX+1),currentY+1);
			
			int offset = 1;
			if(method == 1)
			{
				boolean loop = true;
				if(startY == currentY)
				{
					nextX = startX;
					loop = !loop;
				}
				while(loop)
				{
					int x = currentX;
					if((currentX+offset) < sizeX)
					{
						x = (currentX+offset);
						if(lap.contains(this.table[x][currentY]))
						{
							if((x == startX && currentY == startY) && (this.table[currentX][currentY] != this.table[startX][startY]))
							{
								nextX = x;
								break;
							}
						}
						else
						{
							if(this.table[x][currentY].getSupply() != 0 && !blacklist.contains(this.table[x][currentY]))
							{
								nextX = x;
								break;
							}
						}
					}
					if((currentX-offset) >= 0)
					{
						x = (currentX-offset);
						if(lap.contains(this.table[x][currentY]))
						{
							if((x == startX && currentY == startY) && (this.table[currentX][currentY] != this.table[startX][startY]))
							{
								nextX = x;
								break;
							}
						}
						else
						{
							if(this.table[x][currentY].getSupply() != 0 && !blacklist.contains(this.table[x][currentY]))
							{
								nextX = x;
								break;
							}
						}
					}
					if((currentX-offset < 0) && (currentX+offset >= sizeX))
					{
						System.out.printf("Added (%d,%d) to blacklist\n",currentX+1,currentY+1);
						blacklist.add(this.table[currentX][currentY]);
						lap.remove(this.table[currentX][currentY]);
						if(lap.size() == 0) break;
						nextX = lap.get(lap.size()-1).getX();
						nextY = lap.get(lap.size()-1).getY();
						method = 1;
						break;
					}
					offset++;
				}
				method = 2;
			}
			else if(method == 2)
			{
				boolean loop = true;
				if(startX == currentX)
				{
					nextY = startY;
					loop = !loop;
				}
				while(true)
				{
					int y = currentY;
					if((currentY+offset) < sizeY)
					{
						y = (currentY+offset);
						if(lap.contains(this.table[currentX][y]))
						{
							if((currentX == startX && y == startY) && (this.table[currentX][currentY] != this.table[startX][startY]))
							{
								nextY = y;
								break;
							}
						}
						else
						{
							if(this.table[currentX][y].hasConstraints() && !blacklist.contains(this.table[currentX][y]))
							{
								nextY = y;
								break;
							}
						}
					}
					if((currentY-offset) >= 0)
					{
						y = (currentY-offset);
						if(lap.contains(this.table[currentX][y]))
						{
							if((currentX == startX && y == startY) && (this.table[currentX][currentY] != this.table[startX][startY]))
							{
								nextY = y;
								break;
							}
						}
						else
						{
							if(this.table[currentX][y].hasConstraints() && !blacklist.contains(this.table[currentX][y]))
							{
								nextY = y;
								break;
							}
						}
					}
					if((currentY-offset < 0) && (currentY+offset >= sizeY))
					{
						System.out.printf("Added (%d,%d) to blacklist\n",currentX+1,currentY+1);
						blacklist.add(this.table[currentX][currentY]);
						lap.remove(this.table[currentX][currentY]);
						if(lap.size() == 0) break;
						nextX = lap.get(lap.size()-1).getX();
						nextY = lap.get(lap.size()-1).getY();
						method = 2;
						break;
					}
					offset++;
				}
				method = 1;
			}

			currentX = nextX;
			currentY = nextY;
		}
		while(lap.size() == 1 || !(currentX == startX && currentY == startY));
	}
	public void printRoute()
	{
		System.out.println(getRoute());
	}
	public String getRoute()
	{
		String output = "";
		int idx = 0;
		for(Route r : lap)
		{
			output += String.format("(%d,%d)",r.getX()+1,r.getY()+1);
			idx++;
			if(idx != lap.size()) output += " -> ";
		}
		return output;
	}
	public String getRouteGain()
	{
		String output = "";
		int idx = 0;
		for(Route r : lap)
		{
			output += String.format("%.1f",r.getCost());
			idx++;
			if(idx != lap.size())
			{
				if(idx % 2 == 0)
				{
					 output += " + ";
				}
				else
				{
					output += " - ";
				}
			}
		}
		output += " = " + getGain();
		return output;
	}
	public double getGain()
	{
		double total = 0;
		boolean negative = false;
		for(Route r : lap)
		{
			total += (negative ? -r.getCost() : r.getCost());
			negative = !negative;
		}
		return total;
	}
	public ArrayList<Route> getLap()
	{
		return lap;
	}
	public void rotate(int sizeX, int sizeY)
	{
		double[] diff = new double[sizeX],
				 used = new double[sizeY];
		for(Route r : lap)
		{
			if(r.getSupply() == 0) continue;
			diff[r.getX()] += r.getSupply();
			used[r.getY()] += r.getSupply();
			r.setSupply(0);
		}
		for(Route r: lap)
		{
			int x = r.getX(),
				y = r.getY();
			if(diff[x] > used[y])
			{
				diff[x] -= used[y];
				r.setSupply(used[y]);
				used[y] = 0;
			}
			else
			{
				used[y] -= diff[x];
				r.setSupply(diff[x]);
				diff[x] = 0;
			}
		}
		for(int i = 0; i < diff.length; i++)
		{
			if(diff[i] == 0) continue;
			for(int j = 0; j < used.length; j++)
			{
				if(used[j] == 0) continue;
				if(diff[i] > used[j])
				{
					diff[i] -= used[j];
					table[i][j].setSupply(used[j]);
					used[j] = 0;
				}
				else
				{
					used[j] -= diff[i];
					table[i][j].setSupply(diff[i]);
					diff[i] = 0;
				}
			}
		}
	}
}
