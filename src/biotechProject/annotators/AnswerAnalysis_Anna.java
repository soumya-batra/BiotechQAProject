package biotechProject.annotators;

import java.util.ArrayList;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import biotechProject.types.Question;
import biotechProject.types.Sentence;
import biotechProject.types.Token;
import biotechProject.types.Answer;

public class AnswerAnalysis_Anna {
	private static ArrayList<Answer> Acandidates = new ArrayList<Answer>();
public static ArrayList<Answer> setCandidateAnswers(String Sentence,Question q){
	       // Initialize the tagger
	 
			MaxentTagger tagger = new MaxentTagger(
			 "tagger/english-left3words-distsim.tagger");
			// The sample string
			 
			//String sample = "Surprisingly, this mutant protein, which does not interact with the mitochondrial antiapoptotic protein Bcl-xL, localizes to mitochondria but does not induce apoptosis";
			 
			// The tagged string
			 
			String tagged = tagger.tagString(Sentence.toLowerCase());
			 
			String[] result = tagged.split("\\s");
		     for (int x=0; x<result.length; x++){
		         //System.out.println(result[x]);
		    	 String tok = result[x];
		    	 if (tok.endsWith("_NN")){
		    		     String entity = q.getKeywordList().get(1).getText();
		    			 if(!tok.startsWith(entity)){
		    				    Answer ans = new Answer(tok.substring(0, tok.length()-3));
		    				    ans.SetScore(1);
		    				Acandidates.add(ans);    
	                  }
		    		 
		    	 }
		     }
	return Acandidates;
}
public static ArrayList<Answer> getCandidateAnswers(){
	return Acandidates;
}
}
