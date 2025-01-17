package sirttas.elementalcraft.block.shrine.ore;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.Tags;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.loot.LootHelper;

import java.util.Optional;
import java.util.stream.IntStream;

public class OreShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(OreShrineBlock.NAME);

	public OreShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.ORE_SHRINE, pos, state, PROPERTIES_KEY);
	}

	private Optional<BlockPos> findOre() {
		int range = getIntegerRange();

		return IntStream.range(-range, range + 1)
				.mapToObj(x -> IntStream.range(-range, range + 1).mapToObj(z -> IntStream.range(0, worldPosition.getY() + 1).mapToObj(y -> new BlockPos(worldPosition.getX() + x, y, worldPosition.getZ() + z))))
				.flatMap(s -> s.flatMap(s2 -> s2)).filter(p -> level.getBlockState(p).is(Tags.Blocks.ORES)).findAny();

	}

	@Override
	public AABB getRangeBoundingBox() {
		int range = getIntegerRange();

		return new AABB(this.getBlockPos()).inflate(range, 0, range).move(0, -1, 0).expandTowards(0, 1D - worldPosition.getY(), 0);
	}


	@Override
	protected boolean doPeriod() {
		if (level instanceof ServerLevel serverLevel) {
			return findOre().map(p -> {
				harvest(serverLevel, p, this, Blocks.STONE.defaultBlockState());
				return true;
			}).orElse(false);
		}
		return false;
	}

	public static void harvest(ServerLevel level, BlockPos pos, AbstractShrineBlockEntity shrine, BlockState newBlock) {
		int fortune = shrine.getUpgradeCount(ShrineUpgrades.FORTUNE);

		if (fortune > 0) {
			ItemStack pickaxe = new ItemStack(Items.NETHERITE_PICKAXE);

			pickaxe.enchant(Enchantments.BLOCK_FORTUNE, fortune);
			LootHelper.getDrops(level, pos, pickaxe).forEach(s -> Block.popResource(level, shrine.getBlockPos().above(), s));
		} else {
			LootHelper.getDrops(level, pos, shrine.hasUpgrade(ShrineUpgrades.SILK_TOUCH)).forEach(s -> Block.popResource(level, shrine.getBlockPos().above(), s));
		}
		if (newBlock != null) {
			level.setBlockAndUpdate(pos, newBlock);
		}
	}
}
