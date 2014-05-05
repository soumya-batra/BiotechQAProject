package biotechProject.collection;

import java.util.ArrayList;

import biotechProject.annotators.FindCandidateSentences;
import biotechProject.annotators.QuestionAnalysis_Claire;
import biotechProject.annotators.AnswerAnalysis_Anna;
import biotechProject.types.Question;
import biotechProject.types.Sentence;
import biotechProject.types.Answer;

public class Pipeline {

	public static void main(String[] args) throws Exception {

		Question quest = QuestionAnalysis_Claire.userInput2Question();
		
		QuestionAnalysis_Claire.printQuestionForTest(quest);
		
		ArrayList<Sentence> candidates = FindCandidateSentences.getCandidateSentences(quest);
		System.out.println("candidates size: "+candidates.size());
		for (Sentence sentence : candidates) {
			System.out.println(sentence.getTextString()+" score "+sentence.getSentenceScore());
		}
		//System.out.println("candidate sentense number "+candidates.size());
		int num = Math.min(20, candidates.size());
		for(int i=0; i< num; i++){
			Sentence st = candidates.get(i);
			//System.out.println(st.getTextString()+" score "+st.getSentenceScore());
			AnswerAnalysis_Anna.setCandidateAnswers(st.getTextString(), quest);
		}
		ArrayList<Answer> Acandidates = (ArrayList<Answer>) AnswerAnalysis_Anna.getCandidateAnswers();
		for(Answer ans: Acandidates){
			System.out.println(ans.GetText()+" score "+ans.GetScore());
		}
	}

}