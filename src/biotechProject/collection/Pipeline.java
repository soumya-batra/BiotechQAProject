package biotechProject.collection;

import biotechProject.annotators.QuestionAnalysis_Claire;

import biotechProject.types.Question;

public class Pipeline {

	public static void main(String[] args) {

		Question quest = QuestionAnalysis_Claire.userInput2Question();

		QuestionAnalysis_Claire.printQuestionForTest(quest);

	}

}