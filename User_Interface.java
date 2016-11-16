import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.*;

class User_Interface// extends Canvas 
{	
	
	// initialize window parameters
	public static final int Width = 320;
	public static final int Height = 400;
	public static final String Title = " Panic at the Pizzaria";

	// public static Canvas ui = new Canvas();

	public static void main(String[] a)
	{
		
		Canvas ui = new Canvas();
		// User_Interface ui = new User_Interface();
				
		ui.setPreferredSize( new Dimension(Width, Height));

 		// creating the main window
		JFrame window = new JFrame(Title);
		window.add(ui);
		window.pack();	// I dont know what this does but the window doesnt load properly without it
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);		// stops users from resizing window
		window.setLocationRelativeTo(null);  // window appears in the center of the screen
		window.setVisible(true);
		
		// draw the menu buttons
		Graphics g1 = ui.getGraphics();
		Main_Menu(g1);

		ui.addMouseListener(new Mouse_Input());
	}
	
	// initialize button parameters so they can be drawn later
	public static Rectangle play_button = new Rectangle(User_Interface.Width / 4, User_Interface.Height/3, 160, 50);
	public static Rectangle help_button = new Rectangle(play_button.x, play_button.y + 100, 160, 50);
	public static Rectangle quit_button = new Rectangle(play_button.x, help_button.y + 100 , 160, 50);

	public static void Main_Menu(Graphics g)
	{
		// casting graphics to use 2d commands (makes rect drawing easier)
		Graphics2D g2d = (Graphics2D) g;

		// image title
		Image title_img = null;
		try {	
			title_img = ImageIO.read(new File("Title.png"));	}
		catch (IOException e){}

    	g2d.drawImage(title_img, 0,0,null);
    	g2d.finalize();
	
		// button list 
		Font button_label = new Font("arial", Font.BOLD, 30);
		g.setFont(button_label);

		g.drawString("Start!", play_button.x + 40, play_button.y + 34);
		g2d.draw(play_button);

		g.drawString("Help", help_button.x + 45, help_button.y + 34);
		g2d.draw(help_button);

		g.drawString("Quit", quit_button.x + 45, quit_button.y + 34);
		g2d.draw(quit_button);

	}

	public static void Help_Window()
	{
		Canvas canvas2 = new Canvas();
		canvas2.setPreferredSize(new Dimension(1200, 1200));

		JFrame help_window = new JFrame("Panic at the Pizzaria");

		help_window.add(canvas2);
		help_window.pack();

		help_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		help_window.setResizable(false);		// stops users from resizing help_window
		help_window.setLocationRelativeTo(null);  // help_window appears in the center of the screen
		help_window.setVisible(true);


		Graphics g1 = canvas2.getGraphics();
		Font help_text = new Font("Serif", Font.BOLD, 50);
		g1.setFont(help_text);
		g1.drawString("",0,0);
		g1.drawString("Help info goes here", 0,0);

	}


private static class Mouse_Input implements MouseListener 
{
	public void mouseClicked(MouseEvent e)
	{
		int clicked_x = e.getX();
		int clicked_y = e.getY();

		// check if a button is clicked
		if(clicked_x >= User_Interface.Width/4 && clicked_x <= User_Interface.Width / 4 + 160)
		{
			if(clicked_y >= User_Interface.Height/3 && clicked_y <= User_Interface.Height/3 + 50)
			{
				//play button pressed

			}
		}

		if(clicked_x >= User_Interface.Width/4 && clicked_x <= User_Interface.Width / 4 + 160)
		{
			if(clicked_y >= User_Interface.Height/3 + 100 && clicked_y <= User_Interface.Height/3 + 150)
			{
				//help button pressed
				Help_Window();
			}
		}

		if(clicked_x >= User_Interface.Width/4 && clicked_x <= User_Interface.Width / 4 + 160)
		{
			if(clicked_y >= User_Interface.Height/3 + 200 && clicked_y <= User_Interface.Height/3 + 250)
			{
				//quit button pressed
				System.exit(1);
			}
		}

	}
	
	// unused implementations
	public void mouseEntered(MouseEvent e)
	{}
	public void mouseExited(MouseEvent e)
	{}
	public void mousePressed(MouseEvent e)
	{}
	public void mouseReleased(MouseEvent e)
	{}
}

};