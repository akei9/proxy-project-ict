package proxyproject;

import java.net.*;
import java.io.*;

public class Threads implements Runnable { 
	
    private final Socket client;
    public static int dataTransfer;
    public static int transferSum;
    public static int textTransfer;
    public static int imageTransfer;
    public static int appTransfer;
    public static int videoTransfer;
    
    public Threads(Socket client) {
        this.client = client;
    }	

    @Override
    public void run() {

        Socket server = null;
        HttpRequest request = null;
        HttpResponse response = null;

        /* Reading HTTP request from client */
        try {
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            request = new HttpRequest(fromClient);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        /* Sending request to HTTP server */
        try {
            server = new Socket(request.getHost(), request.getPort()); 
            DataOutputStream toServer = new DataOutputStream(server.getOutputStream()); 
            toServer.writeBytes(request.toString()); // from client data

        } catch (UnknownHostException e) {
             e.printStackTrace();
             return;
        } catch (IOException e) {
             e.printStackTrace();
             return;
        }
        
        /* Read server response and send it to the client */
        try {
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));

            response = new HttpResponse(fromServer); 
            DataOutputStream toClient = new DataOutputStream(client.getOutputStream());

            toClient.writeBytes(response.toString()); 
            toClient.write(response.body);				

            if (response.toLenght() != 0) {
                System.out.println("Wielkość przesyłanej części do klienta:: " + response.toLenght() + " bytes, typ danych:: " + response.toType()); 
                dataTransfer = response.toLenght();
                transferSum += dataTransfer;

                if("text".equals(response.type)) {
//                        textTransfer += dataTransfer;
                    textTransfer = dataTransfer;
                }
                else if("application".equals(response.type)) {
//                        appTransfer += dataTransfer;
                    appTransfer = dataTransfer;
                }
                else if("image".equals(response.type)) {
//                        imageTransfer += dataTransfer;
                    imageTransfer = dataTransfer;
                }
                else if("video".equals(response.type)) {
                    videoTransfer += dataTransfer;
                }

//                            System.out.println("Dane: " + dataTransfer);
                System.out.println("Text data: " + textTransfer + " bytes");
                System.out.println("App data: " + appTransfer + " bytes");
                System.out.println("Image data: " + imageTransfer + " bytes");
                System.out.println("Video data: " + videoTransfer + " bytes");
                System.out.println("Data summary: " + transferSum + " bytes");

            }

            client.close();
            server.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
