package com.windokkstudio.militaryelements.entities.vehicles;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.windokkstudio.militaryelements.MilitaryElements;
import com.windokkstudio.militaryelements.init.EntitiesInit;
import com.windokkstudio.militaryelements.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;

import static net.minecraft.util.Mth.wrapDegrees;

public class JeepEntity extends Entity {

    public JeepEntity(EntityType<JeepEntity> type, Level world) {
        super(type, world);
    }

    public static final EntityDataAccessor<Integer> TIME_SINCE_HIT = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.INT);

    public Quaternion Q_Prev = new Quaternion(Quaternion.ONE);
    public Quaternion Q_Client = new Quaternion(Quaternion.ONE);

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public int getTimeSinceHit() {
        return entityData.get(TIME_SINCE_HIT);
    }

    public Quaternion getQ_Client() {
        return new Quaternion(Q_Client);
    }

    public Quaternion getQ_Prev() {
        return Q_Prev.copy();
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(TIME_SINCE_HIT, 0);

    }

    public void setTimeSinceHit(int timeSinceHit) {
        entityData.set(TIME_SINCE_HIT, timeSinceHit);
    }
}
