package biotechProject.annotators;

import indexer.SearchFiles;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.StringTokenizer;

import synonymQuery.GetSynonymFromInfoplease;

import biotechProject.scoreAlgorithms.TokenOverlap;
import biotechProject.types.Question;
import biotechProject.types.Sentence;
import biotechProject.types.Token;

public class FindCandidateSentences {

	private static ArrayList<Sentence> candidates = new ArrayList<Sentence>();
	private static FileReader fr = null;
	private static BufferedReader br = null;

	// Written by Soumya
	public static ArrayList<Sentence> getCandidateSentences(Question q)
			throws Exception {

		String line;
		String space;
		int i;
		double confidence = 0.0;

		String body = "";
		String temp = new String();

		ArrayList<Token> verbs = q.getVerbList();
        ArrayList<Token> entity = q.getNounEntityList();

		String query = "";
		
		
		query += "\"";
		
		for(Token t:entity)
			query += (" " + t.getText());
		
		query += "\"";


		// Only entity names must be added to the query since querying based on
		// verb is useless

		String[] docPaths = SearchFiles.paths(query);
		

		// Since the documents are obtained in ordered fashion based on number
		// of hits, we find the candidate answers in the first 100 documents. If
		// no result is found, we move to the next 100 and so on
		int start = 0;
		int end = Math.min(docPaths.length, 1);
		for (int j = start; j < end; j++) {
			String file = docPaths[j];
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			while ((line = br.readLine()) != null) {
				if (line.startsWith("<article-title>")) {
					space = line.substring(15);
					if ((i = line.indexOf("</article-title>")) != -1) {
						temp = line;
						line = br.readLine();
					} else {
						while ((i = line.indexOf("</article-title>")) == -1) {
							line = br.readLine();
							space += line;
							temp = line;
						}
					}
					space = space.substring(0, space.length() - 16);

					findCandidates(candidates, space, q, 'T');
					space = "";
				}

				else if (line.startsWith("<abstract>")) {
					space = line.substring(10);
					if ((i = line.indexOf("</abstract>")) != -1) {
						temp = line;
						line = br.readLine();
					} else {
						while ((i = line.indexOf("</abstract>")) == -1) {
							line = br.readLine();
							space += line;
							temp = line;
						}
					}
					space = space.substring(0, space.length() - 11);

					findCandidates(candidates, space, q, 'A');
					space = "";
				}
				else if (line.startsWith("<body>")) {
					while (line.indexOf("</body>") == -1) {
						body += line;
						line = br.readLine();
					}
					break;
				}
			}

//			Search the body only after finding something in the title or the abstract.
			if (candidates.size() >= 0) {
				getCandidateFromBody(body, q);
			}
			br.close();
		}
		
		Collections.sort(candidates);
		return candidates;
	}

	private static void findCandidates(ArrayList<Sentence> candidates,
			String find, Question q, char type) {
		// TODO Auto-generated method stub
		ArrayList<Token> tokens = q.getVerbList();
		tokens.addAll(q.getNounEntityList());
		
		ArrayList<Integer> labels = new ArrayList<Integer>();
		boolean subject = q.getAskSubject();
		String perfect = "";
		int i = 0, j = 0;
		double score = 0.0;

		for (i = 0; i < tokens.size(); i++)
			perfect += tokens.get(i);

		StringTokenizer st = new StringTokenizer(find, ".!?", true);

		while (st.hasMoreTokens()) {
			i = 0;
			j = 0;
			score = 0.0;
			Sentence s = new Sentence(st.nextToken());

			ArrayList<Token> st2 = (ArrayList<Token>) s.getTokenList();

			for (Token t : st2) {
				for (Token t1 : tokens) {
					if (t1.getText().equalsIgnoreCase(t.getText()))
						i++;
				}

				j++;
			}

			if (i > 0) {
				score = (double) i / j;
				
				s.setSentenceScore(new TokenOverlap().tokenOverlap(q, s));
				//candidates.add(s);
			}

		}

	}

	// Written by Haodong
	private static void getCandidateFromBody(String body, Question q) {
		String[] st = body.split("[\n.]");
		//StringTokenizer st = new StringTokenizer(body, ".");
		ArrayList<Token> verbTokens = q.getVerbList();
		ArrayList<Token> nounTokens = q.getNounEntityList();
		ArrayList<String> synonyms = new ArrayList<String>();
		for (Iterator iterator = nounTokens.iterator(); iterator.hasNext();) {
			Token token = (Token) iterator.next();
			synonyms = new GetSynonymFromInfoplease().getSynonyms(token.getText());
		}
		for (String string : synonyms) {
			Token tempToken = new Token(string);
			nounTokens.add(tempToken);
		}
		ArrayList<String> keyWords = new ArrayList<String>();
		for (Token token : verbTokens) {
			keyWords.add(token.getText());
		}
		for (Token token : nounTokens) {
			keyWords.add(token.getText());
		}
		for(String sentence : st) {
			//System.out.println(sentence);
			Sentence s = new Sentence(sentence);
			int flagV = 0;
			int vNum = 0;
			int flagN = 0;
			int nNum = 0;
			for (Token token : verbTokens) {
				if (s.getTextString().indexOf(token.getText()) >= 0) {
					flagV = 1;
					break;
				}
			}
			for (Token token : nounTokens) {
				if (s.getTextString().indexOf(token.getText()) >= 0) {
					flagN = 1;
					break;
				}
			}
			int num = 0;
			if (flagV == 1&&flagN ==1) {
				ArrayList<Token> tokens = s.getTokenList();
				for(Token token : tokens){
					if (keyWords.contains(token.getText())) {
						num++;
					}
				}
				s.setSentenceScore(1.0*num/tokens.size());
				candidates.add(s);
				//System.out.println("candidates from body "+s.getTextString()+" score "+s.getSentenceScore());
			}
		}
	}

}
