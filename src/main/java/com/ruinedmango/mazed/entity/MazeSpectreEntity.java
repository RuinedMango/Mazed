package com.ruinedmango.mazed.entity;

import java.util.EnumSet;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class MazeSpectreEntity extends Monster {
	public static final float FLAP_DEGREES_PER_TICK = 45.836624F;
	public static final int TICKS_PER_FLAP = Mth.ceil((float) (Math.PI * 5.0 / 4.0));
	protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(MazeSpectreEntity.class,
			EntityDataSerializers.BYTE);
	@Nullable
	private BlockPos boundOrigin;
	private boolean hasLimitedLife;
	private int limitedLifeTicks;

	public MazeSpectreEntity(EntityType<? extends MazeSpectreEntity> p_33984_, Level p_33985_) {
		super(p_33984_, p_33985_);
		this.moveControl = new MazeSpectreEntity.MazeSpectreMoveControl(this);
		this.xpReward = 3;
	}

	@Override
	public boolean isFlapping() {
		return this.tickCount % TICKS_PER_FLAP == 0;
	}

	@Override
	protected boolean isAffectedByBlocks() {
		return !this.isRemoved();
	}

	@Override
	public void tick() {
		this.noPhysics = true;
		super.tick();
		this.noPhysics = false;
		this.setNoGravity(true);
		if (this.hasLimitedLife && --this.limitedLifeTicks <= 0) {
			this.limitedLifeTicks = 20;
			this.hurt(this.damageSources().starve(), 1.0F);
		}
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(4, new MazeSpectreEntity.MazeSpectreChargeAttackGoal());
		this.goalSelector.addGoal(8, new MazeSpectreEntity.MazeSpectreRandomMoveGoal());
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
		this.targetSelector.addGoal(2, new MazeSpectreEntity.MazeSpectreCopyOwnerTargetGoal(this));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, false));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 14.0).add(Attributes.ATTACK_DAMAGE, 4.0);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder p_326059_) {
		super.defineSynchedData(p_326059_);
		p_326059_.define(DATA_FLAGS_ID, (byte) 0);
	}

	@Override
	protected void readAdditionalSaveData(ValueInput p_422040_) {
		super.readAdditionalSaveData(p_422040_);
		this.boundOrigin = p_422040_.read("bound_pos", BlockPos.CODEC).orElse(null);
		p_422040_.getInt("life_ticks").ifPresentOrElse(this::setLimitedLife, () -> this.hasLimitedLife = false);
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput p_422240_) {
		super.addAdditionalSaveData(p_422240_);
		p_422240_.storeNullable("bound_pos", BlockPos.CODEC, this.boundOrigin);
		if (this.hasLimitedLife) {
			p_422240_.putInt("life_ticks", this.limitedLifeTicks);
		}
	}

	@Nullable
	public BlockPos getBoundOrigin() {
		return this.boundOrigin;
	}

	public void setBoundOrigin(@Nullable BlockPos boundOrigin) {
		this.boundOrigin = boundOrigin;
	}

	private boolean getMazeSpectreFlag(int mask) {
		int i = this.entityData.get(DATA_FLAGS_ID);
		return (i & mask) != 0;
	}

	private void setMazeSpectreFlag(int mask, boolean value) {
		int i = this.entityData.get(DATA_FLAGS_ID);
		if (value) {
			i |= mask;
		} else {
			i &= ~mask;
		}

		this.entityData.set(DATA_FLAGS_ID, (byte) (i & 0xFF));
	}

	public boolean isCharging() {
		return this.getMazeSpectreFlag(1);
	}

	public void setIsCharging(boolean charging) {
		this.setMazeSpectreFlag(1, charging);
	}

	public void setLimitedLife(int limitedLifeTicks) {
		this.hasLimitedLife = true;
		this.limitedLifeTicks = limitedLifeTicks;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.VEX_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.VEX_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return SoundEvents.VEX_HURT;
	}

	@Override
	public float getLightLevelDependentMagicValue() {
		return 1.0F;
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34002_, DifficultyInstance p_34003_,
			EntitySpawnReason p_364407_, @Nullable SpawnGroupData p_34005_) {
		RandomSource randomsource = p_34002_.getRandom();
		this.populateDefaultEquipmentSlots(randomsource, p_34003_);
		this.populateDefaultEquipmentEnchantments(p_34002_, randomsource, p_34003_);
		return super.finalizeSpawn(p_34002_, p_34003_, p_364407_, p_34005_);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource p_219135_, DifficultyInstance p_219136_) {
		// this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
		// this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
	}

	class MazeSpectreChargeAttackGoal extends Goal {
		public MazeSpectreChargeAttackGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			LivingEntity livingentity = MazeSpectreEntity.this.getTarget();
			return livingentity != null && livingentity.isAlive()
					&& !MazeSpectreEntity.this.getMoveControl().hasWanted()
					&& MazeSpectreEntity.this.random.nextInt(reducedTickDelay(7)) == 0
							? MazeSpectreEntity.this.distanceToSqr(livingentity) > 4.0
							: false;
		}

		@Override
		public boolean canContinueToUse() {
			return MazeSpectreEntity.this.getMoveControl().hasWanted() && MazeSpectreEntity.this.isCharging()
					&& MazeSpectreEntity.this.getTarget() != null && MazeSpectreEntity.this.getTarget().isAlive();
		}

		@Override
		public void start() {
			LivingEntity livingentity = MazeSpectreEntity.this.getTarget();
			if (livingentity != null) {
				Vec3 vec3 = livingentity.getEyePosition();
				MazeSpectreEntity.this.moveControl.setWantedPosition(vec3.x, vec3.y - 1, vec3.z, 1.0);
			}

			MazeSpectreEntity.this.setIsCharging(true);
			MazeSpectreEntity.this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
		}

		@Override
		public void stop() {
			MazeSpectreEntity.this.setIsCharging(false);
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			LivingEntity livingentity = MazeSpectreEntity.this.getTarget();
			if (livingentity != null) {
				if (MazeSpectreEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
					MazeSpectreEntity.this.doHurtTarget(getServerLevel(MazeSpectreEntity.this.level()), livingentity);
					MazeSpectreEntity.this.setIsCharging(false);
				} else {
					double d0 = MazeSpectreEntity.this.distanceToSqr(livingentity);
					if (d0 < 9.0) {
						Vec3 vec3 = livingentity.getEyePosition();
						MazeSpectreEntity.this.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0);
					}
				}
			}
		}
	}

	class MazeSpectreCopyOwnerTargetGoal extends TargetGoal {
		private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight()
				.ignoreInvisibilityTesting();

		public MazeSpectreCopyOwnerTargetGoal(PathfinderMob mob) {
			super(mob, false);
		}

		@Override
		public boolean canUse() {
			return mob != null && mob.getTarget() != null && this.canAttack(mob.getTarget(), this.copyOwnerTargeting);
		}

		@Override
		public void start() {
			MazeSpectreEntity.this.setTarget(mob != null ? mob.getTarget() : null);
			super.start();
		}
	}

	class MazeSpectreMoveControl extends MoveControl {
		public MazeSpectreMoveControl(MazeSpectreEntity MazeSpectre) {
			super(MazeSpectre);
		}

		@Override
		public void tick() {
			if (this.operation == MoveControl.Operation.MOVE_TO) {
				Vec3 vec3 = new Vec3(this.wantedX - MazeSpectreEntity.this.getX(),
						this.wantedY - MazeSpectreEntity.this.getY(), this.wantedZ - MazeSpectreEntity.this.getZ());
				double d0 = vec3.length();
				if (d0 < MazeSpectreEntity.this.getBoundingBox().getSize()) {
					this.operation = MoveControl.Operation.WAIT;
					MazeSpectreEntity.this.setDeltaMovement(MazeSpectreEntity.this.getDeltaMovement().scale(0.5));
				} else {
					MazeSpectreEntity.this.setDeltaMovement(
							MazeSpectreEntity.this.getDeltaMovement().add(vec3.scale(this.speedModifier * 0.05 / d0)));
					if (MazeSpectreEntity.this.getTarget() == null) {
						Vec3 vec31 = MazeSpectreEntity.this.getDeltaMovement();
						MazeSpectreEntity.this
								.setYRot(-((float) Mth.atan2(vec31.x, vec31.z)) * (180.0F / (float) Math.PI));
						MazeSpectreEntity.this.yBodyRot = MazeSpectreEntity.this.getYRot();
					} else {
						double d2 = MazeSpectreEntity.this.getTarget().getX() - MazeSpectreEntity.this.getX();
						double d1 = MazeSpectreEntity.this.getTarget().getZ() - MazeSpectreEntity.this.getZ();
						MazeSpectreEntity.this.setYRot(-((float) Mth.atan2(d2, d1)) * (180.0F / (float) Math.PI));
						MazeSpectreEntity.this.yBodyRot = MazeSpectreEntity.this.getYRot();
					}
				}
			}
		}
	}

	class MazeSpectreRandomMoveGoal extends Goal {
		public MazeSpectreRandomMoveGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			return !MazeSpectreEntity.this.getMoveControl().hasWanted()
					&& MazeSpectreEntity.this.random.nextInt(reducedTickDelay(7)) == 0;
		}

		@Override
		public boolean canContinueToUse() {
			return false;
		}

		@Override
		public void tick() {
			BlockPos blockpos = MazeSpectreEntity.this.getBoundOrigin();
			if (blockpos == null) {
				blockpos = MazeSpectreEntity.this.blockPosition();
			}

			for (int i = 0; i < 3; i++) {
				BlockPos blockpos1 = blockpos.offset(MazeSpectreEntity.this.random.nextInt(15) - 7,
						MazeSpectreEntity.this.random.nextInt(11) - 5, MazeSpectreEntity.this.random.nextInt(15) - 7);
				if (MazeSpectreEntity.this.level().isEmptyBlock(blockpos1)) {
					MazeSpectreEntity.this.moveControl.setWantedPosition(blockpos1.getX() + 0.5, blockpos1.getY() + 0.5,
							blockpos1.getZ() + 0.5, 0.25);
					if (MazeSpectreEntity.this.getTarget() == null) {
						MazeSpectreEntity.this.getLookControl().setLookAt(blockpos1.getX() + 0.5,
								blockpos1.getY() + 0.5, blockpos1.getZ() + 0.5, 180.0F, 20.0F);
					}
					break;
				}
			}
		}
	}

}
