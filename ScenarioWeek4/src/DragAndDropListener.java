import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;


public class DragAndDropListener implements MouseListener, MouseMotionListener {

	private Drawer drawer;
	
	public DragAndDropListener(Drawer drawer) {
		this.drawer = drawer;
	}

	@Override
	public void mousePressed(MouseEvent evt) {
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
	}

	@Override
	public void mouseDragged(MouseEvent evt) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}
	
	@Override
	public void mouseMoved(MouseEvent evt) {
		drawer.addCoordinates(evt.getX(), evt.getY(), evt.getX(), evt.getY());
	}
}

