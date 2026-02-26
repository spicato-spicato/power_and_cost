package gear_maintinence_and_modification;

import gear_maintinence_and_modification.power.PowerLevelCalculator;
import gear_maintinence_and_modification.power.PowerLevelConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Gear_maintinence_and_modification implements ModInitializer {
	public static final String MOD_ID = "gear_maintinence_and_modification";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final PowerLevelConfig POWER_LEVEL_CONFIG = new PowerLevelConfig();
	public static final PowerLevelCalculator POWER_LEVEL_CALCULATOR = new PowerLevelCalculator(POWER_LEVEL_CONFIG);

	@Override
	public void onInitialize() {
		LOGGER.info("Gear Maintenance and Modification initialized");
	}
}