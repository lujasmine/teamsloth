import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.lang.Object;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.DefaultHighlighter;

public class Drawer extends JPanel {

	private JTextArea initialTextArea;
	private DragAndDropListener listener;
	double[][] points = {{10, 5}, {9, 5}, {9, 7}, {8, 7}, {8, 5}, {6, 5}, {6, 7}, {5, 7}, {5, 3}, {4, 3}, {4, 5}, {3, 5}, {3, 3}, {2, 3}, {2, 7}, {1, 7}, {1, 6}, {0, 6}, {0, 10}, {-1, 10}, {-1, 9}, {-3, 9}, {-3, 8}, {-1, 8}, {-1, 6}, {-4, 6}, {-4, 5}, {-3, 5}, {-3, 4}, {-7, 4}, {-7, 3}, {-6, 3}, {-6, 2}, {-11, 2}, {-11, 1}, {-10, 1}, {-10, -1}, {-9, -1}, {-9, 1}, {-8, 1}, {-8, -1}, {-7, -1}, {-7, 1}, {-6, 1}, {-6, -2}, {-5, -2}, {-5, 3}, {-3, 3}, {-3, 0}, {-2, 0}, {-2, 5}, {-1, 5}, {-1, 3}, {0, 3}, {0, 5}, {1, 5}, {1, 1}, {0, 1}, {0, 0}, {1, 0}, {1, -1}, {-2, -1}, {-2, -2}, {-1, -2}, {-1, -3}, {-3, -3}, {-3, -4}, {-1, -4}, {-1, -5}, {0, -5}, {0, -2}, {1, -2}, {1, -3}, {2, -3}, {2, -2}, {3, -2}, {3, -4}, {4, -4}, {4, -2}, {5, -2}, {5, -1}, {2, -1}, {2, 0}, {3, 0}, {3, 1}, {2, 1}, {2, 2}, {7, 2}, {7, 3}, {6, 3}, {6, 4}, {10, 4}};
	
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Path2D path = new Path2D.Double();

	path.moveTo(points[0][0]*40, points[0][1]*40);
    for(int i = 1; i < points.length; i++) {
       path.lineTo(points[i][0]*40, points[i][1]*40);
    }
    path.closePath();
    
    Graphics2D g2d = (Graphics2D) g.create();
    
    AffineTransform old = g2d.getTransform();

    // update graphics object with the inverted y-transform
    g2d.translate(0, getHeight() - 1);
    g2d.translate(460, -300);
    g2d.scale(1, -1);
    
    g2d.fill(path);
    g2d.dispose();
    g2d.setTransform(old);
  }
  
  public void addCoordinates(double x, double y) {
	  initialTextArea = new JTextArea(0, 20);
	  initialTextArea.setEditable(false);
	  initialTextArea.setOpaque(false);
	  initialTextArea.setFont(new Font("Arial", Font.BOLD, 12));
	  //initialTextArea.setBounds(x-(initialTextArea.getWidth()/2)+13,y-(initialTextArea.getHeight()/2)+5,100,20);
	  
	  initialTextArea.setText("");
		
	  try {
		  initialTextArea.getHighlighter().addHighlight(0,initialTextArea.getText().length(),new DefaultHighlighter.DefaultHighlightPainter(Color.white));
	  } catch (Exception e) {}
  }
  
  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.setTitle("DrawPoly");
    frame.setSize(900, 800);
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    Container contentPane = frame.getContentPane();
    contentPane.add(new Drawer());

    frame.show();
  }
}
