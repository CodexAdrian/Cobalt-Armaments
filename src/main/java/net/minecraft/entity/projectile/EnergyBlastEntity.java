package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EnergyBlastEntity extends ProjectileEntity {

    public EnergyBlastEntity(EntityType<? extends EnergyBlastEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {

    }
}
