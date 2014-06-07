package Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.JohnQ1216.ScavengerZombieDefense.Briefing;
import com.QuachProjectZombie.R;

public abstract class Weapon {

	// weapons will be removed after the round ends to prevent crashes.
	// if the weapons run out of ammo, it cannot be used for that round unless
	// refilled by a powerup that round

	// leave this open for other classes to use freely
	private static List<Weapon> chosenWeapons = new ArrayList<Weapon>();

	/*
	 * clipSize: 6 to 12 reloadTime: 1 to 2 seconds. Expressed with decimal.
	 * range: 20% to 100% pushBack: 1 to 4
	 */

	// gun class needed for graphic and sound. The graphic will have random
	// color tinting
	public String weaponNameString;
	double gunClass;

	private int gunClipSize, gunRange, gunPushBack, gunDamage, gunRangeMultiplier;
	// these variable is tangible
	private int totalAmmo, totalAmmoInClip, iceAmmo, exAmmo;
	private int gunReloadTime;
	private boolean gunAutomatic = false;
	protected int imageRCode;

	public boolean checkAuto() {
		return gunAutomatic;
	}

	public String getWeaponClass() {
		return weaponNameString;
	}

	public static int getImageRCode(int index) {

		return chosenWeapons.get(index).imageRCode;
	}

	public static String outPrint(int index) {
		String weaponData = "";

		weaponData = weaponData + "Weapon: "
				+ chosenWeapons.get(index).weaponNameString + "\nClip Size: "
				+ chosenWeapons.get(index).gunClipSize + "\nRange: "
				+ chosenWeapons.get(index).gunRange + "\nReloadTime: "
				+ chosenWeapons.get(index).gunReloadTime / 1000
				+ "\nPushback distance: "
				+ chosenWeapons.get(index).gunPushBack;

		return weaponData;

	}

	public static String outPrint() {

		String weaponData = "";

		for (int i = chosenWeapons.size() - 1; i >= 0; i--) {
			Weapon weapon = chosenWeapons.get(i);
			String fireMode = (weapon.gunAutomatic) ? "Automatic" : "Semi-Auto";
			weaponData = weaponData + "\n\nWeapon Class: "
					+ weapon.weaponNameString + "\nClip Size: "
					+ weapon.gunClipSize + "\nRange: " + weapon.gunRange
					+ "\nReloadTime: " + weapon.gunReloadTime / 1000
					+ "\nPushback distance: " + weapon.gunPushBack
					+ "\nFireMode: " + fireMode;
		}
		return weaponData;
	}

	public static void addDummyPistol() {
		chosenWeapons.add(new Pistol());
	}

	public static Weapon getWeaponIndex(int index) {
		return chosenWeapons.get(index);
	}

	public static int getListSize() {
		return chosenWeapons.size();
	}

	public int getTotalAmmo() {
		return totalAmmo;
	}
	
	public void addIceAmmo(int n){
	   iceAmmo = iceAmmo + n;
	}
	
	public int getIceAmmo(){
		return iceAmmo;		
	}
	
	public void addToTotalAmmo(int a) {
		totalAmmo = totalAmmo + a;
	}

	public void setTotalAmmo(int a) {
		totalAmmo = a;
	}

	public void reload() {
		if (gunClipSize > totalAmmo) {
			totalAmmoInClip = totalAmmo;
			totalAmmo = 0;
		} else {
			totalAmmoInClip = gunClipSize;
			totalAmmo = totalAmmo - totalAmmoInClip;
		}
	}
	
	//called by main
	public int getReloadTime() {
		return gunReloadTime;
	}

	public void freeReload() {
		totalAmmoInClip = gunClipSize;
	}

	public int getTotalAmmoInClip() {
		return totalAmmoInClip;
	}

	public void setTotalAmmoInClip(int i) {
		totalAmmoInClip = i;
	}

	public void subtractBullet() {
		totalAmmoInClip--;
	}

	public int getRange() {
		return gunRange;
	}

	protected void setRange(int r) {
		gunRange = r;
	}

	protected void setGunClipSize(int c) {
		gunClipSize = c;
	}

	protected void setGunReloadTime(int r) {
		gunReloadTime = r;
	}

	protected void setPushBack(int p) {
		gunPushBack = p;
	}

	protected void setAuto(boolean a) {
		gunAutomatic = a;
	}

	protected void setWeaponName(String s) {
		weaponNameString = s;
	}

	protected void setImageRCode(int r) {
		imageRCode = r;
	}
	protected void setDamage(int d){
		gunDamage = d;
	}
	
	public int getDamage(){
		return gunDamage;
	}
	
	protected void setCloseRangeMultiplier(int m){
		gunRangeMultiplier = m;
	}
	public int getRangeMultiplier(){
		return gunRangeMultiplier;
	}

	public static void clearList() {
		chosenWeapons.clear();
	}

	public static void addWeapon(Weapon w) {
		chosenWeapons.add(w);
	}

	public static Weapon getWeaponListIndex(int x) {
		return chosenWeapons.get(x);
	}

	public boolean hasExRound() {
		// TODO Auto-generated method stub
		return exAmmo > 0;
	}
	
	public void addExRound(int n){
		exAmmo = exAmmo + n;
	}
}
