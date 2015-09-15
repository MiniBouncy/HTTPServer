package nl.hanze.web.t41.runner;

import nl.hanze.web.t41.http.HTTPHandlerImpl;
import nl.hanze.web.t41.http.HTTPListener;
import nl.hanze.web.t41.http.HTTPSettings;

import java.util.Scanner;

public class HTTPRunner {
	public static void main (String args[]) {
		/* 
		  *** OPGAVE 1.1 ***
		  zorg ervoor dat het port-nummer en de basis-directory vanuit de command-line kunnen worden meegegeven.
		  LET OP: de default-waarden moet je nog wel instellen in de Settings-klasse.
		*/
		int portnumber = HTTPSettings.PORT_NUM;
	/*	int portnumber = 0;
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter portnumber: ");
        portnumber = sc.nextInt();

        System.out.print("Enter root-directory: ");
        String directory = sc.next();

        System.out.println("portnumber" +portnumber+ "directory:" +directory);

*/
		
	    try {
	    	HTTPListener listener = new HTTPListener (portnumber, new HTTPHandlerImpl());
	    	listener.startUp();	    	
	    } catch (Exception e) {
			e.printStackTrace();
		}
	}
}
