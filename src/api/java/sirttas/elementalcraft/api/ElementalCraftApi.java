package sirttas.elementalcraft.api;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.api.infusion.tool.effect.ToolInfusionEffectType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.SourceTraitValueProviderType;

public class ElementalCraftApi {

	public static final String MODID = "elementalcraft";
	public static final Logger LOGGER = LogManager.getLogger(ElementalCraftApi.MODID);

	public static final ResourceKey<Registry<SourceTraitValueProviderType<?>>> SOURCE_TRAIT_VALUE_PROVIDER_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MODID, ECNames.SOURCE_TRAIT_VALUE_PROVIDER_TYPE));
	public static final ResourceKey<Registry<ToolInfusionEffectType<?>>> TOOL_INFUSION_EFFECT_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MODID, ECNames.TOOL_INFUSION_TYPE));

	public static final ResourceKey<IDataManager<Rune>> RUNE_MANAGER_KEY = IDataManager.createManagerKey(new ResourceLocation(MODID, Rune.NAME));
	public static final IDataManager<Rune> RUNE_MANAGER = IDataManager.builder(Rune.class, Rune.FOLDER)
			.withIdSetter(Rune::setId)
			.merged(Rune::merge)
			.build();
	public static final ResourceKey<IDataManager<ToolInfusion>> TOOL_INFUSION_MANAGER_KEY = IDataManager.createManagerKey(new ResourceLocation(MODID, ToolInfusion.NAME));
	public static final IDataManager<ToolInfusion> TOOL_INFUSION_MANAGER = IDataManager.builder(ToolInfusion.class, ToolInfusion.FOLDER)
			.withDefault(ToolInfusion.NONE)
			.withIdSetter(ToolInfusion::setId)
			.build();
	public static final ResourceKey<IDataManager<SourceTrait>> SOURCE_TRAIT_MANAGER_KEY = IDataManager.createManagerKey(new ResourceLocation(MODID, SourceTrait.NAME));
	public static final IDataManager<SourceTrait> SOURCE_TRAIT_MANAGER = IDataManager.builder(SourceTrait.class, SourceTrait.FOLDER)
			.withIdSetter(SourceTrait::setId)
			.build();

	private ElementalCraftApi() {}
}
