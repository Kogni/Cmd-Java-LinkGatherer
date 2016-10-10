package Objects;

public class Object_Ordbok_Base {
	
	public Object_Phrase Ord[];
	String SearchPhrase;

	public Object_Ordbok_Base( String SearchPhrase ) {
		this.SearchPhrase = SearchPhrase;
		Ord = new Object_Phrase[530];
		for ( int X = 0; X < Ord.length ; X++ ) {
			Ord[X] = new Object_Phrase( "", 0, 0 );
		}

		
		FyllOrdbok();
	}
	
	public void FyllOrdbok() {
		
		int X = 0;
		
		Ord[X].Ordet = SearchPhrase;
		Ord[X].RelationValue = 30;
		Ord[X].SaveValue = 30;
		X++;
		
	}
}
