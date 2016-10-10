package Threads;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import Control.Controller;
import Objects.Object_Phrase;
import Objects.Object_Webpage;

public class Thread_LinkScanner extends Thread {
	
	Controller Class_Controller;
	
	Object_Webpage LinkToCheck;
	long ThreadstartTime = System.currentTimeMillis();
	
	URL url;
	HttpURLConnection connection;
	Thread_TimeKeeper Timer;
	StringBuffer DataImported = new StringBuffer();
	
	int RelationValue = 0;
	int SaveValue = 0;
	
	public Thread_LinkScanner( Controller Class_Controller, Object_Webpage LinkToCheck ) {
		//System.out.println ( "Scanner url="+LinkToCheck.Get_URL()  );
		//System.out.println ( System.currentTimeMillis()+" +Thread_LinkFinder created: "+this.getName()+" "+LinkToCheck.Get_URL() );
		
		this.Class_Controller = Class_Controller;
		this.LinkToCheck = LinkToCheck;
		long TidA = System.currentTimeMillis(); 
		//run();
		long TidB = System.currentTimeMillis(); 
		//System.out.println ( System.currentTimeMillis()+" +Thread_LinkScanner created: "+this.getName()+" Tid="+(TidB-TidA) );
		long ThreadendTime = System.currentTimeMillis();
		//System.out.println(System.currentTimeMillis()+" -Thread_LinkScanner finished: "+this.getName()+" Time (ms) : " + (ThreadendTime - ThreadstartTime)+" "+LinkToCheck.Get_URL()+" Chars: "+DataImported.length());
	}

	public void run() {
		//System.out.println ( "Thread_LinkFinder run" );
		Scan();
	}
	
	private void Scan() {
		try {
			URL url = LinkToCheck.Get_URL();
			//HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			Thread_TimeKeeper Timer = new Thread_TimeKeeper( this );
			Timer.start();

            //URL url = new URL("http://www.google.com/search?q=miniature");
            URLConnection conn =  url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux x86_64; en-GB; rv:1.8.1.6) Gecko/20070723 Iceweasel/2.0.0.6 (Debian-2.0.0.6-0etch1)");
            BufferedReader in = new BufferedReader( new InputStreamReader(conn.getInputStream()) );
            String str;

            while ((str = in.readLine()) != null) {
                    //System.out.println(str);
                    DataImported.append( str );
            }
            //System.out.println(System.currentTimeMillis()+" URL scanned. Data size="+DataImported.length() );
            ScanStrings( DataImported );

		} catch ( FileNotFoundException T ) {
			LinkToCheck.Set_SelfRelationValue( 0 );
			//System.err.println(System.currentTimeMillis()+" URL could not be scanned A)FileNotFoundException "+LinkToCheck.Get_URL() );
		} catch ( UnknownHostException T ) {
			LinkToCheck.Set_SelfRelationValue( 0 );
			//System.err.println(System.currentTimeMillis()+" URL could not be scanned B)UnknownHostException "+LinkToCheck.Get_URL() );
		} catch ( IOException T ) {
			//System.err.println(System.currentTimeMillis()+" URL could not be scanned C)IOException "+LinkToCheck.Get_URL() );
			if ( T.getMessage ( ).indexOf("Server returned HTTP response code: 503 for URL") > -1 ) {
				//Server possibly overloaded, please try again
				
			} else {
				//LinkToCheck.Set_SelfRelationValue( 0 );
				//CastErrors( T );
			}
			LinkToCheck.Set_SearchFailed();
			LinkToCheck.Set_LinkedRelationValue( LinkToCheck.Get_LinkedRelationValue()-10 );
			if ( LinkToCheck.Get_LinkedRelationValue() < this.Class_Controller.InterestBorder ) {
				LinkToCheck.Set_LinkedRelationValue( Class_Controller.InterestBorder );
			}
		} catch ( IllegalArgumentException T ) {
			LinkToCheck.Set_SelfRelationValue( 0 );
			//System.err.println(System.currentTimeMillis()+" URL could not be scanned D)IllegalArgumentException "+LinkToCheck.Get_URL() );
		} catch ( Exception T ) {
			CastErrors( T );
			LinkToCheck.Set_SelfRelationValue( 0 );
		}
		
		//this.Class_Controller.TimeTick( "Thread_LinkScanner completed" );
	}
	
	private void ScanStrings( StringBuffer buffer ) {
		//System.out.println ( System.currentTimeMillis()+" Thread ReadData: "+this.getName()+" "+LinkToCheck.GetURL() );
		long StartTime = System.currentTimeMillis();
		String Buffer = buffer.toString().toLowerCase() ;

		try {
			FindRelationValue( Buffer );
			LinkToCheck.Set_SelfRelationValue( RelationValue );
			
			LinkToCheck.Extract = MakeExtractA( Buffer, LinkToCheck.SearchPhrase );
			
			Thread thread = new Thread_URLFinder( Class_Controller, Buffer, this, LinkToCheck.Get_URL().toString() );
			thread.start();

			long EndTime = System.currentTimeMillis();
			System.out.println( System.currentTimeMillis()+" Web page relation value updated: "+LinkToCheck.Get_URL().toString()+" value="+LinkToCheck.Get_SelfRelationValue()+" Time (ms)=" + (EndTime - StartTime) );
			if ( RelationValue > Class_Controller.InterestBorder ) {
				//System.out.println( System.currentTimeMillis()+" ---> Check out "+LinkToCheck.Get_URL().toString()+" Score: "+LinkToCheck.Get_SelfRelationValue()+" Time (ms) : " + (EndTime - StartTime) );
				System.out.println( System.currentTimeMillis()+" ---> "+LinkToCheck.Get_Extract() );
				FindSaveValue( Buffer );
				//System.out.println( System.currentTimeMillis()+" Web page save value updated: "+LinkToCheck.Get_URL().toString()+" value="+SaveValue );
				if ( SaveValue >= 30 ) {
					SaveToFile();
				}
			}
			
		} catch ( Exception T ) {
			CastErrors( T );
		}
		long EndTime = System.currentTimeMillis();
		//System.out.println ( System.currentTimeMillis()+" ReadData finished: "+this.getName()+" "+LinkToCheck.Get_URL()+" Time (ms) : " + (EndTime - StartTime)+" chars: "+DataImported.length( )+" score: "+RelationValue );
		/*if ( LinkToCheck.Get_URL().toString().equals( "http://www.google.no/search?q=miniatyr")) {
			System.out.println ( System.currentTimeMillis()+" ReadData finished: "+this.getName()+" "+LinkToCheck.Get_URL()+" Time (ms) : " + (EndTime - StartTime)+" chars: "+DataImported.length( )+" score: "+RelationValue );
		}*/
	}
	
	private String MakeExtractA( String Buffer, String SearchPhrase ) throws IOException {
		
		InputStream is = new ByteArrayInputStream(Buffer.getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		Class_Controller.Class_Parser.parse( br );
		//Buffer = Class_Controller.Class_Parser.getText();
		String Extract = Buffer;
		if ( Buffer.indexOf( SearchPhrase ) > -1 ) {
			Extract = Buffer;
		}
		Extract = MakeExtractB( Extract, "<", SearchPhrase );
		Extract = MakeExtractB( Extract, "{", SearchPhrase );
		Extract = MakeExtractB( Extract, ">", SearchPhrase );
		Extract = MakeExtractB( Extract, "}", SearchPhrase );
		Extract = MakeExtractB( Extract, ":", SearchPhrase );
		//Extract = MakeExtractB( Extract, ";", SearchPhrase );
		Extract = MakeExtractB( Extract, "%25", SearchPhrase );
		Extract = MakeExtractB( Extract, "%26", SearchPhrase );
		Extract = MakeExtractB( Extract, "&uml", SearchPhrase );
		Extract = MakeExtractB( Extract, "&uuml", SearchPhrase );
		Extract = MakeExtractB( Extract, "href", SearchPhrase );
		Extract = MakeExtractB( Extract, "http", SearchPhrase );
		Extract = MakeExtractB( Extract, "+", SearchPhrase );
		Extract = MakeExtractB( Extract, "=", SearchPhrase );
		Extract = MakeExtractB( Extract, "\\", SearchPhrase );
		Extract = MakeExtractB( Extract, "/", SearchPhrase );
		//Extract = MakeExtractB( Extract, "&", SearchPhrase );
		Extract = MakeExtractB( Extract, "\"", SearchPhrase );
		Extract = MakeExtractB( Extract, "%", SearchPhrase );
		//Extract = MakeExtractB( Extract, "-", SearchPhrase );
		Extract = MakeExtractB( Extract, "!", SearchPhrase );
		Extract = MakeExtractB( Extract, "?", SearchPhrase );
		Extract = MakeExtractB( Extract, ". ", SearchPhrase );
		Extract = Extract.replaceAll("  ", " ");
		Extract = Extract.replaceAll(" ", "\"] [\"");
		System.out.println( "MakeExtractA C: "+Extract );
		Extract = Extract.replaceAll(".*\"]", "\"].");
		System.out.println( "MakeExtractA D: "+Extract );
		
		return Extract;
	}
	
	private String MakeExtractB( String OldExtract, String Needle, String SearchPhrase ) {
		//System.out.println( "MakeExtractB A: "+OldExtract );
		
		OldExtract = OldExtract.replaceAll("&nbsp", " ");
		OldExtract = OldExtract.replaceAll("&amp", "&");
		OldExtract = OldExtract.replaceAll("&uml", "");
		OldExtract = OldExtract.replaceAll("&uuml", "");
		OldExtract = OldExtract.replaceAll("&raquo", "*");
		OldExtract = OldExtract.replaceAll("&laquo", "'");
		OldExtract = OldExtract.replaceAll("&#8211", "-");
		OldExtract = OldExtract.replaceAll(";", "u");
		OldExtract = OldExtract.replaceAll("  ", " ");
		
		String NewExtract = "";
		int lastIndex = OldExtract.indexOf( Needle );
		while ( lastIndex > -1 ){
	    	//System.out.println( "lastIndex "+lastIndex );
			String Setning = OldExtract.substring( 0, lastIndex );
			if (( Setning.indexOf( SearchPhrase ) > -1 ) && ( Setning.indexOf( " " ) > -1 )) {
				//NewExtract = NewExtract+Setning+". ";
				//System.out.println( "MakeExtractB B Setning="+Setning+" "+Setning.length() );
				NewExtract = ExtendExtract( NewExtract, Setning, SearchPhrase );
			}
			
			//System.out.println( "MakeExtract B: "+NewExtract );
			OldExtract = OldExtract.substring( lastIndex+Needle.length() );			
	    	lastIndex = OldExtract.indexOf( Needle );
	    }
		if (( OldExtract.indexOf( SearchPhrase ) > -1 ) && ( OldExtract.indexOf( " " ) > -1 )) {
			//NewExtract = NewExtract+OldExtract+". ";
			NewExtract = ExtendExtract( NewExtract, OldExtract, SearchPhrase );
		}
		return NewExtract;
	}
	
	private String ExtendExtract( String OldExtract, String Extension, String SearchPhrase ) {
		//System.out.println( "ExtendExtract A Extension="+Extension+" "+Extension.length() );
		String NewExtract = OldExtract;
		if ( Extension.length() > SearchPhrase.length()+1 ) {
			if ( OldExtract.indexOf( Extension ) == -1 ) {
				NewExtract = OldExtract+" "+Extension+".* ";
			}
			//System.out.println( "ExtendExtract B Extension="+Extension+" "+Extension.length() );
		}
		return NewExtract;
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


	public void TimePast( ) {
		if ( connection != null ) {
			connection.disconnect();
		}
	}
	
	private void FindRelationValue( String Buffer ) {
		try {
			RelationValue = 0;
			Object_Phrase[] TempOrdbok = this.Class_Controller.GetOrdliste();
			for ( int X = 0 ; X < TempOrdbok.length ; X++ ) {
				if ( TempOrdbok[X] != null ) {
					if ( TempOrdbok[X].RelationValue != 0 ) {
						int Bonus = 0;
						int Occurence1 = countOccurrences( Buffer, TempOrdbok[X].Ordet.toLowerCase() );
						int Occurence2 = countOccurrences( Buffer, " "+TempOrdbok[X].Ordet.toLowerCase()+" " );
						int Occurence3 = countOccurrences( Buffer, TempOrdbok[X].Ordet.toLowerCase().replaceAll(" ", "") );
						int Occurence4 = countOccurrences( Buffer, TempOrdbok[X].Ordet.toLowerCase().replaceAll(" ", "-") );

						Bonus = Bonus + ( Math.min( Occurence1, 5 )* TempOrdbok[X].RelationValue);
						Bonus = Bonus + ( Math.min( Occurence2, 5 )* TempOrdbok[X].RelationValue*2);
						Bonus = Bonus + ( Math.min( Occurence3, 5 )* TempOrdbok[X].RelationValue);
						Bonus = Bonus + ( Math.min( Occurence4, 5 )* TempOrdbok[X].RelationValue);
						RelationValue = RelationValue + Bonus;
					}
				} else {
					X = TempOrdbok.length;
				}
			}
		} catch ( Exception T ) {
			CastErrors( T );
		}
		//System.out.println( "RelationValue etter="+RelationValue );
	}
	
	private void FindSaveValue( String Buffer ) {
		try {
			SaveValue = 0;
			Object_Phrase[] TempOrdbok = this.Class_Controller.GetOrdliste();
			for ( int X = 0 ; X < TempOrdbok.length ; X++ ) {
				if ( TempOrdbok[X] != null ) {
					if ( TempOrdbok[X].SaveValue != 0 ) {
						int Bonus = 0;
						int Occurence1 = countOccurrences( Buffer, TempOrdbok[X].Ordet.toLowerCase() );
						int Occurence2 = countOccurrences( Buffer, " "+TempOrdbok[X].Ordet.toLowerCase()+" " );
						int Occurence3 = countOccurrences( Buffer, TempOrdbok[X].Ordet.toLowerCase().replaceAll(" ", "") );
						int Occurence4 = countOccurrences( Buffer, TempOrdbok[X].Ordet.toLowerCase().replaceAll(" ", "-") );

						Bonus = Bonus + ( Math.min( Occurence1, 5 )* TempOrdbok[X].SaveValue);
						Bonus = Bonus + ( Math.min( Occurence2, 5 )* TempOrdbok[X].SaveValue*2);
						Bonus = Bonus + ( Math.min( Occurence3, 5 )* TempOrdbok[X].SaveValue);
						Bonus = Bonus + ( Math.min( Occurence4, 5 )* TempOrdbok[X].SaveValue);
						SaveValue = SaveValue + Bonus;
					}
				} else {
					X = TempOrdbok.length;
				}
			}
		} catch ( Exception T ) {
			CastErrors( T );
		}
	}
	
	private void SaveToFile() {
		try {
			
			String Filnavn = "URLsAppendfile.txt";
			File filen;;
			filen = new File ( Filnavn );
			if ( !filen.exists() ) {
				filen.createNewFile();
			}
			
			PrintStream utfil;
			FileOutputStream appendFilen = new FileOutputStream ( Filnavn, true );
			utfil = new PrintStream ( appendFilen );
			
			/*URL Rootsite = Class_Controller.Class_Brain_PageManager.FigureRootsite( LinkToCheck, "BrainIO" ).Get_URL();
			System.out.println( System.currentTimeMillis()+" ---> Check out "+Rootsite.toString()+" Score: "+LinkToCheck.Get_SelfRelationValue() );
			if ( Class_Controller.Class_Brain_PageManager.GetSaved( Rootsite ) == true ) {
				return;
			}
			Class_Controller.Class_Brain_PageManager.SetSaved( Rootsite );
			utfil.println( ""+Rootsite.toString()+"");
			*/

			//System.out.println( System.currentTimeMillis()+" ---> Check out "+LinkToCheck.Get_URL().toString()+" Score: "+LinkToCheck.Get_SelfRelationValue() );
			utfil.println( ""+LinkToCheck.Get_URL().toString()+" - "+LinkToCheck.Get_Extract()+"\n\n");
			//utfil.append( " \n\r"+LinkToCheck.Get_URL().toString() );

			utfil.close ( );
		} catch ( IOException T ) {
			if ( T.getMessage ( ).equals("Access is denied")) {
				SaveToFile();
			} else {
				CastErrors( T );
			}
		} catch ( Exception T ) {
			CastErrors( T );
		}
	}
	
	private void CastErrors( Exception T ) {
		System.err.println("Thread_LinkFinder");
		System.err.println ( "Throwable message: " + T.getMessage ( ) );
		System.err.println ( "Throwable cause: " + T.getCause ( ) );
		System.err.println ( "Throwable class: " + T.getClass ( ) );
		
		System.err.println ( "Origin stack "+0+": ");
		System.err.println ( "Class: " + T.getStackTrace ( )[0].getClassName ( ) );
		System.err.println ( "Method: " + T.getStackTrace ( )[0].getMethodName ( ) );
		System.err.println ( "Line: " + T.getStackTrace ( )[0].getLineNumber ( ) );
		
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

}