package typeSystem;

public class Token {
	String textString;
	
	public Token(String textString){
		this.textString = textString;
	}
	
	public String getText(){
		return textString;
	}
}