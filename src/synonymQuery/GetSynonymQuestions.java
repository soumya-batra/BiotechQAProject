package synonymQuery;

import java.io.IOException;
import java.util.ArrayList;

import biotechProject.types.Question;

public class GetSynonymQuestions {
	public ArrayList<Question> getSynonymQuestions(Question question) {

		return null;
	}

	public static void main(String args[]) throws IOException {
		String query = "insulin";
		GetSynonymFromGpsdb test = new GetSynonymFromGpsdb();
		System.out.println(test.getSynonym(query));
	}
}
