package biotechProject.annotators;

import java.util.InputMismatchException;

import java.util.Scanner;

import biotechProject.types.Question;

public class QuestionAnalysis_Claire {

	public static Question userInput2Question() {

		Scanner sc = new Scanner(System.in);

		String inputLine = sc.nextLine();

		inputLine = inputLine.substring(0, inputLine.length() - 1);

		Question aQuestion = new Question(inputLine);

		return aQuestion;

	}

	public static void printQuestionForTest(Question q) {

		System.out.println(q.getKeywordList().get(0).getText());

		System.out.println(q.getKeywordList().get(1).getText());

	}

}