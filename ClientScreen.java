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
	// alon copied code from youtube and plagiarized this project
	DLList<JButton> view;
	JLabel PlayersInServer;
	Player newS = null;
	Player me;
	boolean once = false;
	Deck deck;
	Deck hand;
	DLList<Player> turns;
	JButton move;
	int index;
	int level;

	public ClientScreen(String name) throws IOException {
		me = new Player(name, 0, false);
		index = 0;
		level = -1;
		hand = new Deck(new DLList<>());

		setLayout(null);
		pGame = new MyHashMap<>();
		this.name = name;
		view = new DLList<>();
		PlayersInServer = new JLabel();
		isServer = false;
		isCreate = false;
		turns = new DLList<>();

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

		move = new JButton("Move1");
		move.addActionListener(this);
		move.setBounds(400, 200, 150, 50);
		move.setVisible(false);

		this.add(CreateGame);
		this.add(JoinGame);
		this.add(cancel);
		this.add(PlayersInServer);
		this.add(start);

		this.add(move);
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		if (deck != null) {
			int x = 10;
			int y = 10;
			int imageWidth = 100;
    		int imageHeight = 100;
			int offset = 60;
			System.out.println("Hand size: "+  hand.size());


			for (int i = 0; i < hand.size(); i++) {

				Card card = hand.getCard(i);
				System.out.println("card debug: "+card.getImage());
				Image image = new ImageIcon(card.getImage()).getImage();
			
    			g.drawImage(image, x, y, imageWidth, imageHeight, this);
				x += offset;

			}

		}

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
			if (pGame.get(newS).get(0).isInGame()) {
				me.setInGame(true);
				PlayersInServer.setVisible(false);
				cancel.setVisible(false);

			}

			for (int i = 0; i < pGame.get(newS).size(); i++) {

				System.out.println("damgss");
				if (i == pGame.get(newS).size() - 1) {
					all += pGame.get(newS).get(i).getName();

				} else {
					all += pGame.get(newS).get(i).getName() + ",";

				}

			}
			PlayersInServer.setText(all);

		}
		if (isCreate) {
			System.out.println("create");

			String all = "Players: ";
			System.out.println(pGame.size() + ": new");
			if (pGame.get(me).size() >= 2 && pGame.get(me).size() <= 8 && once == false) {
				once = true;
				start.setVisible(true);
			}

			for (int i = 0; i < pGame.get(me).size(); i++) {

				if (i == pGame.get(me).size() - 1) {
					all += pGame.get(me).get(i).getName();

				} else {
					all += pGame.get(me).get(i).getName() + ",";

				}

			}
			PlayersInServer.setText(all);

		}

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
			pGame = (MyHashMap<Player, DLList<Player>>) inObj.readObject();
			System.out.println(me.getId() + " my id");

			while (true) {
				System.out.println("wating for object");
				Object obj = inObj.readObject();
				if (obj instanceof Deck) {
					if (!me.isInGame() && level == -1) {
						deck = (Deck) obj;
						System.out.println("works");

					} else if (!me.isInGame() && level != -1) {
						if (pGame.get(newS).get(level).getId() == me.getId()) {

							hand = (Deck) obj;
							level = -1;

						}

					}

				}

				if (obj instanceof MyHashMap) {

					System.out.println("test");
					System.out.println(pGame.size() + "old");
					pGame = (MyHashMap<Player, DLList<Player>>) obj;
					System.out.println(pGame.size() + "new");

				} else if (obj instanceof Integer) {

					index = (int) obj;

				} else if (obj instanceof Character) {
					int r = (char) obj;
					level = r;
				} else if (obj instanceof DLList) {
					turns = (DLList) obj;
					System.out.println("start");

					for (int i = 0; i < turns.size(); i++) {
						System.out.println(turns.get(i).getId());
					}
					System.out.println(me.getId());
					boolean is = false;
					for (int i = 0; i < turns.size(); i++) {
						if (turns.get(i).getId() == me.getId()) {
							is = true;

						}

					}
					if (is == false) {
						System.out.println("not works");
						turns = null;
						index = 0;

					} else {
						if (turns.get(index).getId() == me.getId()) {
							System.out.println("finish");
							move.setVisible(true);
							repaint();
						}

					}

				}
				repaint();

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
				newS = new Player(null, counts, false);
				pGame.get(newS).add(me);
				try {
					System.out.println("do it");
					outObj.reset();
					outObj.writeObject(pGame);
					System.out.println("do it 2");

					outObj.reset();
					outObj.writeObject(newS);

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
			PlayersInServer.setVisible(true);
			repaint();
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
				PlayersInServer.setVisible(true);

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
				if (!arr.get(i).isInGame()) {
					System.out.println("bitck");
					JButton a = new JButton(arr.get(i).getName() + "'s server");
					a.setName(arr.get(i).getId() + "");
					a.addActionListener(this);
					a.setBounds(900, y, 150, 50);
					a.setVisible(true);
					y += 200;
					view.add(a);

				}

			}
			for (int i = 0; i < view.size(); i++) {
				this.add(view.get(i));

			}

		}
		if (e.getSource() == start) {
			System.out.println("starting");
			PlayersInServer.setVisible(false);
			cancel.setVisible(false);
			start.setVisible(false);
			me.setInGame(true);

			for (int i = 0; i < pGame.get(me).size(); i++) {
				pGame.get(me).get(i).setInGame(true);

			}
			DLList<Card> all = new DLList<>();
			String[] suits = { "H", "D", "S", "C" };
			String[] images = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };

			for (String suit : suits) {
				for (int value = 0; value < 13; value++) {

					all.add(new Card(value, suit, images[value] + suit + ".png"));
				}
			}

			deck = new Deck(all);
			System.out.println("statt size: "+deck.size());
			deck.randomize();
			System.out.println("ends size: "+deck.size());
			turns = pGame.get(me);

			try {

				DLList<Card> handC = new DLList<>();
				Deck handd = new Deck(handC);
				for (int s = 0; s < 5; s++) {

					handC.add(deck.getCard(deck.size() - 1 - s));
					deck.remove(deck.size() - 1 - s);
				}
				System.out.println("deck size1: " + deck.size());
				hand = new Deck(handC);

				for (int i = 1; i < turns.size(); i++) {
					handC = new DLList<>();
					System.out.println("deck size2: " + deck.size());
					for (int s = 0; s < 5; s++) {

						handC.add(deck.getCard(deck.size() - 1 - s));
						deck.remove(deck.size() - 1 - s);
					}
					handd = new Deck(handC);

					outObj.reset();
					outObj.writeObject((char) i);
					outObj.reset();
					outObj.writeObject(handd);

				}
				outObj.reset();
				outObj.writeObject(deck);
				outObj.reset();
				outObj.writeObject(pGame);

				repaint();
			} catch (IOException ex) {
				System.out.println("ddam");
				System.err.println(ex);
				System.exit(1);
			}
			move.setVisible(true);

		}
		if (e.getSource() == move) {
			move.setVisible(false);
			index++;
			if (index >= turns.size()) {
				index = 0;
			}
			try {
				outObj.reset();

				outObj.writeObject(index);
				outObj.reset();

				outObj.writeObject(turns);

			} catch (IOException ex) {
				System.out.println("ddam");
				System.err.println(ex);
				System.exit(1);
			}

		}

		repaint();

	}
}