package biotechProject.collection;

import java.util.ArrayList;

import biotechProject.annotators.FindCandidateSentences;
import biotechProject.annotators.QuestionAnalysis_Claire;

import biotechProject.types.Question;
import biotechProject.types.Sentence;

public class Pipeline {

	public static void main(String[] args) {

		Question quest = QuestionAnalysis_Claire.userInput2Question();
		
		QuestionAnalysis_Claire.printQuestionForTest(quest);
		
		ArrayList<Sentence> candidates = FindCandidateSentences.getCandidateSentences("E:/Eclipse Projects/workspace/BioFinalProject/document", quest);
		for(Sentence st : candidates){
			System.out.println(st.getTextString());
		}
	}

}