package biotechProject.annotators;

import java.util.ArrayList;
import biotechProject.types.Question;

import biotechProject.types.Answer;

import biotechProject.types.Token;

import java.util.*;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class AnswerAnalysis_Anna {
	private static List<Answer> Acandidates = new ArrayList<Answer>();

	public static List<Answer> setCandidateAnswers(String Sentence, Question q) {

		MaxentTagger tagger = new MaxentTagger(
				"tagger/english-left3words-distsim.tagger");
		// The sample string

		// String sample =
		// "Surprisingly, this mutant protein, which does not interact with the mitochondrial antiapoptotic protein Bcl-xL, localizes to mitochondria but does not induce apoptosis";
		/** Question Entity Pos Tag **/
		ArrayList<Token> entity = q.getNounEntityList();
		String entString = new String();
		ArrayList<String> entNoun = new ArrayList<String>();
		ArrayList<String> entElse = new ArrayList<String>();
		for (int j = 0; j < entity.size(); j++) {
			entString = entity.get(j).getText();
			String entagString = tagger.tagString(entString);
			String text = entagString.substring(0, entagString.indexOf("_"));
			String pos = entagString.substring(entagString.indexOf("_") + 1,
					entagString.length());
			entity.get(j).setPos(pos);
			if (pos.startsWith("NN"))
				entNoun.add(text);
			else
				entElse.add(text);
		}
		// The tagged string
		String[] sentences = Sentence.split("[,;]");
		// ::TODO consider multiple clause sentences situation
		int chosen = 0;
		for (int cnt = 0; cnt < sentences.length; cnt++)
			// sentence chosen if contains any noun in query nounEntity
			for (int cnt1 = 0; cnt1 < entNoun.size(); cnt1++)
				if (sentences[cnt].contains(entNoun.get(cnt1))) {
					chosen = cnt;
					break;
				}
		Sentence = sentences[chosen];
		Sentence = Sentence.toLowerCase();
		String tagged = tagger.tagString(Sentence);
		String[] result = tagged.split("\\s");
		boolean qtype = q.getAskSubject();
		int ansBegin0 = 0;
		boolean isAttributive = false;
		for (int x = 0; x < result.length; x++) {
			isAttributive = false;
			// System.out.println(result[x]);
			String tok = result[x];
			String pos = tok.substring(tok.indexOf("_") + 1, tok.length());
			// System.out.println("pos: "+pos);
			if (pos.startsWith("V")) {
				String text = tok.substring(0, tok.indexOf("_"));
				// System.out.println("text: "+text);
				int vpos = Sentence.indexOf(text);
				// System.out.println(vpos);
				if (x > 0) {
					String tok0 = result[x - 1];
					String pos0 = tok0.substring(tok0.indexOf("_") + 1,
							tok0.length());
					if (pos0.startsWith("PRP")) {
						isAttributive = true;
						ansBegin0 = vpos + text.length();
					}
				}
				if (!isAttributive) {
					double score0 = 0;
					double score1 = 0;
					double score2 = 0;
					double score3 = 0;
					ArrayList<Token> verbs = q.getVerbList();
					for (int d = 0; d < verbs.size(); d++)
						if (text.equals(verbs.get(d).getText()))
							score0 = 3;
					// base on the assumption that each candidate sentence
					// contains keyword entNouns
					for (int k = 0; k < entNoun.size(); k++)
						if (Sentence.indexOf(entNoun.get(k)) < vpos)
							score1++;
						else
							score1--;
					for (int h = 0; h < entElse.size(); h++) {
						String eString = entElse.get(h);
						if (Sentence.contains(eString)) {
							score2++;
							double diff = vpos - Sentence.indexOf(eString);
							if (Math.signum(diff) == Math.signum(score1))
								score2++;
						}
					}
					/** differentiate 2 types of questions **/
					boolean isPassive = false;
					int l1 = 0;
					int l2 = 0;
					if (x + 2 < result.length) {
						String tok1 = result[x + 1];
						String text1 = tok1.substring(0, tok1.indexOf("_"));
						// String pos1 =
						// tok1.substring(tok1.indexOf("_")+1,tok1.length());
						String tok2 = result[x + 2];
						String text2 = tok2.substring(0, tok2.indexOf("_"));
						String pos2 = tok2.substring(tok2.indexOf("_") + 1,
								tok2.length());
						if (pos2.startsWith("IN"))
							isPassive = true;
						l1 = text1.length();
						l2 = text2.length();
					}
					int ansBegin = vpos + text.length();
					if (isPassive) {
						ansBegin = ansBegin + l1 + l2 + 1;
						x = x + 2;
					}
					String answerText = new String();
					if (score1 < 0) {
						if (vpos > 0)
							answerText = Sentence.substring(ansBegin0, vpos);
						else
							answerText = text;
					} else
						answerText = Sentence.substring(ansBegin,
								Sentence.length());
					if (qtype) {
						if (isPassive != (score1 < 0))
							score3 = 5;
					} else {
						if (isPassive == (score1 < 0))
							score3 = 5;
					}
					if (answerText.length() > 1) {
						// System.out.println("AnswerText: "+answerText);
						Answer answer = new Answer(answerText);
						double score = (Math.abs(score1) + score0 + score2 + score3)
								/ (entNoun.size() + entElse.size() * 2 + 3 + 5);
						answer.SetScore(score);
						Acandidates.add(answer);
					}
				}
			}
		}
		return Acandidates;
	}

	public static void AnswerScoreDescendingSort() {
		if (!Acandidates.isEmpty()) {
			Collections.sort(Acandidates);
		}
	}

	public static void clearAcandidates() {
		Acandidates.clear();
	}

	public static List<Answer> getCandidateAnswers() {
		return Acandidates;
	}
}
