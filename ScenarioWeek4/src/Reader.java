import java.nio.file.Files;
import java.nio.file.Paths;

public static void readData() {
	  final int numberOfQuestions = 30;
	  String[] coordList = new String[numberOfQuestions]; //Coordinate list for each question
	  int[] coordListSize = new int[numberOfQuestions]; //Number of points in each question
	  int eachQuestion = 0; //Counter value for each question
	  try{
		for (String line : Files.readAllLines(Paths.get("guards.txt"))) {
				for (String question : line.split("\\d:")) { //Question contains all the coordinates for each question
					if(question.contains("{")){ //Get rid of null questions
				        question = question.replaceAll("\\s+", ""); //Get rid of whitespace
				        int questionSize = 0; 
				        for (int i = 0; i < question.length(); i++){ //Count number of points in question
				            char c = question.charAt(i);        
				            if(c == '{'){
				            	questionSize = questionSize+1;
				            }
				        }
				        for (String pair : question.split("\\{|\\,|\\}")) { //Get each value
				    		if(pair.matches("\\d|\\d\\.\\d+")==true){ //Get rid of null values and only accept numbers or decimals
				    			double coord = Double.parseDouble(pair); //Convert the string coordinate into a double
				    			System.out.println("Coord:" + coord);
				    		}
				    	}
				        eachQuestion++;
				        coordList[eachQuestion] = question; //Put the currently read list into array
				        coordListSize[eachQuestion] = questionSize; //Put the currently read list size into array
				        System.out.println("Size for question " + eachQuestion + ":" + coordListSize[eachQuestion]);
					}
			    }	
			}
	} catch (IOException e) {
		e.printStackTrace();
	}
  }