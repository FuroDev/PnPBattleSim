import java.util.*;

public class Simulator {
    private static Random random = new Random();

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
            for (int i = 0; i < numberOfBattles; i++) {
                System.out.println("Let the fight begin! Round 1!");
                for (Fighter fighter : allFighters) {
                    System.out.println(fighter.getName() + ": " + fighter.getInitiative());
                }
                int numberOfRounds = 0;

                while (!anyGroupIsDefeated(playerFighters) && !anyGroupIsDefeated(nonPlayerFighters)) {

                    for (Fighter players : playerFighters) {
                        System.out.println(players.getName() + ": " + players.getHealth() + "HP");
                    }
                    for (Fighter nonPlayers : nonPlayerFighters) {
                        System.out.println(nonPlayers.getName() + ": " + nonPlayers.getHealth() + "HP");
                    }
                    System.out.println("=======================================");

                    numberOfRounds++;

                    for (Fighter attacker : allFighters) {

                        boolean IsPlayer = playerFighters.contains(attacker);
                        boolean IsDefeated = attacker.isDefeated();
                        boolean hasAmmunition = attacker.getWeapon().getAmmunition() != 0;

                        if (!IsDefeated && !hasAmmunition) {
                            attacker.getWeapon().reloadWeapon();
                            System.out.println(attacker.getName() + " has to reload the weapon!");
                        }
                        if (IsPlayer && !IsDefeated && hasAmmunition && !anyGroupIsDefeated(nonPlayerFighters)) {
                            Fighter target = nonPlayerFighters.get(random.nextInt(nonPlayerFighters.size()));
                            while (target.isDefeated()) {
                                target = nonPlayerFighters.get(random.nextInt(nonPlayerFighters.size()));
                            }
                            performAttack(attacker, target);
                        } else if (!IsDefeated && !anyGroupIsDefeated(playerFighters)) {
                            Fighter target = playerFighters.get(random.nextInt(playerFighters.size()));
                            while (target.isDefeated()) {
                                target = playerFighters.get(random.nextInt(playerFighters.size()));
                            }
                            performAttack(attacker, target);
                        }
                    }
                    System.out.println("End of round " + numberOfRounds + ".");
                    if (anyGroupIsDefeated(playerFighters) || anyGroupIsDefeated(nonPlayerFighters)) {
                        System.out.println("Prepare for round " + (numberOfRounds + 1) + ".");
                    }
                }

                for (Fighter players : playerFighters) {
                    System.out.println(players.getName() + ": " + players.getHealth() + " HP");
                }
                for (Fighter nonPlayers : nonPlayerFighters) {
                    System.out.println(nonPlayers.getName() + ": " + nonPlayers.getHealth() + " HP");
                }

//              Win or Loss?
                if (groupIsDefeated(nonPlayerFighters)) {
                    System.out.println("You have won! \n ////////////////// \n //////////////////");
                    System.out.println(numberOfRounds);

                } else if (groupIsDefeated(playerFighters)) {
                    System.out.println("You were defeated! \n ////////////////// \n //////////////////");
                }
                for (Fighter fighter : allFighters) {
                    fighter.setHealth(fighter.getMaxHealth());
                }
            }

        }
    }

    private static Fighter createPlayer(Scanner input, String prompt) {
        System.out.println("Creating " + prompt);
        System.out.println("Enter " + prompt + "'s name: ");
        String name = input.nextLine();
        int health = 100;
        int maxHealth = health;

        Weapon weapon = createWeapon(name, input);
        weapon.reloadWeapon();

        System.out.println("Enter " + name + "'s accuracy: ");
        int accuracy = Integer.parseInt(input.nextLine());

        return new Fighter(name, health, maxHealth, weapon, accuracy);
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
        System.out.println("Enter one of the following numbers: " +
                "\n 0 -> Melee \n 1 -> Gun \n 2 -> Shotgun \n 3 -> Rifle \n 4 -> Sniper Rifle");
        int weaponType = Integer.parseInt(input.nextLine());
        if (weaponType != 0) {
            System.out.println("What kind of ammunition type should the weapon have?");
            System.out.println("Enter one of the following numbers: " +
                    "\n 0 -> Projectile \n 1 -> Energy");
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
                initiative = random.nextInt(12) + 1;
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
    }
}