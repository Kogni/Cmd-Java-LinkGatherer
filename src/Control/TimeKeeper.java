package Control;

import java.awt.*;


public class TimeKeeper implements Runnable {
	
	Controller Class_Controller;
	int i = 0;
	
	boolean threadSuspended;
	Thread t = null;
	int Interval = 0;
	int WaitFor = 0;
	
	public TimeKeeper( Controller Class_Controller, int Interval ) {
		System.out.println("TimeKeeper created");
		this.Class_Controller = Class_Controller;
		this.Interval = Interval;
		this.WaitFor = Interval;
		
		start();
	}
	
	public void Startup() {
		System.out.println("TimeKeeper started");
		
		start();
	}

	   public void init() {
		   System.out.println("TimeKeeper init");
	   }

	   public void destroy() {
	   }

	   public void start() {
		   System.out.println("TimeKeeper start");
		   if ( t == null ) {
			   t = new Thread( this );
			   threadSuspended = false;
			   t.start();
		   } else {
			  
	         if ( threadSuspended ) {
	            threadSuspended = false;
	            synchronized( this ) {
	               notify();
	            }
	         }
	      }
	   }

	   public void stop() {
	      threadSuspended = true;
	   }

	   public void run() {
		   System.out.println("TimeKeeper run");
	      try {
	         while (true) {
	        	 //System.out.println("TimeKeeper A "+System.currentTimeMillis());
	        	 WaitFor --;
	        	 if ( WaitFor <= 0 ) {
	        		 Class_Controller.TimeTick( "Timekeeper" );
	        		 WaitFor = Interval;
	        		 //System.out.println("TimeKeeper B "+System.currentTimeMillis());
	        	 }
	        	 
	            if ( threadSuspended ) {
	            	
	               synchronized( this ) {
	                  while ( threadSuspended ) {
	                	  //System.out.println("TimeKeeper C "+System.currentTimeMillis());
	                	  Class_Controller.TimeTick( "Timekeeper" );
	                     wait();
	                  }
	               }
	            }
	            //System.out.println("TimeKeeper D "+System.currentTimeMillis());
	            //Class_Controller.TimeTick( "Timekeeper" );
	            t.sleep( Interval );  // interval given in milliseconds
	         }
	      } catch (InterruptedException T) { 
				System.out.println("Kunne ikke loope timer");
				System.out.println ( "Throwable message: " + T.getMessage ( ) );
				System.out.println ( "Throwable cause: " + T.getCause ( ) );
				System.out.println ( "Throwable class: " + T.getClass ( ) );
	      }
	   }

	   public void paint( Graphics g ) {

	   }
}
