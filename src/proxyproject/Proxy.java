package proxyproject;

import java.net.*;
import java.io.*;
import org.jfree.ui.RefineryUtilities;

public class Proxy {
    
    /** Proxy ports */
    private static int port;
    /** Client socket */
    private static ServerSocket socket;

    public static void init(int p) {
	
        port = p;
		
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
                System.out.println(e + "\nPort zajęty przez inny program. Program został zakończony.");
                e.printStackTrace();
                System.exit(1);
        }
  	
    }

    public static void main(String args[]) {
	
        int port = 8080;
        init(port);
        Socket client = null;
        
        /** Dynamic chart */
        final DynamicChart chartProxy = new DynamicChart("Proxy statistics");
        chartProxy.pack();
        RefineryUtilities.centerFrameOnScreen(chartProxy);
        chartProxy.setVisible(true);
        
        while (true) {
            try {
                client = socket.accept(); 
                (new Thread(new Threads(client))).start();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }

    }
}
