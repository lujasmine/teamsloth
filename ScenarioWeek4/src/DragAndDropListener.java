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
			int x = evt.getPoint().x;
			int y = evt.getPoint().y;
			
			for (int i = initialComponents.size()-1; i >= 0; i--) {
				Component component = initialComponents.get(i);
				
				if(mouseOverComponent(component,x,y)){
					checkAndSetSelectedComponent(selectedComponent);
					simulator.setMouseOverInitial(true);
					simulator.removeAll();
					simulator.repaint();
					simulator.addNameText(x,y, component);
					break;
				} else {
					if (simulator.getMouseOverInitial()) {
						checkAndSetSelectedComponent(selectedComponent);
						simulator.removeAll();
						simulator.repaint();
						simulator.setMouseOverInitial(false);
						simulator.addNameText(x,y, null);
					}
				}
			}
	}
}

