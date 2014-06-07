package Weapon;

import com.QuachProjectZombie.R;

public class Shotgun extends Weapon{

	public Shotgun(){
		super();
		setRange(3);
		setGunClipSize(5);
		setGunReloadTime(700);
		setPushBack(7);
		setAuto(false);
		setWeaponName("Shotgun");
		setImageRCode(R.drawable.shotgun);
		//weaponDefault gives unlimited ammo but lowers reload speed when out of ammo
		setTotalAmmo(0);
		setCloseRangeMultiplier(4);
		setDamage(50);

	}
}