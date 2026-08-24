package power_and_cost.power;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

/**
 * Enchanting-table slot XP from option A: resulting PL = input total PL + offered enchant PLs.
 * Uses the same sigmoid as {@link AnvilCostFormulas#enchantCostFromPL(int)}.
 */
public final class TableEnchantCost {

    private final PowerLevelCalculator calculator;
    private final AnvilCostFormulas formulas;

    public TableEnchantCost(PowerLevelCalculator calculator, AnvilCostFormulas formulas) {
        this.calculator = calculator;
        this.formulas = formulas;
    }

    public int combinedPL(int inputTotalPL, int offeredEnchantmentPL) {
        return inputTotalPL + offeredEnchantmentPL;
    }

    public int slotCostFromPL(int inputTotalPL, int offeredEnchantmentPL) {
        return formulas.enchantCostFromPL(combinedPL(inputTotalPL, offeredEnchantmentPL));
    }

    public int slotCost(ItemStack input, int offeredEnchantmentPL) {
        return slotCostFromPL(calculator.computeTotalPowerValue(input), offeredEnchantmentPL);
    }

    public int slotCost(ItemStack input, Holder<Enchantment> enchantment, int level) {
        return slotCost(input, calculator.computeEnchantmentPower(enchantment, level));
    }

    public int slotCost(ItemStack input, Iterable<EnchantmentInstance> offered) {
        return slotCost(input, offeredEnchantmentPL(offered));
    }

    public int offeredEnchantmentPL(Iterable<EnchantmentInstance> offered) {
        int total = 0;
        for (EnchantmentInstance instance : offered) {
            total += calculator.computeEnchantmentPower(instance.enchantment(), instance.level());
        }
        return total;
    }
}
