import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Reader {
	
	final int numberOfQuestions1 = 31;
	final int numberOfQuestions2 = 21;
	String[] vertexList1 = new String[numberOfQuestions1]; //Vertex list for each question for part 1
	int[] vertexListSize1 = new int[numberOfQuestions1]; //Number of points in each question for part 1
	String[] vertexList2 = new String[numberOfQuestions2]; //Vertex list for each question for part 2
	int[] vertexListSize2 = new int[numberOfQuestions2]; //Number of points in each question for part 2
	String[] guardList = new String[numberOfQuestions2]; //Guard list for part 2
	Drawer drawer;
	
	public Reader(Drawer drawer) {
		this.drawer = drawer;
	}

	public void readData(int part) {
		int eachQuestion = 0; //Counter value for each question
		try {
			System.out.println("Currently reading file for part: " + part);
			for (String line : Files.readAllLines(Paths.get("Part" + part + "Questions.txt"))) {
				for (String question : line.split("\\d:")) { //Question contains all the coordinates for each question
					if(question.contains("(")){ //Get rid of null questions
						question = question.replaceAll("\\s+", ""); //Get rid of whitespace
						String[] strSplit = question.split(";", 2);
						String vertices = strSplit[0];
						String guards = "";
						if(part==2){ //Guard list only exists in part 2
							guards = strSplit[1];
						}
						int questionSize = 0; 
						for (int i = 0; i < vertices.length(); i++){ //Count number of points in question
							char c = vertices.charAt(i);        
							if(c == '('){
								questionSize = questionSize+1;
							}
						}
						eachQuestion++;
						if(part==1){
							vertexList1[eachQuestion] = vertices; //Put the currently read list into array
							vertexListSize1[eachQuestion] = questionSize; //Put the currently read list size into array
						}
						else{
							vertexList2[eachQuestion] = vertices;
							vertexListSize2[eachQuestion] = questionSize;
							guardList[eachQuestion] = guards;
						}
						//System.out.println("Question(" + eachQuestion+ "):" + question);
						//System.out.println("Vertices(" + eachQuestion + "):" + vertices);
						//System.out.println("Guards(" + eachQuestion + "):" + guards);
						//System.out.println("Size for question " + eachQuestion + ":" + coordListSize[eachQuestion]);
					}
				}	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public double[][] getData(int questionNumber, int part) {
		System.out.println("Reading question: " + questionNumber + " for part: " + part);
		int counter = 0;
		int counterTwo = 0;
		
		if(part==1){ //Get data from part 1
			double[][] points = new double[vertexListSize1[questionNumber]][2];
			for (String pair : vertexList1[questionNumber].split("\\(|\\,|\\)")) { //Get each value
				if(pair.matches("\\-?(\\d+|\\d+\\.\\d+)")==true){ //Get rid of null values and only accept numbers or decimals
					double coord = Double.parseDouble(pair); //Convert the string coordinate into a double
					points[counter][counterTwo] = coord;
					counterTwo++;
					if (counterTwo == 2) {
						counter++;
						counterTwo = 0;
					}
				}
			}
			return points;
		}
		else{ //Get data from part 2
			double[][] points = new double[vertexListSize2[questionNumber]][2];
			for (String pair : vertexList2[questionNumber].split("\\(|\\,|\\)")) { //Get each value
				if(pair.matches("\\-?(\\d+|\\d+\\.\\d+)")==true){ //Get rid of null values and only accept numbers or decimals
					double coord = Double.parseDouble(pair); //Convert the string coordinate into a double
					points[counter][counterTwo] = coord;
					counterTwo++;
					if (counterTwo == 2) {
						counter++;
						counterTwo = 0;
					}
				}
			}
		return points;
		}
	}
	
	public void getGuard(int questionNumber){
		double x=0;
		double y=0;
		int counter=0;
		System.out.println("Guard list: " + guardList[questionNumber]);
		for (String pair : guardList[questionNumber].split("\\(|\\,|\\)")) {
			if(pair.matches("\\-?(\\d+|\\d+\\.\\d+)")==true){
				if(counter % 2 ==0){
					counter++;
					x = Double.parseDouble(pair); //Get x coord of guard
				}
				else{
					counter++;
					y = Double.parseDouble(pair); //Get y coord of guard
				}
				if(counter%2 == 0){
					System.out.println("Guard added at x:" + x + "y:" + y);
					//(y*drawer.getScale()+30)+drawer.getFrameHeight()
					drawer.addGuard((x-drawer.getSmallestX())*drawer.getScale(),(y-drawer.getSmallestY())*drawer.getScale()); //Uncomment this when bug is fixed!
				}
			}
		}
	}
	
}