package net.minecraft.entity.projectile;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnergyBlastEntity extends ProjectileEntity {

    public EnergyBlastEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }


    @Override
    protected void initDataTracker() {

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


}
