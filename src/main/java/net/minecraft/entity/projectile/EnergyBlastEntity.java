package net.minecraft.entity.projectile;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static me.codexadrian.silverarmaments.SilverArmaments.ENERGY_BLAST_ENTITY;

public class EnergyBlastEntity extends ProjectileEntity {
    public static final TrackedData<BlockPos> ORIGIN = DataTracker.registerData(EnergyBlastEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);

    public EnergyBlastEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        setORIGIN(NbtHelper.toBlockPos(nbt.getCompound("origin")));
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.put("origin", NbtHelper.fromBlockPos(getORIGIN()));
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    protected void initDataTracker() {
        dataTracker.startTracking(ORIGIN, BlockPos.ORIGIN);
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        BlockPos pos = new BlockPos(hitResult.getPos());
        if(this.getOwner() instanceof PlayerEntity) {
            if ((hitResult.getType() != HitResult.Type.ENTITY || !this.isOwner(((EntityHitResult) hitResult).getEntity())) && this.getOwner().canModifyAt(this.world, pos)) {
                if (!this.world.isClient) {
                    createElectricCloud(pos);
                }
            }
        }
    }


    protected void createElectricCloud(BlockPos pos) {
        AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(this.world, this.getX(), this.getY(), this.getZ());
        areaEffectCloudEntity.setRadius(3.0F);
        areaEffectCloudEntity.setRadiusOnUse(-0.5F);
        areaEffectCloudEntity.setWaitTime(10);
        areaEffectCloudEntity.setRadiusGrowth(-areaEffectCloudEntity.getRadius() / (float) areaEffectCloudEntity.getDuration());
        areaEffectCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1));
        areaEffectCloudEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());
        this.world.spawnEntity(areaEffectCloudEntity);
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (this.world.isClient || (entity == null || !entity.isRemoved()) && this.world.isChunkLoaded(this.getBlockPos())) {
            super.tick();

            HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
            if (hitResult.getType() != HitResult.Type.MISS) {
                this.onCollision(hitResult);
            }

            this.checkBlockCollision();
            Vec3d vec3d = this.getVelocity();
            double d = this.getX() + vec3d.x;
            double e = this.getY() + vec3d.y;
            double f = this.getZ() + vec3d.z;
            //ProjectileUtil.method_7484(this, 0.2F);
            float g = .9F;
            if (this.isTouchingWater()) {
                for(int i = 0; i < 4; ++i) {
                    float h = 0.25F;
                    this.world.addParticle(ParticleTypes.BUBBLE, d - vec3d.x * 0.25D, e - vec3d.y * 0.25D, f - vec3d.z * 0.25D, vec3d.x, vec3d.y, vec3d.z);
                }

                g = 0.8F;
            }
            this.setVelocity(vec3d.multiply(g));
            this.world.addParticle(ParticleTypes.ELECTRIC_SPARK, d, e + 0.5D, f, 0.0D, 0.0D, 0.0D);
            this.setPosition(d, e, f);
        } else {
            this.discard();
        }
    }

    public BlockPos getORIGIN() {
        return dataTracker.get(ORIGIN);
    }

    public void setORIGIN(BlockPos origin) {
        dataTracker.set(ORIGIN, origin);
    }
}
