import java.awt.geom.Line2D;
import java.util.ArrayList;


public class Node {
	
	private double x;
	private double y;
	private ArrayList<Node> connectedNodes = new ArrayList<Node>();
	private ArrayList<Line2D> nodeEdges = new ArrayList<Line2D>();
	private double angleOne;
	private double angleTwo;
	private boolean visible = false;
	
	public Node (double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		
		this.x = x;
	}
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public ArrayList<Node> getConnectedNodes() {
		return connectedNodes;
	}
	
	public void setConnectedNodes(ArrayList<Node> connectedNodes) {
		this.connectedNodes = connectedNodes;
	}
	
	public void addConnectedNode(Node node) {
		connectedNodes.add(node);
	}

	public double getAngleOne() {
		return angleOne;
	}

	public void setAngleOne(double angleOne) {
		this.angleOne = angleOne;
	}

	public double getAngleTwo() {
		return angleTwo;
	}

	public void setAngleTwo(double angleTwo) {
		this.angleTwo = angleTwo;
	}

	public ArrayList<Line2D> getNodeEdges() {
		return nodeEdges;
	}

	public void setNodeEdges(ArrayList<Line2D> nodeEdges) {
		this.nodeEdges = nodeEdges;
	}
	
	public void addNodeEdge(Line2D line) {
		nodeEdges.add(line);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	
	

	
}
