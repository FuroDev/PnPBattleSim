import java.util.*;

public class Weapon {
    private static final Random random = new Random();

    private WeaponType weaponType;
    private AmmunitionType ammunitionType;
    private int ammunition;

    public Weapon(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public Weapon(WeaponType weaponType, AmmunitionType ammunitionType) {
        this.weaponType = weaponType;
        this.ammunitionType = ammunitionType;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public AmmunitionType getAmmunitionType() {
        return ammunitionType;
    }

    public void setAmmunitionType(AmmunitionType ammunitionType) {
        this.ammunitionType = ammunitionType;
    }

    public int getDamage() {

        final boolean carriesMeleeWeapon = getWeaponType() == WeaponType.MELEE;
        final boolean carriesGun = getWeaponType() == WeaponType.GUN;
        final boolean carriesShotgun = getWeaponType() == WeaponType.SHOTGUN;
        final boolean carriesRifle = getWeaponType() == WeaponType.RIFLE;
        final boolean carriesSniperRifle = getWeaponType() == WeaponType.SNIPER_RIFLE;

        final boolean isProjectileWeapon = getAmmunitionType() == AmmunitionType.PROJECTILE;
        final boolean isEnergyWeapon = getAmmunitionType() == AmmunitionType.ENERGY;

        if (carriesMeleeWeapon) {
            return random.nextInt(37) + 8;
        }

        if (carriesGun && isProjectileWeapon) {
            return random.nextInt(37) + 4;
        } else if (carriesGun && isEnergyWeapon) {
            return random.nextInt(19) + 11;
        }

        if (carriesShotgun && isProjectileWeapon) {
            return random.nextInt(37) + 7;
        } else if (carriesShotgun && isEnergyWeapon) {
            return random.nextInt(37) + 5;
        }

        if (carriesRifle && isProjectileWeapon) {
            return random.nextInt(28) + 9;
        } else if (carriesRifle && isEnergyWeapon) {
            return random.nextInt(28) + 7;
        }

        if (carriesSniperRifle && isProjectileWeapon) {
            return random.nextInt(46) + 7;
        } else if (carriesSniperRifle && isEnergyWeapon) {
            return random.nextInt(37) + 9;
        }
        return 0;
    }

    public int getMagazineCapacity() {
        return switch (getWeaponType()) {
            case MELEE -> 9999;
            case GUN -> 6;
            case SHOTGUN -> 3;
            case RIFLE -> 9;
            case SNIPER_RIFLE -> 2;
        };
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

    public static Weapon createWeapon(String name, Scanner input) {
        System.out.println("What kind of weapon should " + name + " use?");
        System.out.println("""
                Enter one of the following numbers:\s
                 0 -> Melee\s
                 1 -> Gun\s
                 2 -> Shotgun\s
                 3 -> Rifle\s
                 4 -> Sniper Rifle""");
        int weaponTypeChoice = Integer.parseInt(input.nextLine());
        WeaponType weaponType = WeaponType.values()[weaponTypeChoice];

        if (weaponType != WeaponType.MELEE) {
            System.out.println();
            System.out.println("""
                    What kind of ammunition type should the weapon have?\s
                    Enter one of the following numbers:\s
                     0 -> Projectile \s
                     1 -> Energy""");
            int ammunitionTypeChoice = Integer.parseInt(input.nextLine());
            AmmunitionType ammunitionType = AmmunitionType.values()[ammunitionTypeChoice];
            return new Weapon(weaponType, ammunitionType);
        }
        return new Weapon(weaponType);
    }
}
