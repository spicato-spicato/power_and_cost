package power_and_cost;

import power_and_cost.power.PowerLevelCalculator;
import power_and_cost.power.PowerLevelConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PowerAndCost implements ModInitializer {
	public static final String MOD_ID = "power_and_cost";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final PowerLevelConfig POWER_LEVEL_CONFIG = new PowerLevelConfig();
	public static final PowerLevelCalculator POWER_LEVEL_CALCULATOR = new PowerLevelCalculator(POWER_LEVEL_CONFIG);

	@Override
	public void onInitialize() {
		LOGGER.info("Power and Cost initialized");
	}
}
