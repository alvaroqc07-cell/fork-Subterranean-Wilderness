package melonslise.subwild.common.world.gen.biome;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import melonslise.subwild.common.config.SubWildConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public record SubWildCaveDecoBiomeModifier(
	List<Rule> rules,
	List<Holder<PlacedFeature>> defaultFeatures,
	List<Rule> legacyRules,
	List<Holder<PlacedFeature>> legacyDefaultFeatures) implements BiomeModifier
{
	public record Rule(HolderSet<Biome> biomes, List<Holder<PlacedFeature>> features)
	{
		public static final Codec<Rule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes").forGetter(Rule::biomes),
			RegistryFileCodec.create(Registries.PLACED_FEATURE, PlacedFeature.DIRECT_CODEC).listOf().fieldOf("features").forGetter(Rule::features)
		).apply(instance, Rule::new));
	}

	public static final Codec<SubWildCaveDecoBiomeModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Rule.CODEC.listOf().fieldOf("rules").forGetter(SubWildCaveDecoBiomeModifier::rules),
		RegistryFileCodec.create(Registries.PLACED_FEATURE, PlacedFeature.DIRECT_CODEC).listOf().fieldOf("default_features").forGetter(SubWildCaveDecoBiomeModifier::defaultFeatures),
		Rule.CODEC.listOf().fieldOf("legacy_rules").forGetter(SubWildCaveDecoBiomeModifier::legacyRules),
		RegistryFileCodec.create(Registries.PLACED_FEATURE, PlacedFeature.DIRECT_CODEC).listOf().fieldOf("legacy_default_features").forGetter(SubWildCaveDecoBiomeModifier::legacyDefaultFeatures)
	).apply(instance, SubWildCaveDecoBiomeModifier::new));

	@Override
	public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder)
	{
		if(phase != Phase.ADD)
			return;
		if(!biome.is(BiomeTags.IS_OVERWORLD))
			return;

		boolean adaptive = SubWildConfig.ADAPT_CAVE_BIOMES_TO_MODDED_BIOMES.get();
		List<Rule> activeRules = adaptive ? this.rules : this.legacyRules;
		List<Holder<PlacedFeature>> activeDefaults = adaptive ? this.defaultFeatures : this.legacyDefaultFeatures;
		List<Holder<PlacedFeature>> selected = null;
		for(Rule rule : activeRules)
		{
			if(rule.biomes().contains(biome))
			{
				selected = rule.features();
				break;
			}
		}
		if(selected == null)
			selected = activeDefaults;

		for(Holder<PlacedFeature> feature : selected)
			builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, feature);
	}

	@Override
	public Codec<? extends BiomeModifier> codec()
	{
		return CODEC;
	}
}
