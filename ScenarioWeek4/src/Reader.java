import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Reader {
	
	final int numberOfQuestions1 = 31;
	final int numberOfQuestions2 = 21;
	String[] vertexList = new String[numberOfQuestions1]; //Coordinate list for each question - both parts
	int[] vertexListSize = new int[numberOfQuestions1]; //Number of points in each question - both parts
	String[] guardList = new String[numberOfQuestions2]; //Guard list for part 2

	public void readData(int part) {
		int eachQuestion = 0; //Counter value for each question
		try {
			for (String line : Files.readAllLines(Paths.get("Part" + part + "Questions.txt"))) {
				for (String question : line.split("\\d:")) { //Question contains all the coordinates for each question
					if(question.contains("(")){ //Get rid of null questions
						question = question.replaceAll("\\s+", ""); //Get rid of whitespace
						//System.out.println("question:" + question);
						String[] strSplit = question.split(";", 2);
						String vertices = strSplit[0];
						String guards = "";
						if(part==2){ //Guard list only exists in part 2
							guards = strSplit[1];
							//System.out.println("g:" + guards); 
						}
						int questionSize = 0; 
						for (int i = 0; i < question.length(); i++){ //Count number of points in question
							char c = question.charAt(i);        
							if(c == '('){
								questionSize = questionSize+1;
							}
						}
						eachQuestion++;
						vertexList[eachQuestion] = question; //Put the currently read list into array
						vertexListSize[eachQuestion] = questionSize; //Put the currently read list size into array
						guardList[eachQuestion] = guards;
						System.out.println("Question(" + eachQuestion+ "):" + question);
						System.out.println("Vertices(" + eachQuestion + "):" + vertices);
						System.out.println("Guards(" + eachQuestion + "):" + guards);
						//System.out.println("Size for question " + eachQuestion + ":" + coordListSize[eachQuestion]);
					}
				}	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public double[][] getData(int number) {
		double[][] points = new double[vertexListSize[number]][2];
		int counter = 0;
		int counterTwo = 0;
		
		for (String pair : vertexList[number].split("\\(|\\,|\\)")) { //Get each value
			if(pair.matches("\\-?(\\d+|\\d+\\.\\d+)")==true){ //Get rid of null values and only accept numbers or decimals
				double coord = Double.parseDouble(pair); //Convert the string coordinate into a double
				points[counter][counterTwo] = coord;
				counterTwo++;
				if (counterTwo == 2) {
					counter++;
					counterTwo = 0;
				}
				//System.out.println("Coord:" + coord);
			}
		}
		
		return points;
	}
	
}