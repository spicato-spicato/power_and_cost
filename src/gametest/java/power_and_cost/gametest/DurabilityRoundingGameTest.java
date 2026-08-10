package power_and_cost.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DurabilityRoundingGameTest {

    @GameTest(maxTicks = 200)
    public void repairWithinFivePercentRoundsToFull(GameTestHelper context) {
        AnvilMenu menu = AnvilGameTestHelper.openAnvilMenu(context);

        ItemStack left = new ItemStack(Items.DIAMOND_SWORD);
        left.setDamageValue(100);
        AnvilGameTestHelper.enchant(context, left, Enchantments.SHARPNESS, 5);

        ItemStack right = new ItemStack(Items.DIAMOND_SWORD);
        right.setDamageValue(95);

        AnvilGameTestHelper.populateAnvil(menu, left, right, null);

        context.succeedWhen(() -> {
            ItemStack result = AnvilGameTestHelper.resultStack(menu);
            if (result.isEmpty()) {
                throw AnvilGameTestHelper.fail("Expected same-item repair result");
            }
            if (result.getDamageValue() != 0) {
                throw AnvilGameTestHelper.fail(
                        "Expected full repair (damage 0) due to 95% rounding, got damage "
                                + result.getDamageValue()
                );
            }
        });
    }
}
