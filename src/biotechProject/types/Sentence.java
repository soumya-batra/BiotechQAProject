//Modified By Haodong 
package biotechProject.types;

import java.util.List;
import java.util.StringTokenizer;

public class Sentence {
	List<Token> tokenList;
	private double sentenceScore;
	private String textString;

	public Sentence(String text) {
		textString = text;
		sentenceScore = 0;
		// break down input string into tokens
		StringTokenizer tokenizer = new StringTokenizer(text);
		while (tokenizer.hasMoreTokens()) {
			Token token = new Token(tokenizer.nextToken());
			tokenList.add(token);
		}
	}

	public void setSentenceScore(double score) {
		sentenceScore = score;
	}

	public double getSentenceScore() {
		return sentenceScore;
	}

	public String getTextString() {
		return textString;
	}
}
