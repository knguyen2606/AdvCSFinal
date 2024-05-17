	import java.net.*;
	import java.io.*;
	import java.util.*;

	public class Server {
		public static ArrayList<PrintWriter> writers = new ArrayList<>();
		
	
	
		
		public static void main(String[] args) throws IOException {
			int portNumber = 1024;
			int counter = 0;
			

		
			ServerSocket serverSocket = new ServerSocket(portNumber);
			Manager m = new Manager();

			// This loop will run and wait for one connection at a time.
			 try {
            while (true) {
				
				System.out.println("wating for connection");
				

                Socket clientSocket = serverSocket.accept();
				System.out.println(counter);
				

                ServerThread st = new ServerThread(m, clientSocket);
                m.add(st);
				m.setId(counter);
				counter++;
                Thread thread = new Thread(st);
                thread.start();
				
				
            }

        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection");
            System.exit(1);
        }

		}

		
		

	}
 