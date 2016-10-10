package Control;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Formatter;

import Objects.Object_Database;
import Objects.Object_Webpage;

public class Brain_IO {

	Controller Class_Controller;
	String Filnavn = "URLs.txt";
	
	File filen;
	String Content;
	FileInputStream fstream;
	DataInputStream in;
	
	String filenameFrom;
	String filenameTo;
	
	boolean DoneLoading = false;
	
	public Brain_IO( Controller Class_Controller ) {
		System.out.println( "Brain_IO started");
		this.Class_Controller = Class_Controller;
		
	}
	/*
	public void Save( String URL ) {
		try {
			FileOutputStream appendFilen = new FileOutputStream ( Filnavn, true );
			PrintStream utfil = new PrintStream ( appendFilen );

			//Formatter output = new Formatter( Filnavn );
			
			//output.format( URL );
			//output.format("\n");
			utfil.println( " \n "+URL+" \n ");
			
			utfil.close ( );
			System.out.println( "URL saved");
		} catch ( Exception T ) {
			System.err.println("Kunne ikke lese fil b");
			System.err.println ( "Throwable message: " + T.getMessage ( ) );
			System.err.println ( "Throwable cause: " + T.getCause ( ) );
			System.err.println ( "Throwable class: " + T.getClass ( ) );
		}
	}*/
	
	private void Backup() {
		try {
			filen = new File ( Filnavn );
			if ( !filen.exists() ) {
				return;
			}
			
			FileOutputStream appendFilen = new FileOutputStream ( Filnavn+".Backup.txt", true );
			PrintStream utfil = new PrintStream ( appendFilen );
			
			FileInputStream fstream2 = new FileInputStream ( filen );
			DataInputStream in2 = new DataInputStream ( fstream2 );
			
			//Formatter output = new Formatter( Filnavn+".Backup.txt" );
			
			while ( in2.available() > 0 ) {
				utfil.println( in2.readLine() );
				//utfil.append( in2.readLine()+" \n");
				//output.format( in2.readLine() );
				//output.format("\n");
			}
			utfil.close ( );
			in2.close ( );
		} catch ( Exception T) {
			CastErrors( T );
		}
	}
	
	public void Load() {
		try {
			System.out.println( System.currentTimeMillis()+" Loader URLr fra fil "+Filnavn );
			filen = new File ( Filnavn );
			if ( !filen.exists() ) {
				filen.createNewFile();
			}
			
			FileInputStream fstream2 = new FileInputStream ( filen );
			DataInputStream in2 = new DataInputStream ( fstream2 );
			while ( in2.available() > 0 ) {
				Class_Controller.SaveURL( in2.readLine(), "Loader", 9999 ) ;
				//System.out.println( "Loadet fra fil: "+in2.readLine() );
			}
			System.out.println( System.currentTimeMillis()+" Ferdig å loade URLr fra fil "+Filnavn );
			
			in2.close ( );
			Backup();
			DoneLoading = true;
			Class_Controller.TimeTick( "IO" );
		} catch ( Exception T) {
			CastErrors( T );
		}
	}

	public void SaveAll( Object_Webpage[] array ) {
		System.out.println( "SaveAll "+array );
		if ( array == null ) {
			return;
		}
		//System.out.println( "SaveAll "+array );
		int Count = 0;
		/*
		Object_Database SortedDatabase = new Object_Database();
		
		for ( int X = 0; X < array.length ; X++ ) {
			if ( array[X] != null ) {
				//if ( array[X].Get_Searched() == true ) {
					if ( array[X].Get_SelfRelationValue() > Class_Controller.InterestBorder ) {
						//SortedDatabase.InsertLink( FigureRootsite( array[X].Get_URL().toString() ), array[X].Get_SelfRelationValue() );
						Count ++;
						SortedDatabase.InsertLink( array[X].Get_URL().toString(), 0, array[X].Get_SelfRelationValue() );
					}
				//}
			}
		}
		System.out.println( System.currentTimeMillis()+" pages to save: "+Count+" links");

		Object_Webpage[] LinkArray = SortedDatabase.GetDatabase();*/
		Object_Webpage[] LinkArray = array;
		
		try {
			filen = new File ( Filnavn );
			filen.delete();
			if ( !filen.exists() ) {
				filen.createNewFile();
			}
			
			PrintStream utfil;
			FileOutputStream appendFilen = new FileOutputStream ( Filnavn, true );
			utfil = new PrintStream ( appendFilen );

			Count = 0;
			for ( int X = 0; X < LinkArray.length ; X++ ) {
				if ( LinkArray[X] != null ) {
					//if ( array[X].relationValue > (Class_Controller.InterestBorder*3) ) {
					//System.out.println( LinkArray[X].Get_URL().toString()+" "+LinkArray[X].Get_SelfRelationValue());
					if ( LinkArray[X].Get_SelfRelationValue() > Class_Controller.InterestBorder ) {
						System.out.println( "-> Lagrer til fil: "+Class_Controller.Class_Brain_PageManager.FigureRootsite( LinkArray[X], "BrainIO" ) );
						utfil.println( "\n"+LinkArray[X].Get_Extract()+" - "+LinkArray[X].Get_URL().toString()+"\n");
						//utfil.println( "\n"+LinkArray[X].Get_URL().toString());
						Count ++;
					}
				}
			}
			utfil.close ( );
			System.out.println( System.currentTimeMillis()+" Database saved: "+Count+" links");
		} catch ( IOException T ) {
			CastErrors( T );
		} catch ( Exception T ) {
			CastErrors( T );
		}
	}
	
	private void CastErrors( Exception T ) {
		System.err.println( this.getClass().getName() );
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
