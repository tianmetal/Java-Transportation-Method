package transportation.application;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import transportation.initializer.LeastCost;
import transportation.initializer.NorthWest;
import transportation.initializer.VogelApprox;
import transportation.method.IterativeComputation;
import transportation.method.SteppingStone;
import transportation.model.Route;

public class OutputFrame extends JFrame
{
	private static final long serialVersionUID = 2L;
	public static final int INITIALIZER_NORTHWEST = 1;
	public static final int INITIALIZER_LEASTCOST = 2;
	public static final int INITIALIZER_VOGEL = 3;
	public static final int METHOD_STEPPINGSTONE = 1;
	public static final int METHOD_ITERATIVE = 2;
	public static final int MODE_MINIMALIZATION = 1;
	public static final int MODE_MAXIMIZATION = 2;
	
	private Route[][] table;
	private double[] supply,demand;
	private int method,mode;
	private JTabbedPane tabs;
	
	public OutputFrame(Route[][] table, double supply[], double demand[], int initializer, int method, int mode)
	{
		String title = "Result: ";
		if(initializer == INITIALIZER_NORTHWEST)
		{
			this.table = NorthWest.initiliaze(table, supply, demand);
			title += "North West + ";
		}
		else if(initializer == INITIALIZER_LEASTCOST)
		{
			this.table = LeastCost.initiliaze(table, supply, demand);
			title += "Least Cost + ";
		}
		else if(initializer == INITIALIZER_VOGEL)
		{
			this.table = VogelApprox.initiliaze(table, supply, demand);
			title += "Vogel Approx + ";
		}
		if(method == METHOD_STEPPINGSTONE)
		{
			title += "Stepping stone";
		}
		else if(method == METHOD_ITERATIVE)
		{
			title += "Computation Method";
		}
		if(mode == MODE_MINIMALIZATION)
		{
			title += " (Minimalization)";
		}
		else if(mode == MODE_MAXIMIZATION)
		{
			title += " (Maximization)";
		}
		this.method = method;
		this.mode = mode;
		
		setTitle(title);
		this.supply = supply;
		this.demand = demand;
		this.tabs = new JTabbedPane();
		
		this.add(tabs);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
		
		int iteration = 1;
		while(true)
		{
			GridBagConstraints gbc = new GridBagConstraints();
			JPanel panel = new JPanel(new GridBagLayout());
			printTable(panel,gbc);
			if(!iterate(panel,gbc))
			{
				tabs.add("Iteration #" + iteration, panel);
				break;
			}
			tabs.add("Iteration #" + iteration, panel);
			iteration++;
		}
		
		this.pack();
	}
	private boolean iterate(JPanel panel, GridBagConstraints gbc)
	{
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridy++;
		if(method == METHOD_STEPPINGSTONE)
		{
			SteppingStone s = null;
			ArrayList<SteppingStone> result = new ArrayList<SteppingStone>();
			for (int i = 0; i < table.length; i++)
			{
				for (int j = 0; j < table[i].length; j++)
				{
					if(table[i][j].getSupply() != 0) continue;
					System.out.printf("Running SS on: (%d,%d)\n",(i+1),(j+1));
					result.add(new SteppingStone(table, table[i][j]));
				}
			}
			
			for(SteppingStone r : result)
			{
				gbc.gridy++;
				panel.add(new JLabel(r.getRoute()),gbc);
				gbc.gridy++;
				panel.add(new JLabel(r.getRouteGain()),gbc);
				if(mode == MODE_MINIMALIZATION)
				{
					if((r.getGain() < 0) && (s == null || r.getGain() < s.getGain()))
					{
						s = r;
					}
				}
				else if(mode == MODE_MAXIMIZATION)
				{
					if((r.getGain() > 0) && (s == null || r.getGain() > s.getGain()))
					{
						s = r;
					}
				}
			}
			if(s != null)
			{
				s.rotate(supply.length, demand.length);
				return true;
			}
		}
		else if(method == METHOD_ITERATIVE)
		{
			ArrayList<Route> basic = new ArrayList<Route>();
					
			double u[] = new double[supply.length],
				   v[] = new double[demand.length];
			boolean[] setU = new boolean[u.length],
					  setV = new boolean[v.length];
			
			for(int i = 0; i < table.length; i++)
			{
				for(int j = 0; j < table[i].length; j++)
				{
					if(!table[i][j].hasConstraints()) continue;
					basic.add(table[i][j]);
				}
			}
			
			Route b = basic.get(0);
			u[b.getX()] = 0.0;
			setU[b.getX()] = true;
			v[b.getY()] = b.getCost();
			setV[b.getY()] = true;
			basic.remove(b);
			System.out.printf("found u%d = 0.0\nfound v%d = %.1f\n",b.getX()+1,b.getY()+1,b.getCost());
			
			do
			{
				int size = basic.size();
				for(int k = 0; k < size; k++)
				{
					Route q = basic.get(k);
					int x = q.getX(),
						y = q.getY();
					if(setU[x] && setV[y])
					{
						basic.remove(q);
						k--;
						size--;
						continue;
					}
					if(setU[x])
					{
						v[y] = q.getCost()-u[x];
						setV[y] = true;
						System.out.printf("found v%d = %.1f\n",y+1,v[y]);
						basic.remove(q);
						k--;
						size--;
					}
					else if(setV[y])
					{
						u[x] = q.getCost()-v[y];
						setU[x] = true;
						System.out.printf("found u%d = %.1f\n",x+1,u[x]);
						basic.remove(q);
						k--;
						size--;
					}
				}
			}
			while(basic.size() != 0);
			
			for(int a = 0; a < u.length; a++)
			{
				gbc.gridy++;
				panel.add(new JLabel(String.format("u%d = %.1f", a+1, u[a])),gbc);
			}
			for(int a = 0; a < v.length; a++)
			{
				gbc.gridy++;
				panel.add(new JLabel(String.format("v%d = %.1f", a+1, v[a])),gbc);
			}
			
			IterativeComputation s = null;
			ArrayList<IterativeComputation> result = new ArrayList<IterativeComputation>();
			for (int i = 0; i < table.length; i++)
			{
				for (int j = 0; j < table[i].length; j++)
				{
					if(table[i][j].hasConstraints()) continue;
					result.add(new IterativeComputation(table[i][j], table, u, v));
				}
			}
			
			for(IterativeComputation r : result)
			{
				gbc.gridy++;
				panel.add(new JLabel(r.getEquation()),gbc);
				if(mode == MODE_MAXIMIZATION)
				{
					if((r.getCoefficient() < 0) && (s == null || r.getCoefficient() < s.getCoefficient()))
					{
						s = r;
					}
				}
				else if(mode == MODE_MINIMALIZATION)
				{
					if((r.getCoefficient() > 0) && (s == null || r.getCoefficient() > s.getCoefficient()))
					{
						s = r;
					}
				}
			}
			if(s != null)
			{
				s.rotate();
				return true;
			}
		}
		return false;
	}
	
	private void printTable(JPanel panel, GridBagConstraints gbc)
	{
		GridBagConstraints g = new GridBagConstraints();
		JPanel p = new JPanel(new GridBagLayout());
		g.insets = new Insets(2, 2, 2, 2);
		g.gridwidth = table[0].length+2;
		g.gridy = 0;
		p.add(new JLabel("Source"),g);
		
		g.gridwidth = 1;
		
		g.gridy++;
		for(int i = 0; i < (table[0].length+2); i++)
		{
			g.gridx = i;
			if(i == 0) p.add(new JLabel("Origin"),g);
			else if(i == (table[0].length+1)) p.add(new JLabel("Supply"),g);
			else p.add(new JLabel("#" + i),g);
		}
		
		for(int i = 0; i < table.length; i++)
		{
			g.gridy++;
			for(int j = 0; j < table[i].length+2; j++)
			{
				g.gridx = j;
				if(j == 0) p.add(new JLabel("#" + (i+1)),g);
				else if(j == table[i].length+1) p.add(new JLabel("" + supply[i]),g);
				else p.add(new JLabel(table[i][j-1].getSupply() + "|" + table[i][j-1].getCost()),g);
			}
		}
		
		g.gridy++;
		for(int i = 0; i < (table[0].length+2); i++)
		{
			g.gridx = i;
			if(i == 0) p.add(new JLabel("Demand"),g);
			else if(i == (table[0].length+1)) continue;
			else p.add(new JLabel("" + demand[i-1]),g);
		}
		
		
		double totalz = 0;
		for (int i = 0; i < table.length; i++)
		{
			for (int j = 0; j < table[i].length; j++)
			{
				totalz += (table[i][j].getCost()*table[i][j].getSupply());
			}
		}
		
		g.gridy++;
		p.add(new JLabel("Z = " + totalz));
		
		gbc.gridy++;
		panel.add(p,gbc);
	}
}
