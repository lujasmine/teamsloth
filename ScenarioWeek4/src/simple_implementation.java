

import java.util.*;
import java.io.*;

// point structure with x,y are coordinates
class Point{
	public double x, y;
	//parameterized constructor
	public Point(double _X, double _Y){
		x = _X;
		y = _Y;
	}
	//copy constructor
	public Point(Point p) {
		x = p.x;
		y = p.y;
	}
	//compare operation
	public boolean Equals(Point p) {
		if (Math.abs(this.x - p.x) > 0.00001)
			return false;
		if (Math.abs(this.y - p.y) > 0.00001)
			return false;
		return true;
	}
	//calculate distance to a point
	public double distance(Point p) {
		double dx = x - p.x;
		double dy = y - p.y;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
}

//segment structure
class Segment {
	
	//2 point of a segment is order by x-coordinate 
	public Point p1, p2;
	
	//parameterized constructor
	public Segment(Point A, Point B)
	{
		if (A.x < B.x)
		{
			p1 = A;
			p2 = B;
		}
		else
			if (A.x > B.x)
			{
				p1 = B;
				p2 = A;
			}
			else // same x-coordinate
				if (A.y < B.y)
				{
					p1 = A;
					p2 = B;
				}
				else
				{
					p1 = B;
					p2 = A;
				}
	}
	
	// give the mid point
	public Point MidPoint()
	{
		return new Point((p1.x+p2.x)/2,(p1.y+p2.y)/2);
	}
	
	//given a point that is in line (p1,p2)
	//check if a point is in this segment
	public boolean Contain(Point P) {
		if ((P.x == p1.x && P.y == p1.y) || (P.x == p2.x && P.y == p2.y))
		{
			return true;
		}
		/* contain algorithm for line
		double ax = p2.x - p1.x;
		double ay = p2.y - p1.y;
		double apx = P.x - p1.x;
		double apy = P.y - p2.y;
		if (ax * apy == ay* apx)
			return true;
		else
			return false;
		*/
		if (P.x > p1.x && P.x < p2.x)
			return true;
		return false;
	}
	
	//given a arbitrary point
	//check if that point is inside this segment
	//segment does not contain its end points
	public boolean ContainArbitrary(double _x, double _y) {
		// contain algorithm for line
		double ax = p2.x - p1.x;
		double ay = p2.y - p1.y;
		double apx = _x - p1.x;
		double apy = _y - p1.y;
		// first check if point is in line
		if (ax * apy != ay* apx)
			return false;

		// then check if point is inside segment
		if ((_x == p1.x && _y == p1.y) || (_x == p2.x && _y == p2.y))
		{
			return false;
		}
		if ((_x > p1.x && _x < p2.x) || (_y > p1.y && _y < p2.y))
			return true;
		return false;
	}
	
	//given an arbitrary point
	//check if a point is in this segment
	public boolean ContainArbitrary(Point P) {
		// contain algorithm for line
		double ax = p2.x - p1.x;
		double ay = p2.y - p1.y;
		double apx = P.x - p1.x;
		double apy = P.y - p1.y;
		// first check if point is in line
		if (ax * apy != ay* apx)
			return false;

		// then check if point is inside segment
		if ((P.x == p1.x && P.y == p1.y) || (P.x == p2.x && P.y == p2.y))
		{
			return true;
		}
		if (P.x > p1.x && P.x < p2.x)
			return true;
		if ((P.x == p1.x && P.x == p2.x) && (P.y > p1.y && P.y < p2.y))
			return true;
		return false;
	}
	
	//given a point that is in line (p1,p2)
	//check if that point is inside this segment
	//segment does not contain its end points
	public boolean Contain(double _x, double _y) {
		if ((_x == p1.x && _y == p1.y) || (_x == p2.x && _y == p2.y))
		{
			return false;
		}
		/* contain algorithm for line
		double ax = p2.x - p1.x;
		double ay = p2.y - p1.y;
		double apx = _x - p1.x;
		double apy = _y - p1.y;
		if (ax * apy == ay* apx)
			return true;
		else
			return false;
		*/
		if ((_x > p1.x && _x < p2.x) || (_y > p1.y && _y < p2.y))
			return true;
		return false;
	}
	
	//compare operation
	public boolean Equals(Segment s) {
		if (!this.p1.Equals(s.p1))
			return false;
		if (!this.p2.Equals(s.p2))
			return false;
		return true;
	}
	
	//check if 2 segments have the same direction
	public boolean sameDirection(Segment s) {
		double ax = p2.x - p1.x;
		double ay = p2.y - p1.y;
		double asx = s.p2.x - s.p1.x;
		double asy = s.p2.y - s.p1.y;
		if (ax * asy == ay* asx) 
			return true;
		else
			return false;
	}

	//find a intersection of this segment with another segment
	//check equal before whenever use this method
	public Point InterSection(Segment s) {
		if (this.p1.Equals(s.p1))
			return this.p1;
		if (this.p2.Equals(s.p2))
			return this.p2;
		// last point of this segment is first point of given segment
		if (this.p2.Equals(s.p1))
			return this.p2;
		// first point of this segment is last point of given segment
		if (this.p1.Equals(s.p2))
			return this.p1;
		// find the intersection of 2 line (p1,p2) and (s.p1, s.p2)
		double vx1,vy1,vx2,vy2;
		vx1 = p2.x - p1.x;
		vy1 = p2.y - p1.y;
		vx2 = s.p2.x - s.p1.x;
		vy2 = s.p2.y - s.p1.y;
		double t = (vy1*(s.p1.x-p1.x)-vx1*(s.p1.y-p1.y)) / (vx1*vy2-vx2*vy1);
		int _x = (int)(vx2*t + s.p1.x);
		int _y = (int)(vy2*t + s.p1.y);
		// check if the intersection inside this segment
		if (Contain(_x,_y) && s.Contain(_x, _y))
			return new Point(_x,_y);
		else
			return null;
	}
	
	//find a inside intersection of this segment with another segment
	//check equal before whenever use this method
	public Point InterSectionExceptEdge(Segment s) {
		if (this.p1.Equals(s.p1))
			return null;
		if (this.p2.Equals(s.p2))
			return null;
		// last point of this segment is first point of given segment
		if (this.p2.Equals(s.p1))
			return null;
		// first point of this segment is last point of given segment
		if (this.p1.Equals(s.p2))
			return null;
		// find the intersection of 2 line (p1,p2) and (s.p1, s.p2)
		double vx1,vy1,vx2,vy2;
		vx1 = p2.x - p1.x;
		vy1 = p2.y - p1.y;
		vx2 = s.p2.x - s.p1.x;
		vy2 = s.p2.y - s.p1.y;
		double t = (1.0f) * (vy1*(s.p1.x-p1.x)-vx1*(s.p1.y-p1.y)) / (1.0f * (vx1*vy2-vx2*vy1));
		double _x = vx2*t + s.p1.x;
		double _y = vy2*t + s.p1.y;
		// check if the intersection inside this segment
		if (this.Contain(_x,_y) && s.Contain(_x, _y))
			return new Point(_x,_y);
		else
			return null;
	}
	
	//find a inside intersection of this segment with another segment
	//check equal before whenever use this method
	public Point InterSectionExceptThisEnds(Segment s) {
		if (this.p1.Equals(s.p1))
			return null;
		if (this.p2.Equals(s.p2))
			return null;
		// last point of this segment is first point of given segment
		if (this.p2.Equals(s.p1))
			return null;
		// first point of this segment is last point of given segment
		if (this.p1.Equals(s.p2))
			return null;
		// find the intersection of 2 line (p1,p2) and (s.p1, s.p2)
		double vx1,vy1,vx2,vy2;
		vx1 = p2.x - p1.x;
		vy1 = p2.y - p1.y;
		vx2 = s.p2.x - s.p1.x;
		vy2 = s.p2.y - s.p1.y;
		double t = (1.0f) * (vy1*(s.p1.x-p1.x)-vx1*(s.p1.y-p1.y)) / (1.0f * (vx1*vy2-vx2*vy1));
		double _x = vx2*t + s.p1.x;
		double _y = vy2*t + s.p1.y;
		// (not include end points)
		if (this.isEndPoint(_x,_y))
			return null;
		// check if the intersection inside this segment (not include end points)
		if (this.Contain(_x,_y) && (s.Contain(_x,_y) || s.isEndPoint(_x,_y)))
			return new Point(_x,_y);
		else
			return null;
	}
	
	//find a inside intersection of this segment with another segment
	//check equal before whenever use this method
	public Point InterSectionExceptThatEnds(Segment s) {
		if (this.p1.Equals(s.p1))
			return null;
		if (this.p2.Equals(s.p2))
			return null;
		// last point of this segment is first point of given segment
		if (this.p2.Equals(s.p1))
			return null;
		// first point of this segment is last point of given segment
		if (this.p1.Equals(s.p2))
			return null;
		// find the intersection of 2 line (p1,p2) and (s.p1, s.p2)
		double vx1,vy1,vx2,vy2;
		vx1 = p2.x - p1.x;
		vy1 = p2.y - p1.y;
		vx2 = s.p2.x - s.p1.x;
		vy2 = s.p2.y - s.p1.y;
		double t = (1.0f) * (vy1*(s.p1.x-p1.x)-vx1*(s.p1.y-p1.y)) / (1.0f * (vx1*vy2-vx2*vy1));
		double _x = vx2*t + s.p1.x;
		double _y = vy2*t + s.p1.y;
		// check if the intersection inside that segment and in this (include ends)
		if (s.Contain(_x,_y) && (this.Contain(_x,_y) || this.isEndPoint(_x,_y)))
			return new Point(_x,_y);
		else
			return null;
	}
	
	// check if a point is on the left of this segment or not
	// 0 if point is on line
	// 1 for left
	// -1 for right
	public int isLeft(Point p) {
		double i_isLeft = ((p2.x - p1.x)*(p.y - p1.y) - (p2.y - p1.y)*(p.x - p1.x));
		if (i_isLeft > 0) // p is on the left
			return 1;
		else if (i_isLeft < 0)
			return -1;
		return 0;
	}

	// check if a point is end point of this segment
	public boolean isEndPoint(Point p) {
		if (p1.Equals(p) || p2.Equals(p))
			return true;
		return false;
	}
	
	// check if a point is end point of this segment
	public boolean isEndPoint(double x, double y) {
		Point tmp = new Point(x,y);
		return isEndPoint(tmp);
	}
}

// polygon structure
class Polygon{
	public List<Point> vertices;

	// null constructor
	public Polygon(){
		vertices = new ArrayList<Point>();
	}
	
	// add a vertex
	public void addVertex(double _x, double _y){
		Point p = new Point(_x, _y);
		vertices.add(p);
		
	}
	
	// number of vertices
	public int size() {
		return vertices.size();
	}

	// index of vertice
	public int IndexOf(Point p)
	{
		for (int i=0; i< size(); i++)
			if (vertices.get(i).Equals(p))
				return i;
		return -1;
	}
	
	// check if a point is inside this polygon or not
	public boolean Inside(Point point)  
    {  
        int j = vertices.size() - 1;  
        boolean oddNodes = false;  
  
        for (int i = 0; i < vertices.size(); i++)  
        {  
            if (vertices.get(i).y < point.y && vertices.get(j).y >= point.y ||  
                vertices.get(j).y < point.y && vertices.get(i).y >= point.y)  
            {  
                if (vertices.get(i).x +  
                    (point.y - vertices.get(i).y)/(vertices.get(j).y - vertices.get(i).y)*(vertices.get(j).x - vertices.get(i).x) < point.x)  
                {  
                    oddNodes = !oddNodes;  
                }  
            }  
            j = i;  
        }
  
        return oddNodes;  
    }
	
	// check if a part of the segment, of which 2 end points are the polygon's vertices, is inside this or not
	public boolean Intersect(Segment s) {
		// a triangle does not Intersect any segment of which end points are the triangle's vertices
		//if (size() == 3)
		//	return false;
		// polygon has more than 3 vertices		
		// split the big segment into parts
		Point split_point = null;
		for (int i=0; i< size(); i++)
		{
			Point p1 = vertices.get(i);
			Point p2 = vertices.get((i+1) % size());
			Segment edge = new Segment(p1,p2);
			split_point = s.InterSectionExceptThisEnds(edge);
			if (split_point != null)
				break;
		}
		// if we can split
		if (split_point != null) // then check each part
		{
			boolean first_part = Intersect(new Segment(s.p1,split_point));
			if (first_part == true) // a part intersects means whole segment intersects
				return first_part;
			// if first part doesn't intersect
			// it depends on second one
			boolean second_part = Intersect(new Segment(split_point,s.p2));
			return second_part;
		}
		// cannot split this segment
		else
		{
			boolean result = Cover (s);
			return result;
		}
	}
	
	public boolean Cover(Segment s)
	{
		// if segment is a edge of this polygon
		int p1_pos = this.IndexOf(s.p1);
		int p2_pos = this.IndexOf(s.p2);
		if (p1_pos != -1 && p2_pos != -1)
		{
			int pos_distance = Math.abs(p1_pos - p2_pos);
			if (pos_distance == 1 || pos_distance == size()-1) // adjcent vertices
				return false;
		}
		
		int accuracy = 10;
		double startX = s.p1.x;
		double startY = s.p1.y;
		double splitPartX = (s.p1.x-s.p2.x)/accuracy;
		double splitPartY = (s.p1.y-s.p2.y)/accuracy;
		startX -= splitPartX;
		startY -= splitPartY;
		Point checkPoint = new Point(startX, startY);
		for (int i = 0; i < accuracy-1; i++) {
			if (!this.Inside(checkPoint)) return false;
			startX -= splitPartX;
			startY -= splitPartY;
			checkPoint = new Point(startX, startY);
		}
		
		return true;
		
		/*Point mid = s.MidPoint();
		if (this.Inside(mid))
			return true;
		else
			return false;*/
	}

	// check if a point is one of this polygon vertices
	public boolean isVertex(Point p) {
		for (int i=0; i< vertices.size(); i++)
			if (vertices.get(i).Equals(p))
				return true;
		return false;
	}
}