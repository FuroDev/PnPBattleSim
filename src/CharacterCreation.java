import java.util.*;

public class CharacterCreation {

    public static int[] defineNumberOfCharacters(Scanner input) {
        System.out.println("First, let us create Fighters.");
        int[] numberOfCharacters = new int[2];
        System.out.println("How many Player-Characters do you want to create?");
        numberOfCharacters[0] = Integer.parseInt(input.nextLine());
        System.out.println("How many Non-Player-Characters do you want to create?");
        numberOfCharacters[1] = Integer.parseInt(input.nextLine());
        return numberOfCharacters;
    }

    public static ArrayList<Fighter> establishPlayerTeam(int numberOfCharacters, Scanner input) {
        ArrayList<Fighter> playerFighters = new ArrayList<>(numberOfCharacters);
        for (int i = 1; i <= numberOfCharacters; i++) {
            Fighter playerFighter = CharacterCreation.createPlayer(input,"Player-Character" + i);
            playerFighters.add(playerFighter);
        }
        return playerFighters;
    }

    public static ArrayList<Fighter> establishEnemyTeam(int numberOfCharacters, Scanner input) {
        ArrayList<Fighter> nonPlayerFighters = new ArrayList<>(numberOfCharacters);
        for (int i = 1; i <= numberOfCharacters; i++) {
            Fighter nonPlayerFighter = CharacterCreation.createEnemy(input,"Non-Player-Character" + i);
            nonPlayerFighters.add(nonPlayerFighter);
        }
        return nonPlayerFighters;
    }

    public static Fighter createPlayer(Scanner input, String prompt) {
        System.out.println("Creating " + prompt);
        System.out.println("Enter " + prompt + "'s name: ");
        String name = input.nextLine();
        int maxHealth = 100;
        int currentHealth = maxHealth;

        Weapon weapon = Weapon.createWeapon(name, input);
        weapon.reloadWeapon();

        System.out.println("Enter " + name + "'s accuracy: ");
        int accuracy = Integer.parseInt(input.nextLine());

        return new Fighter(name, currentHealth, maxHealth, weapon, accuracy);
    }

    public static Fighter createEnemy(Scanner input, String prompt) {
        System.out.println("Creating " + prompt);
        String name = prompt;
        System.out.println("Enter " + prompt + "'s health: ");
        int health = Integer.parseInt(input.nextLine());
        final int maxHealth = health;

        Weapon weapon = Weapon.createWeapon(name, input);
        weapon.reloadWeapon();

        System.out.println("Enter " + prompt + "'s accuracy: ");
        int accuracy = Integer.parseInt(input.nextLine());

        return new Fighter(name, health, maxHealth, weapon, accuracy);
    }
}