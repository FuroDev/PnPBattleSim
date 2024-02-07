import java.util.*;

public class Battle {

    private static final int INITIATIVE_RANGE = 12;
    private static Random random = new Random();

    public static void assignInitiative(ArrayList<Fighter> allFighters) {
        Set<Integer> usedInitiativeValues = new HashSet<>();
        for (Fighter fighter : allFighters) {
            int initiative;
            do {
                initiative = random.nextInt(INITIATIVE_RANGE) + 1;
            } while (!usedInitiativeValues.add(initiative));
            fighter.setInitiative(initiative);
        }
    }

    public static void sortByInitiative(ArrayList<Fighter> allFighters) {
        allFighters.sort((f1, f2) -> Integer.compare(f2.getInitiative(), f1.getInitiative()));
    }
}
