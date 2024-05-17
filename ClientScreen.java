import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientScreen extends JPanel implements ActionListener {
	private JTextArea area = new JTextArea();
	private JTextField field = new JTextField();
	private Socket sock = new Socket("localhost", 1024);
	private String name;
	JButton CreateGame;
	JButton JoinGame;
	JButton start;
	private ObjectOutputStream outObj;
	MyHashMap<Player, DLList<Player>> pGame;

	JButton cancel;
	boolean isServer;
	boolean isCreate;

	DLList<JButton> view;
	JLabel PlayersInServer;
	Player newS = null;
	Player me;

	public ClientScreen(String name) throws IOException {
		me = new Player(name, 0);

		setLayout(null);
		pGame = new MyHashMap<>();
		this.name = name;
		view = new DLList<>();
		PlayersInServer = new JLabel();
		isServer = false;
		isCreate = false;

		CreateGame = new JButton("Create Game");
		CreateGame.addActionListener(this);
		CreateGame.setBounds(900, 200, 150, 50);
		CreateGame.setVisible(true);
		JoinGame = new JButton("Join Game");
		JoinGame.addActionListener(this);
		JoinGame.setBounds(400, 200, 150, 50);
		JoinGame.setVisible(true);
		start = new JButton("Start");
		start.addActionListener(this);
		start.setBounds(600, 200, 150, 50);
		start.setVisible(false);
		PlayersInServer = new JLabel();

		PlayersInServer.setBounds(400, 200, 150, 50);
		PlayersInServer.setVisible(false);

		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		cancel.setBounds(400, 600, 150, 50);
		cancel.setVisible(false);

		this.add(CreateGame);
		this.add(JoinGame);
		this.add(cancel);
		this.add(PlayersInServer);
		this.add(start);

	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		if (isServer) {
			if (pGame.get(newS) == null) {
				isServer = false;
				CreateGame.setVisible(true);
				JoinGame.setVisible(true);
				PlayersInServer.setVisible(false);

			}

			System.out.println(pGame.keySet().toDLList().size() + " size");

			System.out.println("server");

			String all = "Players: ";

			for (int i = 0; i < pGame.get(newS).size(); i++) {
			
					System.out.println("damgss");
					if (i == pGame.get(newS).size() - 1) {
						all += pGame.get(newS).get(i).getName();

					} else {
						all += pGame.get(newS).get(i).getName() + ",";

					
				}

			}
			PlayersInServer.setText(all);
			PlayersInServer.setVisible(true);

		}
		if (isCreate) {
			System.out.println("create");

			String all = "Players: ";
			System.out.println(pGame.size() + ": new");
			if(pGame.get(me).size()>=2 &&pGame.get(me).size()<=8 ){
				start.setVisible(true);
			}

			for (int i = 0; i < pGame.get(me).size(); i++) {
			
					if (i == pGame.get(me).size() - 1) {
						all += pGame.get(me).get(i).getName();

					}else  {
						all += pGame.get(me).get(i).getName() + ",";

					}

				

			}
			PlayersInServer.setText(all);
			PlayersInServer.setVisible(true);

		}
		repaint();

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(1920, 1080);
	}

	@SuppressWarnings("unchecked")
	public void connect() throws IOException {

		String hostName = "localhost";
		int portNumber = 1024;
		Socket serverSocket = new Socket(hostName, portNumber);
		outObj = new ObjectOutputStream(serverSocket.getOutputStream());
		ObjectInputStream inObj = new ObjectInputStream(serverSocket.getInputStream());

		// repaint();

		// listens for inputs
		try {
			me.setId((int) inObj.readObject());
			pGame = (MyHashMap<Player,DLList<Player>>)inObj.readObject();
			System.out.println(me.getId() + " my id");
			

			while (true) {
				System.out.println("wating for object");
				Object obj = inObj.readObject();

				if (obj instanceof MyHashMap) {
					System.out.println("test");
					System.out.println(pGame.size() + "old");
					pGame = (MyHashMap<Player, DLList<Player>>) obj;
					System.out.println(pGame.size() + "new");
					repaint();
				} else if (obj instanceof String) {
					System.out.println((String) obj);
				}

			}

		} catch (ClassNotFoundException e) {
			System.err.println("Class does not exist" + e);
			System.exit(1);
		} catch (IOException e) {

			System.err.println("Couldn't get I/O for the connection to " + e);
			System.exit(1);
		}
	}

	@Override

	public void actionPerformed(ActionEvent e) {

		for (int i = 0; i < view.size(); i++) {
			if (e.getSource() == view.get(i)) {
				System.out.println("good");
				String s = view.get(i).getName();

				System.out.println(s);
				int counts = Integer.parseInt(s);
				newS = new Player(null, counts);
				pGame.get(newS).add(me);
				try {
					outObj.reset();
					outObj.writeObject(pGame);

				} catch (IOException ex) {
					System.out.println("ddam");
					System.err.println(ex);
					System.exit(1);
				}

				System.out.println(newS);

			}
		}
		if (newS != null) {
			for (int i = 0; i < view.size(); i++) {
				this.remove(view.get(i));
			}
			isServer = true;

		}

		if (e.getSource() == CreateGame) {

			try {

				pGame.put(me, new DLList<Player>());
				pGame.get(me).add(me);
				System.out.println(pGame.size());

				outObj.reset();

				outObj.writeObject(pGame);

				CreateGame.setVisible(false);
				JoinGame.setVisible(false);
				cancel.setVisible(true);
				isCreate = true;

				repaint();

			} catch (IOException ex) {
				System.out.println("ddam");
				System.err.println(ex);
				System.exit(1);
			}

		}
		if (e.getSource() == cancel) {
			// Server.pGame.remove(name);
			CreateGame.setVisible(true);
			JoinGame.setVisible(true);
			cancel.setVisible(false);
			PlayersInServer.setVisible(false);
			isCreate = false;

		}
		if (e.getSource() == JoinGame) {
			System.out.println(pGame.size() + " size");

			CreateGame.setVisible(false);
			JoinGame.setVisible(false);

			DLList<Player> arr = pGame.keySet().toDLList();
			System.out.println(arr.size() + " ,ax");
			int y = 200;
			view = new DLList<>();

			for (int i = 0; i < arr.size(); i++) {
				System.out.println("bitck");
				JButton a = new JButton(arr.get(i).getName() + "'s server");
				a.setName(arr.get(i).getId() + "");
				a.addActionListener(this);
				a.setBounds(900, y, 150, 50);
				a.setVisible(true);
				y += 200;
				view.add(a);

			}
			for (int i = 0; i < view.size(); i++) {
				this.add(view.get(i));

			}

		}
		repaint();

	}
}