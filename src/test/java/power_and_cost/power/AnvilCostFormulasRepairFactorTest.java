package power_and_cost.power;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnvilCostFormulasRepairFactorTest {

    private AnvilCostFormulas formulas;

    @BeforeEach
    void setUp() {
        formulas = new AnvilCostFormulas(new PowerLevelConfig());
    }

    @Test
    void lowEnchantPl_nearMaxRepairFactor() {
        double factor = formulas.durabilityRepairFactor(0);
        assertTrue(factor > 0.9, "Unenchanted gear should repair near max effectiveness, got " + factor);
        assertTrue(factor <= 1.1, "Repair factor should not exceed configured max, got " + factor);
    }

    @Test
    void highEnchantPl_nearMinRepairFactor() {
        double factor = formulas.durabilityRepairFactor(250);
        assertTrue(factor <= 0.35, "Expected factor near min 0.33, got " + factor);
        assertTrue(factor >= 0.33, "Expected factor at least 0.33, got " + factor);
    }
}
