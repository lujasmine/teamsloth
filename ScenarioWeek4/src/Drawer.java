import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.lang.Object;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.DefaultHighlighter;

public class Drawer extends JPanel {

	private Path2D path;
	private JFrame f;
	private JTextArea initialTextArea;
	private DragAndDropListener listener;
	private int scale = 40;
	private int translateX;
	private int translateY;
	double[][] points = {{10, 5}, {9, 5}, {9, 7}, {8, 7}, {8, 5}, {6, 5}, {6, 7}, {5, 7}, {5, 3}, {4, 3}, {4, 5}, {3, 5}, {3, 3}, {2, 3}, {2, 7}, {1, 7}, {1, 6}, {0, 6}, {0, 10}, {-1, 10}, {-1, 9}, {-3, 9}, {-3, 8}, {-1, 8}, {-1, 6}, {-4, 6}, {-4, 5}, {-3, 5}, {-3, 4}, {-7, 4}, {-7, 3}, {-6, 3}, {-6, 2}, {-11, 2}, {-11, 1}, {-10, 1}, {-10, -1}, {-9, -1}, {-9, 1}, {-8, 1}, {-8, -1}, {-7, -1}, {-7, 1}, {-6, 1}, {-6, -2}, {-5, -2}, {-5, 3}, {-3, 3}, {-3, 0}, {-2, 0}, {-2, 5}, {-1, 5}, {-1, 3}, {0, 3}, {0, 5}, {1, 5}, {1, 1}, {0, 1}, {0, 0}, {1, 0}, {1, -1}, {-2, -1}, {-2, -2}, {-1, -2}, {-1, -3}, {-3, -3}, {-3, -4}, {-1, -4}, {-1, -5}, {0, -5}, {0, -2}, {1, -2}, {1, -3}, {2, -3}, {2, -2}, {3, -2}, {3, -4}, {4, -4}, {4, -2}, {5, -2}, {5, -1}, {2, -1}, {2, 0}, {3, 0}, {3, 1}, {2, 1}, {2, 2}, {7, 2}, {7, 3}, {6, 3}, {6, 4}, {10, 4}};
	
	private int biggestX;
	private int smallestX;
	private int biggestY;
	private int smallestY;
	//double[][] points = {{5,5},{5,10},{10,10},{10,5}};
	public Drawer() {
		listener = new DragAndDropListener(this);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(this);
	    f.setTitle("Shots on Jamie");
	    f.setSize(900, 800);
	    //f.setPreferredSize(new Dimension(WIDTH, HEIGHT));
	    
	    addCoordinates(0,0,0,0);
	    f.setVisible(true);
	    
	    path = new Path2D.Double();
		
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
	
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    
	    Graphics2D g2d = (Graphics2D) g.create();
	    
	    AffineTransform old = g2d.getTransform();
	
	    // update graphics object with the inverted y-transform
	    
	    g2d.translate(0, getHeight());
	    g2d.translate((-smallestX*scale)+30, (smallestY*scale)-30);
	    g2d.scale(1, -1);
	    
	    g2d.fill(path);
	    g2d.dispose();
	    g2d.setTransform(old);
	    
	    add(initialTextArea);
	}
	  
	  public void addCoordinates(int x, int y, double xCo, double yCo) {
		  xCo = (xCo+(smallestX*scale)-30)/scale;
		  yCo = -((yCo-(smallestY*scale)+30-(getHeight()))/scale);
		  
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
	  
	  public static void main(String[] args) {
		  new Drawer();
	  }
}
