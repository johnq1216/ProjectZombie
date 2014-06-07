package Weapon;

import com.QuachProjectZombie.R;

public class Rifle extends Weapon{

	public Rifle(){
		super();
		setRange(4);
		setGunClipSize(30);
		setGunReloadTime(1100);
		setPushBack(2);
		setAuto(false);
		setWeaponName("Rifle");
		setImageRCode(R.drawable.rifle);
		//weaponDefault gives unlimited ammo but lowers reload speed when out of ammo
		setTotalAmmo(0);
		setCloseRangeMultiplier(2);
		setDamage(50);

	}
}
