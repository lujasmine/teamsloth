import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;


public class Guard {
	
	private double x;
	private double y;
	private ArrayList<IntersectPoint> intersectList = new ArrayList<IntersectPoint>();
	private ArrayList<Line2D> lineList = new ArrayList<Line2D>();
	private Path2D lineOfSight;

	public Guard(double x, double y) {
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

	public ArrayList<IntersectPoint> getIntersectList() {
		return intersectList;
	}

	public void setIntersectList(ArrayList<IntersectPoint> intersectList) {
		this.intersectList = intersectList;
	}

	public Path2D getLineOfSight() {
		return lineOfSight;
	}

	public void setLineOfSight(Path2D lineOfSight) {
		this.lineOfSight = lineOfSight;
	}

	public ArrayList<Line2D> getLineList() {
		return lineList;
	}

	public void setLineList(ArrayList<Line2D> lineList) {
		this.lineList = lineList;
	}
	
	public void addToLineList(Line2D line) {
		lineList.add(line);
	}
	
	
	
}
