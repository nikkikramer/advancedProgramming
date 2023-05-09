package nl.vu.labs.phoenix.ap;

public class Identifier implements IdentifierInterface {
	private StringBuffer sb;
	
	public Identifier() {
		sb = new StringBuffer();
	}
	
	public Identifier(Identifier src) {  //copy constructor
		sb = new StringBuffer(); 
		for(int i=0; i<src.length(); i++) {
			sb.append(src.getCharacter(i));
		}
	}
	
	@Override
	public String value() { 
		return sb.toString();
	}
	
	public char getCharacter(int idx) { 
		return sb.charAt(idx);
	}
	
	public void init(char c) {
		sb = new StringBuffer();
		sb.append(c);
	}

	public void addElement(char c) {
		sb.append(c);
	}
	
	public int length() { 
		return sb.length();
	}
	
	public boolean equals(Object identifier) {
		if(identifier == null) { 
			return false;
		}
		if(!(identifier instanceof Identifier)) {
			return false;
		}
		if(identifier == this) {
			return true;
		}
		return sb.toString().equals(((Identifier) identifier).sb.toString());
	}
	
	public int hashCode() { 
		return sb.toString().hashCode();
	}
	
}
