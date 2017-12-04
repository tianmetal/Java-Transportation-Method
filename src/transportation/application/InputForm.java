package transportation.application;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import transportation.model.Route;

public class InputForm extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JTextField sizeFieldX,sizeFieldY,input[][];
	private JButton sizeButton,confirmButton;
	private JComboBox<String> initializer,method,mode;
	private GridBagConstraints gbc = new GridBagConstraints();
	public InputForm()
	{
		setTitle("Transportation Model");
		GridBagConstraints g = new GridBagConstraints();
		
		panel = new JPanel(new GridBagLayout());
		JPanel header = new JPanel(new GridBagLayout());
		
		sizeFieldX = new JTextField(4);
		sizeFieldY = new JTextField(4);
		sizeButton = new JButton("Confirm");
		
		g.gridx = 0;
		header.add(new JLabel("# Source: "),g);
		
		g.gridx = 1;
		header.add(sizeFieldX,g);
		
		g.gridx = 2;
		header.add(new JLabel("# Target: "),g);
		
		g.gridx = 3;
		header.add(sizeFieldY,g);
		
		g.gridx = 4;
		header.add(sizeButton,g);
		
		panel.add(header,gbc);
		
		sizeButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				int x = Integer.parseInt(sizeFieldX.getText());
				int y = Integer.parseInt(sizeFieldY.getText());
				sizeFieldX.setEnabled(false);
				sizeFieldY.setEnabled(false);
				sizeButton.setEnabled(false);
				generateTable(y, x);
			}
		});
		
		this.add(panel);
		this.pack();
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	private void generateTable(int sizeX, int sizeY)
	{
		GridBagConstraints g = new GridBagConstraints();
		JPanel body = new JPanel(new GridBagLayout());
		
		input = new JTextField[sizeX+1][sizeY+1];
		confirmButton = new JButton("Calculate");
		g.insets = new Insets(1, 2, 1, 2);
		g.gridx = 1; 
		g.gridy = 1;
		g.gridwidth = sizeX;
		body.add(new JLabel("Target"),g);
		
		g.gridx = 0; 
		g.gridy = 2;
		g.gridwidth = 1;
		body.add(new JLabel("Source"),g);
		
		for(int i = 1; i <= sizeX; i++)
		{
			g.gridx = i;
			body.add(new JLabel("#" + i),g);
		}
		
		g.gridx++;
		body.add(new JLabel("Supply"),g);
		
		for(int i = 0; i < sizeY; i++)
		{
			g.gridx = 0;
			g.gridy++;
			body.add(new JLabel("#" + (i+1)),g);
			for(int j = 0; j <= sizeX; j++)
			{
				g.gridx++;
				input[j][i] = new JTextField(4);
				body.add(input[j][i],g);
			}
		}
		
		g.gridx = 0;
		g.gridy++;
		body.add(new JLabel("Demand"),g);
		for(int i = 0; i <= sizeX; i++)
		{
			g.gridx++;
			input[i][sizeY] = new JTextField(4);
			body.add(input[i][sizeY],g);
			if(i == sizeX) input[i][sizeY].setEditable(false);
		}
		
		g.gridx = 0;
		g.gridy++;
		g.gridwidth = (sizeX+2);
		
		int temp = g.gridy;
		JPanel option = new JPanel(new GridBagLayout());
		
		body.add(option,g);
		
		initializer = new JComboBox<String>();
		initializer.addItem("North West");
		initializer.addItem("Least Cost");
		initializer.addItem("Vogel Approx.");
		method = new JComboBox<String>();
		method.addItem("Stepping Stone");
		method.addItem("Iterative Computation");
		mode = new JComboBox<String>();
		mode.addItem("Minimalization");
		mode.addItem("Maximization");
		
		g.gridy = 0;
		g.gridwidth = 1;
		g.gridx = 0;
		option.add(initializer,g);
		g.gridx = 1;
		option.add(method, g);
		g.gridx = 2;
		option.add(mode, g);
		
		g.gridx = 0;
		g.gridy = temp+1;
		g.gridwidth = (sizeX+2);
		g.fill = GridBagConstraints.BOTH;
		body.add(confirmButton, g);
		
		confirmButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int tableX = sizeX,
					tableY = sizeY;
				double totalSupply = 0.0,
					   totalDemand = 0.0;
				
				for (int i = 0; i < tableY; i++)
				{
					totalSupply += Double.parseDouble(input[tableX][i].getText());
				}
				for (int i = 0; i < tableX; i++)
				{
					totalDemand += Double.parseDouble(input[i][tableY].getText());
				}
				if(totalSupply != totalDemand)
				{
					if(totalSupply > totalDemand)
					{
						tableX++;
					}
					else if(totalDemand > totalSupply)
					{
						tableY++;
					}
				}
				
				Route[][] table = new Route[tableY][tableX];
				double[] supply = new double[tableY];
				double[] demand = new double[tableX];
				for (int i = 0; i < table.length; i++)
				{
					for (int j = 0; j < table[i].length; j++)
					{
						if(i == sizeY)
						{
							table[i][j] = new Route(i, j, 0.0);
						}
						else if(j == sizeX)
						{
							table[i][j] = new Route(i, j, 0.0);
						}
						else
						{
							table[i][j] = new Route(i, j, Integer.parseInt(input[j][i].getText()));
						}
					}
				}
				
				if(totalSupply > totalDemand)
				{
					demand[tableX-1] = (totalSupply-totalDemand);
				}
				else if(totalDemand > totalSupply)
				{
					supply[tableY-1] = (totalDemand-totalSupply);
				}
				
				for (int i = 0; i < sizeY; i++)
				{
					supply[i] = Double.parseDouble(input[sizeX][i].getText());
				}
				for (int i = 0; i < sizeX; i++)
				{
					demand[i] = Double.parseDouble(input[i][sizeY].getText());
				}
				
				new OutputFrame(table, supply, demand,(initializer.getSelectedIndex()+1),
						(method.getSelectedIndex()+1),(mode.getSelectedIndex()+1));
			}
		});
		
		gbc.gridy = 1;
		panel.add(body,gbc);
		this.pack();
	}
}
