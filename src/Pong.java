public class Pong 
{
	public static void main(String[] args)
	{
		PongFenster spiel = new PongFenster("Marvin's Pong v1.2.3");
		spiel.setVisible(true);
		spiel.pack();
		spiel.setResizable(false);
	}
}
