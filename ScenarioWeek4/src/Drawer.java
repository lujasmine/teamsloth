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
import java.util.ArrayList;
import java.util.Collections;

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
	private Intersection intersection;
	private Menubar menubar;
	
	private int scale = 30;
	private int translateX;
	private int translateY;
	
	private int biggestX;
	private int smallestX;
	private int biggestY;
	private int smallestY;
	
	private double transformX;
	private double transformY;
	
	private ArrayList<Guard> guardList = new ArrayList<Guard>();
	private ArrayList<Line2D> lineList = new ArrayList<Line2D>();
	private ArrayList<IntersectPoint> intersectList = new ArrayList<IntersectPoint>();
	
	private int galleryNumber;
	
	Line2D testLine = new Line2D.Double(100,100,200,200);
	
	private double[][] galleryPoints;
	
	public Drawer() {
		listener = new DragAndDropListener(this);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		
		reader = new Reader(this);
		reader.readData(1);
		reader.readData(2);
		intersection = new Intersection();
		
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(this);
	    f.setTitle("Shots on Jaz");
	    f.setSize(900, 800);
	    //f.setResizable(false);
	    //f.setPreferredSize(new Dimension(WIDTH, HEIGHT));
	    
	    menubar = new Menubar(f, this);
	    menubar.addMenuBar();
	    
	    addCoordinates(0,0,0,0);
	    f.setVisible(true);
	    
	    //setPolygon();
	    
	    this.setLayout(null);
	    
	    removeAll();
	    repaint();
	}
	
	public void drawLine(Guard guard) {
		Line2D tempLine;
		double angle;
		
		//for (int i = 0; i < guardList.size(); i++) {
			intersectList.clear();
			lineList.clear();
			for (int j = 0; j < galleryPoints.length; j++) {
				angle = Math.atan2(galleryPoints[j][1]-guard.getY(),galleryPoints[j][0]-guard.getX());
				tempLine = new Line2D.Double(guard.getX(), guard.getY(), guard.getX() + Math.cos(angle-0.0000001) * 500, guard.getY() + Math.sin(angle-0.0000001) * 500);
				testAndCreateIntersection(guard, guard.getX(), guard.getY(), path, tempLine, angle-0.0000001);
				
				tempLine = new Line2D.Double(guard.getX(), guard.getY(), galleryPoints[j][0], galleryPoints[j][1]);
				testAndCreateIntersection(guard, guard.getX(), guard.getY(), path, tempLine, angle);
				
				tempLine = new Line2D.Double(guard.getX(), guard.getY(), guard.getX() + Math.cos(angle+0.0000001) * 500, guard.getY() + Math.sin(angle+0.0000001) * 500);
				testAndCreateIntersection(guard, guard.getX(), guard.getY(), path, tempLine, angle+0.0000001);
			}
			
			guard.setIntersectList(intersectList);
			createLineOfSight(guard);
		//}
	}
	
	public void testAndCreateIntersection(Guard guard, double x, double y, Path2D path, Line2D tempLine, double angle) {
		ArrayList<Point2D> tempList = new ArrayList<Point2D>();
		tempList.clear();
		
		try {
			tempList = intersection.getIntersections(path, tempLine);
		} catch (Exception e) {
			System.out.println("Error getting intersections");
		}
		
		double minimum = 0;
		double distance = 0;
		Point2D tempPoint = null;
		for (Point2D points : tempList) {
			distance = calculateDistance(tempLine.getX1(), tempLine.getY1(), points.getX(), points.getY());
			if (minimum == 0 || minimum > distance) {
				minimum = distance;
				tempPoint = points;
			}
		}
		
		try {
			intersectList.add(new IntersectPoint(tempPoint.getX(), tempPoint.getY(), angle));
			tempLine = new Line2D.Double(x, y, tempPoint.getX(), tempPoint.getY());
			guard.addToLineList(tempLine);
		} catch (Exception e) {}
	}
	
	public void createLineOfSight(Guard guard) {
		Path2D tempPath = new Path2D.Double();
		Collections.sort(guard.getIntersectList(), new AngleComparator());
		tempPath.moveTo(guard.getIntersectList().get(0).getX(), guard.getIntersectList().get(0).getY());
		for (int j = 1; j < guard.getIntersectList().size(); j++) {
			tempPath.lineTo(guard.getIntersectList().get(j).getX(), guard.getIntersectList().get(j).getY());
		}
		tempPath.closePath();
		guard.setLineOfSight(tempPath);
	}
			
	
	public double calculateDistance(double x1, double y1, double x2, double y2) {
		double distance = Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
		
		return distance;
	}
	
	public void drawGallery(int questionNumber, int part) {
		scale = 60;
		galleryNumber = questionNumber;
		galleryPoints = reader.getData(questionNumber, part);
		f.setTitle("Shots on Jaz - Part: " + part + " Question: " + questionNumber);
		setPolygon(galleryPoints);
		
		guardList.clear();
		removeAll();
	    repaint();
		if (part == 2) reader.getGuard(questionNumber);
		
	    
	}
	
	public int getGalleryNumber(){
		return galleryNumber;
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
	    	    
	    f.setSize(translateX+(-smallestX*scale)+120, translateY+120);
	    
	    if (translateX > 1000 || translateY > 750) {
	    	scale--;
	    	setPolygon(galleryPoints);
	    	return;
	    }
	    
	    for(int i = 0; i < galleryPoints.length; i++) {
	    	for(int j = 0; j < galleryPoints[i].length; j++) {
	    		galleryPoints[i][j] = galleryPoints[i][j]*scale;
	    	}
	    }
	    
	    
	}
	  
	public void addCoordinates(int x, int y, double xCo, double yCo) {
		xCo = (xCo+(smallestX*scale)-45)/scale;
		yCo = -((yCo-(smallestY*scale)+30-(getHeight()))/scale);
	  
		initialTextArea = new JTextArea(0, 20);
		initialTextArea.setEditable(false);
		initialTextArea.setOpaque(false);
		initialTextArea.setFont(new Font("Arial", Font.BOLD, 12));
		initialTextArea.setBounds(x-(initialTextArea.getWidth()/2)+13,y-(initialTextArea.getHeight()/2)+5,200,20);
  
		initialTextArea.setText("X: " + xCo + ", Y: " + yCo);
		
		try {
			initialTextArea.getHighlighter().addHighlight(0,initialTextArea.getText().length(),new DefaultHighlighter.DefaultHighlightPainter(Color.white));
		} catch (Exception e) {}
	  
		removeAll();
		repaint();
	}
	
	public void addGuard(double x, double y) {
		Guard guard = new Guard((x-((-smallestX*scale)+45)), -(y-(getHeight()+(smallestY*scale)-30)));
		System.out.println("x:" + guard.getX() + ", " + " y: "+ guard.getY());
		guardList.add(guard);
		
		drawLine(guard);
		
		removeAll();
		revalidate();
		repaint();
	}
	
	public void removeGuard(Guard guard) {
		guardList.remove(guard);
		
		removeAll();
		repaint();
	}
	
	public ArrayList<Guard> getGuardList() {
		return guardList;
	}
	
	public double getTransformX() {
		return transformX;
	}
	
	public double getTransformY() {
		return transformY;
	}
	
	public double getScale() {
		return scale;
	}
	
	public int getFrameHeight() {
		return getHeight();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
			
		Graphics2D g2d = (Graphics2D) g.create();
		
		AffineTransform old = g2d.getTransform();
		
		g2d.setBackground(Color.WHITE);
		
		// update graphics object with the inverted y-transform
		
		
		g2d.translate(0, getHeight());
		g2d.translate((-smallestX*scale)+45, (smallestY*scale)-30);
		g2d.scale(1, -1);
		
		g2d.setColor(Color.BLACK);
		g2d.fill(path);
		
		g2d.setColor(Color.RED);
		for (Guard guard : guardList) {
			if (guard.getLineOfSight() != null) {
				g2d.fill(guard.getLineOfSight());
			}
		}
		
		g2d.setColor(Color.YELLOW);
		for (Guard guard : guardList) {
			if (guard.getLineList() != null) {
			ArrayList<Line2D> tempList = guard.getLineList();
			for (Line2D line : tempList) {
				g2d.draw(line);
			}
			}
		}
		
		g2d.setColor(Color.BLUE);
		for (Guard guard : guardList) {
			g2d.fillOval((int)(guard.getX()-scale/10), (int)(guard.getY()-scale/10), scale/5, scale/5);
		}
		
		g2d.setTransform(old);
		g2d.dispose();
		
		
		add(initialTextArea);
	    transformX = (-smallestX*scale)+45;
	    transformY = (getHeight()+(smallestY*scale)-30);
	}
	  
    public static void main(String[] args) {
		new Drawer();
	}
}
