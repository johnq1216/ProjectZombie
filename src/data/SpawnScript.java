package data;

import graphics.SpriteFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import threadAndTimers.Timer;

import android.view.SurfaceView;

import com.JohnQ1216.ScavengerZombieDefense.Briefing;
import com.JohnQ1216.ScavengerZombieDefense.Main;
import com.JohnQ1216.ScavengerZombieDefense.Main.GameView;

import zombieTypes.*;

public class SpawnScript {

	private static int randInt;
	private static Random random = new Random();
	public static List<String> benchList = new ArrayList<String>() {
		{
			// walker: default zombie

			// ragehead: runs faster with each hit
			// runner: runs fast
			// tank: sustains heavy damage
			// Hyper: runs super fast

			// smoker and bomber are next
			add("walker");
			add("ragehead");
			add("runner");
			add("tank");
			add("hyper");
			add("banshee");
		}
	};
	public static List<String> activeList = new ArrayList<String>();

	/**
	 * this method should be called on onCreate() of SpriteFactory
	 * 
	 * If the benchlist is empty, the method does nothing.
	 * 
	 * At level 1, the Walker will be transfered from bench to active
	 * 
	 * After level 1, random zombies will be added to Active
	 * 
	 * @param gameLevel
	 */
	public static void draft(int gameLevel) {

		if (!benchList.isEmpty()) {
			if (gameLevel == 1) {
				activeList.add(benchList.get(0));
				benchList.remove(0);
			} else {
				int random = new Random().nextInt(benchList.size());
				activeList.add(benchList.get(random));
				benchList.remove(random);
			}
		}

	}

	// At the beginning of the run loop in Spritefactory, it'll call this class.
	// Spawnscript will then increment some counters in the Spritefactory.
	// the Spritefactory will rez or spawn zombies based on how much is in the
	// counters.

	// break statements are needed to stop wierdness from happening

	// if a zombie should be resurrected
	public static void checkRand(int rand, SpriteFactory factory) {
		if (activeList.get(rand).equals("runner"))
			factory.counterZombieRunner++;
		else if (activeList.get(rand).equals("tank"))
			factory.counterZombieTank++;
		else if (activeList.get(rand).equals("ragehead"))
			factory.counterEliteRage++;
		else if (activeList.get(rand).equals("hyper"))
			factory.counterEliteHyper++;
		else if (activeList.get(rand).equals("walker"))
			factory.counterZombieWalker++;
		else if (activeList.get(rand).equals("banshee"))
			factory.counterEliteBanshee++;
	}

	public static void spawnZombies(SpriteFactory factory) {
		// TODO Auto-generated method stub

		randInt = random.nextInt(100) + 1;

		switch (Briefing.gameLevel) {
		case 1:
			if (randInt <= 50) {
				factory.counterZombieWalker++;
				// factory.counterEliteBanshee++;

			}
			randInt = random.nextInt(100) + 1;
			if (randInt <= 30) {
				randInt = random.nextInt(activeList.size());
				checkRand(randInt, factory);
			}
			break;
		case 2:
			if (randInt <= 50) {
				factory.counterZombieWalker++;
			}
			randInt = random.nextInt(100) + 1;
			if (randInt <= 30) {
				randInt = random.nextInt(activeList.size());
				checkRand(randInt, factory);
			}
			break;
		case 3:
			if (randInt <= 50) {
				factory.counterZombieWalker++;
			}
			randInt = random.nextInt(100) + 1;
			if (randInt <= 40) {
				randInt = random.nextInt(activeList.size());
				checkRand(randInt, factory);
			}
			break;
		case 4:
			if (randInt <= 50) {
				factory.counterZombieWalker++;
			}
			randInt = random.nextInt(100) + 1;
			if (randInt <= 45) {
				randInt = random.nextInt(activeList.size());
				checkRand(randInt, factory);
			}
			break;

		case 5:
			if (randInt <= 50) {
				factory.counterZombieWalker++;
			}
			randInt = random.nextInt(100) + 1;
			if (randInt <= 50) {
				randInt = random.nextInt(activeList.size());
				checkRand(randInt, factory);
			}
			break;
		default:
			if (randInt <= 30) {
				factory.counterZombieWalker++;
			} else if (randInt <= 20) {
				randInt = random.nextInt(activeList.size());
				checkRand(randInt, factory);
			}
			randInt = random.nextInt(100) + 1;
			if (randInt <= 50) {
				randInt = random.nextInt(activeList.size());
				checkRand(randInt, factory);
			}
		}

	}
}
