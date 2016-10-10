package Control;

import java.net.MalformedURLException;
import java.net.URL;

import Objects.Object_Database;
import Objects.Object_Ordbok_Base;
import Objects.Object_Webpage;
import Objects.Object_Website;

public class Brain_PageManager {
	
	Controller Class_Controller;
	Object_Website[] Websites;
	int WebsiteCounter = 0;
	
	boolean Soeker = false;
	boolean Lagrer = false;

	public Brain_PageManager( Controller Class_Controller ) {
		
		this.Class_Controller = Class_Controller;
		Websites = new Object_Website[99999];
		
	}
	
	public void InsertPage( Object_Webpage Newpage ) {
		Object_Webpage Rootsite = FigureRootsite( Newpage, "InsertPage" );
		CheckRootSite( Rootsite.Get_URL() );
		for ( int x = 0 ; x < Websites.length ; x++ ) {
			if ( Websites[x] != null ) {
				if ( Websites[x].Get_URL().toString().equals( Rootsite.Get_URL().toString() )) {
					/*if ( Newpage.Get_URL().toString().endsWith("http://www.google.no/search?q=miniatyr")) {
						System.out.println( System.currentTimeMillis()+" InsertPage "+Newpage.Get_URL()+", før lagring" );
					}*/
					Websites[x].InsertPage( Newpage );
					/*if ( Newpage.Get_URL().toString().endsWith("http://www.google.no/search?q=miniatyr")) {
						System.out.println( System.currentTimeMillis()+" InsertPage "+Newpage.Get_URL()+", etter lagring" );
					}*/
					return;
				}
			}
		}
	}
	
	public void InsertSite( Object_Website Newpage ) {
		for ( int x = 0 ; x < Websites.length ; x++ ) {
			if ( Websites[x] == null ) {
				Websites[x] = Newpage;
				WebsiteCounter++;
				//System.out.println( System.currentTimeMillis()+" Oppdaget web site: "+Newpage.Get_URL() );
				return;
			} else if ( Websites[x].Get_URL().toString().equals( Newpage.Get_URL().toString() ) ) {
				return;
			}
		}
		System.err.println( System.currentTimeMillis()+" Kan ikke lagre website fordi array er full!" );
	}
	
	public Object_Website FigureRootsite( Object_Webpage Webpage, String Kilde ) {
		//System.out.println( "web page "+Webpage.Get_URL() );
		if ( Webpage.Get_URL().toString().equals("") ) {
			return null;
		} else if ( Webpage.Get_URL().toString().equals("http:/") ) {
			return null;
		} else if ( Webpage.Get_URL().toString().equals("http:") ) {
			return null;
		}
		try {
			String Left = "http://";
			//System.out.println( "left="+Left );
			URL Rootsite = Webpage.Get_URL();
			if ( Left.toString().equals( Webpage.Get_URL().toString() ) ) {
				return null;
			}
			String Right = Rootsite.toString().substring( Left.toString().length(), Rootsite.toString().length() );
			int indx = 0;
			indx = Math.max( Right.indexOf("/"), 0 );
			while ( indx > 0 ) {
				//System.out.println( ""+Webpage.Get_URL()+" "+indx+" a "+Rootsite );
				Rootsite = new URL( Rootsite.toString().substring( 0, ( Left.toString().length()+indx ) ) );
				Right = Rootsite.toString().substring( Left.toString().length(), Rootsite.toString().length() );
				indx = Math.max( Right.indexOf("/"), 0 );
				//System.out.println( ""+Webpage.Get_URL()+" "+indx+" b "+Rootsite );
			}
			indx = 0;
			
			//webpage=http://miniaturepainting.wikispot.org
			//left ="http://";
			//right = miniaturepainting.wikispot.org
			//System.err.println( ""+Webpage.Get_URL().toString() );
			//System.out.println( "Root site: "+Rootsite );
			//System.out.println( "Root site data: "+Left+"*"+Right+"*"+indx );
			//System.out.println( "Root site data: "+"*"+Webpage.Get_URL().toString().substring( Left.toString().length(), (Left.toString().length()+indx)) );
			Rootsite = CheckRootSite( Rootsite );
			return new Object_Website( Rootsite, Webpage.Get_LinkedRelationValue(), 0, Class_Controller.Get_SearchPhrase() );
		} catch ( Exception T ) {
			System.err.println( ""+Webpage.Get_URL().toString()+" fra "+Kilde );
			CastErrors( T );
		}
		
		return null;
	}
	
	private URL CheckRootSite( URL RootsiteA ) {
		try {
			URL RootsiteB = RootsiteA;
			String Left = "http://";
			String Right = RootsiteB.toString().substring( Left.toString().length(), RootsiteB.toString().length() );
			int indx = 0;
			indx = Math.max( Right.indexOf("/"), 0 );
			while ( indx > 0 ) {
				//System.out.println( ""+Webpage.Get_URL()+" "+indx+" a "+Rootsite );
				RootsiteB = new URL( RootsiteB.toString().substring( 0, ( Left.toString().length()+indx ) ) );
				Right = RootsiteB.toString().substring( Left.toString().length(), RootsiteB.toString().length() );
				indx = Math.max( Right.indexOf("/"), 0 );
				//System.out.println( ""+Webpage.Get_URL()+" "+indx+" b "+Rootsite );
			}
			if ( ( RootsiteA.toString().equals(RootsiteB.toString()) ) == false ) {
				System.out.println( ""+RootsiteA+" != "+ RootsiteB );
			}
			
			return RootsiteB;
		} catch ( Exception T ) {
			//System.err.println( ""+Webpage.Get_URL().toString()+" fra "+Kilde );
			CastErrors( T );
		}
		return RootsiteA;
	}
	
	private void CastErrors( Exception T ) {
		System.err.println( this.getClass().getName() );
		System.err.println( T );
		/*
		System.err.println ( "Origin stack "+1+": ");
		System.err.println ( "Class: " + T.getStackTrace ( )[0].getClassName ( ) );
		System.err.println ( "Method: " + T.getStackTrace ( )[0].getMethodName ( ) );
		System.err.println ( "Line: " + T.getStackTrace ( )[0].getLineNumber ( ) );
		*/
		System.err.println ( "Origin stack "+1+": ");
		System.err.println ( "Class: " + T.getStackTrace ( )[1].getClassName ( ) );
		System.err.println ( "Method: " + T.getStackTrace ( )[1].getMethodName ( ) );
		System.err.println ( "Line: " + T.getStackTrace ( )[1].getLineNumber ( ) );
		
		for ( int y = 2 ; y < T.getStackTrace().length ; y++ ) {
			System.err.println (" ");
			System.err.println ( "Origin stack "+y+": ");
			System.err.println ( "Class: " + T.getStackTrace ( )[y].getClassName ( ) );
			System.err.println ( "Method: " + T.getStackTrace ( )[y].getMethodName ( ) );
			System.err.println ( "Line: " + T.getStackTrace ( )[y].getLineNumber ( ) );
		}
	}

	public Object_Webpage[] GetLinksToSave() {
		
		if ( Lagrer == true ) {
			return null;
		}
		Lagrer = true;
		//System.out.println( System.currentTimeMillis()+" Mottatt beskjed om å lagre links");
		int Count = 0;
		Object_Database SortedDatabase = new Object_Database();
		//System.out.println( System.currentTimeMillis()+" lager liste over lenker å lagre A");
		for ( int X = (Websites.length-1); X >= 0 ; X-- ) {
			if ( Websites[X] != null ) {
				//System.out.println( System.currentTimeMillis()+" lager liste over lenker å lagre B#"+X);
				Object_Webpage[] Sider = Websites[X].Get_SiteMap();
				for ( int Y = 0; Y < Sider.length ; Y++ ) {
					if ( Sider[Y] != null ) {
						if ( Sider[Y].Get_SelfRelationValue() > Class_Controller.InterestBorder ) {
							//System.out.println( System.currentTimeMillis()+" lager liste over lenker å lagre B#"+X+"C#"+Y);
							Count ++;
							SortedDatabase.InsertLink( Sider[Y].Get_URL().toString(), Sider[Y].Get_LinkedRelationValue(), Sider[Y].Get_SelfRelationValue(), Class_Controller.Get_SearchPhrase() );
							//System.out.println( System.currentTimeMillis()+" lager liste over lenker å lagre B#"+X+"C#"+Y+" ferdig");
							//System.out.println( this.getClass().toString()+" "+Sider[Y].Get_URL().toString()+" "+Sider[Y].Get_SelfRelationValue() );
							//System.out.println( this.getClass().toString()+" "+Sider[Y].Get_URL().toString()+" "+Sider[Y].hashCode() );
						}
					}
				}
				//System.out.println( System.currentTimeMillis()+" lager liste over lenker å lagre B#"+X+" ferdig/"+this.WebsiteCounter);
			}
		}
		//System.out.println( System.currentTimeMillis()+" pages to save: "+Count+" links");
		Lagrer = false;
		return SortedDatabase.GetDatabase();
	}

	public void InsertLink( String line, int LinkedRelationValue, String Kilde ) {
		LinkedRelationValue = LinkedRelationValue + Adressvalue( line );
		if ( line.equals("") ) {
			return;
		} else if ( line.equals("http:/") ) {
			return;
		} else if ( line.equals("http:") ) {
			return;
		}
		try {
			URL Adresse = new URL( line );
			Object_Webpage New = new Object_Webpage( Adresse, LinkedRelationValue, 0, Class_Controller.Get_SearchPhrase() );
			Object_Website Rootsite = FigureRootsite( New, Kilde );
			if ( Rootsite == null ) {
				return;
			}
			InsertSite( Rootsite );
			if ( New.Get_URL().toString().equals( Rootsite.Get_URL().toString() ) ) {
			} else {
				InsertPage( New );
			}
			//OrderNewSearch();
		} catch ( MalformedURLException T ) {
		} catch ( Exception T ) {
			CastErrors( T );
		}
	}
	
	private int Adressvalue( String line ) {
		int Value = 0;
		Object_Ordbok_Base Ordbok = this.Class_Controller.OrdBok;
		for ( int X = 0 ; X < Ordbok.Ord.length ; X++ ) {
			if ( Ordbok.Ord[X] != null ) {
				if ( Ordbok.Ord[X].RelationValue != 0 ) {
					int Bonus = 0;
					int Occurence1 = countOccurrences( line, Ordbok.Ord[X].Ordet.toLowerCase() );
					int Occurence2 = countOccurrences( line, " "+Ordbok.Ord[X].Ordet.toLowerCase()+" " );
					int Occurence3 = countOccurrences( line, Ordbok.Ord[X].Ordet.toLowerCase().replaceAll(" ", "") );
					int Occurence4 = countOccurrences( line, Ordbok.Ord[X].Ordet.toLowerCase().replaceAll(" ", "-") );

					Bonus = Bonus + ( Math.min( Occurence1, 3 )* Ordbok.Ord[X].RelationValue);
					Bonus = Bonus + ( Math.min( Occurence2, 3 )* Ordbok.Ord[X].RelationValue*2);
					Bonus = Bonus + ( Math.min( Occurence3, 3 )* Ordbok.Ord[X].RelationValue);
					Bonus = Bonus + ( Math.min( Occurence4, 3 )* Ordbok.Ord[X].RelationValue);
					Value = Value + Bonus;
				}
			} else {
				X = Ordbok.Ord.length;
			}
		}
		
		return Value;
	}
	
	public static int countOccurrences( String haystack, String needle ) {
		
		//System.out.println( "countOccurrences "+needle );
	    int count = 0;
	    int lastIndex = haystack.indexOf( needle, 0 );
	    //System.out.println( needle +" "+haystack.indexOf( needle, lastIndex ) );

	    while ( lastIndex != -1 ){
	    	//System.out.println( "lastIndex "+lastIndex );
	    	haystack = haystack.substring( (lastIndex+needle.length()), haystack.length() );
	    	lastIndex = haystack.indexOf( needle, 0 );

	    	if( lastIndex != -1){
	    		count ++;
	    	}
	    }
	    return count;
	}

	public void OrderNewSearch() {
		//System.out.println( this.getClass().toString()+" OrderNewSearch A" );
		if ( Soeker == true ) {
			return;
		}
		if ( this.Class_Controller.Doneloading == false ) {
			return;
		}
		Soeker = true;
		long Start = System.currentTimeMillis(); 
		for ( int x = 0 ; x < Websites.length ; x++ ) {
			if ( Websites[x] != null ) {
				long TidA = System.currentTimeMillis(); 
				Object_Webpage Temp = Websites[x].Get_NextPageSearch();
				long TidB = System.currentTimeMillis(); 
				if ( Temp != null ) {
					if ( Temp.Searched == false ) {
						//System.out.println( this.getClass().toString()+" OrderNewSearch B "+Temp.Get_URL().toString()+" "+Temp.Searched+" "+Temp );
						if ( Temp.Get_LinkedRelationValue() > 1 ) {
							this.Class_Controller.SearchURL( Temp );
							Soeker = false;
							return;
						}
					}
				}
				long TidC = System.currentTimeMillis(); 
			}
		}
		long slutt = System.currentTimeMillis(); 
		Soeker = false;
	}

	public boolean GetSaved( URL rootsite ) {
		for ( int x = 0 ; x < Websites.length ; x++ ) {
			if ( Websites[x] != null ) {
				if ( Websites[x].Get_URL().toString().equals( rootsite.toString() )) {
					return Websites[x].GetSaved( rootsite );
				}
			} else {
				return false;
			}
		}
		return false;
	}

	public void SetSaved(URL rootsite) {
		for ( int x = 0 ; x < Websites.length ; x++ ) {
			if ( Websites[x] != null ) {
				if ( Websites[x].Get_URL().toString().equals( rootsite.toString() )) {
					Websites[x].SetSaved( true );
					return;
				}
			} else {
				return;
			}
		}
	}
	
}
