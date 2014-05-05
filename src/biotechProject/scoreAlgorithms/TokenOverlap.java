package biotechProject.scoreAlgorithms;


import java.util.ArrayList;

import biotechProject.types.Question;
import biotechProject.types.Sentence;
import biotechProject.types.Token;

public class TokenOverlap {
	public double tokenOverlap(Question quesion, Sentence sentence){
		ArrayList<Token> questionTokens = quesion.getKeywordList();
		ArrayList<Token> sentenceTokens = (ArrayList<Token>) sentence.getTokenList();
		int count = 0;
		for (Token token : sentenceTokens){
			for(Token qToken : questionTokens){
				if (token.getText().equals(qToken.getText())) {
					count++;
				}
			}
		}
		return 1.0*count/sentenceTokens.size();
	}
}
