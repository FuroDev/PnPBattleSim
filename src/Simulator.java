import java.util.*;

public class Simulator {
    private static final Random random = new Random();

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Welcome to the Battle Simulator.");

//      Character Creation
        int[] numberOfCharacters = CharacterCreation.defineNumberOfCharacters(input);
        ArrayList<Fighter> playerFighters = CharacterCreation.establishPlayerTeam(numberOfCharacters[0], input);
        ArrayList<Fighter> nonPlayerFighters = CharacterCreation.establishEnemyTeam(numberOfCharacters[1], input);
        ArrayList<Fighter> allFighters = new ArrayList<>(numberOfCharacters[0] + numberOfCharacters[1]);
        allFighters.addAll(playerFighters);
        allFighters.addAll(nonPlayerFighters);

        // Start Battle
        Battle.assignInitiative(allFighters);
        Battle.sortByInitiative(allFighters);

//      Battle Simulator
        System.out.println("All characters created. How many battles do you want to simulate?");
        int numberOfBattlesToSimulate = Integer.parseInt(input.nextLine());
        int[] turnsUntilEachBattleEnded = new int[numberOfBattlesToSimulate];
        int[] playerDeathsEachBattle = new int[numberOfBattlesToSimulate];
        boolean[] isPlayerVictory = new boolean[numberOfBattlesToSimulate];
        int playerVictoriesCount = 0;

        for (int i = 0; i < numberOfBattlesToSimulate; i++) {
            System.out.println("Let the fight begin! Round 1!");
            for (Fighter fighter : allFighters) {
                System.out.println(fighter.getName() + "'s initiative: " + fighter.getInitiative());
            }
            int numberOfTurns = 0;

            while (!groupIsDefeated(playerFighters) && !groupIsDefeated(nonPlayerFighters)) {
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
                    if (IsPlayer && !IsDefeated && hasAmmunition && !endOfTurn && !groupIsDefeated(nonPlayerFighters)) {
                        Fighter target = nonPlayerFighters.get(random.nextInt(nonPlayerFighters.size()));
                        while (target.isDefeated()) {
                            target = nonPlayerFighters.get(random.nextInt(nonPlayerFighters.size()));
                        }
                        performAttack(attacker, target);
                    } else if (!IsDefeated && !endOfTurn && !groupIsDefeated(playerFighters)) {
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
                isPlayerVictory[i] = true;
                playerVictoriesCount++;

            } else if (groupIsDefeated(playerFighters)) {
                System.out.println("You were defeated! The battle lasted " + numberOfTurns + " rounds. " +
                        "\n ////////////////// \n //////////////////");
                isPlayerVictory[i] = false;
            }
            turnsUntilEachBattleEnded[i] = numberOfTurns;
            for (Fighter player : playerFighters) {
                if (player.isDefeated()) {
                    playerDeathsEachBattle[i]++;
                }
            }

            for (Fighter fighter : allFighters) {
                fighter.setCurrentHealth(fighter.getMaxHealth());
                fighter.getWeapon().reloadWeapon();
            }
        }

//      statistics
        for (int j = 0; j < turnsUntilEachBattleEnded.length; j++) {
            System.out.println("Battle " + (j+1) + " --- " + turnsUntilEachBattleEnded[j] + " rounds --- " +
                    "Player deaths: " + playerDeathsEachBattle[j] +
                    (isPlayerVictory[j] ? " --- -> Victory" : " --- -> Defeat"));
        }
        System.out.println("Win rate: " + (100/numberOfBattlesToSimulate * playerVictoriesCount) + "%");
        input.close();
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