import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.DefaultHighlighter;

public class Drawer extends JPanel {

	private Path2D path = new Path2D.Double();
	private JFrame f;
	private JTextArea initialTextArea;
	private DragAndDropListener listener;
	private Reader reader;
	
	private int scale = 15;
	private int translateX;
	private int translateY;
	
	private int biggestX;
	private int smallestX;
	private int biggestY;
	private int smallestY;
	
	public Drawer() {
		listener = new DragAndDropListener(this);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		
		reader = new Reader();
		reader.readData();
		
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(this);
	    f.setTitle("Shots on Jamie");
	    f.setSize(900, 800);
	    f.setResizable(false);
	    //f.setPreferredSize(new Dimension(WIDTH, HEIGHT));
	    
	    addMenuBar();
	    
	    addCoordinates(0,0,0,0);
	    f.setVisible(true);
	    
	    //setPolygon();
	    
	    this.setLayout(null);
	    
	    removeAll();
	    repaint();
	}
	
	public void addMenuBar() {
		
		JMenuBar menuBar = new JMenuBar();
		f.setJMenuBar(menuBar);
		
		JMenu file = new JMenu("File");
		menuBar.add(file);
		JMenuItem open = new JMenuItem("Open");
		file.add(open);
		
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					
					String name = JOptionPane.showInputDialog("Number:");
					
					drawGallery(Integer.parseInt(name));

				}	catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}
	
	public void drawGallery(int number) {
		double[][] points;
		
		points = reader.getData(number);
		setPolygon(points);
	}
	
	public void setPolygon(double[][] points) {
		path.reset();
		biggestX = 0;
		smallestX = 0;
		biggestY = 0;
		smallestY = 0;
		
		path.moveTo(points[0][0]*scale, points[0][1]*scale);
	    for(int i = 1; i < points.length; i++) {
	       path.lineTo(points[i][0]*scale, points[i][1]*scale);
	       
	       if (points[i][0] > biggestX) biggestX = (int) points[i][0];
	       if (points[i][0] < smallestX) smallestX = (int) points[i][0];
	       if (points[i][1] > biggestY) biggestY = (int) points[i][1];
	       if (points[i][1] < smallestY) smallestY = (int) points[i][1];
	       
	    }
	    
	    path.closePath();
	    
	    translateX = (biggestX - smallestX)*scale;
	    translateY = (biggestY - smallestY)*scale;
	    
	    f.setSize(translateX+100, translateY+100);
	    
	    removeAll();
	    repaint();
	    
	}
	  
	public void addCoordinates(int x, int y, double xCo, double yCo) {
		xCo = (xCo+(smallestX*scale)-30)/scale;
		yCo = -((yCo-(smallestY*scale)+15-(getHeight()))/scale);
	  
		initialTextArea = new JTextArea(0, 20);
		initialTextArea.setEditable(false);
		initialTextArea.setOpaque(false);
		initialTextArea.setFont(new Font("Arial", Font.BOLD, 12));
		initialTextArea.setBounds(x-(initialTextArea.getWidth()/2)+13,y-(initialTextArea.getHeight()/2)+5,100,20);
  
		initialTextArea.setText("X: " + xCo + ", Y: " + yCo);
		
		try {
			initialTextArea.getHighlighter().addHighlight(0,initialTextArea.getText().length(),new DefaultHighlighter.DefaultHighlightPainter(Color.white));
		} catch (Exception e) {}
	  
		removeAll();
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
			
		Graphics2D g2d = (Graphics2D) g.create();
		
		AffineTransform old = g2d.getTransform();
		
		// update graphics object with the inverted y-transform
		
		g2d.translate(0, getHeight());
		g2d.translate((-smallestX*scale)+30, (smallestY*scale)-15);
		g2d.scale(1, -1);
		
		g2d.setColor(Color.BLACK);
		g2d.fill(path);
		
		g2d.dispose();
		g2d.setTransform(old);
		
		add(initialTextArea);
	}
	  
    public static void main(String[] args) {
		new Drawer();
	}
}
