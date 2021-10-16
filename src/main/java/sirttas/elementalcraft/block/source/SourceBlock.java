package sirttas.elementalcraft.block.source;

import java.util.stream.Stream;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.source.ISourceInteractable;
import sirttas.elementalcraft.block.AbstractECEntityBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.material.ECMaterials;

public class SourceBlock extends AbstractECEntityBlock {

	private static final VoxelShape SHAPE = Block.box(4D, 0D, 4D, 12D, 8D, 12D);

	public static final String NAME = "source";

	public SourceBlock() {
		super(BlockBehaviour.Properties.of(ECMaterials.SOURCE).strength(-1.0F, 3600000.0F).lightLevel(s -> 7).noOcclusion().noDrops());
		this.registerDefaultState(this.stateDefinition.any().setValue(ElementType.STATE_PROPERTY, ElementType.NONE));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SourceBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createECTicker(level, type, SourceBlockEntity.TYPE, level.isClientSide ? SourceBlockEntity::clientTick : SourceBlockEntity::serverTick);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		container.add(ElementType.STATE_PROPERTY);
	}

	@Override
	@Deprecated
	public boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return showShape(state, context) ? SHAPE : Shapes.empty();
	}
	
	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	private boolean showShape(BlockState state, CollisionContext context) {
		if (context instanceof EntityCollisionContext entityContext) {
			return entityContext.getEntity()
					.filter(LivingEntity.class::isInstance)
					.map(LivingEntity.class::cast)
					.filter(e -> Stream.of(e.getMainHandItem(), e.getOffhandItem())
							.anyMatch(s -> s.getItem() instanceof ISourceInteractable sourceInteractable && sourceInteractable.canIteractWithSource(s, state)))
					.isPresent();
			}
		return false;
	}

	/**
	 * Called after the block is set in the Chunk data, but before the Tile
	 * Entity is set
	 */
	@Override
	@Deprecated
	public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (ElementType.getElementType(state) == ElementType.NONE) {
			worldIn.setBlockAndUpdate(pos, state.setValue(ElementType.STATE_PROPERTY, ElementType.random()));
		}
	}

	@Override
	@Deprecated
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		return super.canBeReplaced(state, context) && BlockEntityHelper.getBlockEntityAs(context.getLevel(), context.getClickedPos(), SourceBlockEntity.class).map(s -> !s.isStabalized()).orElse(true);
	}
	
	@Override
	@Deprecated
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}
}
