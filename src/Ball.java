import javax.swing.*;

public class Ball 
{
	double x = 240;
	double y = 140;
	double temposchub = 0.5;
	int speedLevel = 0;
	double tempo = 3;
	double steigung_x = tempo;
	double steigung_y = tempo;
	
	//Simple Physik
	String richtung = "Rechts unten";

	
	Ball()
	{}
	
	void reset()
	{
		tempo = 3;
		speedLevel = 0;
		if (Status.letzterPunkt == false)
		{
			x = 240;
			y = 5* ((int) (Math.random() * 56));
			steigung_x = tempo;
			double posOderNeg = 1 - Math.random();
			if (posOderNeg > 0)
			{
				steigung_y = tempo;
				richtung = "Rechts unten";
			}
			else
			{
				steigung_y = tempo*-1;
				richtung = "Rechts oben";
			}
		}
		else
		{
			x = 240;
			y = 5* ((int) (Math.random() * 56));
			steigung_x = tempo*-1;
			double posOderNeg = 1 - Math.random();
			if (posOderNeg > 0)
			{
				steigung_y = tempo;
				richtung = "Links unten";
			}
			else
			{
				steigung_y = tempo*-1;
				richtung = "Links oben";
			}
		}
	}
	
	void vonRealistischAufSimpel()
	{
		tempo += ((speedLevel*temposchub)*0.5);
		
		if (steigung_x >= 0)
		{
			if (steigung_y >= 0)
				richtung = "Rechts unten";
			else
				richtung = "Rechts oben";
		}
		else
		{
			if (steigung_y >= 0)
				richtung = "Links unten";
			else
				richtung = "Links oben";
		}			
	}
	
	void vonSimpelAufRealistisch()
	{
		switch(richtung)
		{
			case "Rechts unten":
				steigung_x = tempo + (speedLevel*temposchub/2);
				steigung_y = tempo + (speedLevel*temposchub/2);
				break;
			
			case "Rechts oben":
				steigung_x = tempo + (speedLevel*temposchub/2);
				steigung_y = (tempo + (speedLevel*temposchub/2))*-1;
				break;
				
			case "Links unten":
				steigung_x = (tempo + (speedLevel*temposchub/2))*-1;
				steigung_y = tempo + (speedLevel*temposchub/2);
				break;
			
			case "Links oben":
				steigung_x = (tempo + (speedLevel*temposchub/2))*-1;
				steigung_y = (tempo + (speedLevel*temposchub/2))*-1;
				break;
		}
	}
	
	// Simple Physik
	// ---------------------------------------------------------------------------------------------
	
	
	void bewegen()
	{
		switch(richtung)
		{
			case "Rechts unten":
				x += tempo;
				y += tempo;
				if (y >= 280)
				{
					richtung = "Rechts oben";
				}
				if (x >= 480)
				{
					richtung = "Links unten";
				}
				break;
				
			case "Rechts oben":
				x += tempo;
				y -= tempo;
				if (y <= 0)
				{
					richtung = "Rechts unten";
				}
				if (x >= 480)
				{
					richtung = "Links oben";
				}
				break;
				
			case "Links unten":
				x -= tempo;
				y += tempo;
				if (y >= 280)
				{
					richtung = "Links oben";
				}
				if (x <= 0)
				{
					richtung = "Rechts unten";
				}
				break;
			
			case "Links oben":
				x -= tempo;
				y -= tempo;
				if (y <= 0)
				{
					richtung = "Links unten";
				}
				if (x <= 0)
				{
					richtung = "Rechts oben";
				}
				break;
		} 
		
		if ((x <= 0) && (y <= 0))
		{
			richtung = "Rechts unten";
		}
		
		if ((x >= 480) && (y <= 0))
		{
			richtung = "Links unten";
		}
		
		if ((x <= 0) && (y >= 280))
		{
			richtung = "Rechts oben";
		}
		
		if ((x >= 480) && (y >= 280))
		{
			richtung = "Links oben";
		}
	}
	
	void spielerKollision(int balken_x, int balken_y)
	{
		switch(richtung)
		{
			case "Links oben":
				if ((((balken_x + 20) >= x) && (balken_x +20) <= (x+10)) && (y+10 >= balken_y && (y+10 <= (balken_y + 80))))
				{
					richtung = "Rechts oben"; 
					tempo += (temposchub * 0.5);
					speedLevel += 1;
				}
				break;
				
			case "Links unten":
				if ((((balken_x + 20) >= x) && (balken_x +20) <= (x+10)) && (y+10 >= balken_y && (y+10 <= (balken_y + 80))))
				{
					richtung = "Rechts unten"; 
					tempo += (temposchub * 0.5);
					speedLevel += 1;
				}
				break;
				
			default:
				break;	
		}
	}
	
	void gegnerKollision(int balken_x, int balken_y)
	{
		switch(richtung)
		{				
			case "Rechts oben":
				if ((((balken_x - 20) <= x) && ((balken_x - 10) >= x) && (y+10 >= balken_y && (y+10 <= (balken_y + 80)))))
				{
					richtung = "Links oben";
					tempo += (temposchub * 0.5);
					speedLevel += 1;
				}
				break;
				
			case "Rechts unten":
				if ((((balken_x - 20) <= x) && ((balken_x - 10) >= x) && (y+10 >= balken_y && (y+10 <= (balken_y + 80)))))
				{
					richtung = "Links unten";
					tempo += (temposchub * 0.5);
					speedLevel += 1;
				}
				break;	
				
			default: 
				break;
		}
	}
	
	
	// Realistische Physik
	// ---------------------------------------------------------------------------------------------
	
	void r_bewegen()
	{
		x += steigung_x;
		y += steigung_y;
		
		if (y <= 0) {steigung_y *= -1; y = 0;}
		
		if (y >= 280) {steigung_y *= -1; y = 280;}
		
		if (x <= 0) {steigung_x *= -1; x = 0;}
		
		if (x >= 480) {steigung_x *= -1; x = 480;}
	}
	
	void r_spielerKollision(int balken_x, int balken_y)
	{
		if (((x <= (balken_x + 20)) && (x >= (balken_x + 10))) && (((y+10) >= balken_y) && ((y+10) <= (balken_y + 40)))) 	// Trifft obere Hälfte vom Balken (+10 Pixel dahinter wegen Kollisionsabfrage, +10 y-Pixel beim Ball, da so die Mitte und nicht die obere linke Ecke als Aufprallpunkt berechnet wird)
		{
			double steigungsTempo = Math.sqrt(Math.pow(steigung_x, 2) + Math.pow(steigung_y, 2));     						// Steigungsvektor
			int neueSteigung_x = ((int) (y+10)) - balken_y;																	// Aufprallpunkt kann als neue x-Steigung genommen werden. Je näher zur Mitte (näher an 40), desto mehr x
			int neueSteigung_y = 40 - neueSteigung_x;																		// und weniger y. Vice versa.
				
			double neueSteigungMitTempo_x = Math.sqrt((Math.pow(steigungsTempo, 2) / 40) * neueSteigung_x);   				// Tempoerhaltung dank Satz des Pythagoras. Hypotenuse bzw. Steigungsvektor durch 40 (Balkenlänge) multipliziert mit den neuen Steigungen
			double neueSteigungMitTempo_y = Math.sqrt((Math.pow(steigungsTempo, 2) / 40) * neueSteigung_y);	  				// Da beide Steigungen zusammen bereits 40 ergeben, bekommt man so einen neuen Steigungsvektor mit gleicher Länge heraus
				
			steigung_x = neueSteigungMitTempo_x +temposchub;
			steigung_y = neueSteigungMitTempo_y *-1;
			speedLevel += 1;
			x = 40;
				
		}
		
		if (((x <= (balken_x + 20)) && (x >= (balken_x + 10))) && (((y+10) > (balken_y+40)) && ((y+10) <= (balken_y + 80))))
		{
			double steigungsTempo = Math.sqrt(Math.pow(steigung_x, 2) + Math.pow(steigung_y, 2));
			int neueSteigung_y = ((int) (y+10)) - (balken_y+40);
			int neueSteigung_x = 40 - neueSteigung_y;
			
			double neueSteigungMitTempo_x = Math.sqrt((Math.pow(steigungsTempo, 2) / 40) * neueSteigung_x);
			double neueSteigungMitTempo_y = Math.sqrt((Math.pow(steigungsTempo, 2) / 40) * neueSteigung_y);
			
			steigung_x = neueSteigungMitTempo_x + temposchub;
			steigung_y = neueSteigungMitTempo_y;
			speedLevel += 1;
			x = 40;
			
		}
	}

	
	void r_gegnerKollision(int balken_x, int balken_y)
	{
		if (((x >= (balken_x - 20)) && (x <= (balken_x - 10))) && (((y+10) >= balken_y) && ((y+10) <= (balken_y + 40))))
		{
			double steigungsTempo = Math.sqrt(Math.pow(steigung_x, 2) + Math.pow(steigung_y, 2));
			int neueSteigung_x = ((int) (y+10)) - balken_y;
			int neueSteigung_y = 40 - neueSteigung_x;
				
			double neueSteigungMitTempo_x = Math.sqrt((Math.pow(steigungsTempo, 2) / 40) * neueSteigung_x);
			double neueSteigungMitTempo_y = Math.sqrt((Math.pow(steigungsTempo, 2) / 40) * neueSteigung_y);
				
			steigung_x = (neueSteigungMitTempo_x + temposchub)*-1;
			steigung_y = neueSteigungMitTempo_y*-1;
			speedLevel += 1;
			x = 440;
				
		}
		
		if (((x >= (balken_x - 20)) && (x <= (balken_x - 10))) && (((y+10) > (balken_y+40)) && ((y+10) <= (balken_y + 80))))
		{
			double steigungsTempo = Math.sqrt(Math.pow(steigung_x, 2) + Math.pow(steigung_y, 2));
			int neueSteigung_y = ((int) (y+10)) - (balken_y+40);
			int neueSteigung_x = 40 - neueSteigung_y;
			
			double neueSteigungMitTempo_x = Math.sqrt((Math.pow(steigungsTempo, 2) / 40) * neueSteigung_x);
			double neueSteigungMitTempo_y = Math.sqrt((Math.pow(steigungsTempo, 2) / 40) * neueSteigung_y);
			
			steigung_x = (neueSteigungMitTempo_x + temposchub)*-1;
			steigung_y = neueSteigungMitTempo_y;
			speedLevel += 1;
			x = 440;
		}
	}
	
	int imAus()
	{
		if (x <= 0)
			return -1;
		if (x >= 480)
			return 1;
		else
			return 0;
	}
}
