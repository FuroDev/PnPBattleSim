import java.util.*;

public class Fighter {
    private String name;
    private int health;
    private int maxHealth;
    private int damage;
    private int accuracy;
    private int initiative;

    public Fighter(String name, int health, int maxHealth, int damage, int accuracy) {
        this.name = name;
        this.health = health;
        this.damage = damage;
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

    public int getDamage() {
        return damage;
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

    public void setDamage(int damage) {
        this.damage = damage;
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

    public int damageToTarget() {
        Random random = new Random();
        int accuracyCheck = random.nextInt(100) + 1;
        boolean accuracyCheckFail = accuracyCheck > getAccuracy();
        boolean accuracyCheckCrit = accuracyCheck <= getAccuracy() / 10;
        if (accuracyCheckFail) {
            return -1;
        } else if (accuracyCheckCrit) {
            return getDamage()*2;
        }
        return getDamage();
    }

    public boolean isDefeated() {
        return health <= 0;
    }
}