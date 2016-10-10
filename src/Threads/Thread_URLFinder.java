package Threads;

import java.util.StringTokenizer;
import Control.Controller;

public class Thread_URLFinder extends Thread {
	
	Controller Class_Controller;
	Thread_LinkScanner Owner;
	long ThreadstartTime = System.currentTimeMillis();
	String Data;
	String URLsource;
	
	public Thread_URLFinder( Controller Class_Controller, String Data, Thread_LinkScanner Owner, String URLsource ) {
		//System.out.println ( System.currentTimeMillis()+": Thread_DataCleaner init" );
		//System.out.println ( System.currentTimeMillis()+" +Thread_DataCleaner created: "+this.getName() );
		
		this.Class_Controller = Class_Controller;
		this.Owner = Owner;
		this.Data = Data;
		this.URLsource = URLsource;
		try {
			//run();
		} catch ( Exception T ) {
			CastErrors( T );
		}
	}

	public void run() {
		try {
			SplitLine1( Data );
			//long ThreadendTime = System.currentTimeMillis();
			//System.out.println("Time (ms) : " + (ThreadendTime - ThreadstartTime));
		} catch ( Exception T ) {
			CastErrors( T );
		}
		//this.Class_Controller.TimeTick( "Thread_URLFinder completed" );
		//System.out.println(System.currentTimeMillis()+"- Thread_DataCleaner finished: "+this.getName());
		/*if ( URLsource.equals( "http://www.google.no/search?q=miniatyr")) {
			System.out.println(System.currentTimeMillis()+"- Thread_URLFinder finished: "+this.getName());
		}*/
	}
	
	private void SplitLine1( String Pagecontent ) {
		StringTokenizer token = new StringTokenizer(Pagecontent);
		int count = 0;
		String Frase;
		while ( token.hasMoreTokens() ){
			Frase = token.nextToken(); //<- Henter 1 og 1 string fra pagecontent
			//System.out.println("Tokens left: "+token.countTokens());
			//Line = CleanHTML( Line );
			Frase = OnlyURLS( Frase ); //<-sletter strings som ikke inneholder noen url, og returnerer f.o.m http dersom stringen inneholder en url
			//String Temp1 = Frase;
			Frase = CleanRightofURL( Frase ); // <-Sletter alt på høyresiden av Frase, som ikke er en del av url.
			//String Temp2 = Frase;
			Frase = OnlyURLS( Frase );
			if ( Frase.equals("") == false ) { //<- betyr at Frase inneholder en url
				count++;
				//SplitLine2( Line );
				//System.out.println("før CleanRightofURL: "+Temp1+" etter: "+Temp2);
				//System.out.println( "Class_Controller="+Class_Controller );
				if ( URLsource.equals( "http://www.google.no/search?q=miniatyr")) {
					//System.out.println ( System.currentTimeMillis()+" Thread_DataCleaner a "+this.getName()+" "+URLsource+" "+this.getName() );
				}
				this.Class_Controller.SaveURL( Frase, "Thread searching", Owner.RelationValue );
				if ( URLsource.equals( "http://www.google.no/search?q=miniatyr")) {
					//System.out.println ( System.currentTimeMillis()+" Thread_DataCleaner b "+this.getName()+" "+URLsource+" "+this.getName() );
				}
				if ( URLsource.equals( "http://www.google.no/search?q=miniatyr")) {
					//System.out.println ( System.currentTimeMillis()+" Thread_DataCleaner c "+this.getName()+" "+Frase );
					//System.out.println("Tokens left: "+token.countTokens());
				}
			}
		}
		//System.out.println("Number of URLs 1: "+ count);
		/*if ( URLsource.equals( "http://www.google.no/search?q=miniatyr")) {
			System.out.println ( System.currentTimeMillis()+" Thread_URLFinder d "+this.getName()+" "+URLsource );
		}*/
	}
	
	private void SplitLine2( String Line ) {
		StringTokenizer token = new StringTokenizer(Line);
		int count = 0;
		while ( token.hasMoreTokens() ){
			
			Line = token.nextToken();
			Line = OnlyURLS( Line );
			Line = CleanRightofURL( Line );
			if ( Line.equals("") == false ) {
				count++;
				System.out.println("SplitLine2: "+Line+" splits: "+(count-1)+" "+this.getName());
			}
			
		}
		//System.out.println("Number of URLs 2: "+ count);
	}
	
	private static String CleanHTML( String source ) {
		source = replaceAll( source, "Ã¥", "å" );
		source = replaceAll( source, "Ã¸", "ø" );
		source = replaceAll( source, "<p>", "" );
		source = replaceAll( source, "</p>", "" );
		source = replaceAll( source, "<strong>", "" );
		source = replaceAll( source, "</strong>", "" );
		source = replaceAll( source, "<br>", "" );
		source = replaceAll( source, "</br>", "" );
		source = replaceAll( source, "<br", "" );
		source = replaceAll( source, "<html>", "" );
		source = replaceAll( source, "</html>", "" );
		source = replaceAll( source, "<body>", "" );
		source = replaceAll( source, "</body>", "" );
		source = replaceAll( source, "src=", "" );
		source = replaceAll( source, "<script>", "" );
		source = replaceAll( source, "</script>", "" );
		source = replaceAll( source, "xmlns=", "" );
		source = replaceAll( source, "profile=", "" );
		source = replaceAll( source, "href=", "" );
		source = replaceAll( source, "content=", "" );
		source = replaceAll( source, "action=", "" );
		source = replaceAll( source, "value=", "" );
		
		source = replaceAll( source, "/>", "" );
		source = replaceAll( source, ">", "" );
		source = replaceAll( source, "'", "" );
		source = replaceAll( source, "\"", "" );
		source = replaceAll( source, ")", "" );

		return source;
	}
	
	
	private static String replaceAll( String source, String toReplace, String replacement ) {
		int idx = source.lastIndexOf( toReplace );
		if ( idx != -1 ) {
			StringBuffer ret = new StringBuffer( source );
			ret.replace( idx, idx+toReplace.length(), replacement );
			while( (idx=source.lastIndexOf(toReplace, idx-1)) != -1 ) {
				ret.replace( idx, idx+toReplace.length(), replacement );
			}
			source = ret.toString();
		}

		return source;
	}
	
	private String OnlyURLS( String source ) {
		int idx = source.indexOf( "http://" );
		if ( idx == -1 ) {
			return "";
		} else {
			String Sub = source.substring(idx, source.length());
			Sub = CleanRightofURL( Sub );
			Sub = IgnoreURLS( Sub );
			return Sub+" ";
		}
	}
	
	private String IgnoreURLS( String source ) {
		
		if ( source.equals("http:") ) {
			source = "";
			return source;
		}
		
		if ( source.indexOf( ".png" ) == -1 ) {
		} else {
			if ( source.indexOf( ".png" ) == (source.length()-".png".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}
		
		if ( source.indexOf( ".gif" ) == -1 ) {
		} else {
			if ( source.indexOf( ".gif" ) == (source.length()-".gif".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}
		
		if ( source.indexOf( ".jpg" ) == -1 ) {
		} else {
			if ( source.indexOf( ".jpg" ) == (source.length()-".jpg".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}
		
		if ( source.indexOf( ".css" ) == -1 ) {
		} else {
			if ( source.indexOf( ".css" ) == (source.length()-".css".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}
		
		if ( source.indexOf( ".js" ) == -1 ) {
		} else {
			if ( source.indexOf( ".js" ) == (source.length()-".js".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}
		
		if ( source.indexOf( ".rdf" ) == -1 ) {
		} else {
			if ( source.indexOf( ".rdf" ) == (source.length()-".rdf".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}
		
		if ( source.indexOf( ".pdf" ) == -1 ) {
		} else {
			if ( source.indexOf( ".pdf" ) == (source.length()-".pdf".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}
		
		if ( source.indexOf( ".ico" ) == -1 ) {
		} else {
			if ( source.indexOf( ".ico" ) == (source.length()-".ico".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}
		
		if ( source.indexOf( ".php" ) == -1 ) {
		} else {
			if ( source.indexOf( ".php" ) == (source.length()-".php".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}
		
		if ( source.indexOf( ".php4" ) == -1 ) {
		} else {
			if ( source.indexOf( ".php4" ) == (source.length()-".php4".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}
		
		if ( source.indexOf( ".py" ) == -1 ) {
		} else {
			if ( source.indexOf( ".py" ) == (source.length()-".py".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}

		if ( source.indexOf( ".dtd" ) == -1 ) {
		} else {
			if ( source.indexOf( ".dtd" ) == (source.length()-".dtd".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}
		
		if ( source.indexOf( ".epl" ) == -1 ) {
		} else {
			if ( source.indexOf( ".epl" ) == (source.length()-".epl".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}
		
		if ( source.indexOf( ".jspa" ) == -1 ) {
		} else {
			if ( source.indexOf( ".jspa" ) == (source.length()-".jspa".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}

		if ( source.indexOf( ".txt" ) == -1 ) {
		} else {
			if ( source.indexOf( ".txt" ) == (source.length()-".txt".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}
		
		if ( source.indexOf( ".swf" ) == -1 ) {
		} else {
			if ( source.indexOf( ".swf" ) == (source.length()-".swf".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}
		
		if ( source.indexOf( ".asp" ) == -1 ) {
		} else {
			if ( source.indexOf( ".asp" ) == (source.length()-".asp".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}
		
		if ( source.indexOf( ".aspx" ) == -1 ) {
		} else {
			if ( source.indexOf( ".aspx" ) == (source.length()-".aspx".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}
		
		if ( source.indexOf( ".mil" ) == -1 ) {
		} else {
			if ( source.indexOf( ".mil" ) == (source.length()-".mil".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}
		
		if ( source.indexOf( ".mvc" ) == -1 ) {
		} else {
			if ( source.indexOf( ".mvc" ) == (source.length()-".mvc".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}
		
		if ( source.indexOf( ".g" ) == -1 ) {
		} else {
			if ( source.indexOf( ".g" ) == (source.length()-".g".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}
		
		if ( source.indexOf( ".shtml" ) == -1 ) {
		} else {
			if ( source.indexOf( ".shtml" ) == (source.length()-".shtml".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}
		
		if ( source.indexOf( ".mv" ) == -1 ) {
		} else {
			if ( source.indexOf( ".mv" ) == (source.length()-".mv".length()) ) {
				source = "";
				return source;
			} else {
				
			}
		}
		
		return source;
	}
	
	
	private String CleanRightofURL( String source ) {

		if ( source.indexOf( ";" ) == -1 ) {
		} else {
			source = source.substring(0, source.indexOf( ";" ));
		}
		
		if ( source.indexOf( "?" ) == -1 ) {
		} else {
			source = source.substring(0, source.indexOf( "?" ));
		}
		
		if ( source.indexOf( "\"" ) == -1 ) {
		} else {
			source = source.substring(0, source.indexOf( "\"" ));
		}
		
		if ( source.indexOf( "#" ) == -1 ) {
		} else {
			source = source.substring(0, source.indexOf( "#" ));
		}
		
		if ( source.indexOf( " " ) == -1 ) {
		} else {
			source = source.substring(0, source.indexOf( " " ));
		}
		
		if ( source.indexOf( "///" ) == -1 ) {
		} else {
			source = source.substring(0, source.indexOf( "///" ));
		}
		
		if ( source.indexOf( "=" ) == -1 ) {
		} else {
			source = source.substring(0, source.indexOf( "=" ));
		}
		
		if ( source.indexOf( ")" ) == -1 ) {
		} else {
			source = source.substring(0, source.indexOf( ")" ));
		}
		
		if ( source.indexOf( "<" ) == -1 ) {
		} else {
			source = source.substring(0, source.indexOf( "<" ));
		}
		
		if ( source.indexOf( ">" ) == -1 ) {
		} else {
			source = source.substring(0, source.indexOf( ">" ));
		}
		
		if ( source.indexOf( "'" ) == -1 ) {
		} else {
			source = source.substring(0, source.indexOf( "'" ));
		}
		
		if ( source.indexOf( "&" ) == -1 ) {
		} else {
			source = source.substring(0, source.indexOf( "&" ));
		}
		
		if ( source.indexOf( ".." ) == -1 ) {
		} else {
			source = source.substring(0, source.indexOf( ".." ));
		}
		
		if ( source.indexOf( "(" ) == -1 ) {
		} else {
			source = source.substring(0, source.indexOf( "(" ));
		}
		
		if ( source.indexOf( ")" ) == -1 ) {
		} else {
			source = source.substring(0, source.indexOf( ")" ));
		}
		
		if ( source.indexOf( "[" ) == -1 ) {
		} else {
			source = source.substring(0, source.indexOf( "[" ));
		}
		
		if ( source.indexOf( "]" ) == -1 ) {
		} else {
			source = source.substring(0, source.indexOf( "]" ));
		}
		/*
		idx = source.lastIndexOf("/");
		String Temp = "http://";
		if ( idx == -1 ) {
		} else {
			if ( idx > Temp.length() ) {
				source = source.substring(0, idx);
			} else {
				
			}
		}*/
		
		return source;
	}
	
	private void CastErrors( Exception T ) {
		System.err.println("Thread_LinkFinder");
		System.err.println ( "Throwable message: " + T.getMessage ( ) );
		System.err.println ( "Throwable cause: " + T.getCause ( ) );
		System.err.println ( "Throwable class: " + T.getClass ( ) );
		
		System.err.println ( "Origin stack "+1+": ");
		System.err.println ( "Class: " + T.getStackTrace ( )[0].getClassName ( ) );
		System.err.println ( "Method: " + T.getStackTrace ( )[0].getMethodName ( ) );
		System.err.println ( "Line: " + T.getStackTrace ( )[0].getLineNumber ( ) );
		
		System.err.println ( "Origin stack "+1+": ");
		System.err.println ( "Class: " + T.getStackTrace ( )[1].getClassName ( ) );
		System.err.println ( "Method: " + T.getStackTrace ( )[1].getMethodName ( ) );
		System.err.println ( "Line: " + T.getStackTrace ( )[1].getLineNumber ( ) );
		/*
		for ( int y = 2 ; y < T.getStackTrace().length ; y++ ) {
			System.err.println (" ");
			System.err.println ( "Origin stack "+y+": ");
			System.err.println ( "Class: " + T.getStackTrace ( )[y].getClassName ( ) );
			System.err.println ( "Method: " + T.getStackTrace ( )[y].getMethodName ( ) );
			System.err.println ( "Line: " + T.getStackTrace ( )[y].getLineNumber ( ) );
		}*/
	}
}