import java.util.Scanner;

import javax.swing.JFrame;

public class Client {

    public static void main(String[] abg) throws Exception {
        JFrame frame = new JFrame("Poker Clash");
        Scanner in = new Scanner(System.in);
      
    

       
      
      
        ClientScreen s = new ClientScreen();
       
        frame.add(s);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        s.connect();
    }
}