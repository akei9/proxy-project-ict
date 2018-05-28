package proxyproject;

import java.io.*;

public class HttpRequest {
    
    final static String CRLF = "\r\n";
    final static int HTTP_PORT = 80;
    String method;
    String URI;
    String version;
    String headers = "";
    private String host;
    private int port;

    public HttpRequest(BufferedReader from) {
	
    	String firstLine = "";
	
        try {
            firstLine = from.readLine();
            String[] tmp = firstLine.split(" ");

            /* GET = GET http://www.google.pt HTTP/1.0 */
            method = tmp[0]; /* method GET */
            URI =  tmp[1]; /* URI */
            version =  tmp[2]; /* HTTP version */

            try {
                String line = from.readLine();
                while (line.length() != 0) {
                    
                    headers += line + CRLF;
                    
                    if (line.startsWith("Host:")) {
                        tmp = line.split(" ");
                        if (tmp[1].indexOf(':') > 0) {	//  "Host :" or "Host:"
                            String[] tmp2 = tmp[1].split(":");
                            host = tmp2[0];
                            port = Integer.parseInt(tmp2[1]);
                        } else {
                            host = tmp[1];
                            port = HTTP_PORT;
                        }
                        
                    }
                    line = from.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
	} catch (IOException e) {
            e.printStackTrace();
	}
    
    }

    public String getHost() {
	return host;
    }

    public int getPort() {
	return port;
    }
 
    @Override
    public String toString() {
    	String req = "";
    	req = method + " " + URI + " " + version + CRLF;
    	req += headers;
    	req += "Connection: close" + CRLF;
    	req += CRLF;
	return req;
    } 

}