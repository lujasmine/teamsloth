import java.util.Comparator;


public class AngleComparator implements Comparator<IntersectPoint> {
	@Override
	public int compare(IntersectPoint ip1, IntersectPoint ip2) {
	    if (ip1.getAngle() > ip2.getAngle()) {
	    	return -1;
	    } else if (ip1.getAngle() < ip2.getAngle()) {
	        return 1;
	    }
	    return 0;
	}
	
}
