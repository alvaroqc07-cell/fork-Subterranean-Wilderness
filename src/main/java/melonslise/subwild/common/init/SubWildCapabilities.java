package melonslise.subwild.common.init;

import melonslise.subwild.common.capability.CapabilityProvider;
import melonslise.subwild.common.capability.INoise;
import melonslise.subwild.common.capability.OpenSimplex2FNoise;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.AttachCapabilitiesEvent;

public final class SubWildCapabilities
{
	public static final Capability<INoise> NOISE_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

	public static void attachToWorld(AttachCapabilitiesEvent<Level> event)
	{
		if(event.getObject() instanceof ServerLevel serverLevel)
			event.addCapability(OpenSimplex2FNoise.ID, new CapabilityProvider<>(NOISE_CAPABILITY, new OpenSimplex2FNoise(serverLevel.getSeed())));
	}
}
