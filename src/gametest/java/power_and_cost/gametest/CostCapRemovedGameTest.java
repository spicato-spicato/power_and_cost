package power_and_cost.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CostCapRemovedGameTest {

    @GameTest(maxTicks = 200)
    public void highPlCombineIsNotCappedAtForty(GameTestHelper context) {
        AnvilMenu menu = AnvilGameTestHelper.openAnvilMenu(context);

        ItemStack left = new ItemStack(Items.NETHERITE_SWORD);
        AnvilGameTestHelper.enchant(context, left, Enchantments.SHARPNESS, 5);
        AnvilGameTestHelper.enchant(context, left, Enchantments.LOOTING, 3);
        AnvilGameTestHelper.enchant(context, left, Enchantments.UNBREAKING, 3);
        AnvilGameTestHelper.enchant(context, left, Enchantments.FIRE_ASPECT, 2);
        AnvilGameTestHelper.enchant(context, left, Enchantments.SWEEPING_EDGE, 3);
        AnvilGameTestHelper.enchant(context, left, Enchantments.KNOCKBACK, 2);

        ItemStack book = AnvilGameTestHelper.enchantedBook(context, Enchantments.EFFICIENCY, 5);
        AnvilGameTestHelper.populateAnvil(menu, left, book, null);

        context.succeedWhen(() -> {
            if (AnvilGameTestHelper.resultStack(menu).isEmpty()) {
                throw AnvilGameTestHelper.fail("Expected high-PL book combine result");
            }
            if (menu.getCost() <= 40) {
                throw AnvilGameTestHelper.fail(
                        "Expected cost above vanilla 40-level cap, got " + menu.getCost()
                );
            }
        });
    }
}
