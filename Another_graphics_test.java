import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Another_graphics_test extends JFrame
{
	public static void main(String[] args)
	{
		Another_graphics_test test = new Another_graphics_test();
		test.setVisible(true);
	}

	public Another_graphics_test()
	{
		super();
		this.setSize(400, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new CardLayout());
		this.add(new menuPanel());
	}



	private class menuPanel extends JLayeredPane
	{
		JLabel background;

		JLabel title;
		BreadButton startButton;
		BreadButton helpButton;
		BreadButton exitButton;

		public menuPanel()
		{
			super();

			this.setLayout(new FillLayout());

			title = new JLabel(new ImageIcon("image/Title.png"));
			title.setAlignmentX(Component.CENTER_ALIGNMENT);

			startButton = new BreadButton("Start");
			helpButton = new BreadButton("Help");
			exitButton = new BreadButton("Exit");

			background = new JLabel(new ImageIcon("image/Placeholder_Menu.png"));

			Box foreground = Box.createVerticalBox();



			foreground.add(Box.createVerticalGlue());
			foreground.add(title);
			foreground.add(Box.createVerticalStrut(10));



			Box rightAlignment = Box.createHorizontalBox();
			rightAlignment.add(Box.createGlue());

			Box verticalButtons = Box.createVerticalBox();
			verticalButtons.add(startButton);
			verticalButtons.add(helpButton);
			verticalButtons.add(exitButton);

			rightAlignment.add(verticalButtons);



			foreground.add(rightAlignment);
			foreground.add(Box.createVerticalGlue());

			// this.add(background, new Integer(5));
			this.add(foreground, new Integer(10));
		}

		private class BreadButton extends JButton
		{
			public BreadButton(String text)
			{
				super(text, new ImageIcon(new ImageIcon("image/Breadstick Button.png").getImage().getScaledInstance( 300, 50,  java.awt.Image.SCALE_SMOOTH)));

				setHorizontalTextPosition(JButton.CENTER);
				setVerticalTextPosition(JButton.CENTER);
				setOpaque(false);
				setContentAreaFilled(false);
				setMargin(new Insets(0, 0, 0, 0));
				setBorderPainted(false);
			}
		}

		private class FillLayout implements LayoutManager
		{
			@Override
			public void addLayoutComponent(String str, Component comp){}

			@Override
			public void removeLayoutComponent(Component comp){}

			@Override
			public Dimension preferredLayoutSize(Container parent)
			{
				double maxX = 0;
				double maxY = 0;


				int numComponenets = parent.getComponentCount();

				for (int i = 0; i < numComponenets; i++)
				{
					Dimension childSize = parent.getComponent(i).getPreferredSize();
					if (childSize.getWidth() > maxX)
						maxX = childSize.getWidth();
					if (childSize.getHeight() > maxY)
						maxY = childSize.getHeight();
				}

				Dimension out = new Dimension();
				out.setSize(maxX, maxY);
				return out;
			}

			@Override
			public Dimension minimumLayoutSize(Container parent)
			{
				double maxX = 0;
				double maxY = 0;


				int numComponenets = parent.getComponentCount();

				for (int i = 0; i < numComponenets; i++)
				{
					Dimension childSize = parent.getComponent(i).getMinimumSize();
					if (childSize.getWidth() > maxX)
						maxX = childSize.getWidth();
					if (childSize.getHeight() > maxY)
						maxY = childSize.getHeight();
				}

				Dimension out = new Dimension();
				out.setSize(maxX, maxY);
				return out;
			}

			@Override
			public void layoutContainer(Container parent)
			{
				int numComponenets = parent.getComponentCount();

				for (int i = 0; i < numComponenets; i++)
				{
					parent.getComponent(i).setBounds(0, 0, parent.getWidth(), parent.getHeight());
				}
			}
		}
	}

	
}