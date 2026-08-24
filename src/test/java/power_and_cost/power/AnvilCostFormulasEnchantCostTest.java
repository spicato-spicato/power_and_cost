package power_and_cost.power;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnvilCostFormulasEnchantCostTest {
    private AnvilCostFormulas formulas;
    private PowerLevelConfig config;

    @BeforeEach
    void setUp() {
        config = new PowerLevelConfig();
        formulas = new AnvilCostFormulas(config);
    }

    @Test
    void plZero_returnsMinimumCost() {
        assertEquals(1, formulas.enchantCostFromPL(0));
        assertEquals(config.getEnchantCostMin(), formulas.enchantCostFromPL(0));
    }

    @Test
    void costStaysAtFloorUntilAboutPl40() {
        assertEquals(1, formulas.enchantCostFromPL(39));
        assertTrue(formulas.enchantCostFromPL(41) >= 2, "Expected cost to rise near PL 40");
    }

    @Test
    void veryHighPl_approachesMaximumCost() {
        int cost = formulas.enchantCostFromPL(1000);
        assertTrue(cost >= 49, "Expected cost near max 50, got " + cost);
        assertTrue(cost <= 50, "Expected cost at most 50, got " + cost);
    }

    @Test
    void midpointPl_returnsHalfwayBetweenMinAndMax() {
        int x0 = config.getEnchantCostX0();
        int expected = (config.getEnchantCostMin() + config.getEnchantCostMax()) / 2;
        assertEquals(expected, formulas.enchantCostFromPL(x0));
    }
}
