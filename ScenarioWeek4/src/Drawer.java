import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.DefaultHighlighter;

public class Drawer extends JPanel {

	private Path2D.Double path = new Path2D.Double();
	private JFrame f;
	private JTextArea initialTextArea;
	private DragAndDropListener listener;
	private Reader reader;
	private Intersection intersection;
	private Menubar menubar;
	
	private double scale = 30;
	private double translateX;
	private double translateY;
	
	private double biggestX;
	private double smallestX;
	private double biggestY;
	private double smallestY;
	
	private double transformX;
	private double transformY;
	
	private ArrayList<Guard> guardList = new ArrayList<Guard>();
	private ArrayList<IntersectPoint> intersectList = new ArrayList<IntersectPoint>();
	private ArrayList<Line2D.Double> edges = new ArrayList<Line2D.Double>();
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private ArrayList<Point2D.Double> guardPoints = new ArrayList<Point2D.Double>();
	
	private int galleryNumber;
	private Polygon galleryPolygon = new Polygon();
	
	//Line2D testLine = new Line2D.Double(100,100,200,200);
	Line2D.Double testLine = new Line2D.Double(100, 100,  100+ Math.cos(0) * 500, 100+ Math.sin(0) * 500);
	
	private double[][] galleryPoints;
	
	private Algorithm algorithm;
	
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
	
	public void createEdges() {
		Line2D.Double tempLine;
		for (int i = 0; i < galleryPoints.length; i++) {
			galleryPolygon.addVertex(galleryPoints[i][0], galleryPoints[i][1]);
			nodes.add(new Node(galleryPoints[i][0], galleryPoints[i][1]));
			if (i == galleryPoints.length-1) {
				tempLine = new Line2D.Double(galleryPoints[i][0], galleryPoints[i][1], galleryPoints[0][0], galleryPoints[0][1]);
			} else {
				tempLine = new Line2D.Double(galleryPoints[i][0], galleryPoints[i][1], galleryPoints[i+1][0], galleryPoints[i+1][1]);
			}
			edges.add(tempLine);
		}
	}
	
	public void drawLine(Guard guard) {
		Line2D.Double tempLine;
		double angle;
		
		//for (int i = 0; i < guardList.size(); i++) {
			intersectList.clear();
			for (int j = 0; j < galleryPoints.length; j++) {
				angle = Math.atan2(galleryPoints[j][1]-guard.getY(),galleryPoints[j][0]-guard.getX());
				double precision = 0.00001;
				tempLine = new Line2D.Double(guard.getX(), guard.getY(), guard.getX() + Math.cos(angle-precision) * 2000, guard.getY() + Math.sin(angle-precision) * 2000);
				testAndCreateIntersection(guard, guard.getX(), guard.getY(), path, tempLine, angle-precision);
				
				tempLine = new Line2D.Double(guard.getX(), guard.getY(), galleryPoints[j][0], galleryPoints[j][1]);
				testAndCreateIntersection(guard, guard.getX(), guard.getY(), path, tempLine, angle);
				
				tempLine = new Line2D.Double(guard.getX(), guard.getY(), guard.getX() + Math.cos(angle+precision) * 2000, guard.getY() + Math.sin(angle+precision) * 2000);
				testAndCreateIntersection(guard, guard.getX(), guard.getY(), path, tempLine, angle+precision);
			}
			
			/*for (int i = 0; i < nodes.size(); i++) {
				if (guard.getX() == nodes.get(i).getX() && guard.getY() == nodes.get(i).getY()) {
					double biggestAngle = 0;
					double smallestAngle = 0;
					for (int k = 0; k < guard.getIntersectList().size(); k++) {
						if (guard.getIntersectList().get(k).getAngle() > biggestAngle) {
							biggestAngle = guard.getIntersectList().get(k).getAngle();
						}
						if (guard.getIntersectList().get(k).getAngle() < smallestAngle) {
							biggestAngle = guard.getIntersectList().get(k).getAngle();
						}
					}
					intersectList.add(new IntersectPoint(guard.getX(), guard.getY(), (biggestAngle-smallestAngle)/2));
					break;
				}
			}*/
			guard.setIntersectList(intersectList);
			/*Collections.sort(guard.getIntersectList(), new AngleComparator());
			for (int i = 0; i < nodes.size(); i++) {
				if (guard.getX() == nodes.get(i).getX() && guard.getY() == nodes.get(i).getY()) {
					intersectList.add(new IntersectPoint(guard.getX(), guard.getY(), (guard.getIntersectList().get(0).getAngle()+0.001)));
					break;
				}
			}*/
			Collections.sort(guard.getIntersectList(), new AngleComparator());
			createLineOfSight(guard);
		//}
	}
	
	public void testAndCreateIntersection(Guard guard, double x, double y, Path2D.Double path, Line2D.Double tempLine, double angle) {
		ArrayList<Point2D.Double> tempList = new ArrayList<Point2D.Double>();
		tempList.clear();
		//guard.addToLineList(tempLine);
		
		for (int i = 0; i < edges.size(); i++) {
			Point2D.Double tempPoint = null;
			if (tempLine.intersectsLine(edges.get(i))) {
				tempPoint = intersection.getIntersection(tempLine, edges.get(i));
				if (tempPoint != null && !Double.isNaN(tempPoint.getX()) && !Double.isNaN(tempPoint.getY()) && (tempPoint.getX() != guard.getX() || tempPoint.getY() != guard.getY())) {
					//System.out.println(tempPoint);
					tempList.add(tempPoint);
				}
			}
		}
		
		/*try {
			tempList = intersection.getIntersections(path, tempLine);
		} catch (Exception e) {
			System.out.println("Error getting intersections");
		}*/
		
		double minimum = 0;
		double distance = 0;
		Point2D.Double tempPoint = null;
		for (Point2D.Double intersectPoint : tempList) {
			if (intersectPoint != null) {
				//System.out.println(intersectPoint);
				distance = calculateDistance(tempLine.getX1(), tempLine.getY1(), intersectPoint.getX(), intersectPoint.getY());
				if (minimum == 0 || minimum > distance) {
					minimum = distance;
					tempPoint = intersectPoint;
				}
			}
		}
		
		try {
			tempLine = new Line2D.Double(x, y, tempPoint.getX(), tempPoint.getY());
			Segment tempSeg = new Segment(new Point(tempLine.getX1(), tempLine.getY1()), new Point(tempLine.getX2(), tempLine.getY2()));
			
			for (int i = 0; i < edges.size(); i++) {
				Segment edgeSeg = new Segment(new Point(edges.get(i).getX1(), edges.get(i).getY1()), new Point(edges.get(i).getX2(), edges.get(i).getY2()));
				if (tempSeg.sameDirection(edgeSeg)) {
					if ((tempLine.getX1() == edges.get(i).getX1() && tempLine.getY1() == edges.get(i).getY1() && 
							tempLine.getX2() == edges.get(i).getX2() && tempLine.getY2() == edges.get(i).getY2()) ||
							(tempLine.getX1() == edges.get(i).getX2() && tempLine.getY1() == edges.get(i).getY2() && 
							tempLine.getX2() == edges.get(i).getX1() && tempLine.getY2() == edges.get(i).getY1())) {
						if (angle < 0) angle += 2*Math.PI;
						intersectList.add(new IntersectPoint(tempPoint.getX(), tempPoint.getY(), angle));
						guard.addToLineList(tempLine);
						return;
					}
				}	
			}
			if (insidePolygon(tempSeg)) {
				if (angle < 0) angle += 2*Math.PI;
				intersectList.add(new IntersectPoint(tempPoint.getX(), tempPoint.getY(), angle));
				guard.addToLineList(tempLine);
				return;
			} 
		} catch (Exception e) {}
		
		for (int i = 0; i < intersectList.size(); i++) {
			if (intersectList.get(i).getX() == guard.getX() && intersectList.get(i).getY() == guard.getY()) {
				return;
			}
		}
		
		intersectList.add(new IntersectPoint(guard.getX(), guard.getY(), angle));
		
	}
	
	public boolean isNode(Point2D.Double tempPoint) {
		for (int k = 0; k < nodes.size(); k++) {
			if (tempPoint.getX() == nodes.get(k).getX() && tempPoint.getY() == nodes.get(k).getY()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean insidePolygon(Segment s) {
		Point2D.Double tempPoint = null;
		double intersectCounter = 0;
		Line2D.Double tempLine;
		
		double precision = 1;
		int accuracy = 10;
		double startX = s.p1.x;
		double startY = s.p1.y;
		double splitPartX = (s.p1.x-s.p2.x)/accuracy;
		double splitPartY = (s.p1.y-s.p2.y)/accuracy;
		startX -= splitPartX;
		startY -= splitPartY;
		for (int i = 0; i < accuracy-1; i++) {
			tempPoint = null;
			intersectCounter = 0;
			tempLine = new Line2D.Double(startX, startY, startX + Math.cos(0) * 2000, startY+ Math.sin(0) * 2000);
			for (int j = 0; j < edges.size(); j++) {
				if (tempLine.intersectsLine(edges.get(j))) {
					tempPoint = intersection.getIntersection(tempLine, edges.get(j));
					if (!Double.isNaN(tempPoint.getX())) {
						intersectCounter++;
						//System.out.println(tempPoint.getX() + " " + tempPoint.getY() + " : " + intersectCounter);
					}
				}
				if (tempPoint != null && !Double.isNaN(tempPoint.getX())) {
					if (isNode(tempPoint)) {
						intersectCounter = 1;
						break;
					}
					tempPoint = null;
				}
			}
		
			if (intersectCounter%2 == 0) {
				//System.out.println(startX + " " + startY + " : " + intersectCounter);
				return false;
			}
			
			tempPoint = null;
			intersectCounter = 0;
			tempLine = new Line2D.Double(startX, startY, startX + Math.cos(90) * 2000, startY+ Math.sin(90) * 2000);
			for (int j = 0; j < edges.size(); j++) {
				if (tempLine.intersectsLine(edges.get(j))) {
					tempPoint = intersection.getIntersection(tempLine, edges.get(j));
					if (!Double.isNaN(tempPoint.getX())) {
						intersectCounter++;
						//System.out.println(tempPoint.getX() + " " + tempPoint.getY() + " : " + intersectCounter);
					}
				}
				if (tempPoint != null && !Double.isNaN(tempPoint.getX())) {
					if (isNode(tempPoint)) {
						intersectCounter = 1;
						break;
					}
					tempPoint = null;
				}
			}
		
			if (intersectCounter%2 == 0) {
				//System.out.println(startX + " " + startY + " : " + intersectCounter);
				return false;
			}
			
			startX -= splitPartX;
			startY -= splitPartY;
		}
		
		return true;
	}
		
	
	public void createLineOfSight(Guard guard) {
		Path2D.Double tempPath = new Path2D.Double();
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
		galleryPolygon = new Polygon();
		edges.clear();
		guardList.clear();
		
		galleryNumber = questionNumber;
		galleryPoints = reader.getData(questionNumber, part);
		f.setTitle("Art Gallery Problem - Part: " + part + " Question: " + questionNumber);
		setPolygon(galleryPoints);
		
		createEdges();	  

		removeAll();
	    repaint();
	    if (part == 1) {
	    	algorithm = new Algorithm(galleryPoints);
	    	guardPoints = algorithm.getGuardPoints();
	    	
	    	for (int i = 0; i < guardPoints.size(); i++) {
	    		addGuard(guardPoints.get(i).getX(), guardPoints.get(i).getY());
	    	}
	    }
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
		
		//path.moveTo(points[0][0]*scale, points[0][1]*scale);
	    for(int i = 1; i < points.length; i++) {
	       //path.lineTo(points[i][0]*scale, points[i][1]*scale);
	       
	       if (points[i][0] > biggestX) biggestX = (int) points[i][0];
	       if (points[i][0] < smallestX) smallestX = (int) points[i][0];
	       if (points[i][1] > biggestY) biggestY = (int) points[i][1];
	       if (points[i][1] < smallestY) smallestY = (int) points[i][1];
	    }
	    
	    //path.closePath();
	    
	    translateX = (biggestX - smallestX)*scale;
	    translateY = (biggestY - smallestY)*scale;
	    //smallestX = smallestX-1;
	    //smallestY = smallestY-1;
	    	    
	    //f.setSize(-200, 300);
	    
	    if (translateX > 1100 || translateY > 550) {
	    	scale--;
	    	setPolygon(galleryPoints);
	    	return;
	    }
	    
	    translateX = 0;
	    translateY = 0;
	    
	    for(int i = 0; i < galleryPoints.length; i++) {
	    	galleryPoints[i][0] = (galleryPoints[i][0])*scale;
			galleryPoints[i][1] = (galleryPoints[i][1])*scale;
	    
	    }
	    
	    for(int i = 0; i < galleryPoints.length; i++) {
	    	while(galleryPoints[i][0] < 0) {
	    		for(int j = 0; j < galleryPoints.length; j++) {
	    			galleryPoints[j][0]++;
	    		}
	    		translateX++;
	    	}
	    	while(galleryPoints[i][1] < 0) {
	    		for(int j = 0; j < galleryPoints.length; j++) {
	    			galleryPoints[j][1]++;
	    		}
    			translateY++;
	    	}
	    }
	    
	    translateX += 50;
	    translateY += 50;
	    
	    for(int i = 0; i < galleryPoints.length; i++) {
	    	galleryPoints[i][0] += 50;
			galleryPoints[i][1] += 50;
	    	if (i == 0) path.moveTo((galleryPoints[i][0]), (galleryPoints[i][1]));
	    	else path.lineTo((galleryPoints[i][0]), (galleryPoints[i][1]));
	    	if (galleryPoints[i][0] > biggestX) biggestX =  galleryPoints[i][0];
		    if (galleryPoints[i][0] < smallestX) smallestX = galleryPoints[i][0];
		    if (galleryPoints[i][1] > biggestY) biggestY =  galleryPoints[i][1];
		    if (galleryPoints[i][1] < smallestY) smallestY =  galleryPoints[i][1];
	    }
	    
	    path.closePath();
	    
	    f.setSize((int)biggestX+100, (int)biggestY+100);  
	}
	  
	public void addCoordinates(int x, int y, double xCo, double yCo) {
		xCo = (xCo-translateX)/scale;
		yCo = (yCo-translateY)/scale;
	  
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
		Guard guard = new Guard(x, y);
		//System.out.println("x:" + guard.getX() + ", " + " y: "+ guard.getY());
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
	
	public double getTranslateX() {
		return translateX;
	}
	
	public double getTranslateY() {
		return translateY;
	}
	
	public int getGuardListSize() {
		return guardList.size();
	}
	
	public double getGuardX(int guard) {
		return (guardList.get(guard).getX()-translateX)/scale;
	}
	
	public double getGuardY(int guard) {
		return (guardList.get(guard).getY()-translateY)/scale;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
			
		Graphics2D g2d = (Graphics2D) g.create();
		
		AffineTransform old = g2d.getTransform();
		
		g2d.setBackground(Color.WHITE);
		
		// update graphics object with the inverted y-transform
		
		
		/*g2d.translate(0, getHeight());
		g2d.translate((-smallestX*scale)+45, (smallestY*scale)-30);
		g2d.scale(1, -1);*/
		
		g2d.setColor(Color.BLACK);
		g2d.fill(path);
		
		g2d.setColor(Color.RED);
		for (Guard guard : guardList) {
			if (guard.getLineOfSight() != null) {
				g2d.fill(guard.getLineOfSight());
			}
		}
		
		/*g2d.setColor(Color.YELLOW);
		for (Guard guard : guardList) {
			if (guard.getLineList() != null) {
				ArrayList<Line2D> tempList = guard.getLineList();
				for (Line2D line : tempList) {
					g2d.draw(line);
				}
			}
		}*/
		
		g2d.setColor(Color.BLUE);
		for (Guard guard : guardList) {
			g2d.fillOval((int)(guard.getX()-(int)scale/4), (int)(guard.getY()-(int)scale/4), (int)scale/2, (int)scale/2);
		}
		
		//g2d.draw(testLine);
		
		g2d.setTransform(old);
		g2d.dispose();
		
		
		add(initialTextArea);
	}
	  
    public static void main(String[] args) {
		new Drawer();
	}
}
