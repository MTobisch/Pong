public class Gegnerbalken
{
	int x = 460;
	int y = 150;
	boolean schlaueKI = false;
	int tempo = 4;
	double zielpos_y;
	double yFelderBisAufschlag;
	
	Gegnerbalken()
	{}
	
	void bewegen(int ballpos_x, int ballpos_y, String richtung)
	{
		if (schlaueKI == false)
		{
			if (ballpos_y > y + 40)
			{
				if (y < 220)
				y += tempo;
			}
			else
			{
				if (y > 0)
				y -= tempo;
			}
		}
		
		if (schlaueKI == true)
		{
			int zielpos_y = 0;
			
			switch(richtung)
			{
				case "Links oben":
					break;
					
				case "Links unten":
					break;
					
				case "Rechts oben":
					zielpos_y = ((440 - ballpos_x) - ballpos_y);
					if (zielpos_y < 0)
						zielpos_y *= -1;
					if (zielpos_y > 280)
						zielpos_y = 280 - (zielpos_y - 280);
					
					if ((y+30 < zielpos_y) && (y < 220))
						y += tempo;
					if ((y+30 > zielpos_y) && (y > 0))
						y-= tempo;
					
					break;
					
				case "Rechts unten":
					zielpos_y = ((440 - ballpos_x) + ballpos_y);
					if (zielpos_y < 0)
						zielpos_y *= -1;
					if (zielpos_y > 280)
						zielpos_y = 280 - (zielpos_y - 280);
					
					if ((y+30 < zielpos_y) && (y < 220))
						y += tempo;
					if ((y+30 > zielpos_y) && (y > 0))
						y-= tempo;
					
					break;
			}
		}
	}	
	
	void r_bewegen(double ballpos_x, double ballpos_y, double steigung_x, double steigung_y)
	{
		if (schlaueKI == false)
		{
			if (((int) ballpos_y) > y + 40)
			{
				if (((y < 220) && ((ballpos_y) - (y+40)) > tempo/2))    	// mind. Abstand zum Bewegen tempo/2, da Gegnerbalken sonst "zittert"
				y += tempo;
			}
			else
			{
				if ((y > 0) && (((y+40) - ballpos_y) > tempo/2))
				y -= tempo;
			}
		}
		
		if (schlaueKI == true)
		{
			if (steigung_x >= 0)
			{				
				int hochOderRunter;
				yFelderBisAufschlag = Math.sqrt(Math.pow((440 - ballpos_x) * (steigung_y / steigung_x), 2));
				
				if (steigung_y >= 0) 
				{
					hochOderRunter = 1;
				}
				else
				{
					hochOderRunter = -1;
				}
				
				if ((((ballpos_y + yFelderBisAufschlag) > 280) && (hochOderRunter == 1)) ||
					(((ballpos_y - yFelderBisAufschlag) < 0) && (hochOderRunter == -1)))
				{
					if (hochOderRunter == 1)
					{
						yFelderBisAufschlag = yFelderBisAufschlag - (280 - ballpos_y);
						hochOderRunter = -1;
					}
					else
					{
						yFelderBisAufschlag = yFelderBisAufschlag - ballpos_y;
						hochOderRunter = 1;
					}
					
					while (yFelderBisAufschlag > 280)
					{
						yFelderBisAufschlag -= 280;
						hochOderRunter *= -1;
					}
					
					if (hochOderRunter == 1)
					{
						zielpos_y = yFelderBisAufschlag;
					}
					else
					{
						zielpos_y = 280 - yFelderBisAufschlag;
					}
				}
				else
				{
					if (hochOderRunter == 1)
					{
						zielpos_y = ballpos_y + yFelderBisAufschlag;
					}
					else
					{
						zielpos_y = ballpos_y - yFelderBisAufschlag;
					}
				}
				
				// Gottseidank funktioniert der Scheiß endlich
				
				if ((y+40 < zielpos_y+10) && (y < 220))
					y += tempo;
				if ((y+40 > zielpos_y+10) && (y > 0))
					y-= tempo;
				
			}
			else		// Zurück in die Mitte, wenn Ball wegfliegt
			{
				if (y+40 < 150)
					y += tempo;
				if (y+40 > 150)
					y-= tempo;
			}
		}
	}
}