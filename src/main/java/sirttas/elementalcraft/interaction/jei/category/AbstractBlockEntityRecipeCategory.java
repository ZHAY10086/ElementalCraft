package sirttas.elementalcraft.interaction.jei.category;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.world.item.crafting.Recipe;
import sirttas.elementalcraft.inventory.IInventoryTile;
import sirttas.elementalcraft.inventory.InventoryTileWrapper;

public abstract class AbstractBlockEntityRecipeCategory<K extends IInventoryTile, T extends Recipe<InventoryTileWrapper<K>>> extends AbstractInventoryRecipeCategory<InventoryTileWrapper<K>, T> {

	protected AbstractBlockEntityRecipeCategory(String translationKey, IDrawable icon, IDrawable background) {
		super(translationKey, icon, background);
	}
}
