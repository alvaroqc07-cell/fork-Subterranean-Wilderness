package melonslise.subwild.common.world.gen.biome;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

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

public record SubWildCaveDecoBiomeModifier(List<Rule> rules, Holder<PlacedFeature> defaultFeature) implements BiomeModifier
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
		RegistryFileCodec.create(Registries.PLACED_FEATURE, PlacedFeature.DIRECT_CODEC).fieldOf("default_feature").forGetter(SubWildCaveDecoBiomeModifier::defaultFeature)
	).apply(instance, SubWildCaveDecoBiomeModifier::new));

	@Override
	public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder)
	{
		if(phase != Phase.ADD)
			return;
		if(!biome.is(BiomeTags.IS_OVERWORLD))
			return;

		List<Holder<PlacedFeature>> selected = null;
		for(Rule rule : this.rules)
		{
			if(rule.biomes().contains(biome))
			{
				selected = rule.features();
				break;
			}
		}
		if(selected != null)
		{
			for(Holder<PlacedFeature> feature : selected)
				builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, feature);
			return;
		}

		builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, this.defaultFeature);
	}

	@Override
	public Codec<? extends BiomeModifier> codec()
	{
		return CODEC;
	}
}
