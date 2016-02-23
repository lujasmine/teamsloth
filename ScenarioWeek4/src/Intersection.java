import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Intersection {
    	public ArrayList<Point2D> getIntersections(Path2D path, Line2D line) throws Exception {

        PathIterator pathIt = path.getPathIterator(null); //Getting an iterator along the polygon path
        double[] coords = new double[6]; //Double array with length 6 needed by iterator
        double[] firstCoords = new double[2]; //First point (needed for closing polygon path)
        double[] lastCoords = new double[2]; //Previously visited point
        ArrayList<Point2D> intersections = new ArrayList<Point2D>(); //List to hold found intersections
        pathIt.currentSegment(firstCoords); //Getting the first coordinate pair
        lastCoords[0] = firstCoords[0]; //Priming the previous coordinate pair
        lastCoords[1] = firstCoords[1];
        pathIt.next();
        while(!pathIt.isDone()) {
            final int type = pathIt.currentSegment(coords);
            switch(type) {
                case PathIterator.SEG_LINETO : {
                    Line2D currentLine = new Line2D.Double(lastCoords[0], lastCoords[1], coords[0], coords[1]);
                    if(currentLine.intersectsLine(line))
                        intersections.add(getIntersection(currentLine, line));
                    lastCoords[0] = coords[0];
                    lastCoords[1] = coords[1];
                    break;
                }
                case PathIterator.SEG_CLOSE : {
                    final Line2D.Double currentLine = new Line2D.Double(coords[0], coords[1], firstCoords[0], firstCoords[1]);
                    if(currentLine.intersectsLine(line))
                        intersections.add(getIntersection(currentLine, line));
                    break;
                }
                default : {
                    throw new Exception("Unsupported PathIterator segment type.");
                }
            }
            pathIt.next();
        }
        return intersections;

    }

    public Point2D getIntersection(Line2D line1, Line2D line2) {

        final double x1,y1, x2,y2, x3,y3, x4,y4;
        x1 = line1.getX1(); y1 = line1.getY1(); x2 = line1.getX2(); y2 = line1.getY2();
        x3 = line2.getX1(); y3 = line2.getY1(); x4 = line2.getX2(); y4 = line2.getY2();
        final double x = ((x2 - x1)*(x3*y4 - x4*y3) - (x4 - x3)*(x1*y2 - x2*y1)) /
                ((x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4));
        final double y = ((y3 - y4)*(x1*y2 - x2*y1) - (y1 - y2)*(x3*y4 - x4*y3)) /
                ((x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4));

        return new Point2D.Double(x, y);

    }
}
