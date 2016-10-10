package Objects;

import java.net.URL;

public class Object_Website extends Object_Webpage {
	
	Object_Webpage[] SiteMap;

	public Object_Website( URL Adresse, int LinkedRelationValue, int SelfRelationValue, String SearchPhrase ) {
		
		super( Adresse, LinkedRelationValue, SelfRelationValue, SearchPhrase);
		SiteMap = new Object_Webpage[999];
		
	}

	public void InsertPage( Object_Webpage newpage ) {
		
		for ( int x = 0 ; x < SiteMap.length ; x++ ) {
			if ( SiteMap[x] == null ) {
				SiteMap[x] = newpage;
				//System.out.println( System.currentTimeMillis()+" Lagret web page: "+newpage.Get_URL() );
				return;
			} else if ( SiteMap[x].Get_URL().toString().equals( newpage.Get_URL().toString() ) ) {
				return;
			}
		}
		
	}

	public Object_Webpage Get_NextPageSearch() {
 
		int LinkedRelation = 0;
		Object_Webpage Highest = null;
		if ( this.Get_Searched() == false ) {
			if ( this.Get_LinkedRelationValue() > LinkedRelation ) {
				Highest = this;
				LinkedRelation = this.Get_LinkedRelationValue();
			}
		}
		
		for ( int x = 0 ; x < SiteMap.length ; x++ ) {
			if ( SiteMap[x] != null ) {
				if ( SiteMap[x].Get_Searched() == false ) {
					if ( SiteMap[x].Get_LinkedRelationValue() > LinkedRelation ) {
						LinkedRelation = SiteMap[x].Get_LinkedRelationValue();
						Highest = SiteMap[x];
						//System.out.println( SiteMap[x].Get_URL()+" "+SiteMap[x].Get_LinkedRelationValue() );
					}
				}
			}
		}
		return Highest;
	}

	public int Get_SiteRelationValue() {

		int SiteRelationValue = 0;
		for ( int x = 0 ; x < SiteMap.length ; x++ ) {
			if ( SiteMap[x] != null ) {
				//if ( SiteMap[x].Get_Searched() == true ) {
					SiteRelationValue = SiteRelationValue + SiteMap[x].Get_SelfRelationValue();
				//}
			}
		}
		
		return SiteRelationValue;
	}

	public Object_Webpage[] Get_SiteMap() {
		return SiteMap;
	}

	public boolean GetSaved( URL rootsite ) {
		
		for ( int x = 0 ; x < SiteMap.length ; x++ ) {
			if ( SiteMap[x] != null ) {
				if ( SiteMap[x].Get_URL().toString().equals( rootsite.toString() ) ) {
					return SiteMap[x].Saved;
				}
			} else {
				return false;
			}
		}
		return false;
	}

	public void SetSaved(boolean b) {
		Saved = true;
	}
	
}
