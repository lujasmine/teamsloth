import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;


public class Algorithm {
	ArrayList<Line2D.Double> edges = new ArrayList<Line2D.Double>();
	ArrayList<Node> nodes = new ArrayList<Node>();
	ArrayList<Point2D.Double> guardPoints = new ArrayList<Point2D.Double>();
	Polygon gallery = new Polygon();
	
	double galleryPoints[][];
	
	public Algorithm(double[][] galleryPoints) {
		this.galleryPoints = galleryPoints;
		
		for (int i = 0; i < galleryPoints.length; i++) {
			gallery.addVertex(galleryPoints[i][0], galleryPoints[i][1]);
		}
		
		createNodesAndEdges();
		//calculateNodeAngleRanges();
		createConnections();
		placeGuards();
		
		//runTests();
		
		for (Point2D point : guardPoints) {
			System.out.println(point);
		}
		System.out.println("Number of Guards: " + guardPoints.size());
	}
	
	public ArrayList<Point2D.Double> getGuardPoints() {
		return guardPoints;
	}
	
	public void createNodesAndEdges() {
		Line2D.Double tempLine;
		for (int i = 0; i < galleryPoints.length; i++)
			nodes.add(new Node(galleryPoints[i][0], galleryPoints[i][1]));
		for (int i = 0; i < nodes.size(); i++) {
			if (i == nodes.size()-1) {
				tempLine = new Line2D.Double(nodes.get(i).getX(), nodes.get(i).getY(), nodes.get(0).getX(), nodes.get(0).getY());
				nodes.get(i).addConnectedNode(nodes.get(0));
				nodes.get(0).addConnectedNode(nodes.get(i));
			} else {
				tempLine = new Line2D.Double(nodes.get(i).getX(), nodes.get(i).getY(), nodes.get(i+1).getX(), nodes.get(i+1).getY());
				nodes.get(i).addConnectedNode(nodes.get(i+1));
				nodes.get(i+1).addConnectedNode(nodes.get(i));
			}
			edges.add(tempLine);
		}
	}
	
	public void runTests() {
		//Segment testLine = new Segment(new Point(nodes.get(24).getX(), nodes.get(24).getY()), new Point(nodes.get(26).getX(), nodes.get(26).getY()));
		
		//if (gallery.Cover(testLine)) System.out.println("covers");
		//if (checkPossibleConnection(nodes.get(24), nodes.get(25))) System.out.println("HI");
		//System.out.println(nodes.get(1).getX() + " " + nodes.get(1).getY());
		for (Node node : nodes.get(1).getConnectedNodes()) {
			System.out.println(node.getX() + " " + node.getY());
		}
		
		//System.out.println(nodes.get(1).getConnectedNodes().size());
		//System.out.println(nodes.get(35).getConnectedNodes().size());
		
		//System.out.println((Math.toDegrees(Math.atan2(6-3.47, -3.43+6.08))+360)%360);
		
		//angleRangeCheckTest(nodes.get(24), nodes.get(24).getAngleOne(), nodes.get(24).getAngleTwo());
	}
	
	/*public void calculateNodeAngleRanges() {
		for (Node node: nodes) {
			double angle1 = Math.atan2(node.getConnectedNodes().get(0).getY() - node.getY(), node.getConnectedNodes().get(0).getX() - node.getX());
			//if (angle1 < 0) angle1 = angle1 + 2*Math.PI;
			//if (angle1  == 0) angle1  = 2*Math.PI;
			angle1 = (Math.toDegrees(angle1)+360)%360;
			double angle2 = Math.atan2(node.getConnectedNodes().get(1).getY() - node.getY(), node.getConnectedNodes().get(1).getX() - node.getX());
			//if (angle2 < 0) angle2 = angle2 + 2*Math.PI;
			//if (angle2 == 0) angle2 = 2*Math.PI;
			angle2 = (Math.toDegrees(angle2)+360)%360;
			
			if (angle1 > angle2) {
				if (angleRangeCheck(node, angle1, angle2)) {
					//angle2 = angle2 + 360;
					node.setAngleOne(angle2);
					node.setAngleTwo(angle1);
				} else {
					//angle1 = angle1 + 360;
					node.setAngleOne(angle1);
					node.setAngleTwo(angle2);
				}
			} else {
				if (angleRangeCheck(node, angle2, angle1)) {
					//angle1 = angle1 + 360;
					node.setAngleOne(angle1);
					node.setAngleTwo(angle2);
				} else {
					//angle2 = angle2 + 360;
					node.setAngleOne(angle2);
					node.setAngleTwo(angle1);
				}
			}
		}
	}
	
	public boolean angleRangeCheck(Node node, double angle1, double angle2) {
		angle1 = (Math.toRadians(angle1));
		angle2 = (Math.toRadians(angle2));
		double tempAngle = (angle1-angle2)/2;
		Line2D.Double tempLine = new Line2D.Double(node.getX(), node.getY(), node.getX() + Math.sin(tempAngle) * 1000, node.getY() + Math.cos(tempAngle) * 1000);
		int intersectCounter = 0;
		for (int i = 0; i < edges.size(); i++) {
			if (testIntersection(tempLine, edges.get(i))) {
				if (!nodeIntersect(tempLine, edges.get(i))) intersectCounter++;
			}
		}
		if (intersectCounter%2 == 0) return true;
		else return false;
	}
	
	public boolean angleRangeCheckTest(Node node, double angle1, double angle2) {
		angle1 = (Math.toRadians(angle1));
		angle2 = (Math.toRadians(angle2));
		double tempAngle = (angle1-angle2)/2;
		Line2D.Double tempLine = new Line2D.Double(node.getX(), node.getY(), node.getX() + Math.sin(tempAngle) * 1000, node.getY() + Math.cos(tempAngle) * 1000);
		int intersectCounter = 0;
		for (int i = 0; i < edges.size(); i++) {
			if (testIntersection(tempLine, edges.get(i))) {
				System.out.println(getIntersectionPoint(tempLine, edges.get(i)));
				if (!nodeIntersect(tempLine, edges.get(i))) intersectCounter++;
			}
		}
		System.out.println(intersectCounter);
		if (intersectCounter%2 == 0) return true;
		else return false;
	}
	
	public boolean nodeIntersect(Line2D.Double line1, Line2D.Double line2) {
		for (int j = 0; j < nodes.size(); j++) {
			Point2D.Double tempPoint = getIntersectionPoint(line1, line2);
			//System.out.println(tempPoint);
			if (precisionCheck(tempPoint, nodes.get(j)) || Double.isNaN(tempPoint.getX())) {
				return true;
			}
		}
		return false;
	}*/
		
		
	
	public void createConnections() {
		for (int i = 0; i < nodes.size(); i++) {
			for (int j = 0; j < nodes.size(); j++) {
				if (!nodes.get(i).getConnectedNodes().contains(nodes.get(j)) && nodes.get(i) != nodes.get(j)) {
					if (checkPossibleConnection(nodes.get(i), nodes.get(j))) {
						nodes.get(i).addConnectedNode(nodes.get(j));
						nodes.get(j).addConnectedNode(nodes.get(i));
					}
				}
			}
		}
	}
	
	public boolean checkPossibleConnection(Node node1, Node node2) {
		Segment tempPoint = new Segment(new Point(node1.getX(), node1.getY()), new Point(node2.getX(), node2.getY()));
		if (!gallery.Cover(tempPoint)) return false;
		
		//Line2D.Double tempLine = new Line2D.Double(node1.getX(), node1.getY(), node2.getX(), node2.getY());
		
		/*double tempAngle = Math.atan2(node2.getY() - node1.getY(), node2.getX() - node1.getX());
		if (tempAngle < 0) tempAngle = tempAngle + 2*Math.PI;
		if (tempAngle == 0) tempAngle = 2*Math.PI;
		tempAngle = (Math.toDegrees(tempAngle)+360)%360;
		System.out.println(tempAngle + " " + node1.getAngleOne() + " " + node1.getAngleTwo());
		if (tempAngle > node1.getAngleOne() || tempAngle < node1.getAngleTwo()) return false;*/

		/*int intersectCounter = 0;
		for (int i = 0; i < edges.size(); i++) {
			if (testIntersection(tempLine, edges.get(i))) {
				if (!checkIfNodeIntersect(tempLine, edges.get(i), node1, node2)) {
					System.out.println(getIntersectionPoint(tempLine, edges.get(i)));
					intersectCounter++;
				}
			}
		}
		System.out.println(intersectCounter);
		if (intersectCounter > 0) return false;*/
		
		return true;
	}
	
	/*public boolean checkIfParallel(Line2D line, Double angle) {
		double angle1 = Math.atan2(line.getX1() - line.getX2(), line.getY1() - line.getY2());
		if (angle1 < 0) angle1 = angle1 + 2*Math.PI;
		double angle2 = Math.atan2(line.getX2() - line.getX1(), line.getY2() - line.getY1());
		if (angle2 < 0) angle2 = angle2 + 2*Math.PI;
		
		if (angle == angle1 || angle == angle2) return true;
		else return false;
	}
	
	public boolean checkAngle(Node node1, Node node2) {
		double tempAngle = Math.atan2(node2.getY() - node1.getY(), node2.getX() - node1.getX());
		//if (tempAngle < 0) tempAngle = tempAngle + 2*Math.PI;
		tempAngle = (Math.toDegrees(tempAngle)+360)%360;
		if (tempAngle > node1.getAngleOne() || tempAngle < node1.getAngleTwo()) return false;
		else return true;
	}
		
	
	public boolean checkIfNodeIntersect(Line2D.Double line1, Line2D.Double line2, Node node1, Node node2) {
		for (int j = 0; j < nodes.size(); j++) {
			Point2D.Double tempPoint = getIntersectionPoint(line1, line2);
			//System.out.println(tempPoint);
			if (precisionCheck(tempPoint, nodes.get(j)) || Double.isNaN(tempPoint.getX())) {
				if (nodes.get(j) == node1) return true;
				if (nodes.get(j) == node2) return true;
				if (checkAngle(nodes.get(j), node2)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean precisionCheck(Point2D.Double point, Node node) {
		double precision = 0.0000000001; 
		if (point.getX() <= node.getX()+precision && point.getX() >= node.getX()-precision 
				&& point.getY() <= node.getY()+precision && point.getY() >= node.getY()-precision)
			return true;
		
		return false;
	}
		
	public boolean testIntersection(Line2D line1, Line2D line2) {
		return line1.intersectsLine(line2);
	}*/
	
	public void placeGuards() {
		/*for (Node node : nodes) {
			if (!node.isVisible()) {
				Node tempNode = node;
				int biggestSize = node.getConnectedNodes().size();
				for (int i = 0; i < node.getConnectedNodes().size(); i++) {
					if (node.getConnectedNodes().get(i).getConnectedNodes().size() > biggestSize && !node.getConnectedNodes().get(i).isVisible()) {
						biggestSize = node.getConnectedNodes().get(i).getConnectedNodes().size();
						tempNode = node.getConnectedNodes().get(i);
					}
				}
				tempNode.setVisible(true);
				System.out.println(tempNode.getX() + " " + tempNode.getY());
				for (int i = 0; i < tempNode.getConnectedNodes().size(); i++) {
					tempNode.getConnectedNodes().get(i).setVisible(true);
				}
				
				guardPoints.add(new Point2D.Double(tempNode.getX(), tempNode.getY()));
			}
		}*/
		Node tempNode = null;
		int biggestSize = 0;
		
		for (Node node : nodes) {
			if (node.getConnectedNodes().size() > biggestSize && !node.isVisible()) {
				biggestSize = node.getConnectedNodes().size();
				tempNode = node;
			}
		}
		
		if (tempNode != null) {
			tempNode.setVisible(true);
			for (int i = 0; i < tempNode.getConnectedNodes().size(); i++) {
				tempNode.getConnectedNodes().get(i).setVisible(true);
			}
			guardPoints.add(new Point2D.Double(tempNode.getX(), tempNode.getY()));
			
			placeGuards();
		}
		
	}
					
				
	
	/*public Point2D.Double getIntersectionPoint(Line2D line1, Line2D line2) {

        final double x1,y1, x2,y2, x3,y3, x4,y4;
        x1 = line1.getX1(); y1 = line1.getY1(); x2 = line1.getX2(); y2 = line1.getY2();
        x3 = line2.getX1(); y3 = line2.getY1(); x4 = line2.getX2(); y4 = line2.getY2();
        final double x = ((x2 - x1)*(x3*y4 - x4*y3) - (x4 - x3)*(x1*y2 - x2*y1)) /
                ((x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4));
        final double y = ((y3 - y4)*(x1*y2 - x2*y1) - (y1 - y2)*(x3*y4 - x4*y3)) /
                ((x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4));

        return new Point2D.Double(x, y);
    }*/
	
	 /*public Point2D.Double getIntersectionPoint(Line2D.Double line1, Line2D.Double line2) {
		      double px = line1.getX1(),
		            py = line1.getY1(),
		            rx = line1.getX2()-px,
		            ry = line1.getY2()-py;
		      double qx = line2.getX1(),
		            qy = line2.getY1(),
		            sx = line2.getX2()-qx,
		            sy = line2.getY2()-qy;

		      double det = sx*ry - sy*rx;
		       double z = (sx*(qy-py)+sy*(px-qx))/det;
		        return new Point2D.Double((double)(px+z*rx), (double)(py+z*ry));
	}*/
}
