package power_and_cost.power;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import power_and_cost.MinecraftTestBootstrap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TableEnchantCostTest {

    private static final int SHARPNESS_PER_LEVEL = 14;
    private static final int UNBREAKING_PER_LEVEL = 8;
    private static final int SHARPNESS_5 = SHARPNESS_PER_LEVEL * 5;
    private static final int UNBREAKING_3 = UNBREAKING_PER_LEVEL * 3;
    private static final int WOOD_MATERIAL = 10;
    private static final int NETHERITE_MATERIAL = 100;

    private PowerLevelConfig config;
    private AnvilCostFormulas formulas;
    private TableEnchantCost tableCost;

    @BeforeAll
    static void bootstrapMinecraft() {
        MinecraftTestBootstrap.init();
    }

    @BeforeEach
    void setUp() {
        config = new PowerLevelConfig();
        formulas = new AnvilCostFormulas(config);
        tableCost = new TableEnchantCost(new PowerLevelCalculator(config), formulas);
    }

    @Test
    void slotCost_equalsAnvilSigmoidOnCombinedPl() {
        int inputPl = 40;
        int offerPl = SHARPNESS_5;
        assertEquals(
                formulas.enchantCostFromPL(inputPl + offerPl),
                tableCost.slotCostFromPL(inputPl, offerPl)
        );
    }

    @Test
    void sameOffer_netheriteCostsAtLeastAsMuchAsWood() {
        int woodCost = tableCost.slotCostFromPL(WOOD_MATERIAL, SHARPNESS_5);
        int netheriteCost = tableCost.slotCostFromPL(NETHERITE_MATERIAL, SHARPNESS_5);
        assertTrue(
                netheriteCost >= woodCost,
                "Netherite cost " + netheriteCost + " should be >= wood cost " + woodCost
        );
    }

    @Test
    void sameOffer_netheriteItemCostsAtLeastAsMuchAsWoodItem() {
        int woodCost = tableCost.slotCost(new ItemStack(Items.WOODEN_SWORD), SHARPNESS_5);
        int netheriteCost = tableCost.slotCost(new ItemStack(Items.NETHERITE_SWORD), SHARPNESS_5);
        assertTrue(
                netheriteCost >= woodCost,
                "Netherite item cost " + netheriteCost + " should be >= wood item cost " + woodCost
        );
        assertEquals(tableCost.slotCostFromPL(WOOD_MATERIAL, SHARPNESS_5), woodCost);
        assertEquals(tableCost.slotCostFromPL(NETHERITE_MATERIAL, SHARPNESS_5), netheriteCost);
    }

    @Test
    void book_usesMaterialPlZero() {
        int bookCost = tableCost.slotCost(new ItemStack(Items.BOOK), SHARPNESS_5);
        assertEquals(tableCost.slotCostFromPL(0, SHARPNESS_5), bookCost);
        assertEquals(formulas.enchantCostFromPL(SHARPNESS_5), bookCost);
    }

    @Test
    void multiEnchantOffer_sumsEnchantmentPower() {
        int offered = SHARPNESS_5 + UNBREAKING_3;
        assertEquals(70, config.getEnchantmentPower("minecraft:sharpness", 5));
        assertEquals(24, config.getEnchantmentPower("minecraft:unbreaking", 3));
        assertEquals(offered, 70 + 24);
        int cost = tableCost.slotCostFromPL(WOOD_MATERIAL, offered);
        assertEquals(formulas.enchantCostFromPL(WOOD_MATERIAL + offered), cost);
    }

    @Test
    void costsStayWithinConfiguredMinMax() {
        int min = config.getEnchantCostMin();
        int max = config.getEnchantCostMax();
        int low = tableCost.slotCostFromPL(0, 0);
        int high = tableCost.slotCostFromPL(10_000, 10_000);
        assertTrue(low >= min, "Low cost " + low + " below min " + min);
        assertTrue(low <= max, "Low cost " + low + " above max " + max);
        assertTrue(high >= min, "High cost " + high + " below min " + min);
        assertTrue(high <= max, "High cost " + high + " above max " + max);
    }
}
