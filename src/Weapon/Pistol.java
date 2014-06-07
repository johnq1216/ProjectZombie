package Weapon;
import com.QuachProjectZombie.R;

public class Pistol extends Weapon{

	public Pistol(){
		setRange(3);
		setGunClipSize(8);
		setTotalAmmoInClip(8);
		setGunReloadTime(600);
		setPushBack(1);
		setAuto(false);
		setWeaponName("Pistol");
		setImageRCode(R.drawable.pistol);
		//weaponDefault gives unlimited ammo but lowers reload speed when out of ammo
		setDamage(50);
		setCloseRangeMultiplier(2);
		setTotalAmmo(50);
	}

}
