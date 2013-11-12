package biotechProject.scoreAlgorithms;

import java.util.HashMap;
import java.util.ArrayList;
import biotechProject.types.Question;
import biotechProject.types.Sentence;
import biotechProject.types.Token;

public class CosineSimilarity {
	
	public double cosineSimilarity(Question question, Sentence sentence){
		HashMap<String, Integer> qTokenVector = new HashMap<String, Integer>();
		HashMap<String, Integer> sTokenVector = new HashMap<String, Integer>();
		qTokenVector = listToVector(question.getKeywordList());
		sTokenVector = listToVector(sentence.getTokenList());
		
		double cosine_similarity = 0.0;
		int sum = 0;
		int qSum = 0;
		int sSum = 0;
		for(String key : qTokenVector.keySet()){
			if (sTokenVector.containsKey(key)) {
				sum += qTokenVector.get(key)*sTokenVector.get(key);
			}
			qSum += qTokenVector.get(key)*qTokenVector.get(key);
		}
		for(String key : sTokenVector.keySet()){
			sSum += sTokenVector.get(key)*sTokenVector.get(key);
		}
		cosine_similarity = 1.0*sum/Math.sqrt(qSum*sSum);
		return cosine_similarity;
	}
	
	
	public HashMap<String, Integer> listToVector(ArrayList<Token> tokenList){
		HashMap<String, Integer> tokenVector = new HashMap<String, Integer>();
		for(Token token : tokenList){
			if (tokenVector.containsKey(token.getText())) {
				tokenVector.put(token.getText(), tokenVector.get(token.getText()+1));
			}else {
				tokenVector.put(token.getText(), 1);
			}
		}
		return tokenVector;
	}
}
