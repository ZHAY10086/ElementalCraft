package sirttas.elementalcraft.block.entity.renderer;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.container.ContainerRenderer;
import sirttas.elementalcraft.block.diffuser.DiffuserRenderer;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.binder.BinderRenderer;
import sirttas.elementalcraft.block.instrument.crystallizer.CrystallizerRenderer;
import sirttas.elementalcraft.block.instrument.inscriber.InscriberRenderer;
import sirttas.elementalcraft.block.instrument.io.firefurnace.FireFurnaceRenderer;
import sirttas.elementalcraft.block.instrument.io.mill.AirMillRenderer;
import sirttas.elementalcraft.block.instrument.io.purifier.PurifierRenderer;
import sirttas.elementalcraft.block.pipe.ElementPipeRenderer;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserRenderer;
import sirttas.elementalcraft.block.shrine.ShrineRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration.AccelerationShrineUpgradeRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.vortex.VortexShrineUpgradeRenderer;
import sirttas.elementalcraft.block.sorter.SorterRenderer;
import sirttas.elementalcraft.block.source.SourceRenderer;
import sirttas.elementalcraft.block.synthesizer.mana.ManaSynthesizerRenderer;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerRenderer;
import sirttas.elementalcraft.block.source.displacement.plate.SourceDisplacementPlateBlockEntity;
import sirttas.elementalcraft.block.source.displacement.plate.SourceDisplacementPlateRenderer;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ECRenderers {

	private ECRenderers() {}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent evt) {
		register(ECBlockEntityTypes.PIPE, ElementPipeRenderer::new);
		register(ECBlockEntityTypes.INFUSER, () -> new SingleItemRenderer<>(new Vec3(0.5, 0.2, 0.5)));
		register(ECBlockEntityTypes.EXTRACTOR, RuneRenderer::create);
		register(ECBlockEntityTypes.EVAPORATOR, () -> new SingleItemRenderer<>(new Vec3(0.5, 0.2, 0.5), 0.5F));
		register(ECBlockEntityTypes.SOLAR_SYNTHESIZER, SolarSynthesizerRenderer::new);
		register(ECBlockEntityTypes.MANA_SYNTHESIZER, ManaSynthesizerRenderer::new);
		register(ECBlockEntityTypes.DIFFUSER, DiffuserRenderer::new);
		register(ECBlockEntityTypes.BINDER, BinderRenderer::new);
		register(ECBlockEntityTypes.BINDER_IMPROVED, BinderRenderer::new);
		register(ECBlockEntityTypes.CRYSTALLIZER, CrystallizerRenderer::new);
		register(ECBlockEntityTypes.INSCRIBER, InscriberRenderer::new);
		register(ECBlockEntityTypes.AIR_MILL, AirMillRenderer::new);
		register(ECBlockEntityTypes.PEDESTAL, () -> new SingleItemRenderer<>(new Vec3(0.5, 0.9, 0.5)));
		register(ECBlockEntityTypes.PURE_INFUSER, PureInfuserRenderer::new);
		register(ECBlockEntityTypes.FIRE_FURNACE, FireFurnaceRenderer::new);
		register(ECBlockEntityTypes.FIRE_BLAST_FURNACE, FireFurnaceRenderer::new);
		register(ECBlockEntityTypes.PURIFIER, PurifierRenderer::new);
		register(ECBlockEntityTypes.ACCELERATION_SHRINE_UPGRADE, AccelerationShrineUpgradeRenderer::new);
		register(ECBlockEntityTypes.VORTEX_SHRINE_UPGRADE, VortexShrineUpgradeRenderer::new);
		register(ECBlockEntityTypes.SORTER, SorterRenderer::new);
		register(ECBlockEntityTypes.SOURCE, SourceRenderer::new);

		register(ECBlockEntityTypes.FIRE_PYLON, ShrineRenderer::new);
		register(ECBlockEntityTypes.GROVE_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.BUDDING_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.BREEDING_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.SPAWNING_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.LAVA_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.ORE_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.OVERLOAD_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.SWEET_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.GROWTH_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.HARVEST_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.ENDER_LOCK_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.SPRING_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.VACUUM_SHRINE, ShrineRenderer::new);

		register(ECBlockEntityTypes.CONTAINER, ContainerRenderer::new);
		register(ECBlockEntityTypes.CREATIVE_CONTAINER, ContainerRenderer::new);
		register(ECBlockEntityTypes.RESERVOIR, ContainerRenderer::new);

		register(ECBlockEntityTypes.SOURCE_DISPLACEMENT_PLATE, SourceDisplacementPlateRenderer::new);
	}

	public static void initRenderLayouts() {
		setRenderLayer(ECBlocks.SMALL_CONTAINER, RenderType.cutout());
		setRenderLayer(ECBlocks.CONTAINER, RenderType.cutout());
		setRenderLayer(ECBlocks.FIRE_RESERVOIR, RenderType.cutout());
		setRenderLayer(ECBlocks.EARTH_RESERVOIR, RenderType.cutout());
		setRenderLayer(ECBlocks.WATER_RESERVOIR, RenderType.cutout());
		setRenderLayer(ECBlocks.AIR_RESERVOIR, RenderType.cutout());
		setRenderLayer(ECBlocks.CREATIVE_CONTAINER, RenderType.cutout());
		setRenderLayer(ECBlocks.EVAPORATOR, RenderType.cutout());
		setRenderLayer(ECBlocks.SMALL_SPRINGALINE_BUD, RenderType.cutout());
		setRenderLayer(ECBlocks.MEDIUM_SPRINGALINE_BUD, RenderType.cutout());
		setRenderLayer(ECBlocks.LARGE_SPRINGALINE_BUD, RenderType.cutout());
		setRenderLayer(ECBlocks.SPRINGALINE_CLUSTER, RenderType.cutout());
		setRenderLayer(ECBlocks.SPAWNING_SHRINE, RenderType.cutout());
		setRenderLayer(ECBlocks.FIRE_BLAST_FURNACE, RenderType.translucent());
		setRenderLayer(ECBlocks.BURNT_GLASS, RenderType.translucent());
		setRenderLayer(ECBlocks.BURNT_GLASS_PANE, RenderType.translucent());
		setRenderLayer(ECBlocks.SPRINGALINE_GLASS, RenderType.translucent());
		setRenderLayer(ECBlocks.SPRINGALINE_GLASS_PANE, RenderType.translucent());
		setRenderLayer(ECBlocks.SOURCE, RenderType.translucent());
		setRenderLayer(ECBlocks.CAPACITY_SHRINE_UPGRADE, RenderType.translucent());
		setRenderLayer(ECBlocks.OPTIMIZATION_SHRINE_UPGRADE, RenderType.translucent());
	}

	public static <T extends BlockEntity> void register(RegistryObject<BlockEntityType<T>> type, Supplier<BlockEntityRenderer<? super T>> renderProvider) {
		register(type.get(), renderProvider);
	}

	public static <T extends BlockEntity> void register(BlockEntityType<T> type, Supplier<BlockEntityRenderer<? super T>> renderProvider) {
		BlockEntityRenderers.register(type, d -> renderProvider.get());
	}

	public static void setRenderLayer(RegistryObject<? extends Block> block, RenderType type) {
		setRenderLayer(block.get(), type);
	}

	public static void setRenderLayer(Block block, RenderType type) {
		ItemBlockRenderTypes.setRenderLayer(block, type);
	}
}
