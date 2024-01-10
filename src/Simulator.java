import java.util.*;

public class Simulator {
    private static Random random = new Random();
    private static final int INITIATIVE_RANGE = 12;
    private static final int PLAYER_HEALTH = 100;

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            ArrayList<Fighter> playerFighters = new ArrayList<>();
            ArrayList<Fighter> nonPlayerFighters = new ArrayList<>();

            System.out.println("Welcome to the Battle Simulator.");

//          Character Creation
            System.out.println("Okay. Now, let us create Fighters.");
            System.out.println("How many Player-Characters do you want to create?");
            int playerCharactersCount = Integer.parseInt(input.nextLine());
            for (int i = 1; i <= playerCharactersCount; i++) {
                Fighter playerFighter = createPlayer(input, "Player-Character" + i);
                playerFighters.add(playerFighter);
            }
            System.out.println("How many Non-Player-Characters do you want to create?");
            int nonPlayerFightersCount = Integer.parseInt(input.nextLine());
            for (int i = 1; i <= nonPlayerFightersCount; i++) {
                Fighter nonPlayerFighter = createEnemy(input, "Non-Player-Character" + i);
                nonPlayerFighters.add(nonPlayerFighter);
            }

//          Assign initiative for all
            ArrayList<Fighter> allFighters = new ArrayList<>();
            allFighters.addAll(playerFighters);
            allFighters.addAll(nonPlayerFighters);
            assignInitiative(allFighters);
//          sort allFighters by initiative numerically
            allFighters.sort((f1, f2) -> Integer.compare(f2.getInitiative(), f1.getInitiative()));

//          Battle Simulator
            System.out.println("All characters created. How many battles do you want to simulate?");
            int numberOfBattles = Integer.parseInt(input.nextLine());
            int[] turnsInBattle = new int[numberOfBattles];
            int[] playerDeathsInBattle = new int[numberOfBattles];
            boolean[] playerVictories = new boolean[numberOfBattles];
            int playerVictoriesCount = 0;

            for (int battleIndex = 0; battleIndex < numberOfBattles; battleIndex++) {
                System.out.println("Let the fight begin! Round 1!");
                for (Fighter fighter : allFighters) {
                    System.out.println(fighter.getName() + "'s initiative: " + fighter.getInitiative());
                }
                int numberOfTurns = 0;

                while (!anyGroupIsDefeated(playerFighters) && !anyGroupIsDefeated(nonPlayerFighters)) {
                    System.out.println("Prepare for round " + (numberOfTurns + 1) + ".");

                    for (Fighter players : playerFighters) {
                        System.out.println(players.getName() + ": " + players.getCurrentHealth() + "HP");
                    }
                    for (Fighter nonPlayers : nonPlayerFighters) {
                        System.out.println(nonPlayers.getName() + ": " + nonPlayers.getCurrentHealth() + "HP");
                    }
                    System.out.println("=======================================");

                    numberOfTurns++;

                    for (Fighter attacker : allFighters) {

                        boolean IsPlayer = playerFighters.contains(attacker);
                        boolean IsDefeated = attacker.isDefeated();
                        boolean hasAmmunition = attacker.getWeapon().getAmmunition() != 0;
                        boolean endOfTurn = false;

                        if (!IsDefeated && !hasAmmunition) {
                            attacker.getWeapon().reloadWeapon();
                            System.out.println(attacker.getName() + " is out of ammunition and reloads!");
                            endOfTurn = true;
                        }
                        if (IsPlayer && !IsDefeated && hasAmmunition && !endOfTurn && !anyGroupIsDefeated(nonPlayerFighters)) {
                            Fighter target = nonPlayerFighters.get(random.nextInt(nonPlayerFighters.size()));
                            while (target.isDefeated()) {
                                target = nonPlayerFighters.get(random.nextInt(nonPlayerFighters.size()));
                            }
                            performAttack(attacker, target);
                        } else if (!IsDefeated && !endOfTurn && !anyGroupIsDefeated(playerFighters)) {
                            Fighter target = playerFighters.get(random.nextInt(playerFighters.size()));
                            while (target.isDefeated()) {
                                target = playerFighters.get(random.nextInt(playerFighters.size()));
                            }
                            performAttack(attacker, target);
                        }
                    }
                    System.out.println("End of round " + numberOfTurns + ".");
                }

                for (Fighter players : playerFighters) {
                    System.out.println(players.getName() + ": " + players.getCurrentHealth() + " HP");
                }
                for (Fighter nonPlayers : nonPlayerFighters) {
                    System.out.println(nonPlayers.getName() + ": " + nonPlayers.getCurrentHealth() + " HP");
                }

//              victory or defeat?
                if (groupIsDefeated(nonPlayerFighters)) {
                    System.out.println("You have won! The battle lasted " + numberOfTurns + " rounds. " +
                            "\n ////////////////// \n //////////////////");
                    playerVictories[battleIndex] = true;
                    playerVictoriesCount++;

                } else if (groupIsDefeated(playerFighters)) {
                    System.out.println("You were defeated! The battle lasted " + numberOfTurns + " rounds. " +
                            "\n ////////////////// \n //////////////////");
                    playerVictories[battleIndex] = false;
                }
                turnsInBattle[battleIndex] = numberOfTurns;
                for (Fighter player : playerFighters) {
                    if (player.isDefeated()) {
                        playerDeathsInBattle[battleIndex]++;
                    }
                }

                for (Fighter fighter : allFighters) {
                    fighter.setCurrentHealth(fighter.getMaxHealth());
                    fighter.getWeapon().reloadWeapon();
                }
            }

//          statistics
            for (int j = 0; j < turnsInBattle.length; j++) {
                System.out.println("Battle " + (j+1) + " // " + turnsInBattle[j] + " rounds // " +
                        "Player deaths: " + playerDeathsInBattle[j] +
                        (playerVictories[j] ? " // -> Victory" : " // -> Defeat"));
            }
            System.out.println("Win rate: " + (numberOfBattles/100*playerVictoriesCount) + "%");
        }
    }

    private static Fighter createPlayer(Scanner input, String prompt) {
        System.out.println("Creating " + prompt);
        System.out.println("Enter " + prompt + "'s name: ");
        String name = input.nextLine();
        int maxHealth = PLAYER_HEALTH;
        int currentHealth = maxHealth;

        Weapon weapon = createWeapon(name, input);
        weapon.reloadWeapon();

        System.out.println("Enter " + name + "'s accuracy: ");
        int accuracy = Integer.parseInt(input.nextLine());

        return new Fighter(name, currentHealth, maxHealth, weapon, accuracy);
    }

    private static Fighter createEnemy(Scanner input, String prompt) {
        System.out.println("Creating " + prompt);
        String name = prompt;
        System.out.println("Enter " + prompt + "'s health: ");
        int health = Integer.parseInt(input.nextLine());
        final int maxHealth = health;

        Weapon weapon = createWeapon(name, input);
        weapon.reloadWeapon();

        System.out.println("Enter " + prompt + "'s accuracy: ");
        int accuracy = Integer.parseInt(input.nextLine());

        return new Fighter(name, health, maxHealth, weapon, accuracy);
    }

    private static Weapon createWeapon(String name, Scanner input) {
        System.out.println("What kind of weapon should " + name + " use?");
        System.out.println("""
                Enter one of the following numbers:\s
                 0 -> Melee\s
                 1 -> Gun\s
                 2 -> Shotgun\s
                 3 -> Rifle\s
                 4 -> Sniper Rifle""");
        int weaponType = Integer.parseInt(input.nextLine());
        if (weaponType != 0) {
            System.out.println();
            System.out.println("""
                    What kind of ammunition type should the weapon have?\s
                    Enter one of the following numbers:\s
                     0 -> Projectile \s
                     1 -> Energy""");
            int ammunitionType = Integer.parseInt(input.nextLine());
            return new Weapon(weaponType, ammunitionType);
        }
        return new Weapon(weaponType);
    }

    private static void assignInitiative(ArrayList<Fighter> allFighters) {
        Set<Integer> usedInitiativeValues = new HashSet<>();
        for (Fighter fighter : allFighters) {
            int initiative;
            do {
                initiative = random.nextInt(INITIATIVE_RANGE) + 1;
            } while (!usedInitiativeValues.add(initiative));
            fighter.setInitiative(initiative);
        }
    }

    private static boolean anyGroupIsDefeated(ArrayList<Fighter> fighters) {
        for (Fighter fighter : fighters) {
            if (!fighter.isDefeated()) {
                return false;
            }
        }
        return true;
    }

    private static boolean groupIsDefeated(ArrayList<Fighter> fighters) {
        int fightersDefeated = 0;
        for (Fighter fighter : fighters) {
            if (fighter.isDefeated()) {
                fightersDefeated++;
            }
        }
        return fightersDefeated == fighters.size();
    }

    private static void performAttack(Fighter attacker, Fighter target) {
        System.out.println(attacker.getName() + " attacks " + target.getName() + ".");
        int[] attackResult = attacker.damageToTarget();
        int damage = attackResult[0];
        boolean isCriticalHit = attackResult[1] == 1;

        if (damage == -1) {
            System.out.println(attacker.getName() + " misses " + target.getName() + ".");
        } else if (isCriticalHit) {
            System.out.println(target.getName() + " gets critically hit and loses " + damage + "HP!");
        } else {
            System.out.println(target.getName() + " loses " + damage + "HP.");
        }
        target.takeDamage(damage);
        attacker.getWeapon().setAmmunition(attacker.getWeapon().getAmmunition() - 1);
    }
}