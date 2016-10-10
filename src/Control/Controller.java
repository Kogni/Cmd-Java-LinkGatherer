package Control;

import Objects.Object_Ordbok_Base;
import Objects.Object_Phrase;
import Objects.Object_Webpage;
import Threads.Thread_LinkScanner;

public class Controller{

	//Brain_Links Class_Brain_Links;
	public Brain_PageManager	Class_Brain_PageManager;
	public Html2Text			Class_Parser;
	Brain_IO					Class_Brain_IO;
	Brain_Ordbok				Class_Brain_Ordbok;

	TimeKeeper					Class_TimeKeeper;
	int							TickInterval		= 40;
	//Thread_LinkScanner Threads[];
	int							ThreadsRunning		= 0;
	int							ThreadsStarted		= 0;
	int							ThreadsCompleted	= 0;

	Object_Ordbok_Base			OrdBok;

	boolean						Doneloading			= false;

	public int					InterestBorder		= 30;

	String						SearchPhrase		= "bipolar lidelse";

	public Controller() {

		System.out.println( System.currentTimeMillis() + ": SearchPhrase = " + SearchPhrase );
		OrdBok = new Object_Ordbok_Base( SearchPhrase );
		//Class_Brain_Links = new Brain_Links( this );
		Class_Brain_PageManager = new Brain_PageManager( this );
		Class_Brain_Ordbok = new Brain_Ordbok( this );
		Class_TimeKeeper = new TimeKeeper( this, TickInterval );
		//Threads = new Thread_LinkFinder[2];
		Class_Brain_IO = new Brain_IO( this );
		Class_Parser = new Html2Text();

		Class_Brain_Ordbok.GoogleOrdliste();
		Class_Brain_IO.Load();
		Doneloading = true;
		Class_Brain_PageManager.OrderNewSearch();
	}

	public void TimeTick( String Sender ) {
		//System.out.println( System.currentTimeMillis()+": TimeTick fra "+Sender );
		if ( Sender.equals( "LinkThread completed" ) ) {
			ThreadsRunning--;
			ThreadsCompleted++;
			//System.out.println( "ThreadsCompleted="+ThreadsCompleted );
		}

		//for ( int X = 0 ; X < 3 ; X++ ) {
		//if ( ThreadsRunning < 10 ) {
		//System.out.println( "ber om å motta url å searche" );
		//Class_Brain_Links.OrderLinkGathering();
		Class_Brain_PageManager.OrderNewSearch();
		//}
		//}

	}

	public void SearchURL( Object_Webpage temp ) {

		//Thread_LinkFinder Temp = new Thread_LinkFinder( this, object_Link );
		//System.out.println( Class_Brain_IO +" "+Class_Brain_IO.DoneLoading );
		//System.out.println( this.getClass().toString()+" SearchURL A "+temp.Get_URL().toString() );
		if ( Class_Brain_IO == null ) {
			return;
		}
		else {
			if ( Doneloading == false ) {
				return;
			}
		}
		if ( temp == null ) {
			return;
		}
		else if ( temp.Get_URL().toString().equals( "" ) ) {
			return;
		}
		long TidA = System.currentTimeMillis();
		System.out.println( this.getClass().toString() + " SearchURL B " + temp.Get_URL().toString() );
		temp.Set_Searched();
		//System.out.println( System.currentTimeMillis()+": Received url to search:"+temp.Get_URL()+" "+temp.Get_LinkedRelationValue() );
		long TidB = System.currentTimeMillis();
		//System.out.println( System.currentTimeMillis()+" SearchURL del A Tid="+(TidB-TidA) );
		Thread thread = new Thread_LinkScanner( this, temp );
		long TidC = System.currentTimeMillis();
		//System.out.println( System.currentTimeMillis()+" SearchURL del B Tid="+(TidC-TidB) );
		thread.start();
		//Thread_LinkFinder.start();
		ThreadsRunning++;
		ThreadsStarted++;
		long TidD = System.currentTimeMillis();
		//System.out.println( System.currentTimeMillis()+" SearchURL del C Tid="+(TidD-TidC) );
		//System.out.println( System.currentTimeMillis()+" Nytt søk startet. Tid="+(TidD-TidA) );
		//System.out.println( "ThreadsStarted="+ThreadsStarted);
		//System.out.println( this.getClass().toString()+" SearchURL C "+temp.Get_URL().toString()+" "+temp.Searched+" "+temp );
	}

	public void SaveURL( String line, String Sender, int LinkedRelationValue ) {
		if ( line.equals( "" ) ) {
			return;
		}
		else if ( line.equals( "http:/" ) ) {
			return;
		}
		else if ( line.equals( "http:" ) ) {
			return;
		}
		//System.out.println( System.currentTimeMillis()+" SaveURL fra "+Sender+": "+line );
		//System.out.println( Class_Brain_Links + " " + line );
		//Class_Brain_Links.InsertLink( line, relationValue );
		//if ( line.endsWith("http://www.google.no/search?q=miniatyr")) {
		//System.out.println( System.currentTimeMillis()+" SaveURL fra "+Sender+": "+line+" før lagring" );
		//}
		Class_Brain_PageManager.InsertLink( line, LinkedRelationValue, "controller.SaveURL" );
		//if ( line.equals("http://www.google.no/search?q=miniatyr")) {
		//System.out.println( System.currentTimeMillis()+" SaveURL fra "+Sender+": "+line+" etter lagring" );
		//}
	}

	public void SaveDatabase( Object_Webpage UpdatedPage ) {
		//System.out.println( "ber om å lagre database "+Class_Brain_IO );
		if ( Class_Brain_IO != null ) {
			if ( Doneloading == true ) {
				//if ( Class_Brain_PageManager.)
				//Class_Brain_IO.SaveAll( Class_Brain_Links.LinkArray );
				System.out.println( "ber om å lagre database " );
				Class_Brain_IO.SaveAll( Class_Brain_PageManager.GetLinksToSave() );
				//System.out.println( "skal ha lagret database " );
			}
		}
	}

	public Object_Phrase[] GetOrdliste() {
		return this.Class_Brain_Ordbok.Get_Ordliste();
	}

	public String Get_SearchPhrase() {
		return SearchPhrase;
	}

}
