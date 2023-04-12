package com.windokkstudio.militaryelements.entities.vehicles;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.windokkstudio.militaryelements.MilitaryElements;
import com.windokkstudio.militaryelements.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;

import static net.minecraft.util.Mth.wrapDegrees;

public class JeepEntity extends Entity implements IEntityAdditionalSpawnData {

    public static final EntityDataAccessor<Integer> MAX_HEALTH = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> HEALTH = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> MAX_SPEED = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<String> MATERIAL = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Integer> TIME_SINCE_HIT = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> DAMAGE_TAKEN = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> THROTTLE = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Byte> PITCH_UP = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.BYTE);
    public static final EntityDataAccessor<Byte> YAW_RIGHT = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.BYTE);
    public Quaternion Q_Client = new Quaternion(Quaternion.ONE);
    private Block planksMaterial;
    private int damageTimeout;

    public JeepEntity(EntityType<? extends JeepEntity> entityTypeIn, Level worldIn) {
        this(entityTypeIn, worldIn, Blocks.OAK_PLANKS);
    }

    public JeepEntity(EntityType<? extends JeepEntity> entityTypeIn, Level worldIn, Block material) {
        super(entityTypeIn, worldIn);
        maxUpStep = 0.9999f;
        setMaterial(material);
        setMaxSpeed(1f);
    }

    public JeepEntity(EntityType<? extends JeepEntity> entityTypeIn, Level worldIn, Block material, double x, double y, double z) {
        this(entityTypeIn, worldIn, material);
        setPos(x, y, z);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void defineSynchedData() {
        entityData.define(MAX_HEALTH, 10);
        entityData.define(HEALTH, 10);
        entityData.define(MAX_SPEED, 0.25f);
        entityData.define(MATERIAL, ForgeRegistries.BLOCKS.getKey(Blocks.OAK_PLANKS).toString());
        entityData.define(TIME_SINCE_HIT, 0);
        entityData.define(DAMAGE_TAKEN, 0f);
        entityData.define(THROTTLE, 0);
        entityData.define(PITCH_UP, (byte) 0);
        entityData.define(YAW_RIGHT, (byte) 0);
    }

    public void setMaxSpeed(float maxSpeed) {
        entityData.set(MAX_SPEED, maxSpeed);
    }

    public Quaternion getQ_Client() {
        return new Quaternion(Q_Client);
    }

    public void setHealth(int health) {
        entityData.set(HEALTH, Math.max(health, 0));
    }

    public int getHealth() {
        return entityData.get(HEALTH);
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return getItemStack();
    }

    public void setMaterial(String material) {
        entityData.set(MATERIAL, material);
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(material));
        planksMaterial = block == null ? Blocks.OAK_PLANKS : block;
    }

    @SuppressWarnings("ConstantConditions")
    public void setMaterial(Block material) {
        entityData.set(MATERIAL, ForgeRegistries.BLOCKS.getKey(material).toString());
        planksMaterial = material;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (player.isShiftKeyDown() && itemStack.isEmpty()) {
            boolean hasPlayer = false;
            for (Entity passenger : getPassengers()) {
                if ((passenger instanceof Player)) {
                    hasPlayer = true;
                    break;
                }
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity entity = source.getDirectEntity();
        if (entity == getControllingPassenger() && entity instanceof Player player) {
            return false;
        }

        setTimeSinceHit(20);
        setDamageTaken(getDamageTaken() + 10 * amount);

        if (isInvulnerableTo(source) || damageTimeout > 0) {
            return false;
        }
        if (level.isClientSide || isRemoved()) {
            return false;
        }
        int health = getHealth();
        if (health < 0) {
            return false;
        }

        setHealth((int) (health - amount));
        damageTimeout = 10;
        boolean isPlayer = source.getDirectEntity() instanceof Player;
        boolean creativePlayer = isPlayer && ((Player) source.getEntity()).getAbilities().instabuild;
        if (creativePlayer) {
            kill();
        }
        return true;
    }

    public boolean isPickable() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
    }


    @Nullable
    public Entity getControllingPassenger() {
        List<Entity> list = getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("max_speed")) {
            entityData.set(MAX_SPEED, compound.getFloat("max_speed"));
        }

        if (compound.contains("max_health")) {
            int maxHealth = compound.getInt("max_health");
            if (maxHealth <= 0) {
                maxHealth = 20;
            }
            entityData.set(MAX_HEALTH, maxHealth);
        }

        if (compound.contains("health")) {
            int health = compound.getInt("health");
            entityData.set(HEALTH, health);
        }

        if (compound.contains("material")) {
            setMaterial(compound.getString("material"));
        }


    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("health", entityData.get(HEALTH));
        compound.putInt("max_health", entityData.get(MAX_HEALTH));
        compound.putFloat("max_speed", entityData.get(MAX_SPEED));
        compound.putString("material", entityData.get(MATERIAL));
    }

    @Override
    protected boolean canRide(Entity entityIn) {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (MATERIAL.equals(key) && level.isClientSide()) {
            Block block = ForgeRegistries.BLOCKS.getValue((new ResourceLocation(entityData.get(MATERIAL))));
            planksMaterial = block == null ? Blocks.OAK_PLANKS : block;
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.5;
    }

    public static final TagKey<Block> FIREPROOF_MATERIALS_TAG = BlockTags.create(new ResourceLocation(MilitaryElements.MODID, "fireproof_materials"));

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (source.isExplosion()) {
            return false;
        }
        if (source.isFire() && planksMaterial.builtInRegistryHolder().is(FIREPROOF_MATERIALS_TAG)) {
            return true;
        }
        if (source.getDirectEntity() != null && source.getDirectEntity().isPassengerOfSameVehicle(this)) {
            return true;
        }
        return super.isInvulnerableTo(source);
    }

    @Override
    public boolean fireImmune() {
        return planksMaterial.builtInRegistryHolder().is(FIREPROOF_MATERIALS_TAG);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        if ((onGroundIn || isOnWater())) {
            fallDistance = 0.0F;
        }
    }

    public boolean isOnWater() {
        return level.getBlockState(new BlockPos(position().add(0, 0.4, 0))).getFluidState().is(FluidTags.WATER);
    }

    //on dismount
    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
        return super.getDismountLocationForPassenger(livingEntity);
    }

    public ItemStack getItemStack() {
        ItemStack itemStack = getItem().getDefaultInstance();
        CompoundTag compound = new CompoundTag();
        addAdditionalSaveData(compound);
        compound.putInt("health", entityData.get(MAX_HEALTH));
        compound.putBoolean("Used", true);
        itemStack.addTagElement("EntityTag", compound);
        return itemStack;
    }

    protected Item getItem() {
        return ItemInit.JEEP.get();
    }


    @Override
    public void absMoveTo(double x, double y, double z, float yaw, float pitch) {
        double d0 = Mth.clamp(x, -3.0E7D, 3.0E7D);
        double d1 = Mth.clamp(z, -3.0E7D, 3.0E7D);
        xOld = d0;
        yOld = y;
        zOld = d1;
        setPos(d0, y, d1);
        setYRot(yaw % 360.0F);
        setXRot(pitch % 360.0F);

        yRotO = getYRot();
        xRotO = getXRot();
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
    }


    /**
     * Sets the time to count down from since the last time entity was hit.
     */
    public void setTimeSinceHit(int timeSinceHit) {
        entityData.set(TIME_SINCE_HIT, timeSinceHit);
    }

    /**
     * Sets the damage taken from the last hit.
     */
    public void setDamageTaken(float damageTaken) {
        entityData.set(DAMAGE_TAKEN, damageTaken);
    }

    /**
     * Gets the damage taken from the last hit.
     */
    public float getDamageTaken() {
        return entityData.get(DAMAGE_TAKEN);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {

    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {

    }

}
