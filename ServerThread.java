import java.util.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;


public class ServerThread implements Runnable {

    public Socket clientSocket;
    private PrintWriter writer;
    private ObjectOutputStream outObj;
    private Manager manager;
    //test
     
    public ServerThread(Manager manager,Socket clientSocket_) {
        clientSocket = clientSocket_;
        this.manager = manager;

    
			
        
         
        
    }
     public void send(Object obj) {
        try {
             System.out.println("obj1 "+obj);
            outObj.reset();
            outObj.writeObject(obj);
            System.out.println("obj2 "+obj);
            

        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection");
            System.out.println("leaving");
            System.exit(1);
        }

    }
    

    @Override
    public void run() {

        try {

            outObj = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inObj = new ObjectInputStream(clientSocket.getInputStream());
            outObj.writeObject(manager.getId());
            outObj.writeObject(manager.getMap());
           
            while (true) {
                System.out.println("Waiting for object");
                Object obj = inObj.readObject();
                 System.out.println("brodcasting object");
                manager.brodcast(obj);

            }
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection");
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.println("Class does not exist" + e);
            System.exit(1);
        }
    }
}