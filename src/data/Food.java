package data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//food gives buffs.  Effect is immediate and lasts one round
public class Food {
	// adds to health
	public static int jerky;
	// adds to insta kill chance
	public static int fruit;
	// adds to reload speed
	public static int twinkie;
	// adds to ammo
	public static int mushroom;

	public static void applyFoodEffects() {
		// this method is called once in the main's devConfig method
	}

	// number of loops is same as number of hotspots detected
	public static void generateFood(int loops) {
		// every time food is searched, all values go to zero
		jerky = fruit = twinkie = mushroom = 0;
		Random random = new Random();
		for (int x = loops; x > 0; x--) {
			//will change to 7 for chance of loss. For now, need see if work.
			int chance = random.nextInt(4) + 1;
			switch (chance) {
			case 1:
				jerky++;
				break;
			case 2:
				fruit++;
				break;
			case 3:
				twinkie++;
				break;
			case 4:
				mushroom++;
				break;				
			}
		}

	}
	public static String getInfo(){
		
		String info = "Found: \n\n " +
				jerky + " Jerky\n" +
				fruit + " Fruit\n" +
				twinkie + " Twinkie\n" +
				mushroom + " Mushroom\n";
		
		return info;
		
	}
	
	public static int getJerky(){
		
		return jerky;
		
	}
}
