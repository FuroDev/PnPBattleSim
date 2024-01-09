import java.util.*;

public class Weapon {
    private static final Random random = new Random();

    private static final int MELEE_WEAPON_TYPE = 0;
    private static final int GUN_WEAPON_TYPE = 1;
    private static final int SHOTGUN_WEAPON_TYPE = 2;
    private static final int RIFLE_WEAPON_TYPE = 3;
    private static final int SNIPER_RIFLE_WEAPON_TYPE = 4;

    private static final int PROJECTILE_WEAPON = 0;
    private static final int ENERGY_WEAPON = 1;

    private int weaponType;
    private int ammunitionType;
    private int ammunition;

    public Weapon(int weaponType) {
        this.weaponType = weaponType;
    }

    public Weapon(int weaponType, int ammunitionType) {
        this.weaponType = weaponType;
        this.ammunitionType = ammunitionType;
    }

    public int getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(int type) {
        this.weaponType = type;
    }

    public int getAmmunitionType() {
        return ammunitionType;
    }

    public void setAmmunitionType(int ammunitionType) {
        this.ammunitionType = ammunitionType;
    }

    public int getDamage() {

        final boolean carriesMeleeWeapon = getWeaponType() == MELEE_WEAPON_TYPE;
        final boolean carriesGun = getWeaponType() == GUN_WEAPON_TYPE;
        final boolean carriesShotgun = getWeaponType() == SHOTGUN_WEAPON_TYPE;
        final boolean carriesRifle = getWeaponType() == RIFLE_WEAPON_TYPE;
        final boolean carriesSniperRifle = getWeaponType() == SNIPER_RIFLE_WEAPON_TYPE;

        final boolean isProjectileWeapon = getAmmunitionType() == PROJECTILE_WEAPON;
        final boolean isEnergyWeapon = getAmmunitionType() == ENERGY_WEAPON;

        if (carriesMeleeWeapon) {
            return random.nextInt(44) + 8;
        }

        if (carriesGun && isProjectileWeapon) {
            return random.nextInt(40) + 4;
        } else if (carriesGun && isEnergyWeapon) {
            return random.nextInt(29) + 11;
        }

        if (carriesShotgun && isProjectileWeapon) {
            return random.nextInt(43) + 7;
        } else if (carriesShotgun && isEnergyWeapon) {
            return random.nextInt(41) + 5;
        }

        if (carriesRifle && isProjectileWeapon) {
            return random.nextInt(36) + 9;
        } else if (carriesRifle && isEnergyWeapon) {
            return random.nextInt(34) + 7;
        }

        if (carriesSniperRifle && isProjectileWeapon) {
            return random.nextInt(52) + 7;
        } else if (carriesSniperRifle && isEnergyWeapon) {
            return random.nextInt(45) + 9;
        }
        return 0;
    }

    public int getMagazineCapacity() {
        switch (getWeaponType()) {
            case MELEE_WEAPON_TYPE:
                return 9999;
            case GUN_WEAPON_TYPE:
                return 6;
            case SHOTGUN_WEAPON_TYPE:
                return 3;
            case RIFLE_WEAPON_TYPE:
                return 9;
            case SNIPER_RIFLE_WEAPON_TYPE:
                return 2;
            default:
                return 0;
        }
    }

    public int getAmmunition() {
        return ammunition;
    }

    public void setAmmunition(int ammunition) {
        this.ammunition = ammunition;
    }

    public void reloadWeapon() {
        setAmmunition(getMagazineCapacity());

    }
}
