package nl.hanze.web.t41.http;

import java.io.InputStream;

public class HTTPRequest {
	private String uri = "";
	private InputStream in;

	public HTTPRequest(InputStream in) {
		this.in = in;
	}

	public String getUri() {
		return uri;
	}

	public void setUri() {
		int index1, index2;
		String request = parseRequest();
		index1 = request.indexOf(' ');

		if (index1 != -1) {
			index2 = request.indexOf(' ', index1 + 1);
			if (index2 > index1)
				uri = request.substring(index1 + 1, index2);
		}
	}

	public void showRequest() {
		System.out.print(parseRequest());
	}

	private String parseRequest() {
		StringBuffer request = new StringBuffer(HTTPSettings.BUFFER_SIZE);
		int i;
		byte[] buffer = new byte[HTTPSettings.BUFFER_SIZE];

		try {
			i = in.read(buffer);
		} catch (Exception e) {
			e.printStackTrace();
			i = -1;
		}

		for (int j = 0; j < i; j++) {
			request.append((char) buffer[j]);
		}

		return request.toString();
	}

}
