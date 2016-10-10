package Threads;

public class Thread_TimeKeeper implements Runnable {
	
	Thread_LinkScanner Owner;
	
	boolean threadSuspended;
	Thread t = null;
	//int i = 0;
	int Interval = 999;
	
	public Thread_TimeKeeper( Thread_LinkScanner Owner ) {
		//System.out.println("TimeKeeper created");
		this.Owner = Owner;
		
		start();
	}
	
	public void Startup() {
		//System.out.println("TimeKeeper started");
		

	}

	   public void init() {
		   
	   }

	   public void destroy() {
	   }

	   public void start() {
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
		   
	      try {
	         while (true) {
	        	//System.out.println("++i");
	            //++i;
	            //if ( i == 10 ) {
	            //   i = 0;
	            //}
	            if ( threadSuspended ) {
	               synchronized( this ) {
	                  while ( threadSuspended ) {
	                     wait();
	                  }
	               }
	            }
	            Owner.TimePast(  );
	            threadSuspended = true;
	         }
	      } catch (InterruptedException T) { 
				System.err.println("Thread_TimeKeeper");
				System.err.println ( "Throwable message: " + T.getMessage ( ) );
				System.err.println ( "Throwable cause: " + T.getCause ( ) );
				System.err.println ( "Throwable class: " + T.getClass ( ) );
	      }
	   }

}
