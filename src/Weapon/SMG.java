package Weapon;

import com.QuachProjectZombie.R;

public class SMG extends Weapon{

	public SMG(){
		super();
		//reduced range for balance
		setRange(1);
		setGunClipSize(40);
		setGunReloadTime(1300);
		setPushBack(1);
		setAuto(true);
		setWeaponName("SMG");
		setImageRCode(R.drawable.smg);
		//weaponDefault gives unlimited ammo but lowers reload speed when out of ammo
		setTotalAmmo(0);
		setCloseRangeMultiplier(2);
		setDamage(50);

		
	}
}
