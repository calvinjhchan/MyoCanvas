import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class Drawing extends JPanel implements Runnable
{
	Menu menu;
	MyoPaint parent;
	double prepointx=-1.0;
	double prepointy=-1.0;
	BufferedImage image = new BufferedImage(1366, 768,BufferedImage.TYPE_INT_ARGB);
	Color set=Color.BLACK;
	public Drawing(Menu m,MyoPaint p)
	{
		menu=m;
		parent=p;
		Thread animator = new Thread (this);
		animator.start ();
		setBackground (Color.white);
	}
	public void update(Graphics g)
	{
		paint(g);
	}
	public void paint (Graphics g) throws NumberFormatException
	{
		double pointx=MouseInfo.getPointerInfo().getLocation().getX()-10.0-parent.getLocation().getX();
		double pointy=MouseInfo.getPointerInfo().getLocation().getY()-40.0-parent.getLocation().getY();
		if(prepointx!=-1.0)
		{
			Graphics2D g2 =image.createGraphics();
			g2.setColor(set);
			g.drawImage(image,0,0,null);
			if (menu.selected().equals("Brush")&&parent.getButtonState())
			{
				g2.setStroke(new BasicStroke(3));
				g2.drawLine((int)prepointx,(int)prepointy,(int)(pointx),(int)(pointy));
			}
			else if (menu.selected().equals("Pencil")&&parent.getButtonState())
			{
				g2.setStroke(new BasicStroke(1));
				g2.drawLine((int)prepointx,(int)prepointy,(int)(MouseInfo.getPointerInfo().getLocation().getX()-10.0),(int)(MouseInfo.getPointerInfo().getLocation().getY()-40.0));
			}
			else if(menu.selected().equals("Eraser")&&parent.getButtonState())
			{
				g2.setStroke(new BasicStroke(10));
				g2.setColor(Color.white);
				g2.drawLine((int)prepointx,(int)prepointy,(int)(MouseInfo.getPointerInfo().getLocation().getX()-10.0),(int)(MouseInfo.getPointerInfo().getLocation().getY()-40.0));
			}
			else if(menu.selected().equals("Clear All")&&parent.getButtonState())
			{
				g2.setColor(Color.white);
				g2.fillRect(0, 0, 1366, 768);
			}
			
		}
		prepointx=pointx;
		prepointy=pointy;
	}
	public void run()
	{
		while(true)
			repaint();
	}
}
