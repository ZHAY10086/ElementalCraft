package sirttas.elementalcraft.interaction.mekanism;

import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.impl.CrushingIRecipe;
import mekanism.common.recipe.impl.EnrichingIRecipe;
import mekanism.common.recipe.impl.InjectingIRecipe;
import mekanism.common.recipe.impl.PurifyingIRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.api.pureore.injector.PureOreRecipeInjector;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.interaction.mekanism.injector.ChemicalDissolutionPureOreRecipeInjector;
import sirttas.elementalcraft.interaction.mekanism.injector.ItemStackGasToItemStackPureOreRecipeInjector;
import sirttas.elementalcraft.interaction.mekanism.injector.ItemStackToItemStackPureOreRecipeInjector;
import sirttas.elementalcraft.item.pureore.injector.PureOreRecipeInjectors;

public class MekanismInteraction {

	public static void registerPureOreRecipeInjectors(IForgeRegistry<PureOreRecipeInjector<?, ? extends IRecipe<?>>> registry) {
		if (Boolean.TRUE.equals(ECConfig.COMMON.mekanismPureOreDissolutionRecipe.get())) {
			PureOreRecipeInjectors.register(registry, new ChemicalDissolutionPureOreRecipeInjector(MekanismRecipeType.DISSOLUTION));
		}
		if (Boolean.TRUE.equals(ECConfig.COMMON.mekanismPureOreInjectingRecipe.get())) {
			PureOreRecipeInjectors.register(registry, new ItemStackGasToItemStackPureOreRecipeInjector<>(MekanismRecipeType.INJECTING, InjectingIRecipe::new));
		}
		if (Boolean.TRUE.equals(ECConfig.COMMON.mekanismPureOrePurifyingRecipe.get())) {
			PureOreRecipeInjectors.register(registry, new ItemStackGasToItemStackPureOreRecipeInjector<>(MekanismRecipeType.PURIFYING, PurifyingIRecipe::new));
		}
		if (Boolean.TRUE.equals(ECConfig.COMMON.mekanismPureOreEnrichingRecipe.get())) {
			PureOreRecipeInjectors.register(registry, new ItemStackToItemStackPureOreRecipeInjector<>(MekanismRecipeType.ENRICHING, EnrichingIRecipe::new));
		}
		if (Boolean.TRUE.equals(ECConfig.COMMON.mekanismPureOreCrushingRecipe.get())) {
			PureOreRecipeInjectors.register(registry, new ItemStackToItemStackPureOreRecipeInjector<>(MekanismRecipeType.CRUSHING, CrushingIRecipe::new));
		}
	}
}
