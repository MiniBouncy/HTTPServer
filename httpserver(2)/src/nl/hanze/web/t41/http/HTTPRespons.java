package nl.hanze.web.t41.http;


import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

public class HTTPRespons extends RecursiveAction {
	private OutputStream out;
	private HTTPRequest request;

	public HTTPRespons(OutputStream out) {
		this.out = out;
	}

	public void setRequest(HTTPRequest request) {
		this.request = request;
	}

	public void sendResponse() throws IOException {
		byte[] bytes = new byte[HTTPSettings.BUFFER_SIZE];
		FileInputStream fis = null;
		String fileName = request.getUri();

		try {		
			File file = new File(HTTPSettings.DOC_ROOT, fileName);			
			FileInputStream inputStream = getInputStream(file);
			
			if (file.exists()) out.write(getHTTPHeader(fileName)); 
			else out.write(getHTTPHeader(""));
			String fileType = getFileType(fileName);
            if(file.exists() && Arrays.asList(HTTPSettings.SUPPORTED_IMGTYPES).contains(fileType)) { // check if imgtype given is in allowed list
                BufferedImage img = ImageIO.read(file); // if so, read it

                String imgStr = encode(img, fileType); // encode it in base64
                out.write(imgStr.getBytes()); // print the encoding
            } else{ // if file is not supported
                int ch = inputStream.read(bytes, 0, HTTPSettings.BUFFER_SIZE);
                while (ch != -1) {
                    out.write(bytes, 0, ch);
                    ch = inputStream.read(bytes, 0, HTTPSettings.BUFFER_SIZE);
                }
            }

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				fis.close();
		}

	}
	
	private FileInputStream getInputStream (File file) throws IOException {
		FileInputStream fis = null;


		/*
		  *** OPGAVE 4: 1b ***
		  Stuur het bestand terug wanneer het bestaat; anders het standaard 404-bestand.
		*/
		try{
            fis = new FileInputStream(file);
        }catch(FileNotFoundException e){
            File notFound = new File(HTTPSettings.DOC_ROOT, HTTPSettings.FILE_NOT_FOUND);
            fis = new FileInputStream(notFound);
        }
        return fis;
	}

	private byte[] getHTTPHeader(String fileName) throws IOException {
		String fileType = getFileType(fileName);		
		String header = "Date: " + HTTPSettings.getDate() + "Server: Barts eigen server\r\n";
        File file = new File(HTTPSettings.DOC_ROOT,fileName); // make new file to check below conditions

		/*
		 *** OPGAVE 4: 1b, 1c en 1d
		   zorg voor een goede header:
		   200 als het bestand is gevonden;
		   404 als het bestand niet bestaat
		   500 als het bestand wel bestaat maar niet van een ondersteund type is
		   
		   zorg ook voor ene juiste datum en tijd, de juiste content-type en de content-length.
		*/
        String status;
        if(fileName.equals("")){
            status = "404 Not found";
            file = new File (HTTPSettings.DOC_ROOT, HTTPSettings.FILE_NOT_FOUND); // give standard 404 file
        } else if(!Arrays.asList(HTTPSettings.SUPPORTED_FILETYPES).contains(fileType) && !Arrays.asList(HTTPSettings.SUPPORTED_IMGTYPES).contains(fileType)){ // check if fileType is supported, or not found
            status = "500 Internal Server Error";
            file = new File(HTTPSettings.DOC_ROOT, HTTPSettings.FILE_NOT_FOUND);
        } else{
            status = "200 Request OK";
        }

        long contentLength = file.length();
        FileNameMap map = URLConnection.getFileNameMap(); // MIME type string
        String contentType = map.getContentTypeFor("file://" + fileName); // get content by searching in path with fileName
        // print header

        header += "HTTP status: " + status + "\n"
                + "Content-Type: " +contentType + "\n"
                + "Content-length " + contentLength + "\n";
        System.out.println("Server Response: \n" + header);


        byte[] rv = header.getBytes();
        return rv;

       /* HTTPHandlerImpl handler = new HTTPHandlerImpl();
        DateFormat format = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss");
        Date date = new Date();

        if(!fileName.equals("")){
            header += "Status: 404";
            header += "Date and time: " + format.format(date);
        }*/
       // handler.handleRequest(getInputStream(sendResponse().file), out);

	}

	private String getFileType(String fileName) {
		int i = fileName.lastIndexOf(".");
		String ext = "";
		if (i > 0 && i < fileName.length() - 1) {
			ext = fileName.substring(i + 1);
		}

		return ext;
	}

	private void showResponse(byte[] respons) {
		StringBuffer buf = new StringBuffer(HTTPSettings.BUFFER_SIZE);

		for (int i = 0; i < respons.length; i++) {
			buf.append((char) respons[i]);
		}
		System.out.print(buf.toString());

	}

    /**
     * Encode supported imgType to string
     * @param img that needs to be encoded
     * @param type like gif, png,jpeg jpg, pdf
     * @return encoded img string
     */

    public static String encode(BufferedImage img, String type){
        String imgStr = null;
        ByteArrayOutputStream outStr = new ByteArrayOutputStream();

        try{
         //   ImageIO.write(img, type, outStr);
            byte[] imgBytes = outStr.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imgStr = encoder.encode(imgBytes);
            outStr.close();
        } catch(IOException e){
            e.printStackTrace();
        }
        return imgStr;
    }

    @Override
    protected void compute() {
        byte[] bytes = new byte[HTTPSettings.BUFFER_SIZE];
        FileInputStream fis = null;
        String fileName = request.getUri();

        try {
            File file = new File(HTTPSettings.DOC_ROOT, fileName);
            FileInputStream inputStream = getInputStream(file);

            if (file.exists()) {
                out.write(getHTTPHeader(fileName));
            } else {
                out.write(getHTTPHeader(""));
            }
            String fileType = getFileType(fileName);
            if (file.exists() && Arrays.asList(HTTPSettings.SUPPORTED_IMGTYPES).contains(fileType)) {
                BufferedImage img = ImageIO.read(file);
                String imgStr = encode(img, fileType);
                System.out.println(imgStr);
                out.write(imgStr.getBytes());
            } else {

                int ch = inputStream.read(bytes, 0, HTTPSettings.BUFFER_SIZE);
                while (ch != -1) {
                    out.write(bytes, 0, ch);
                    ch = inputStream.read(bytes, 0, HTTPSettings.BUFFER_SIZE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
