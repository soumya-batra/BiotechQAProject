//By Haodong
package typeSystem;

import java.util.List;
import java.util.StringTokenizer;

public class SentenceType {
	List<Token> tokenList;
	private double sentenceScore;
	private boolean isSelected;
	private String textString;
	
	public SentenceType(String text) {
		textString = text;
		sentenceScore = 0;
		//break down input string into tokens
		StringTokenizer tokenizer = new StringTokenizer(text);
		while (tokenizer.hasMoreTokens()) {
			Token token = new Token(tokenizer.nextToken());
			tokenList.add(token);
		}
	}
	
	public void setSentenceScore(double score){
		sentenceScore = score;
	}
	
	public double getSentenceScore(){
		return sentenceScore;
	}
	
	public void setIsSelected(boolean isSelected){
		this.isSelected = isSelected;
	}
	
	public boolean getIsSelected(){
		return isSelected;
	}
	
	public String getTextString(){
		return textString;
	}
}
