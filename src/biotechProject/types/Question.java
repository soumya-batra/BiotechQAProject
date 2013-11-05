package biotechProject.types;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Question {

	/**
	 * the string of input question, without question mark
	 */
	private String questionString = null;

	/**
	 * When the format of input question is "What does [ENTITY] [VERB]", then
	 * first token in keywordList will be [ENTITY], second token will be [VERB].
	 * When the format of input question is "What [VERB] [ENTITY]", then first
	 * token in keywordList will be [ENTITY], second will be [VERB]
	 */
	private ArrayList<Token> keywordList = new ArrayList<Token>();

	/**
	 * When input question is "What [VERB] [ENTITY]", then askSubject is true.
	 * When input question is "What does [ENTITY] [VERB]" then askSubject is
	 * false
	 */
	private boolean askSubject = true;

	/**
	 * construct the question according to the user input string. It's assumed
	 * that parameter inputStr does not contain question mark
	 */
	public Question(String inputStr) {
		this.questionString = inputStr;
		String[] questionWord = inputStr.split("\\s");
		int secondWordLength = questionWord[1].length();
		int lastWordLength = questionWord[questionWord.length - 1].length();

		// question format is "What does [ENTITY] [VERB]"
		if (questionWord[1].equals("does")) {
			// first token is [ENTITY]
			// extract first token by removing "What does " and [ VERB]"
			Token token1 = new Token(inputStr.substring(10, inputStr.length()
					- lastWordLength - 1));
			// second token is [VERB]
			// extract second token by get the last word from questionWord array
			Token token2 = new Token(questionWord[questionWord.length - 1]);

			this.askSubject = false;
			this.keywordList.add(token1);
			this.keywordList.add(token2);
		}

		// question format is "What [VERB] [ENTITY]"
		else {
			// first token is [VERB]
			// extract first token get the second word from qeustionWord array
			Token token1 = new Token(questionWord[1]);
			// second token is [ENTITY]
			// extract second token by removing "What [VERB] "
			Token token2 = new Token(inputStr.substring(6 + secondWordLength));

			this.askSubject = true;
			this.keywordList.add(token1);
			this.keywordList.add(token2);
		}
	}

	public String getQuestionString() {
		return this.questionString;
	}

	public ArrayList<Token> getKeywordList() {
		return this.keywordList;
	}

	public boolean getAskSubject() {
		return this.askSubject;
	}

}