package power_and_cost.power;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnvilCostFormulasEnchantCostTest {

    private AnvilCostFormulas formulas;

    @BeforeEach
    void setUp() {
        formulas = new AnvilCostFormulas(new PowerLevelConfig());
    }

    @Test
    void plZero_returnsMinimumCost() {
        assertEquals(4, formulas.enchantCostFromPL(0));
    }

    @Test
    void veryHighPl_approachesMaximumCost() {
        int cost = formulas.enchantCostFromPL(1000);
        assertTrue(cost >= 49, "Expected cost near max 50, got " + cost);
        assertTrue(cost <= 50, "Expected cost at most 50, got " + cost);
    }

    @Test
    void midpointPl_returnsHalfwayBetweenMinAndMax() {
        assertEquals(27, formulas.enchantCostFromPL(255));
    }
}
