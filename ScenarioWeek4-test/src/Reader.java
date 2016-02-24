import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Reader {
	
	final int numberOfQuestions = 31;
	String[] coordList = new String[numberOfQuestions]; //Coordinate list for each question
	int[] coordListSize = new int[numberOfQuestions]; //Number of points in each question

	public void readData() {
		int eachQuestion = 0; //Counter value for each question
		try {
			for (String line : Files.readAllLines(Paths.get("Part1Questions.txt"))) {
				for (String question : line.split("\\d:")) { //Question contains all the coordinates for each question
					if(question.contains("(")){ //Get rid of null questions
						question = question.replaceAll("\\s+", ""); //Get rid of whitespace
						int questionSize = 0; 
						for (int i = 0; i < question.length(); i++){ //Count number of points in question
							char c = question.charAt(i);        
							if(c == '('){
								questionSize = questionSize+1;
							}
						}
						eachQuestion++;
						coordList[eachQuestion] = question; //Put the currently read list into array
						coordListSize[eachQuestion] = questionSize; //Put the currently read list size into array
						//System.out.println("Size for question " + eachQuestion + ":" + coordListSize[eachQuestion]);
					}
				}	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public double[][] getData(int number) {
		double[][] points = new double[coordListSize[number]][2];
		int counter = 0;
		int counterTwo = 0;
		
		for (String pair : coordList[number].split("\\(|\\,|\\)")) { //Get each value
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