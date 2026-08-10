package power_and_cost.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import power_and_cost.power.AnvilCostFormulas;
import power_and_cost.power.PowerLevelCalculator;
import power_and_cost.power.PowerLevelConfig;

public class SameItemRepairDurabilityGameTest {

    private static final PowerLevelConfig CONFIG = new PowerLevelConfig();
    private static final PowerLevelCalculator CALCULATOR = new PowerLevelCalculator(CONFIG);
    private static final AnvilCostFormulas FORMULAS = new AnvilCostFormulas(CONFIG);

    @GameTest(maxTicks = 200)
    public void sameItemRepairScalesDurabilityByRepairFactor(GameTestHelper context) {
        AnvilMenu menu = AnvilGameTestHelper.openAnvilMenu(context);

        ItemStack left = new ItemStack(Items.DIAMOND_SWORD);
        left.setDamageValue(1200);
        ItemStack right = new ItemStack(Items.DIAMOND_SWORD);
        right.setDamageValue(1200);

        AnvilGameTestHelper.populateAnvil(menu, left, right, null);

        context.succeedWhen(() -> {
            ItemStack result = AnvilGameTestHelper.resultStack(menu);
            if (result.isEmpty()) {
                throw AnvilGameTestHelper.fail("Expected same-item repair result");
            }

            double factor = FORMULAS.durabilityRepairFactor(CALCULATOR.computeEnchantmentPowerValue(left));
            int vanillaResultDamage = AnvilGameTestHelper.vanillaSameItemResultDamage(left, right);
            int vanillaRestored = left.getDamageValue() - vanillaResultDamage;
            int expectedRestored = (int) Math.round(vanillaRestored * factor);
            int expectedDamage = Math.max(0, left.getDamageValue() - expectedRestored);

            if (result.getDamageValue() != expectedDamage) {
                throw AnvilGameTestHelper.fail(
                        "Expected damage " + expectedDamage + " (factor " + factor + "), got "
                                + result.getDamageValue()
                );
            }
        });
    }
}
