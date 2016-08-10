import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PongFenster extends JFrame
{
	Canvas spielfeld;
	Engine engine;
	Ball ball;
	Spielerbalken spielerbalken;
	Gegnerbalken gegnerbalken;
	JLabel roteAnzeige;  
	JLabel tempoAnzeige;
	JLabel physikAnzeige;
	Integer spielerPunkte = new Integer (0);
	Integer gegnerPunkte = new Integer (0);
	int punkteAbstand = 110;
	Timer antiVerzoegerung;
	
	PongFenster(String name)
	{
		super(name);
		
		//Diverse Initialisierungen
		engine = new Engine("Engine");
		ball = new Ball();
		spielerbalken = new Spielerbalken();
		gegnerbalken = new Gegnerbalken();
		
		// Menü
		JMenuBar menuleiste = new JMenuBar();
		JMenu menu1 = new JMenu("Spiel...");
		JMenu menu2 = new JMenu("Optionen");
		JMenuItem item1 = new JMenuItem("Start");
		JMenuItem item2 = new JMenuItem("Programm beenden");
		JMenuItem item3 = new JMenuItem("Hardmode!");
		JMenuItem item4 = new JMenuItem("Realistische Physik An/Aus");
		JMenuItem item5 = new JMenuItem("Ballbeschleunigung...");
		JMenuItem item6 = new JMenuItem("Multiplayer An/Aus");
		roteAnzeige = new JLabel("");
		tempoAnzeige = new JLabel("   Ballbeschleunigung:  " + (((int) (ball.temposchub*100))) + "   ");
		physikAnzeige = new JLabel("Physik: Realisitisch   ");
		setJMenuBar(menuleiste);
		menuleiste.add(menu1);
		menuleiste.add(menu2);
		menuleiste.add(roteAnzeige);
		menuleiste.add(tempoAnzeige);
		menuleiste.add(Box.createHorizontalGlue());
		menuleiste.add(physikAnzeige);
		
		menu1.add(item1);
		menu1.add(item2);
		menu2.add(item3);
		menu2.add(item4);
		menu2.add(item5);
		menu2.add(item6);
		
		item1.addActionListener(new MenuLauscher());
		item2.addActionListener(new MenuLauscher());
		item3.addActionListener(new MenuLauscher());
		item4.addActionListener(new MenuLauscher());
		item5.addActionListener(new MenuLauscher());
		item6.addActionListener(new MenuLauscher());

		
		// Alles zusammenfügen
		setLayout(new GridBagLayout());
		GridBagConstraints d = new GridBagConstraints();
		
		d.gridx = 0;
		d.gridy = 0;
		d.fill = GridBagConstraints.BOTH;
		spielfeld = new Canvas();
		add(spielfeld, d);
			
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);		
		
		// Key Bindings
		// ----------------------------------------------------------------------------------
		
		InputMap im = spielfeld.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = spielfeld.getActionMap();

		im.put(KeyStroke.getKeyStroke("UP"), "UpArrow");
		im.put(KeyStroke.getKeyStroke("DOWN"), "DownArrow");
		im.put(KeyStroke.getKeyStroke("W"), "W");
		im.put(KeyStroke.getKeyStroke("S"), "S");
		
		im.put(KeyStroke.getKeyStroke("released UP"), "released UpArrow");               // Released-Bindings für Multiplayer-Modus
		im.put(KeyStroke.getKeyStroke("released DOWN"), "released DownArrow");			 // Das gleichzeitige Drücken mehrerer Tasten wird nicht erkannt,
		im.put(KeyStroke.getKeyStroke("released W"), "released W");						 // daher muss man die Bewegung mehrerer Balken mit einer Methode steuern,
		im.put(KeyStroke.getKeyStroke("released S"), "released S");						 // die einzelne Switches abfragt (die wiederrum beim Drücken/Loslassen
																						 // immer umgedreht werden (zB. key_UP = true). Daher auch die Released-Dinger.
		am.put("UpArrow", new DrueckTastenAktion("Hoch"));
		am.put("DownArrow", new DrueckTastenAktion("Runter"));
		am.put("W", new DrueckTastenAktion("W"));
		am.put("S", new DrueckTastenAktion("S"));
		
		am.put("released UpArrow", new LoslassTastenAktion("released Hoch"));
		am.put("released DownArrow", new LoslassTastenAktion("released Runter"));
		am.put("released W", new LoslassTastenAktion("released W"));
		am.put("released S", new LoslassTastenAktion("released S"));
		
		// Anti-Verzoegerungs-Timer, da Windows beim Gedrückthalten einer Taste eine kleine Verzögerung einbaut, bevor sich die Balken bewegen.
		// Besonders im Multiplayer ist das nervig, weil Java den "Timer" für das Erkennen des Gedrückthaltens bei jedem Tastendruck zurücksetzt,
		// ergo bremsen sich beide Spieler oft gegenseitig aus. Durch den Einbau eines eigenen Timers zum Bewegen der Balken, der beim Drücken/Loslassen 
		// von Tasten ein/ausgeschaltet wird, kann man das umgehen. 
		
		antiVerzoegerung = new Timer(50, new ActionListener()
		{		
			public void actionPerformed(ActionEvent e)
			{	
				if (Status.zweiSpieler == true)
				{					
					if (Status.up_key == true)
					{
						if (gegnerbalken.y > 0)
						{
							gegnerbalken.y -= 10;
						}
					}
							
					if (Status.down_key == true)
					{
						if (gegnerbalken.y < 220)
						{
							gegnerbalken.y += 10;
						}
					}
					
					if (Status.w_key == true)
					{
						if (spielerbalken.y > 0)
						{
							spielerbalken.y -= 10;
						}
					}
							
					if (Status.s_key == true)
					{
						if (spielerbalken.y < 220)
						{
							spielerbalken.y += 10;
						}
					}
				}
				else
				{
					if (Status.up_key == true)
					{
						if (spielerbalken.y > 0)
						{
							spielerbalken.y -= 10;
						}
					}
							
					if (Status.down_key == true)
					{
						if (spielerbalken.y < 220)
						{
							spielerbalken.y += 10;
						}
					}
					
					if (Status.w_key == true)
					{
						if (Status.up_key == false)  // Um zu verhindern, dass das Tempo beim zusätzlichen Drücken von Up verdoppelt wird
						{
							if (spielerbalken.y > 0)
							{
								spielerbalken.y -= 10;
							}
						}
					}
							
					if (Status.s_key == true)
					{
						if (Status.down_key == false)  // dito
						{
							if (spielerbalken.y < 220)
							{
								spielerbalken.y += 10;
							}
						}
					}
				}
			}
		});
	}
	
	class Canvas extends JPanel
	{		
		Canvas()
		{
			setForeground(Color.black);
			setBackground(Color.white);
			addMouseMotionListener(new Maustracker());
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			g.setColor(new Color(150,80,0));
			g.drawLine(250,0,250,300);
			
			if(Status.spielAktiv == true)
			{
				g.setColor(Color.LIGHT_GRAY);
				g.setFont(new Font("Stencil Std", Font.PLAIN, 150));
				g.drawString(spielerPunkte.toString(), punkteAbstand, 200);
				g.drawString(gegnerPunkte.toString(), 290, 200);
				
				g.setColor(Color.BLUE);				
				g.fillRoundRect(spielerbalken.x, spielerbalken.y, 20, 80, 20, 20);
				g.fillRoundRect(gegnerbalken.x, gegnerbalken.y, 20, 80, 20, 20);
				
				g.setColor(Color.ORANGE);
				g.fillOval((int) ball.x, (int) ball.y, 20, 20);
			}
		}	
		
		public Dimension getMinimumSize()
		{
			return new Dimension(500,300);
		}
			
		public Dimension getPreferredSize()
		{
			return getMinimumSize();
		} 		
	}
	
	// Diverse Listener
	// ------------------------------------------------------------------------------------
	
	class MenuLauscher implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String menupunkt;
			
			menupunkt = e.getActionCommand();
			
			if (menupunkt.equals("Start"))
			{
				if (Status.spielAktiv == true)              
				{
					engine.stop();
				}
			else
				{
					engine.start();
					Status.spielAktiv = true;
				}
			}
			
			if (menupunkt.equals("Programm beenden"))
				System.exit(0);
			
			if (menupunkt.equals("Hardmode!"))
				if (gegnerbalken.schlaueKI == true)
				{
					gegnerbalken.schlaueKI = false;
					roteAnzeige.setText("");
				}
				else
				{
					if (Status.zweiSpieler == true)
					{
						JOptionPane.showMessageDialog(null, "Bitte zuerst Multiplayer-Modus deaktivieren!", "Fehler", JOptionPane.INFORMATION_MESSAGE); 
					}
					else
					{
						gegnerbalken.schlaueKI = true;
						roteAnzeige.setForeground(Color.RED);
						roteAnzeige.setText("   Hardmode aktiv!");
					}
				}
			
			if(menupunkt.equals("Realistische Physik An/Aus"))
				if (Status.realistischePhysik == true)
				{
					Status.realistischePhysik = false;
					physikAnzeige.setText("Physik: Simpel   ");
					ball.vonRealistischAufSimpel();
				}
				else
				{
					Status.realistischePhysik = true;
					physikAnzeige.setText("Physik: Realistisch   ");
					ball.vonSimpelAufRealistisch();
				}		
			
			if (menupunkt.equals("Ballbeschleunigung..."))
			{
				String[] tempoauflistung = { "0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
				String gewünschtesTempo = (String) JOptionPane.showInputDialog(null, 
				        "Wie stark soll der Ball beschleunigen?",
				        "Je höher, desto besser",
				        JOptionPane.QUESTION_MESSAGE, 
				        null, 
				        tempoauflistung, 
				        tempoauflistung[0]);
				
				ball.temposchub = (Double.parseDouble(gewünschtesTempo))/100;
				tempoAnzeige.setText("   Ballbeschleunigung:  " + ((int)(ball.temposchub*100)) + "   ");
				
			}
			
			if (menupunkt.equals("Multiplayer An/Aus"))
			{
				if (Status.zweiSpieler == false)
				{
					Status.zweiSpieler = true;
					gegnerbalken.schlaueKI = false;
					roteAnzeige.setForeground(Color.RED);
					roteAnzeige.setText("   Multiplayer aktiv!");
				}
				else
				{
					Status.zweiSpieler = false;
					roteAnzeige.setText("");
				}
			}
		}
	}
     	
	public class DrueckTastenAktion extends AbstractAction
	{
		String test;
		
		public DrueckTastenAktion(String taste)
		{
			test = taste;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			switch(test)
			{
				case "Hoch":
					Status.up_key = true;
					break;
				case "Runter":
					Status.down_key = true;
					break;
				case "W":
					Status.w_key = true;
					break;
				case "S":
					Status.s_key = true;
					break;
			}
			
			if (Status.antiVerzoegerungAktiv == false)
			antiVerzoegerung.start();
			Status.antiVerzoegerungAktiv = true;
		}
	}
	
	public class LoslassTastenAktion extends AbstractAction
	{
		String test;
		
		public LoslassTastenAktion(String taste)
		{
			test = taste;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			switch(test)
			{
				case "released Hoch":
					Status.up_key = false;
					break;
				case "released Runter":
					Status.down_key = false;
					break;
				case "released W":
					Status.w_key = false;
					break;
				case "released S":
					Status.s_key = false;
					break;
			}
			
			if ((Status.up_key == false) &&
				(Status.down_key == false) &&
				(Status.w_key == false) &&
				(Status.s_key == false))
				{
					antiVerzoegerung.stop();
					Status.antiVerzoegerungAktiv = false;
				}
		}
	}
	
	
	class Maustracker extends MouseMotionAdapter  // Zum Ziehen der Balken mit der Maus
	{
		public void mouseDragged(MouseEvent e)   
		{
			 if (((e.getX() >= spielerbalken.x-20) && (e.getX() <= (spielerbalken.x+60))) &&		// Klicken auf Spielerbalken
				((e.getY() >= spielerbalken.y) && (e.getY() <= (spielerbalken.y+80))))
			{
				if ((e.getY() >= 40) && (e.getY() <= 260))
				spielerbalken.y = e.getY() - 40;
			}
			 
			 if (Status.zweiSpieler == true)
			 {
				 if (((e.getX() >= gegnerbalken.x-40) && (e.getX() <= (gegnerbalken.x+40))) &&		// Klicken auf Gegnerbalken
					((e.getY() >= gegnerbalken.y) && (e.getY() <= (gegnerbalken.y+80))))
				 {
					 if ((e.getY() >= 40) && (e.getY() <= 260))
						 gegnerbalken.y = e.getY() - 40;
				 }
			 }
		}
	}
	
	// Engine
	
	public class Engine 
	{
		String name;
		int resultat;
		
		Timer uhr = new Timer(20, new ActionListener()     // Der Motor der ganzen Spielphysik. Vorsicht: Änderungen am Tempo beschleunigen/bremsen auch den Ball und Gegnerbalken!
		{
			public void actionPerformed(ActionEvent e)
			{
				if (Status.realistischePhysik == false)
				{
					ball.spielerKollision(spielerbalken.x, spielerbalken.y);
					ball.gegnerKollision(gegnerbalken.x, gegnerbalken.y);
					ball.bewegen();
					if (Status.zweiSpieler == false)
						gegnerbalken.bewegen((int) ball.x, (int) ball.y, ball.richtung);
				}
				else
				{
					ball.r_spielerKollision(spielerbalken.x, spielerbalken.y);
					ball.r_gegnerKollision(gegnerbalken.x, gegnerbalken.y);
					ball.r_bewegen();
					if (Status.zweiSpieler == false)
						gegnerbalken.r_bewegen(ball.x, ball.y, ball.steigung_x, ball.steigung_y);
				}
				punkteKorrektur();
				spielfeld.repaint();
				resultat = ball.imAus();
				gameOver();
			}});
		
		Engine(String n)
		{
			name = n;
		}
		
		void start()
		{
			uhr.start();
		}
		
		void stop()
		{
			uhr.stop();
			Status.spielAktiv = false;
			ball.reset();
			spielerPunkte = 0;
			gegnerPunkte = 0;
			punkteAbstand = 110;
			spielfeld.repaint();
		}
		
		void punkteKorrektur()    // Spielerpunkteanzeige im Hintergrund ist zu weit rechts, wenn über 10. Daher wird sie hier nach links verschoben
		{
			if (spielerPunkte > 9)
				punkteAbstand = 20;
		}
		
		
		void gameOver()
		{
			switch(resultat)
			{
				case -1:
					gegnerPunkte += 1;
					Status.letzterPunkt = false;
					ball.reset();
					break;
					
				case 1:
					spielerPunkte += 1;
					Status.letzterPunkt = true;
					ball.reset();
					break;
				
				case 0:
					break;
			}
		}
	}
}


