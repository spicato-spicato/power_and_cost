package power_and_cost.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import power_and_cost.power.PowerLevelCalculator;
import power_and_cost.power.PowerLevelConfig;

public class EnchantmentPowerLevelGameTest {

    private static final PowerLevelCalculator CALCULATOR = new PowerLevelCalculator(new PowerLevelConfig());

    @GameTest(maxTicks = 200)
    public void sharpnessFiveDiamondSwordHasTotalPl140(GameTestHelper context) {
        ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
        AnvilGameTestHelper.enchant(context, sword, Enchantments.SHARPNESS, 5);

        context.succeedWhen(() -> {
            if (CALCULATOR.computeMaterialPowerValue(sword) != 70) {
                throw AnvilGameTestHelper.fail("Expected material PL 70");
            }
            if (CALCULATOR.computeEnchantmentPowerValue(sword) != 70) {
                throw AnvilGameTestHelper.fail("Expected enchantment PL 70");
            }
            if (CALCULATOR.computeTotalPowerValue(sword) != 140) {
                throw AnvilGameTestHelper.fail("Expected total PL 140, got " + CALCULATOR.computeTotalPowerValue(sword));
            }
        });
    }
}
