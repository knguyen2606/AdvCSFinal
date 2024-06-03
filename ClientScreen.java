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
	int sizeMiddle;

	
	boolean isServer;
	boolean isCreate;
	JTextField startPoints;
	JTextField bettingField;
	JTextField chipsField;
	// alon copied code from youtube and plagiarized this project
	DLList<JButton> view;
	JLabel PlayersInServer;
	Player newS = null;
	Player me;
	boolean once = false;
	Deck deck;
	Deck hand;
	JLabel enterusernameLabel;
	JTextField enterUsernameField;
	JButton PlayButton;
	DLList<Player> turns;
	JButton check;
	JButton callButton;
	JButton foldButton;
	JButton ResetButton;
	int totalPoints;
	int callNumber;
	JLabel GameTitle;
	JButton InstuctionsButton;
	JLabel InstructionsScreen;
	JLabel chipInstructions;

	JButton betButton;
	int index;
	int level;
	JButton backButton;

	Deck middle;
	boolean isMiddleS;

	public ClientScreen() throws IOException {

		level = -1;
		hand = new Deck(new DLList<>());
		me = new Player(name, 0, false, hand);

		middle = new Deck(new DLList<>());
		sizeMiddle = 0;

		totalPoints = 0;
		callNumber = 0;
		isMiddleS = false;

		setLayout(null);
		pGame = new MyHashMap<>();

		view = new DLList<>();
		PlayersInServer = new JLabel();
		isServer = false;
		isCreate = false;
		turns = new DLList<>();

		CreateGame = new JButton("Create Game");
		CreateGame.addActionListener(this);
		CreateGame.setBounds(900, 200, 150, 50);
		CreateGame.setVisible(false);
		JoinGame = new JButton("Join Game");
		JoinGame.addActionListener(this);
		JoinGame.setBounds(400, 200, 150, 50);
		JoinGame.setVisible(false);
		start = new JButton("Start");
		start.addActionListener(this);
		start.setBounds(600, 200, 150, 50);
		start.setVisible(false);
		PlayersInServer = new JLabel();

		PlayersInServer.setBounds(400, 200, 150, 50);
		PlayersInServer.setVisible(false);

	

		check = new JButton("check");
		check.addActionListener(this);
		check.setBounds(1200, 400, 150, 50);
		check.setVisible(false);
		foldButton = new JButton("Fold");
		foldButton.addActionListener(this);
		foldButton.setBounds(1200, 500, 150, 50);
		foldButton.setVisible(false);
		callButton = new JButton("Call");
		callButton.addActionListener(this);
		callButton.setBounds(1200, 300, 150, 50);
		callButton.setVisible(false);
		betButton = new JButton("Raise");
		betButton.addActionListener(this);
		betButton.setBounds(1200, 200, 150, 50);
		betButton.setVisible(false);
		startPoints = new JTextField("20");
		startPoints.setBounds(600, 400, 150, 50);
		startPoints.setVisible(false);
		chipsField = new JTextField("");
		chipsField.setBounds(600, 400, 150, 50);
		chipsField.setVisible(false);

		bettingField = new JTextField("");
		bettingField.setBounds(1500, 200, 150, 50);
		bettingField.setVisible(false);
		ResetButton = new JButton("Restart");
		ResetButton.addActionListener(this);
		ResetButton.setBounds(1300, 400, 150, 50);
		ResetButton.setVisible(false);
		PlayButton = new JButton("Play");
		PlayButton.addActionListener(this);
		PlayButton.setBounds(700, 500, 150, 50);
		PlayButton.setVisible(true);
		InstuctionsButton = new JButton("Instuctions");
		InstuctionsButton.addActionListener(this);
		InstuctionsButton.setBounds(700, 400, 150, 50);
		InstuctionsButton.setVisible(true);
		enterUsernameField = new JTextField("");
		enterUsernameField.setBounds(625, 300, 300, 50);
		enterUsernameField.setVisible(true);
		enterusernameLabel = new JLabel("enter Username");
		enterusernameLabel.setBounds(700, 250, 150, 50);
		enterusernameLabel.setVisible(true);
		GameTitle = new JLabel("Poker Clash");
		GameTitle.setBounds(650, 100, 500, 50);
		GameTitle.setVisible(true);
		GameTitle.setFont(new Font("TimesRoman", Font.PLAIN, 50));
		InstructionsScreen = new JLabel("");
		InstructionsScreen.setBounds(650, 100, 500, 500);
		InstructionsScreen.setVisible(false);
		InstructionsScreen.setFont(new Font("TimesRoman", Font.PLAIN,20));
		chipInstructions = new JLabel("");
		chipInstructions.setBounds(300, 100, 700, 500);
		chipInstructions.setVisible(false);
		
		backButton = new JButton("back");
		backButton.addActionListener(this);
		backButton.setBounds(200, 600, 150, 50);
		backButton.setVisible(false);

		this.add(CreateGame);
		this.add(JoinGame);
	
		this.add(PlayersInServer);
		this.add(start);
		this.add(startPoints);
		this.add(betButton);
		this.add(chipsField);
		this.add(bettingField);
		this.add(foldButton);
		this.add(callButton);
		this.add(ResetButton);
		this.add(PlayButton);
		this.add(InstuctionsButton);

		this.add(enterUsernameField);
		this.add(enterusernameLabel);
		this.add(GameTitle);
		this.add(check);
		this.add(InstructionsScreen);
		this.add(backButton);
		this.add(chipInstructions);
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		if (me.loss) {
			g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
			g.setColor(Color.RED);

			g.drawString("End of Game you Loss!!!", 1000, 100);

			ResetButton.setVisible(true);
			check.setVisible(false);
			betButton.setVisible(false);
			bettingField.setVisible(false);
			foldButton.setVisible(false);
			callButton.setVisible(false);
		}
		if (me.won) {
			g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
			g.setColor(Color.GREEN);
			g.drawString("End of Game you won!!!", 1000, 100);
			ResetButton.setVisible(true);
			check.setVisible(false);
			betButton.setVisible(false);
			bettingField.setVisible(false);
			foldButton.setVisible(false);
			callButton.setVisible(false);
		}
		g.setFont(new Font("TimesRoman", Font.PLAIN, 18));
		g.setColor(Color.BLACK);

		if (deck != null && turns != null) {

			int centerX = 700; // Center x-coordinate of the circle
			int centerY = 400; // Center y-coordinate of the circle
			int radius = 340; // Radius of the circle
			int imageWidth = 100;
			int imageHeight = 100;
			int offset = 60; // Offset for drawing each card
			int numPlayers = turns.size(); // Number of players

			double angleIncrement = 2 * Math.PI / numPlayers; // Angle between each player in radians

			System.out.println("Hand size: " + hand.size());
			System.out.println("turns size: " + turns.size());

			for (int i = 0; i < numPlayers; i++) {
				double angle = i * angleIncrement;
				int playerX = centerX + (int) (radius * Math.cos(angle));
				int playerY = centerY + (int) (radius * Math.sin(angle));

				// Draw player's name

				if (turns.get(i).getPoints() != 0) {
					g.drawString(String.valueOf(turns.get(i).getPoints()), playerX, playerY - 30);

				}

				g.drawString(String.valueOf(turns.get(i).getChips()), playerX, playerY + 125);

				// Draw the name or "me"
				if (turns.get(i).getFold()) {
					g.drawString(turns.get(i).getId() != me.getId() ? turns.get(i).getName() + "-" + "folded"
							: "me" + "-" + "folded", playerX, playerY - 50);

				} else {

					if (turns.get(i).won) {
						g.setColor(Color.GREEN);
					} else if (turns.get(i).loss) {
						g.setColor(Color.RED);
					} else {
						g.setColor(Color.BLACK);

					}

					g.drawString(
							turns.get(i).getId() != me.getId() ? turns.get(i).getName()
									: "me :" + turns.get(index).handStatus,
							playerX,
							playerY - 50);

					g.setColor(Color.BLACK);
				}

				// Calculate starting position for player's cards
				int cardX = playerX - (hand.size() / 2) * offset;
				int cardY = playerY;
				boolean ifWin = false;
				for (int d = 0; d < turns.size(); d++) {
					if (turns.get(d).won) {
						ifWin = true;
					}

				}

				for (int s = 0; s < hand.size(); s++) {
					System.out.println("on my way");
					Image image;
					if (turns.get(i) != null && turns.get(i).getId() != me.getId()) {
						if (ifWin) {
							System.out.println("picture hand");

							image = new ImageIcon(turns.get(i).getHand().getCard(s).getImage()).getImage();
						} else {
							image = new ImageIcon("back.png").getImage();

						}

					} else {
						Card card = hand.getCard(s);
						image = new ImageIcon(card.getImage()).getImage();
					}

					g.drawImage(image, cardX, cardY, imageWidth, imageHeight, this);
					cardX += offset;
				}

			}
			int middleY = centerY;
			int middleX = centerX - 225;

			for (int i = 0; i < sizeMiddle; i++) {
				Image image = new ImageIcon(middle.getCard(i).getImage()).getImage();
				g.drawImage(image, middleX, middleY, imageWidth, imageHeight, this);

				middleX += 100;

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
			System.out.println(pGame.get(me).size() + ": new");
			if (pGame.get(me).size() >= 2 && pGame.get(me).size() <= 8 && once == false) {
				System.out.println("dam works niggler");
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
						DLList<Card> middleC = new DLList<>();
						for (int i = deck.size() - 1; i >= 0; i--) {
							if (i == deck.size() - 6) {
								break;

							}
							middleC.add(deck.getCard(i));
						}
						middle = new Deck(middleC);
						repaint();

					} else if (!me.isInGame() && level != -1) {
						if (pGame.get(newS).get(level).getId() == me.getId()) {

							hand = (Deck) obj;

							me.setHand(hand);

						}
						level = -1;

					}

				}

				if (obj instanceof MyHashMap) {

					System.out.println("test");
					System.out.println(pGame.size() + "old");
					pGame = (MyHashMap<Player, DLList<Player>>) obj;
					System.out.println(pGame.size() + "new");

				} else if (obj instanceof Integer) {
					System.out.println("step 1");

					if (isMiddleS && deck != null) {
						System.out.println("step 1 mybe");
						sizeMiddle = (int) obj;
						isMiddleS = false;
						if (sizeMiddle >= 3) {
							Deck newHand = new Deck(new DLList<>());
							for (int i = 0; i < hand.size(); i++) {
								newHand.getDeck().add(hand.getCard(i));
							}
							for (int i = 0; i < sizeMiddle; i++) {
								newHand.getDeck().add(middle.getCard(i));
							}
							PokerHandChecker poke = new PokerHandChecker(newHand);

							String checker = poke.determineHand();

							turns.get(index).handStatus = checker;

						}

					} else {
						System.out.println("step 1 completed");

						index = (int) obj;
						System.out.println("dam: " + index);

					}

				} else if (obj instanceof String) {
					if (obj.equals("SizeMiddle")) {
						isMiddleS = true;
					}

				}

				else if (obj instanceof Character) {
					int r = (char) obj;
					level = r;
				} else if (obj instanceof DLList) {
					turns = (DLList) obj;
					System.out.println("start");

					for (int i = 0; i < turns.size(); i++) {
						if (turns.get(i).getId() == me.getId()) {
							me.loss = turns.get(i).loss;
							me.won = turns.get(i).won;
						}
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
						turns = new DLList<>();
						deck = null;
						middle = new Deck(null);
						index = 0;

					} else {

						if (turns.get(index).getId() == me.getId()) {
							System.out.println("finish");
							turns.get(index).setHand(hand);
							boolean checkFC = true;
							for (int i = 0; i < turns.size(); i++) {
								if (turns.get(i).getPoints() != turns.get(index).getPoints()
										&& !turns.get(i).getFold()) {
									checkFC = false;
								}

							}
							if (checkFC) {
								if (!turns.get(index).getFold() && !turns.get(index).loss && !turns.get(index).won) {
									check.setVisible(true);

								}

							}
							boolean allFold = true;
							if (!turns.get(index).getFold()) {
								for (int i = 0; i < turns.size(); i++) {
									if (turns.get(i).getId() == me.getId()) {
										continue;
									}
									if (!turns.get(i).getFold()) {
										allFold = false;

									}
								}
								if (allFold) {
									turns.get(index).won = allFold;
									me.won = allFold;

									System.out.println("you win!!!");

								} else {
									betButton.setVisible(true);
									bettingField.setVisible(true);
									foldButton.setVisible(true);
									int biggest = 0;
									for (int i = 0; i < turns.size(); i++) {
										if (turns.get(i).getPoints() > biggest) {
											biggest = turns.get(i).getPoints();
										}

									}
									if (biggest > turns.get(index).getPoints()) {
										callNumber = biggest;
										biggest -= turns.get(index).getPoints();
										callButton.setText("Call: " + biggest);
										callButton.setVisible(true);

									}
								}

							} else {
								index--;
								if (index < 0) {
									index = turns.size() - 1;
								}
								try {

									outObj.reset();
									outObj.writeObject(index);
									outObj.reset();

									outObj.writeObject(turns);
									if (sizeMiddle <= middle.size()) {
										System.out.println(sizeMiddle + ": size");
										outObj.reset();
										outObj.writeObject("SizeMiddle");
										outObj.reset();
										outObj.writeObject(sizeMiddle);

									}

								} catch (IOException ex) {
									System.out.println("ddam");
									System.err.println(ex);
									System.exit(1);
								}

							}

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
				newS = new Player(null, counts, false, hand);

				chipsField.setVisible(false);
				chipInstructions.setVisible(false);
				int vals = Integer.parseInt(chipsField.getText());
				me.setChips(vals);
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
				chipsField.setBounds(600, 600, 150, 50);
				chipInstructions.setText("Enter the number of chips you start with in the first box and the starting points in the second box");
				chipInstructions.setVisible(true);
				chipsField.setVisible(true);
				pGame.put(me, new DLList<Player>());
				pGame.get(me).add(me);
				System.out.println(pGame.size());
				;

				CreateGame.setVisible(false);
				JoinGame.setVisible(false);
				
				PlayersInServer.setVisible(true);
			
				startPoints.setVisible(true);

				isCreate = true;

				outObj.reset();

				outObj.writeObject(pGame);

			} catch (IOException ex) {
				System.out.println("ddam");
				System.err.println(ex);
				System.exit(1);
			}

		}
		
		if (e.getSource() == JoinGame) {

			chipsField.setVisible(true);
			System.out.println(pGame.size() + " size");
			chipInstructions.setText("enter your starting points");
			chipInstructions.setVisible(true);

			CreateGame.setVisible(false);
			JoinGame.setVisible(false);

			DLList<Player> arr = pGame.keySet().toDLList();
			System.out.println(arr.size() + " ,ax");
			int y = 200;
			view = new DLList<>();

			for (int i = 0; i < arr.size(); i++) {
				if (!arr.get(i).isInGame()) {
					System.out.println("test");
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
			String valueS = startPoints.getText();
			me.setPoints(Integer.parseInt(valueS));
			startPoints.setVisible(false);
			chipInstructions.setVisible(false);
			me.setChips(Integer.parseInt(chipsField.getText()) - me.getPoints());

			System.out.println("starting: " + me.getChips());
			PlayersInServer.setVisible(false);
		
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
			System.out.println("statt size: " + deck.size());
			deck.randomize();
			System.out.println("ends size: " + deck.size());
			turns = pGame.get(me);

			try {

				DLList<Card> handC = new DLList<>();
				Deck handd = new Deck(handC);

				for (int s = 0; s < 2; s++) {

					handC.add(deck.getCard(deck.size() - 1 - s));
					deck.remove(deck.size() - 1 - s);
				}
				System.out.println("deck size1: " + deck.size());
				hand = new Deck(handC);

				for (int i = 1; i < turns.size(); i++) {

					handC = new DLList<>();
					System.out.println("deck size2: " + deck.size());
					for (int s = 0; s < 2; s++) {

						handC.add(deck.getCard(deck.size() - 1 - s));
						deck.remove(deck.size() - 1 - s);
					}
					handd = new Deck(handC);

					outObj.reset();
					outObj.writeObject((char) i);
					outObj.reset();
					outObj.writeObject(handd);

				}
				int o = 0;

				for (int i = 0; i < turns.size(); i++) {

					if (turns.get(i).getId() == me.getId()) {
						turns.get(i).setPoints(me.getPoints() - o);
						turns.get(i).setChips(me.getChips());

					} else {

						turns.get(i).setPoints(me.getPoints() - o);

						turns.get(i).setChips(turns.get(i).getChips() - turns.get(i).getPoints());

					}
					if (me.getPoints() - o != 0) {
						o += 10;
					}

				}
				index = turns.size() - 1;
				outObj.reset();

				outObj.writeObject(index);

				outObj.reset();

				outObj.writeObject(turns);
				outObj.reset();
				outObj.writeObject(deck);

				DLList<Card> middleC = new DLList<>();
				for (int i = deck.size() - 1; i >= 0; i--) {
					if (i == deck.size() - 6) {
						break;

					}
					middleC.add(deck.getCard(i));
				}

				middle = new Deck(middleC);

				outObj.reset();
				outObj.writeObject(pGame);

				repaint();
			} catch (IOException ex) {
				System.out.println("ddam");
				System.err.println(ex);
				System.exit(1);
			}
			// move.setVisible(true);
			// betButton.setVisible(true);
			chipsField.setVisible(false);

		}
		if (e.getSource() == check) {

			check.setVisible(false);
			betButton.setVisible(false);
			bettingField.setVisible(false);
			foldButton.setVisible(false);
			callButton.setVisible(false);

			index--;

			if (index < 0) {
				index = turns.size() - 1;
				;
				boolean isEquals = true;
				for (int i = 0; i < turns.size(); i++) {
					if (i == turns.size() - 1) {
						break;
					}
					if (turns.get(i).getPoints() != turns.get(i + 1).getPoints()) {
						isEquals = false;
					}

				}
				if (isEquals) {
					if (sizeMiddle <= middle.size() - 1) {
						if (sizeMiddle == 0) {

							sizeMiddle = 3;
						} else {
							sizeMiddle++;
						}
					} else {
						PokerHandChecker bestHand = new PokerHandChecker(turns.get(0).getHand());
						int bestPlayerIndex = 0;

						// Loop through each turn in the turns list starting from the second hand
						for (int i = 1; i < turns.size(); i++) {
							// Create a new PokerHandChecker object for the current turn's hand
							PokerHandChecker currentHand = new PokerHandChecker(turns.get(i).getHand());

							// Compare the current hand with the best hand
							if (PokerHandChecker.compareHands(currentHand, bestHand) > 0) {
								bestPlayerIndex = i;
								// If the current hand is better, update bestHand to be the current hand
								bestHand = currentHand;
							}
						}
						for (int i = 0; i < turns.size(); i++) {
							if (i == bestPlayerIndex) {
								turns.get(i).won = true;
							} else {
								turns.get(i).loss = true;

							}
						}
					}

				}

			}
			try {

				outObj.reset();
				outObj.writeObject(index);
				outObj.reset();

				outObj.writeObject(turns);
				if (sizeMiddle <= middle.size()) {
					System.out.println(sizeMiddle + ": size");
					outObj.reset();
					outObj.writeObject("SizeMiddle");
					outObj.reset();
					outObj.writeObject(sizeMiddle);

				}

			} catch (IOException ex) {
				System.out.println("ddam");
				System.err.println(ex);
				System.exit(1);
			}
		}

		if (e.getSource() == betButton) {
			System.out.println("right player: " + turns.get(index).getName());

			System.out.println("right index: " + index);

			int betAmount = Integer.parseInt(bettingField.getText());
			System.out.println("bet Amount: " + betAmount);
			System.out.println("bet is: " + turns.get(index).getPoints());
			boolean isbigger = true;
			for (int i = 0; i < turns.size(); i++) {
				if (turns.get(i).getPoints() >= betAmount) {
					isbigger = false;
				}

			}
			if (isbigger && betAmount <= turns.get(index).getChips() + turns.get(index).getPoints()) {
				check.setVisible(false);
				betButton.setVisible(false);
				bettingField.setVisible(false);
				foldButton.setVisible(false);
				callButton.setVisible(false);

				turns.get(index).setChips(turns.get(index).getChips() - betAmount + turns.get(index).getPoints());

				turns.get(index).setPoints(betAmount);

				index--;

				if (index < 0) {
					index = turns.size() - 1;

					boolean isEquals = true;
					for (int i = 0; i < turns.size(); i++) {
						if (i == turns.size() - 1) {
							break;
						}
						if (turns.get(i).getPoints() != turns.get(i + 1).getPoints()) {
							isEquals = false;
						}

					}
					if (isEquals) {
						if (sizeMiddle <= middle.size() - 1) {
							if (sizeMiddle == 0) {

								sizeMiddle = 3;
							} else {
								sizeMiddle++;
							}
						} else {
							PokerHandChecker bestHand = new PokerHandChecker(turns.get(0).getHand());
							int bestPlayerIndex = 0;

							// Loop through each turn in the turns list starting from the second hand
							for (int i = 1; i < turns.size(); i++) {
								// Create a new PokerHandChecker object for the current turn's hand
								PokerHandChecker currentHand = new PokerHandChecker(turns.get(i).getHand());

								// Compare the current hand with the best hand
								if (PokerHandChecker.compareHands(currentHand, bestHand) > 0
										&& !turns.get(i).getFold()) {
									bestPlayerIndex = i;
									// If the current hand is better, update bestHand to be the current hand
									bestHand = currentHand;
								}
							}
							for (int i = 0; i < turns.size(); i++) {
								if (i == bestPlayerIndex) {
									turns.get(i).won = true;
								} else {
									turns.get(i).loss = true;

								}
							}
						}

					}

				}

				try {

					outObj.reset();
					outObj.writeObject(index);
					outObj.reset();

					outObj.writeObject(turns);
					if (sizeMiddle <= middle.size()) {
						System.out.println(sizeMiddle + ": size");
						outObj.reset();
						outObj.writeObject("SizeMiddle");
						outObj.reset();
						outObj.writeObject(sizeMiddle);

					}

				} catch (IOException ex) {
					System.out.println("ddam");
					System.err.println(ex);
					System.exit(1);
				}
			}

		}
		if (e.getSource() == foldButton) {
			// turns.get(index).setFold();
			me.loss = true;
			// turns.get(index).loss = true;
			turns.remove(index);
			check.setVisible(false);
			betButton.setVisible(false);
			bettingField.setVisible(false);
			foldButton.setVisible(false);
			callButton.setVisible(false);
			index--;
			if (index < 0) {
				index = turns.size() - 1;
			}
			try {

				outObj.reset();
				outObj.writeObject(index);
				outObj.reset();

				outObj.writeObject(turns);
				if (sizeMiddle <= middle.size()) {
					System.out.println(sizeMiddle + ": size");
					outObj.reset();
					outObj.writeObject("SizeMiddle");
					outObj.reset();
					outObj.writeObject(sizeMiddle);

				}

			} catch (IOException ex) {
				System.out.println("ddam");
				System.err.println(ex);
				System.exit(1);
			}
			repaint();

		}
		if (e.getSource() == callButton) {

			check.setVisible(false);
			betButton.setVisible(false);
			bettingField.setVisible(false);
			foldButton.setVisible(false);
			callButton.setVisible(false);

			turns.get(index).setChips(turns.get(index).getChips() - callNumber + turns.get(index).getPoints());

			turns.get(index).setPoints(callNumber);

			index--;

			if (index < 0) {
				index = turns.size() - 1;

				boolean isEquals = true;
				for (int i = 0; i < turns.size(); i++) {
					if (i == turns.size() - 1) {
						break;
					}
					if (turns.get(i).getPoints() != turns.get(i + 1).getPoints()) {
						isEquals = false;
					}

				}
				if (isEquals) {
					if (sizeMiddle <= middle.size() - 1) {
						if (sizeMiddle == 0) {

							sizeMiddle = 3;
						} else {
							sizeMiddle++;
						}

					} else {
						PokerHandChecker bestHand = new PokerHandChecker(turns.get(0).getHand());
						int bestPlayerIndex = 0;

						// Loop through each turn in the turns list starting from the second hand
						for (int i = 1; i < turns.size(); i++) {
							// Create a new PokerHandChecker object for the current turn's hand
							PokerHandChecker currentHand = new PokerHandChecker(turns.get(i).getHand());

							// Compare the current hand with the best hand
							if (PokerHandChecker.compareHands(currentHand, bestHand) > 0) {
								bestPlayerIndex = i;
								// If the current hand is better, update bestHand to be the current hand
								bestHand = currentHand;
							}
						}
						for (int i = 0; i < turns.size(); i++) {
							if (i == bestPlayerIndex) {
								turns.get(i).won = true;
							} else {
								turns.get(i).loss = true;

							}
						}
					}

				}

			}

			try {

				outObj.reset();
				outObj.writeObject(index);
				outObj.reset();

				outObj.writeObject(turns);
				if (sizeMiddle <= middle.size()) {
					System.out.println(sizeMiddle + ": size");
					outObj.reset();
					outObj.writeObject("SizeMiddle");
					outObj.reset();
					outObj.writeObject(sizeMiddle);

				}

			} catch (IOException ex) {
				System.out.println("ddam");
				System.err.println(ex);
				System.exit(1);
			}
		}
		if (e.getSource() == ResetButton) {
			pGame = new MyHashMap<>();
			int id = me.getId();

			me = new Player(name, id, false, new Deck(new DLList<>()));
			hand = new Deck(new DLList<>());
			middle = new Deck(new DLList<>());
			deck = new Deck(new DLList<>());
			sizeMiddle = 0;
			totalPoints = 0;
			callNumber = 0;

			index = 0;
			isServer = false;
			isCreate = false;
			once = false;
			newS = null;

			isMiddleS = false;
			turns = new DLList<>();

			level = -1;

			// Reset visibility of components
			CreateGame.setVisible(true);
			JoinGame.setVisible(true);
			start.setVisible(false);
			
			PlayersInServer.setText("");
			PlayersInServer.setVisible(false);
			startPoints.setVisible(false);
			check.setVisible(false);
			foldButton.setVisible(false);
			callButton.setVisible(false);
			betButton.setVisible(false);
			bettingField.setVisible(false);
			ResetButton.setVisible(false);
			chipsField.setVisible(false);

			// Remove dynamic buttons if any
			for (int i = 0; i < view.size(); i++) {
				this.remove(view.get(i));
			}
			revalidate();

		}
		if (e.getSource() == PlayButton) {
			if (!enterUsernameField.getText().isEmpty()) {
				name = enterUsernameField.getText();
				me.setName(name);

				PlayButton.setVisible(false);
				GameTitle.setVisible(false);
				enterUsernameField.setVisible(false);
				enterusernameLabel.setVisible(false);
				InstuctionsButton.setVisible(false);
				JoinGame.setVisible(true);
				CreateGame.setVisible(true);
			}
		}
		if (e.getSource() == InstuctionsButton) {
			PlayButton.setVisible(false);
			GameTitle.setVisible(false);
			enterUsernameField.setVisible(false);
			enterusernameLabel.setVisible(false);
			InstuctionsButton.setVisible(false);
			String allstring = "Welcome to Poker Clash! In this multiplayer poker game, your goal is to win chips by forming the best possible five-card hand. Each player is dealt two private cards, and five community cards are dealt face-up on the table. Players use these seven cards to make their best hand. The game has several rounds: pre-flop, flop, turn, and river, with betting rounds in between. You can check, bet, call, raise, or fold depending on your cards and strategy. The player with the best hand, or the last player remaining after all others have folded, wins the pot. Remember, skillful bluffing and strategic betting are key to becoming a poker legend!";

			String modifiedString = allstring.replace(",", ",<br>");

			InstructionsScreen.setText("<html>" + modifiedString + "</html>");

			InstructionsScreen.setVisible(true);
			backButton.setVisible(true);

		}
		if(e.getSource() == backButton){
			PlayButton.setVisible(true);
			GameTitle.setVisible(true);
			enterUsernameField.setVisible(true);
			enterusernameLabel.setVisible(true);
			InstuctionsButton.setVisible(true);
			InstructionsScreen.setVisible(false);
			backButton.setVisible(false);

		}

		repaint();

	}
}
