package proxyproject;

import java.io.*;

public class HttpResponse {
    final static String CRLF = "\r\n";
    final static int BUF_SIZE = 8192;
    final static int MAX_OBJECT_SIZE = 300000;
    
    int status;
    String version;
    String statusLine = "";
    String headers = "";
    String type = "";
    byte[] body = new byte[MAX_OBJECT_SIZE];
    private int length = -1;
    
    public HttpResponse(BufferedReader fromServer) {
        boolean gotStatusLine = false;
        try {
            String line = fromServer.readLine();
            
            while (line.length() != 0) {
                if (!gotStatusLine) {
                    statusLine = line;
                    gotStatusLine = true;
                } else 
                    headers += line + CRLF;

                if (line.startsWith("Content-Length:")) {
                    String[] tmp = line.split(" ");
                    length = Integer.parseInt(tmp[1]);
                }

                if (line.startsWith("Content-Type:")) {
                    String[] tmp = line.split(" ");
                    String tmp2 = tmp[1];
                    String[] tmp3 = tmp2.split("/");
                    type = tmp3[0];
                }
                line = fromServer.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            int bytesRead = 0;
            char[] buf = new char[BUF_SIZE];
            boolean loop = false;

            if (length == -1) loop = true;

            while (bytesRead < length || loop) {
                int res = fromServer.read(buf, 0, BUF_SIZE);
                if (res == -1) break;
                for (int i = 0;i < res && (i + bytesRead) < MAX_OBJECT_SIZE;i++) 
                    body[bytesRead + i] = (byte) (buf[i] & 0x00FF);

                bytesRead += res;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public int toLenght() { 
    	int header_length=0;
    	if (length == -1) 
            return header_length;
    	else 
            return length;
    }
    
    public String toType() { 
    	if (length == -1) 
            return "";
    	else 
            return type;
    }
    
    @Override
    public String toString() {
    	String res = "";
    	res = statusLine + CRLF;
    	res += headers;
    	res += CRLF;
    	return res;
    }
}
