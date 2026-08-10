package power_and_cost.power;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import power_and_cost.MinecraftTestBootstrap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PowerLevelCalculatorMaterialTest {

    private static PowerLevelCalculator calculator;

    @BeforeAll
    static void bootstrapMinecraft() {
        MinecraftTestBootstrap.init();
        calculator = new PowerLevelCalculator(new PowerLevelConfig());
    }

    @Test
    void ironSword_hasMaterialPl40() {
        assertEquals(40, calculator.computeMaterialPowerValue(new ItemStack(Items.IRON_SWORD)));
    }

    @Test
    void diamondPickaxe_hasMaterialPl70() {
        assertEquals(70, calculator.computeMaterialPowerValue(new ItemStack(Items.DIAMOND_PICKAXE)));
    }

    @Test
    void unknownItem_hasMaterialPl0() {
        assertEquals(0, calculator.computeMaterialPowerValue(new ItemStack(Items.STICK)));
    }
}
