package power_and_cost.mixin;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(EnchantmentMenu.class)
public interface EnchantmentMenuAccess {

    @Invoker("getEnchantmentList")
    List<EnchantmentInstance> invokeGetEnchantmentList(
            RegistryAccess registryAccess,
            ItemStack stack,
            int slot,
            int enchantmentPower
    );
}
