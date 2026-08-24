package power_and_cost;

/**
 * Enchanting-table XP to charge, separate from vanilla {@code costs[]} (bookshelf power).
 */
public interface TableXpCostAccess {

    int[] power_and_cost$xpCosts();
}
