package power_and_cost.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import power_and_cost.power.AnvilCostFormulas;
import power_and_cost.power.PowerLevelCalculator;
import power_and_cost.power.PowerLevelConfig;

public class EnchantedBookCombineCostGameTest {

    private static final PowerLevelConfig CONFIG = new PowerLevelConfig();
    private static final PowerLevelCalculator CALCULATOR = new PowerLevelCalculator(CONFIG);
    private static final AnvilCostFormulas FORMULAS = new AnvilCostFormulas(CONFIG);

    @GameTest(maxTicks = 200)
    public void bookCombineUsesResultTotalPl(GameTestHelper context) {
        AnvilMenu menu = AnvilGameTestHelper.openAnvilMenu(context);

        ItemStack left = new ItemStack(Items.DIAMOND_SWORD);
        ItemStack book = AnvilGameTestHelper.enchantedBook(context, Enchantments.SHARPNESS, 5);
        AnvilGameTestHelper.populateAnvil(menu, left, book, null);

        context.succeedWhen(() -> {
            ItemStack result = AnvilGameTestHelper.resultStack(menu);
            if (result.isEmpty()) {
                throw AnvilGameTestHelper.fail("Expected book combine result");
            }

            int resultPl = CALCULATOR.computeTotalPowerValue(result);
            int expectedCost = FORMULAS.enchantCostFromPL(resultPl);

            if (menu.getCost() != expectedCost) {
                throw AnvilGameTestHelper.fail(
                        "Expected cost " + expectedCost + " from result PL " + resultPl + ", got " + menu.getCost()
                );
            }
        });
    }
}
