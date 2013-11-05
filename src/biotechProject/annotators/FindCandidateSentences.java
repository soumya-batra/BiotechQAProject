package biotechProject.annotators;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import biotechProject.types.Question;
import biotechProject.types.Sentence;
import biotechProject.types.Token;

public class FindCandidateSentences {

	private static ArrayList<Sentence> candidates = null;
	private static FileReader fr = null;
	private static BufferedReader br = null;
	
	//Written by Soumya
	public static ArrayList<Sentence> getCandidateSentences(String folder,
			Question q) {

		String line;
		String space;
		int i;
		double confidence = 0.0;
		

		// Read all pre-processed documents in the folder
		File[] directory = new File(folder).listFiles();
		for (File file : directory) {
			space = "";
			if (file.isFile()) {
				try {
					fr = new FileReader(file);
					br = new BufferedReader(fr);

					while ((line = br.readLine()) != null) {
						if (line.startsWith("<article-title>")) {
							while ((i = line.indexOf("</article-title>")) == -1) {
								space += line;
								line = br.readLine();
							}
							space = space.substring(14)
									+ line.substring(0, i + 1);

							findCandidates(candidates, space, q, 'T');
							space = "";
						}

						else if (line.startsWith("<abstract>")) {
							while ((i = line.indexOf("</abstract>")) == -1) {
								space += line;
								line = br.readLine();
							}
							space = space.substring(14)
									+ line.substring(0, i + 1);

							findCandidates(candidates, space, q ,'A');
							space = "";
							break;
						}
					}
			/*		if (candidates.size() == 0)
						getCandidateFromBody();
						// Call CanddiateSentence_Haodong. Pass BufferedReader object br and confidence. Add candidates returned to 'candidates'.
						; */
					if(candidates.size() > 0)
						getCandidateFromBody();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		

try {
	br.close();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
		return candidates;
	}

	private static void findCandidates(ArrayList<Sentence> cands, String find,
			Question q, char type) {
		// TODO Auto-generated method stub

		ArrayList<Token> tokens = q.getKeywordList();
		ArrayList<Integer> labels = new ArrayList<Integer>();
		boolean subject = q.getAskSubject();
		String perfect = "";
		int i = 0, j =0;
		double score = 0.0;
		
		for(i = 0; i < tokens.size(); i++)
			perfect += tokens.get(i);
		
		StringTokenizer st = new StringTokenizer(find, ".!?");
		
		while(st.hasMoreTokens())
		{
			i = 0; j = 0;score = 0.0;
			Sentence s = new Sentence(st.nextToken());

		ArrayList<Token> st2 = (ArrayList<Token>) s.getTokenList();

		//Code for finding exact match has to be written
	/*	for(int l = 0; l < st2.size(); l++)
		{
			for(int k = 0; k < tokens.size(); k++)
			{
				if(tokens.get(k).equals(st2.get(l).toString()))
				{
					i++;
					labels.add(l);
				}
			}
			j++;
		} */
		for(Token t : st2)
		{
			if(tokens.contains(t))
				i++;
			j++;
		}

		if(i>0)
		{
			score = (double) i/j;
		/*	switch(type)
			{
			case 'T' : score = score*
					break;
			case 'A' : score = score;
			           break;
			} */
			s.setSentenceScore(score );
			candidates.add(s);
		}
		
		}
		
	}
	
	//Written by Haodong
	private static void getCandidateFromBody()
	{
		
	}

}
