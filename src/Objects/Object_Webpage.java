package Objects;

import java.net.URL;

public class Object_Webpage {

	URL Adresse;
	int LinkedRelationValue;
	int SelfRelationValue;
	public boolean Searched = false;
	boolean Saved = false;
	
	public String SearchPhrase;
	public String Extract = "";
	
	public Object_Webpage( URL Adresse, int LinkedRelationValue, int SelfRelationValue, String SearchPhrase ) {
		this.Adresse = Adresse;
		this.LinkedRelationValue = LinkedRelationValue;
		this.SelfRelationValue = SelfRelationValue;
		this.SearchPhrase = SearchPhrase;
	}
	
	
	public URL Get_URL() {
		return Adresse;
	}

	public int Get_LinkedRelationValue() {
		return LinkedRelationValue;
	}
	
	public boolean Get_Searched() {
		return Searched;
	}
	
	public int Get_SelfRelationValue() {
		return SelfRelationValue;
	}
	
	
	
	public void Set_LinkedRelationValue( int value ) {
		LinkedRelationValue = value;
	}
	
	public void Set_Searched() {
		Searched = true;
	}
	
	public void Set_SearchFailed() {
		System.out.println( this.getClass().toString()+" Set_SearchFailed "+Adresse.toString() );
		//Searched = false;
	}
	
	public void Set_SelfRelationValue( int value ) {
		SelfRelationValue = value;
	}


	public String Get_Extract() {
		return Extract;
	}
}
