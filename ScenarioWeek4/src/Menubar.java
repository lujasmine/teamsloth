import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
		JMenuItem open1 = new JMenuItem("Open question for part 1");
		file.add(open1);
		JMenuItem open2 = new JMenuItem("Open question for part 2");
		file.add(open2);
		JMenuItem export = new JMenuItem("Export");
		file.add(export);
		
		export.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int galleryNumber = drawer.getGalleryNumber();
				File file = new File("Question" + galleryNumber + "answer.txt");
			    try {
					file.createNewFile();
				    FileWriter writer = new FileWriter(file); 
				    writer.write("sloth\nrl5qgj5n4mc68qsekeig4j4jfs\n"); 
				    writer.write(galleryNumber+": ");
				    writer.flush();
				    writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		open1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					
					String name = JOptionPane.showInputDialog("Question Number:");
					drawer.drawGallery(Integer.parseInt(name), 1);

				}	catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
		open2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					
					String name = JOptionPane.showInputDialog("Question Number:");
					drawer.drawGallery(Integer.parseInt(name), 2);

				}	catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}
}
