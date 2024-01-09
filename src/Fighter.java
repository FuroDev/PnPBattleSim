import java.util.*;

public class Fighter {
    private String name;
    private int health;
    private int maxHealth;
    private Weapon weapon;
    private int accuracy;
    private int initiative;

    public Fighter(String name, int health, int maxHealth, Weapon weapon, int accuracy) {
        this.name = name;
        this.health = health;
        this.weapon = weapon;
        this.accuracy = accuracy;
        this.maxHealth = maxHealth;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getInitiative() {
        return initiative;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public void takeDamage(int damage) {
        if (damage == -1) {
            damage = 0;
        }
        setHealth(getHealth() - damage);
        if (getHealth() <= 0) {
            setHealth(0);
            System.out.println(getName() + " has been defeated.");
        }
    }

    public int[] damageToTarget() {
        Random random = new Random();
        int accuracyCheck = random.nextInt(100) + 1;

        boolean accuracyCheckFail = accuracyCheck > getAccuracy();
        boolean accuracyCheckCrit = accuracyCheck <= getAccuracy() / 10;

        int damage;
        int isCriticalHit;

        if (accuracyCheckCrit) {
            isCriticalHit = 1;
        } else {
            isCriticalHit = 0;
        }

        if (accuracyCheckFail) {
            damage = -1;
            isCriticalHit = 0;
        } else if (accuracyCheckCrit){
            damage = weapon.getDamage() * 2;
        } else {
            damage = weapon.getDamage();
        }
        return new int[] {damage, isCriticalHit};
    }

    public boolean isDefeated() {
        return health <= 0;
    }
}