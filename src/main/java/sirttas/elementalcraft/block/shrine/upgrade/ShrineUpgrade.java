package sirttas.elementalcraft.block.shrine.upgrade;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.Block;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import sirttas.dpanvil.api.codec.CodecHelper;
import sirttas.dpanvil.api.predicate.block.BlockPosPredicates;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.dpanvil.api.predicate.block.logical.OrBlockPredicate;
import sirttas.elementalcraft.block.shrine.TileShrine;
import sirttas.elementalcraft.data.predicate.block.shrine.HasShrineUpgradePredicate;
import sirttas.elementalcraft.upgrade.AbstractUpgrade;

public class ShrineUpgrade extends AbstractUpgrade<ShrineUpgrade.BonusType> {


	public static final Codec<ShrineUpgrade> CODEC = RecordCodecBuilder.create(builder -> AbstractUpgrade.codec(builder, BonusType.CODEC).apply(builder, ShrineUpgrade::new));

	private ShrineUpgrade(IBlockPosPredicate predicate, Map<BonusType, Float> bonuses, int maxAmount) {
		super(predicate, new EnumMap<>(bonuses), maxAmount);
	}

	boolean canUpgrade(TileShrine shrine) {
		return canUpgrade(shrine.getWorld(), shrine.getPos(), shrine.getUpgradeCount(this));
	}

	public void addInformation(List<ITextComponent> tooltip) {
		bonuses.forEach((type, multiplier) -> tooltip.add(new TranslationTextComponent("shrine_upgrade_bonus.elementalcraft." + type.getString(), formatMultiplier(multiplier))
				.mergeStyle(type.isPositive() ^ multiplier < 1 ? TextFormatting.BLUE : TextFormatting.RED)));
		if (maxAmount > 0) {
			tooltip.add(new StringTextComponent(""));
			tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.max_amount", maxAmount).mergeStyle(TextFormatting.YELLOW));
		}
	}

	private String formatMultiplier(Float multiplier) {
		if (multiplier >= 10) {
			return new DecimalFormat("�#.##").format(multiplier);
		}
		return String.format("%+d%%", Math.round((multiplier - 1) * 100));
	}

	public enum BonusType implements IStringSerializable {
		NONE("none", false), 
		SPEED("speed", false), 
		ELEMENT_CONSUMPTION("element_consumption", false), 
		CAPACITY("capacity", true), 
		RANGE("range", true), 
		STRENGTH("strength", true);

		public static final Codec<BonusType> CODEC = IStringSerializable.createEnumCodec(BonusType::values, BonusType::byName);

		private final String name;
		private final boolean positive;

		private BonusType(String name, boolean positive) {
			this.name = name;
			this.positive = positive;
		}

		@Nonnull
		@Override
		public String getString() {
			return this.name;
		}

		public boolean isPositive() {
			return positive;
		}

		public static BonusType byName(String name) {
			for (BonusType bonusType : values()) {
				if (bonusType.name.equals(name)) {
					return bonusType;
				}
			}
			return NONE;
		}
	}

	public static class Builder {

		public static final Codec<Builder> CODEC = ShrineUpgrade.CODEC.xmap(shrineUpgrade -> {
			Builder builder = create().predicate(shrineUpgrade.predicate).max(shrineUpgrade.maxAmount);

			shrineUpgrade.bonuses.forEach(builder::addBonus);
			return builder;
		}, builder -> new ShrineUpgrade(builder.predicate, builder.bonuses, builder.maxAmount));

		private IBlockPosPredicate predicate;
		private final Map<BonusType, Float> bonuses;
		private int maxAmount;
		private Set<ResourceLocation> incompatibilities;

		private Builder() {
			this.bonuses = new EnumMap<>(BonusType.class);
			this.predicate = null;
			this.maxAmount = 0;
			this.incompatibilities = Sets.newHashSet();
		}

		public static Builder create() {
			return new Builder();
		}

		public Builder match(Block... block) {
			return predicate(BlockPosPredicates.match(block));
		}

		public Builder match(INamedTag<Block> tag) {
			return predicate(BlockPosPredicates.match(tag));
		}

		public Builder predicate(IBlockPosPredicate predicate) {
			this.predicate = predicate;
			return this;
		}

		public Builder max(int max) {
			maxAmount = max;
			return this;
		}

		public Builder incompatibleWith(ResourceLocation... locs) {
			Collections.addAll(incompatibilities, locs);
			return this;
		}

		public Builder addBonus(BonusType type, float value) {
			this.bonuses.put(type, value);
			return this;
		}

		public JsonElement toJson() {
			if (!incompatibilities.isEmpty()) {
				predicate = predicate.and(getIncompatibilitiesPredicate());
			}
			return CodecHelper.encode(CODEC, this);
		}

		private IBlockPosPredicate getIncompatibilitiesPredicate() {
			return (incompatibilities.size() == 1 ? new HasShrineUpgradePredicate(Iterables.getOnlyElement(incompatibilities))
					: new OrBlockPredicate(incompatibilities.stream().map(HasShrineUpgradePredicate::new).collect(Collectors.toList()))).not();
		}
	}
}
