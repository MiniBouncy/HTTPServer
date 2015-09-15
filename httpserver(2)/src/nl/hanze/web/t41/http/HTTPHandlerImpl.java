package nl.hanze.web.t41.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.ForkJoinPool;

public class HTTPHandlerImpl implements HTTPHandler {

    private static final ForkJoinPool fjPool = new ForkJoinPool(); // make pool to give process to new request

    public void handleRequest(InputStream in, OutputStream out) {
		/*
		 ***  OPGAVE 4: 1c ***
		 stel de juiste bestand-typen in.
		*/
		
		HTTPRequest request = new HTTPRequest(in);
		HTTPRespons respons = new HTTPRespons(out);

        request.setUri();
        respons.setRequest(request);


        showDateAndTime();
        System.out.println(": " + request.getUri());

        fjPool.invoke(respons); // perform task and return stack trace in case of 500

		/*try {
			respons.sendResponse();			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	
	private void showDateAndTime () {
		DateFormat format = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss");
		Date date = new Date();
		System.out.print(format.format(date));
		
	}
}
