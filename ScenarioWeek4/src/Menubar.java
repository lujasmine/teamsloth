import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


public class Menubar {
	
	JFrame f;
	Drawer drawer;
	
	public Menubar(JFrame f, Drawer drawer) {
		this.f = f;
		this.drawer = drawer;
	}
	
	public void addMenuBar() {
		
		JMenuBar menuBar = new JMenuBar();
		f.setJMenuBar(menuBar);
		
		JMenu file = new JMenu("File");
		menuBar.add(file);
		JMenuItem open = new JMenuItem("Open");
		file.add(open);
		JMenuItem export = new JMenuItem("Export");
		file.add(export);
		
		export.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File file = new File(Drawer.galleryNumber + ".txt");
			    try {
					file.createNewFile();
				    FileWriter writer = new FileWriter(file); 
				    writer.write("sloth\nrl5qgj5n4mc68qsekeig4j4jfs\n"); 
				    writer.flush();
				    writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					
					String name = JOptionPane.showInputDialog("Number:");
					
					drawer.drawGallery(Integer.parseInt(name));

				}	catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}
}
