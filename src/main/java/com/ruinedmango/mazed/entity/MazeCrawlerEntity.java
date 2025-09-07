package com.ruinedmango.mazed.entity;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;

public class MazeCrawlerEntity extends Monster {
	private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(MazeCrawlerEntity.class,
			EntityDataSerializers.BYTE);

	public MazeCrawlerEntity(EntityType<? extends MazeCrawlerEntity> p_33786_, Level p_33787_) {
		super(p_33786_, p_33787_);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Armadillo.class, 6.0F, 1.0, 1.2,
				p_320185_ -> !((Armadillo) p_320185_).isScared()));
		this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
		this.goalSelector.addGoal(4, new MazeCrawlerEntity.MazeCrawlerAttackGoal(this));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new MazeCrawlerEntity.MazeCrawlerTargetGoal<>(this, Player.class));
		this.targetSelector.addGoal(3, new MazeCrawlerEntity.MazeCrawlerTargetGoal<>(this, IronGolem.class));
	}

	@Override
	protected PathNavigation createNavigation(Level level) {
		return new WallClimberNavigation(this, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder p_326135_) {
		super.defineSynchedData(p_326135_);
		p_326135_.define(DATA_FLAGS_ID, (byte) 0);
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide) {
			this.setClimbing(this.horizontalCollision);
		}
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 16.0).add(Attributes.MOVEMENT_SPEED, 0.3F);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.SPIDER_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return SoundEvents.SPIDER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.SPIDER_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
		this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
	}

	@Override
	public boolean onClimbable() {
		return this.isClimbing();
	}

	@Override
	public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
		if (!state.is(Blocks.COBWEB)) {
			super.makeStuckInBlock(state, motionMultiplier);
		}
	}

	@Override
	public boolean canBeAffected(MobEffectInstance potionEffect) {
		return potionEffect.is(MobEffects.POISON) ? false : CommonHooks.canMobEffectBeApplied(this, potionEffect, null);
	}

	public boolean isClimbing() {
		return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
	}

	/**
	 * Updates the WatchableObject (Byte) created in entityInit(), setting it to
	 * 0x01 if par1 is true or 0x00 if it is false.
	 */
	public void setClimbing(boolean climbing) {
		byte b0 = this.entityData.get(DATA_FLAGS_ID);
		if (climbing) {
			b0 = (byte) (b0 | 1);
		} else {
			b0 = (byte) (b0 & -2);
		}

		this.entityData.set(DATA_FLAGS_ID, b0);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_33790_, DifficultyInstance p_33791_,
			EntitySpawnReason p_362727_, @Nullable SpawnGroupData p_33793_) {
		p_33793_ = super.finalizeSpawn(p_33790_, p_33791_, p_362727_, p_33793_);

		RandomSource randomsource = p_33790_.getRandom();

		if (p_33793_ == null) {
			p_33793_ = new MazeCrawlerEntity.MazeCrawlerEffectsGroupData();
			if (p_33790_.getDifficulty() == Difficulty.HARD
					&& randomsource.nextFloat() < 0.1F * p_33791_.getSpecialMultiplier()) {
				((MazeCrawlerEntity.MazeCrawlerEffectsGroupData) p_33793_).setRandomEffect(randomsource);
			}
		}

		if (p_33793_ instanceof MazeCrawlerEntity.MazeCrawlerEffectsGroupData mazecrawler$efg) {
			Holder<MobEffect> holder = mazecrawler$efg.effect;
			if (holder != null) {
				this.addEffect(new MobEffectInstance(holder, -1));
			}
		}

		return p_33793_;
	}

	@Override
	public Vec3 getVehicleAttachmentPoint(Entity p_316696_) {
		return p_316696_.getBbWidth() <= this.getBbWidth() ? new Vec3(0.0, 0.3125 * this.getScale(), 0.0)
				: super.getVehicleAttachmentPoint(p_316696_);
	}

	static class MazeCrawlerAttackGoal extends MeleeAttackGoal {
		public MazeCrawlerAttackGoal(MazeCrawlerEntity mazeCrawlerEntity) {
			super(mazeCrawlerEntity, 1.0, true);
		}

		@Override
		public boolean canUse() {
			return super.canUse() && !this.mob.isVehicle();
		}

		@Override
		public boolean canContinueToUse() {
			float f = this.mob.getLightLevelDependentMagicValue();
			if (f >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
				this.mob.setTarget(null);
				return false;
			} else {
				return super.canContinueToUse();
			}
		}
	}

	public static class MazeCrawlerEffectsGroupData implements SpawnGroupData {
		@Nullable
		public Holder<MobEffect> effect;

		public void setRandomEffect(RandomSource random) {
			int i = random.nextInt(5);
			if (i <= 1) {
				this.effect = MobEffects.SPEED;
			} else if (i <= 2) {
				this.effect = MobEffects.STRENGTH;
			} else if (i <= 3) {
				this.effect = MobEffects.REGENERATION;
			} else if (i <= 4) {
				this.effect = MobEffects.INVISIBILITY;
			}
		}
	}

	static class MazeCrawlerTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
		public MazeCrawlerTargetGoal(MazeCrawlerEntity mazeCrawlerEntity, Class<T> entityTypeToTarget) {
			super(mazeCrawlerEntity, entityTypeToTarget, true);
		}

		@Override
		public boolean canUse() {
			return super.canUse();
		}
	}

}
