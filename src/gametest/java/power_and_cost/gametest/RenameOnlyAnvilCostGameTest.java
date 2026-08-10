package power_and_cost.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class RenameOnlyAnvilCostGameTest {

    @GameTest(maxTicks = 200)
    public void renameOnlyCostsOneLevel(GameTestHelper context) {
        AnvilMenu menu = AnvilGameTestHelper.openAnvilMenu(context);

        ItemStack left = new ItemStack(Items.IRON_SWORD);
        AnvilGameTestHelper.populateAnvil(menu, left, ItemStack.EMPTY, "Renamed Sword");

        context.succeedWhen(() -> {
            if (AnvilGameTestHelper.resultStack(menu).isEmpty()) {
                throw AnvilGameTestHelper.fail("Expected rename result");
            }
            if (menu.getCost() != 1) {
                throw AnvilGameTestHelper.fail("Expected rename cost of 1 level, got " + menu.getCost());
            }
        });
    }
}
