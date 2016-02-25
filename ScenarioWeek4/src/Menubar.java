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
	private int part = 0;
	
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
		JMenuItem export1 = new JMenuItem("Export for part 1");
		file.add(export1);
		JMenuItem export2 = new JMenuItem("Export for part 2");
		file.add(export2);
		
		export1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(part == 1){
					int galleryNumber = drawer.getGalleryNumber();
					int guardListSize = drawer.getGuardListSize();
					if(guardListSize>0){
						File file = new File("Part1Question" + galleryNumber + "answer.txt");
					    try {
							file.createNewFile();
						    FileWriter writer = new FileWriter(file); 
						    writer.write("sloth\nrl5qgj5n4mc68qsekeig4j4jfs\n"); 
						    writer.write(galleryNumber+": ");
						    for(int eachGuard = 0; eachGuard < guardListSize; eachGuard++){
						    	double x = drawer.getGuardX(eachGuard);
						    	double y = drawer.getGuardY(eachGuard);
						    	if(eachGuard == guardListSize-1){
						    		writer.write("(" + x + ", " + y + ")");
						    	}
						    	else{
						    		writer.write("(" + x + ", " + y + "),");
						    	}
						    }
						    writer.flush();
						    writer.close();
						    JOptionPane.showMessageDialog(f,  "Export successful as 'Part1Question" + galleryNumber + "answer.txt'.");
					    } catch (IOException e) {
					    	e.printStackTrace();
					    }
					}
					else{
						JOptionPane.showMessageDialog(f, "No guards have been input!", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				else{
					JOptionPane.showMessageDialog(f,
						    "You have not opened a part 1 question.",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				}
			}		
		});
		
		export2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(part==2){
					int galleryNumber = drawer.getGalleryNumber();
					File file = new File("Part2Question" + galleryNumber + "answer.txt");
					try {
						file.createNewFile();
						FileWriter writer = new FileWriter(file);
						writer.write("sloth\nrl5qgj5n4mc68qsekeig4j4jfs\n"); 
					    writer.write(galleryNumber+": ");
					    writer.flush();
					    writer.close();
					    JOptionPane.showMessageDialog(f,  "Export successful as 'Part2Question" + galleryNumber + "answer.txt'.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else{
					JOptionPane.showMessageDialog(f,
						    "You have not opened a part 2 question.",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		open1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					
					String name = JOptionPane.showInputDialog("Question Number:");
					part = 1;
					drawer.drawGallery(Integer.parseInt(name), part);

				}	catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
		open2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					
					String name = JOptionPane.showInputDialog("Question Number:");
					part = 2;
					drawer.drawGallery(Integer.parseInt(name), part);

				}	catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}
}
