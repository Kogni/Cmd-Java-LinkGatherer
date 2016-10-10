package Objects;

import java.net.URL;

public class Object_Link {
	
	URL url;
	boolean SearchedForLinks = false;
	public int relationValue = 0;

	public Object_Link( URL url, int relationValue ) {
		this.url = url;
		this.relationValue = relationValue;
	}

	public URL GetURL() {
		return url;
	}

	public boolean GetSearched() {
		return SearchedForLinks;
	}

	public void SetSearched(boolean b) {
		//System.out.println( "setter searched til "+b );
		SearchedForLinks = true;
	}
	
}
