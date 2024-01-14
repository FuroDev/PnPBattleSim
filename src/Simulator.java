import java.util.*;

public class Simulator {
    private static Random random = new Random();
    private static final int INITIATIVE_RANGE = 12;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ArrayList<Fighter> playerFighters = new ArrayList<>();
        ArrayList<Fighter> nonPlayerFighters = new ArrayList<>();

        System.out.println("Welcome to the Battle Simulator.");

//          Character Creation
        CharacterCreation characterCreation = new CharacterCreation();
        int playerCharactersCount = characterCreation.defineNumberOfCharacters(input);
        for (int i = 1; i <= playerCharactersCount; i++) {
            Fighter playerFighter = characterCreation.createPlayer(input,"Player-Character" + i);
            playerFighters.add(playerFighter);
        }
        System.out.println("How many Non-Player-Characters do you want to create?");
        int nonPlayerFightersCount = characterCreation.defineNumberOfCharacters(input);
        for (int i = 1; i <= nonPlayerFightersCount; i++) {
            Fighter nonPlayerFighter = characterCreation.createEnemy(input,"Non-Player-Character" + i);
            nonPlayerFighters.add(nonPlayerFighter);
        }

//      Assign initiative for all
        ArrayList<Fighter> allFighters = new ArrayList<>();
        allFighters.addAll(playerFighters);
        allFighters.addAll(nonPlayerFighters);
        assignInitiative(allFighters);
//      sort allFighters by initiative numerically
        allFighters.sort((f1, f2) -> Integer.compare(f2.getInitiative(), f1.getInitiative()));

//      Battle Simulator
        System.out.println("All characters created. How many battles do you want to simulate?");
        int numberOfBattles = Integer.parseInt(input.nextLine());
        int[] turnsInBattle = new int[numberOfBattles];
        int[] playerDeathsInBattle = new int[numberOfBattles];
        boolean[] playerVictories = new boolean[numberOfBattles];
        int playerVictoriesCount = 0;

        for (int i = 0; i < numberOfBattles; i++) {
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

//          victory or defeat?
            if (groupIsDefeated(nonPlayerFighters)) {
                System.out.println("You have won! The battle lasted " + numberOfTurns + " rounds. " +
                        "\n ////////////////// \n //////////////////");
                playerVictories[i] = true;
                playerVictoriesCount++;

            } else if (groupIsDefeated(playerFighters)) {
                System.out.println("You were defeated! The battle lasted " + numberOfTurns + " rounds. " +
                        "\n ////////////////// \n //////////////////");
                playerVictories[i] = false;
            }
            turnsInBattle[i] = numberOfTurns;
            for (Fighter player : playerFighters) {
                if (player.isDefeated()) {
                    playerDeathsInBattle[i]++;
                }
            }

            for (Fighter fighter : allFighters) {
                fighter.setCurrentHealth(fighter.getMaxHealth());
                fighter.getWeapon().reloadWeapon();
            }
        }

//      statistics
        for (int j = 0; j < turnsInBattle.length; j++) {
            System.out.println("Battle " + (j+1) + " // " + turnsInBattle[j] + " rounds // " +
                    "Player deaths: " + playerDeathsInBattle[j] +
                    (playerVictories[j] ? " // -> Victory" : " // -> Defeat"));
        }
        System.out.println("Win rate: " + (100/numberOfBattles * playerVictoriesCount) + "%");
        input.close();
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