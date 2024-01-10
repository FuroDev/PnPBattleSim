import java.util.*;

public class Fighter {
    private String name;
    private int currentHealth;
    private int maxHealth;
    private Weapon weapon;
    private int accuracy;
    private int initiative;

    public Fighter(String name, int currentHealth, int maxHealth, Weapon weapon, int accuracy) {
        this.name = name;
        this.currentHealth = currentHealth;
        this.weapon = weapon;
        this.accuracy = accuracy;
        this.maxHealth = maxHealth;
    }

    public String getName() {
        return name;
    }

    public int getCurrentHealth() {
        return currentHealth;
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

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
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
        setCurrentHealth(getCurrentHealth() - damage);
        if (getCurrentHealth() <= 0) {
            setCurrentHealth(0);
            System.out.println(getName() + " has been defeated.");
        }
    }

    public int[] damageToTarget() {
        Random random = new Random();
        int accuracyCheck = random.nextInt(99) + 1;

        boolean accuracyCheckFail = accuracyCheck > getAccuracy();
        boolean accuracyCheckCritical = accuracyCheck <= getAccuracy() / 10;

        int damage;
        int isCriticalHit = 0;

        if (accuracyCheckCritical) {
            isCriticalHit = 1;
        }

        if (accuracyCheckFail) {
            damage = -1;
        } else if (accuracyCheckCritical){
            damage = weapon.getDamage() * 2;
        } else {
            damage = weapon.getDamage();
        }
        return new int[] {damage, isCriticalHit};
    }

    public boolean isDefeated() {
        return currentHealth <= 0;
    }
}