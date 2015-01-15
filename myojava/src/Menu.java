import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class Menu extends JPanel implements Runnable
{
	final int MENU_RADIUS = 200;
	final int MENU_BUTTON_DIAMETER = 150;
	final int MENU_CENTRE_LEFT = 683;
	final int MENU_CENTRE_TOP = 384;
	final int MENU_COUNT = 6;
	Thread animator;
	int menuTimer = 1;
	int rotateDirection = 0;
	int deg = 270;
	BufferedImage[] image = new BufferedImage [MENU_COUNT];
	BufferedImage menuArrow;
	String selected = "Brush";
	Font menuFont = new Font ("Calibri", Font.BOLD, 20);
	public Menu()
	{
		setBackground (Color.white);
		try
		{
			File input = new File ("arrow.png");
			menuArrow = ImageIO.read (input);
		}
		catch (IOException e)
		{
			System.out.println ("Error: Image files not found!");
		}
		for (int i = 0 ; i < MENU_COUNT ; i++) //initialize image array
		{
			try
			{
				File input = new File ("menubutton" + i + ".png");
				image [i] = ImageIO.read (input);
			}
			catch (IOException ie)
			{
				System.out.println ("Error: Image files not found!");
			}
		}
	}
	public void run ()
	{
		while (Thread.currentThread () == animator) //loop while animator is current thread
		{
			try
			{
				Thread.sleep (8); // delay for 8 ms
			}
			catch (InterruptedException e)
			{
				System.out.println ("Error: Could not delay!");
			}

			deg = deg + rotateDirection; // increase angle
			if (deg == 360&&rotateDirection==1) // reset to 0 at 360
				deg = 0;
			else if(deg==0&&rotateDirection==-1)
				deg = 360;
			else if (deg == -360 / MENU_COUNT)
				deg = 360 - 360 / MENU_COUNT;

			if (deg % (360 / MENU_COUNT)-30 == 0)
			{
				menuTimer = 1;
				animator = null;
				switch (deg)
				{
				case (270):
					selected = "Brush";
				break;
				case (330):
					selected = "Clear All";
				break;
				case (30):
					selected = "Colors";
				break;
				case (90):
					selected = "Eraser";
				break;
				case (150):
					selected = "Quit";
				break;
				case (210):
					selected = "Pencil";
				break;
				default:
					selected = "Error";
					break;
				}
			}
			repaint ();
		}
	}
	public void update (Graphics g)
	{
		paint (g);
	}

	public boolean canRotate()
	{
		return (menuTimer==1);
	}
	public void paint (Graphics g) throws NumberFormatException
	{
		g.setFont (menuFont);
		for (int i = 0 ; i < MENU_COUNT ; i++)
			g.drawImage (image [i], (int) (MENU_RADIUS * Math.cos (Math.toRadians (deg + i * 360 / MENU_COUNT)) + MENU_CENTRE_LEFT - MENU_BUTTON_DIAMETER / 2), (int) (MENU_RADIUS * Math.sin (Math.toRadians (deg + i * 360 / MENU_COUNT)) + MENU_CENTRE_TOP - MENU_BUTTON_DIAMETER / 2), null);
		g.drawImage (menuArrow, MENU_CENTRE_LEFT - 25, 10, null);
		g.clearRect (MENU_CENTRE_LEFT - 80, MENU_CENTRE_TOP - 20, 160, 40);
		g.drawString (selected, MENU_CENTRE_LEFT - 10, MENU_CENTRE_TOP);
	}
	public void rotateMenu(int dir)
	{
		if (menuTimer==0)
			return;
		menuTimer=0;
		animator = new Thread (this);
		animator.start ();
		rotateDirection = dir;
	}
	public String selected()
	{
		return selected;
	}
}
