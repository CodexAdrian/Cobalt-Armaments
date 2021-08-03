package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EnergyBlastEntity extends ProjectileEntity {

    EnergyBlastEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {

    }

}
