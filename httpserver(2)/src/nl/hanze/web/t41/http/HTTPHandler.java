package nl.hanze.web.t41.http;

import java.io.*;

public interface HTTPHandler {
	public void handleRequest(InputStream in, OutputStream out);
}
