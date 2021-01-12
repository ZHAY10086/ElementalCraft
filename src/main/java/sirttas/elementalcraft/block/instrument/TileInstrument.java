package sirttas.elementalcraft.block.instrument;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.tile.ICraftingTile;
import sirttas.elementalcraft.block.tile.TileECCrafting;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;
import sirttas.elementalcraft.rune.handler.CapabilityRuneHandler;
import sirttas.elementalcraft.rune.handler.RuneHandler;

public abstract class TileInstrument<T extends ICraftingTile, R extends IInstrumentRecipe<T>> extends TileECCrafting<T, R> implements IElementTypeProvider {

	private int progress = 0;
	private final RuneHandler runeHandler;

	public TileInstrument(TileEntityType<?> tileEntityTypeIn, IRecipeType<R> recipeType, int transferSpeed, int maxRunes) {
		super(tileEntityTypeIn, recipeType, transferSpeed);
		runeHandler = maxRunes > 0 ? new RuneHandler(maxRunes) : null;
	}

	@Override
	public void tick() {
		super.tick();
		if (progressOnTick()) {
			makeProgress();
		}
	}

	protected boolean progressOnTick() {
		return true;
	}

	protected boolean makeProgress() {
		IElementStorage tank = getTank();

		if (recipe != null && progress >= recipe.getElementAmount()) {
			process();
			progress = 0;
			return true;
		} else if (this.isRecipeAvailable() && tank != null) {
			int oldProgress = progress;
			float preservation = runeHandler.getElementPreservation();

			progress += tank.extractElement(Math.round(runeHandler.getTransferSpeed(this.transferSpeed) / preservation), recipe.getElementType(), false) * preservation;
			if (progress / this.transferSpeed > oldProgress / this.transferSpeed) {
				onProgress();
			}
			return true;
		} else if (recipe == null) {
			progress = 0;
		}
		return false;
	}

	protected void onProgress() {
		if (world.isRemote) {
			ParticleHelper.createElementFlowParticle(getElementType(), world, Vector3d.copyCentered(pos), Direction.UP, 1, world.rand);
		}
	}

	@Override
	public ElementType getElementType() {
		ElementType tankType = this.getTankElementType();
		
		return tankType != ElementType.NONE || recipe == null ? tankType : recipe.getElementType();
	}

	@Override
	public boolean isRunning() {
		return progress > 0;
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt(ECNames.PROGRESS, progress);
		compound.put(ECNames.RUNE_HANDLER, CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY.writeNBT(runeHandler, null));
		return compound;
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		progress = compound.getInt(ECNames.PROGRESS);
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY.readNBT(runeHandler, null, compound.get(ECNames.RUNE_HANDLER));
		}
	}

	@Override
	public void clear() {
		super.clear();
		progress = 0;
	}

	@Override
	public int getProgress() {
		return progress;
	}

	public float getProgressRatio() {
		return progress / recipe.getElementAmount();
	}

	public RuneHandler getRuneHandler() {
		return runeHandler;
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(Capability<U> cap, @Nullable Direction side) {
		if (!this.removed && cap == CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY) {
			return LazyOptional.of(runeHandler != null ? () -> runeHandler : null).cast();
		}
		return super.getCapability(cap, side);
	}
}
