package Control;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import Objects.*;

public class Brain_Ordbok {

	Controller Class_Controller;
	Object_Phrase[] Ordliste;
	
	public Brain_Ordbok( Controller controller ) {

		this.Class_Controller = controller;
		
		Ordliste = new Object_Phrase[999];
		/*
		for ( int X = 0; X < Ordliste.length ; X++ ) {
			Ordliste[X] = new Object_Ord( "", 0 );
		}*/
		
		//Object_Ordbok_Miniatyrer Tempordbok = new Object_Ordbok_Miniatyrer();
		Object_Ordbok_Base Tempordbok = Class_Controller.OrdBok;
		for ( int x = 0 ; x < Tempordbok.Ord.length ; x++ ) {
			if ( Tempordbok.Ord[x] != null ) {
				Ordliste[x] = new Object_Phrase( Tempordbok.Ord[x].Ordet, Tempordbok.Ord[x].RelationValue, Tempordbok.Ord[x].SaveValue );
			}
		}
	}

	public Object_Phrase[] Get_Ordliste() {
		return Ordliste;
	}
	
	public void GoogleOrdliste() {
		//http://www.google.no/search?q=reaper+miniatures
		System.out.println( System.currentTimeMillis()+" Loader ordlista" );
		for ( int x = 0 ; x < Ordliste.length ; x++ ) {
			if ( Ordliste[x] != null ) {
				//System.out.println( "a "+x+" "+Ordliste[x].Ordet );
				if ( Ordliste[x].Ordet.equals( "" ) == false ) {
					//System.out.println( "b "+x+" "+Ordliste[x].Ordet );
					if ( Ordliste[x].RelationValue > 0 ) {
						//System.out.println( System.currentTimeMillis()+" Googler etter #"+x+" "+Ordliste[x].Ordet );
						URL New = null;
						try {
							New = new URL( "http://www.google.no/search?q="+Ordliste[x].Ordet );
						    URLConnection myURLConnection = New.openConnection();
						    myURLConnection.connect();
						} catch ( MalformedURLException e ) {     // new URL() failed
						} catch ( Exception T ) {
							CastErrors( T );
						}
						//System.out.println( "c "+New );
						if ( New != null ) {
							//System.out.println( "d "+New );
							Class_Controller.SaveURL( New.toString(), "Ordbok", 999 );
							//Class_Controller.SearchURL( new Object_Webpage( New, (Ordliste[x].Value*10), (Ordliste[x].Value*10)) );
						} else {
							System.err.println( this.getClass().getName() );
						}
			
						//System.out.println( System.currentTimeMillis()+" Googler etter #"+x+" "+Ordliste[x].Ordet+" #2" );
						New = null;
						try {
							New = new URL( "http://www.google.no/search?q="+Ordliste[x].Ordet.replaceAll(" ", "") );
							URLConnection myURLConnection = New.openConnection();
						    myURLConnection.connect();
						} catch ( MalformedURLException e ) {     // new URL() failed
						} catch ( Exception T ) {
							CastErrors( T );
						}
						if ( New != null ) {
							Class_Controller.SaveURL( New.toString(), "Ordbok", 999 );
							//Class_Controller.SearchURL( new Object_Webpage( New, (Ordliste[x].Value*10), (Ordliste[x].Value*10)) );
						} else {
							System.err.println( this.getClass().getName() );
						}
						
						//System.out.println( System.currentTimeMillis()+" Googler etter #"+x+" "+Ordliste[x].Ordet+" #3" );
						New = null;
						try {
							New = new URL( "http://www.google.no/search?q="+Ordliste[x].Ordet.replaceAll(" ", "-") );
							URLConnection myURLConnection = New.openConnection();
						    myURLConnection.connect();
						} catch ( MalformedURLException e ) {     // new URL() failed
						} catch ( Exception T ) {
							CastErrors( T );
						}
						if ( New != null ) {
							Class_Controller.SaveURL( New.toString(), "Ordbok", 999 );
							//Class_Controller.SearchURL( new Object_Webpage( New, (Ordliste[x].Value*10), (Ordliste[x].Value*10)) );
						} else {
							System.err.println( this.getClass().getName() );
						}
					}
				}
			}
		}
		System.out.println( System.currentTimeMillis()+" Ferdig å google etter ordlista" );
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
