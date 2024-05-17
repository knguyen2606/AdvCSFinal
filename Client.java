import java.util.Scanner;

import javax.swing.JFrame;

public class Client {

    public static void main(String[] abg) throws Exception {
        JFrame frame = new JFrame("Game");
        Scanner in = new Scanner(System.in);
      
        System.out.println("Enter your name");

       
      
      
        ClientScreen s = new ClientScreen(in.nextLine());
       
        frame.add(s);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        s.connect();
    }
}