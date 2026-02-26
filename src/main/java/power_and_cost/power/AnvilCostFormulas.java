package power_and_cost.power;

/**
 * Sigmoid formulas for anvil cost and repair effectiveness.
 */
public final class AnvilCostFormulas {

    private final PowerLevelConfig config;

    public AnvilCostFormulas(PowerLevelConfig config) {
        this.config = config;
    }

    /**
     * XP level cost for enchant/combine operations.
     * Uses total power value (material + enchantment) of the resulting item.
     * Formula: ceil(minCost + (maxCost - minCost) / (1 + e^(-k × (totalPL - x0))))
     */
    public int enchantCostFromPL(int totalPL) {
        int minCost = config.getEnchantCostMin();
        int maxCost = config.getEnchantCostMax();
        int x0 = config.getEnchantCostX0();
        double k = config.getEnchantCostK();
        double exp = Math.exp(-k * (totalPL - x0));
        double cost = minCost + (maxCost - minCost) / (1.0 + exp);
        return (int) Math.ceil(cost);
    }

    /**
     * Durability repair factor (multiplier of vanilla repair quantity).
     * Uses enchantment power value only.
     * Formula: minRepair + (maxRepair - minRepair) / (1 + e^(k × (enchantmentPowerValue - x0)))
     */
    public double durabilityRepairFactor(int enchantmentPowerValue) {
        double minRepair = config.getRepairMin();
        double maxRepair = config.getRepairMax();
        int x0 = config.getRepairMidpoint();
        double k = config.getRepairSteepness();
        double exp = Math.exp(k * (enchantmentPowerValue - x0));
        return minRepair + (maxRepair - minRepair) / (1.0 + exp);
    }
}
