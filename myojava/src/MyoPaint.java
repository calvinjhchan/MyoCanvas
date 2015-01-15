import java.awt.*;
import java.awt.event.*;
import com.thalmic.myo.*;
import javax.swing.*;
@SuppressWarnings("serial")
public class MyoPaint extends JFrame
implements MouseListener, KeyListener
{
	static double miny=0.0,minp=1.0,maxp=0.0,maxy=1.0,midz=0.5;
	DataCollector collector=new DataCollector(this);
	Menu menu;
	Drawing drawing;
	Colors colors;
	JPanel lolz;
	boolean mouseDown=false;
	boolean move=true;
	public static void main(String[]arg)
	{
		new MyoPaint();
	}
	public MyoPaint()
	{
		lolz=new JPanel(new CardLayout());
		add(lolz);
		setBackground (Color.white);
		setResizable(false);
		menu = new Menu();
		drawing = new Drawing(menu,this);
		colors=new Colors();
		lolz.add(menu,"menu");
		lolz.add(drawing,"draw");
		lolz.add(colors,"color");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width=screenSize.getWidth();
		double height=screenSize.getHeight();
		setSize(1366,768);
		setVisible(true);
		validate();
		addMouseListener (this);
		addKeyListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			Hub hub = new Hub("com.example.test");
			System.out.println("Attempting to find a Myo...");
			Myo myo = hub.waitForMyo(10000);
			if (myo==null)
				throw new Exception("Unable to find a Myo!");
			System.out.println("Connected to a Myo armband!");
			hub.addListener(collector);
			while(move){
				hub.run(10);
				double changeX=(collector.getYaw()-miny)/(maxy-miny);
				changeX=Math.min(Math.max(0, changeX),1);
				double changeY=(collector.getPitch()-minp)/(maxp-minp);
				changeY=Math.min(Math.max(0, 1-changeY),1);
				double changeZ=collector.getRoll();
				if(menu.isShowing()||colors.isShowing())
				{
					if(changeZ>midz+2)
						rotateMenu(-1);
					else if(changeZ<midz-2)
						rotateMenu(1);
				}
				Robot robot=new Robot();
				robot.mouseMove((int)(width*changeX),(int)(height*changeY));
			}
		} catch (Exception e) {
			System.err.println("Error: ");
			e.printStackTrace();
			System.exit(1);
		}
	}


	public void mouseReleased (MouseEvent e)
	{
		if (e.getButton () == MouseEvent.BUTTON1)
			mouseDown = false;
	}


	public void mousePressed (MouseEvent e)
	{
		if (e.getButton () == MouseEvent.BUTTON1)
			mouseDown=true;
	}


	public void mouseEntered (MouseEvent e)
	{
	}


	public void mouseExited (MouseEvent e)
	{
	}


	public void mouseClicked (MouseEvent e)
	{
		if (e.getButton () == MouseEvent.BUTTON1)
		{
			rotateMenu(-1);
		}
		else if (e.getButton () == MouseEvent.BUTTON3)
		{
			rotateMenu(1);
		}
		if (e.getButton () == MouseEvent.BUTTON2)
		{
			openMenu();
		}
	}
	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_LEFT) {
			miny = collector.getYaw();
			maxp = collector.getPitch();
			System.out.println("miny="+miny);
			System.out.println("maxp="+maxp);
		}

		if (key == KeyEvent.VK_RIGHT) {
			maxy = collector.getYaw();
			minp = collector.getPitch();
			System.out.println("maxy="+maxy);
			System.out.println("minp="+minp);
		}
		if(key==KeyEvent.VK_Q)
			System.exit(0);
		if(key==KeyEvent.VK_UP)
		{
			midz=collector.getRoll();
			System.out.println("midz="+midz);
		}
	}
	public void keyReleased(KeyEvent e) {

	}
	public void keyTyped(KeyEvent e) {

	}
	public void openMenu()
	{
		if(menu.isShowing()&&menu.canRotate())
		{
			if(menu.selected().equals("Quit"))
				System.exit(0);
			else if(menu.selected().equals("Colors"))
				change("color");
			else
				change("draw");
		}
		else if(colors.isShowing())
		{
			if(colors.selected().equals("Black"))
				drawing.set=Color.black;
			else if(colors.selected().equals("Pink"))
				drawing.set=Color.pink;
			else if(colors.selected().equals("Orange"))
				drawing.set=Color.orange;
			else if(colors.selected().equals("Yellow"))
				drawing.set=Color.yellow;
			else if(colors.selected().equals("Purple"))
				drawing.set=Color.magenta;
			else if(colors.selected().equals("Blue"))
				drawing.set=Color.blue;
			else if(colors.selected().equals("Green"))
				drawing.set=Color.green;
			else if(colors.selected().equals("Red"))
				drawing.set=Color.red;
			change("menu");
		}
		else
		{
			change("menu");
		}
	}
	public void change(String dir)
	{
		CardLayout cl = (CardLayout)(lolz.getLayout());
		cl.show(lolz, dir);
		repaint();
	}
	public boolean getButtonState()
	{
		return mouseDown;
	}
	public void rotateMenu(int dir)
	{
		if(menu.isShowing())
		{
			if(menu.canRotate())
			{
				menu.rotateMenu(dir);
			}
		}
		else if(colors.isShowing())
		{
			if(colors.canRotate())
			{
				colors.rotateMenu(dir);
			}
		}
	}
}
