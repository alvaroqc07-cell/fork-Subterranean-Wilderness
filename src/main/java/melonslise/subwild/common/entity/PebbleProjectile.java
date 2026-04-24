package melonslise.subwild.common.entity;

import melonslise.subwild.common.init.SubWildBlocks;
import melonslise.subwild.common.init.SubWildEntities;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class PebbleProjectile extends ThrowableItemProjectile
{
	private static final float DAMAGE = 1.0f;

	public PebbleProjectile(EntityType<? extends PebbleProjectile> type, Level level)
	{
		super(type, level);
	}

	public PebbleProjectile(Level level, LivingEntity shooter)
	{
		super(SubWildEntities.PEBBLE_PROJECTILE.get(), shooter, level);
	}

	public PebbleProjectile(Level level, double x, double y, double z)
	{
		super(SubWildEntities.PEBBLE_PROJECTILE.get(), x, y, z, level);
	}

	@Override
	protected Item getDefaultItem()
	{
		return SubWildBlocks.PEBBLE.get().asItem();
	}

	@Override
	protected void onHitEntity(EntityHitResult result)
	{
		super.onHitEntity(result);

		if(!this.level().isClientSide)
		{
			result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), DAMAGE);
			this.spawnImpactParticles(this.position());
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult result)
	{
		super.onHitBlock(result);

		if(!this.level().isClientSide)
			this.spawnImpactParticles(result.getLocation());
	}

	@Override
	protected void onHit(HitResult result)
	{
		super.onHit(result);

		if(!this.level().isClientSide)
			this.discard();
	}

	private void spawnImpactParticles(Vec3 impactPos)
	{
		ItemStack stack = this.getItemRaw().isEmpty() ? this.getItem() : this.getItemRaw();
		Block particleBlock = stack.getItem() instanceof BlockItem blockItem ? blockItem.getBlock() : SubWildBlocks.PEBBLE.get();
		((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, particleBlock.defaultBlockState()),
			impactPos.x(), impactPos.y(), impactPos.z(), 8, 0.08d, 0.08d, 0.08d, 0.02d);
	}
}
