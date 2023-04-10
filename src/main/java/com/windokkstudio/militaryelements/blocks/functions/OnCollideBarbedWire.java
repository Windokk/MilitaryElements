package com.windokkstudio.militaryelements.blocks.functions;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class OnCollideBarbedWire {

    public static void execute(Entity entity) {
        if (entity == null)
            return;
        entity.hurt(DamageSource.GENERIC, 1);
        entity.makeStuckInBlock(Blocks.AIR.defaultBlockState(), new Vec3(0.25, 0.05, 0.25));
    }

}
