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
		if(evt.getButton() == MouseEvent.BUTTON1) {
			drawer.addGuard(evt.getX(), evt.getY());
		}
		if(evt.getButton() == MouseEvent.BUTTON3 ) {
			for (int i = drawer.getGuardList().size()-1; i >= 0; i--) {
				if(mouseOverComponent(drawer.getGuardList().get(i), evt.getX(), evt.getY())){
					drawer.removeGuard(drawer.getGuardList().get(i));
				}
			}
		}
	}
	
	private boolean mouseOverComponent(Guard guard, int x, int y) {
		return guard.getX()-15 <= x
			&& guard.getX()+15 >= x
			&& guard.getY()-15 <= y 
			&& guard.getY()+15 >= y;
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

