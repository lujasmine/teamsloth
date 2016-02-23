import java.util.ArrayList;


public class Guard {
	
	private double x;
	private double y;
	private ArrayList<IntersectPoint> intersectList = new ArrayList<IntersectPoint>();

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
	
	
	
}
