package com.windokkstudio.militaryelements.entities.vehicles;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.windokkstudio.militaryelements.MilitaryElements;
import com.windokkstudio.militaryelements.init.DataSerializersInit;
import com.windokkstudio.militaryelements.init.ItemInit;
import com.windokkstudio.militaryelements.init.networking.ChangeThrottlePacket;
import com.windokkstudio.militaryelements.misc.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;

import static com.windokkstudio.militaryelements.misc.MathUtil.*;
import static net.minecraft.util.Mth.wrapDegrees;

public class JeepEntity extends Entity implements IEntityAdditionalSpawnData {
    public static final EntityDataAccessor<Integer> MAX_HEALTH = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> HEALTH = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> MAX_SPEED = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> TIME_SINCE_HIT = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> DAMAGE_TAKEN = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Quaternion> Q = SynchedEntityData.defineId(JeepEntity.class, DataSerializersInit.QUATERNION_SERIALIZER_ENTRY.get());
    public static final EntityDataAccessor<Float> THROTTLE = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Byte> PITCH_UP = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.BYTE);
    public static final EntityDataAccessor<Byte> YAW_RIGHT = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.BYTE);
    public static final int MAX_THROTTLE = 5;
    public Quaternion Q_Client = new Quaternion(Quaternion.ONE);
    public Quaternion Q_Prev = new Quaternion(Quaternion.ONE);

    private int onGroundTicks;
    public float rotationRoll;
    public float prevRotationRoll;
    private float deltaRotation;
    private float deltaRotationLeft;
    private int deltaRotationTicks;
    private int damageTimeout;
    public int notMovingTime;
    float yawSpeed = 0;
    private int lerpSteps;
    private int lerpStepsQ;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    protected float pitchSpeed = 0;
    protected float rollSpeed = 0;
    public int goldenHeartsTimeout = 0;
    public boolean isPickable() {
        return true;
    }
    private static final TempMotionVars TEMP_MOTION_VARS = new TempMotionVars();
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
    public boolean getOnGround() {
        return onGround || onGroundTicks > 1;
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
    public Player getPlayer() {
        if (getControllingPassenger() instanceof Player) {
            return (Player) getControllingPassenger();
        }
        return null;
    }
    public boolean isPowered() {
        return true;
    }



    public JeepEntity(EntityType<JeepEntity> type, Level world) {
        super(type, world);
    }


    //Data
    @Override
    protected void defineSynchedData() {
        entityData.define(MAX_HEALTH, 10);
        entityData.define(HEALTH, 10);
        entityData.define(Q, Quaternion.ONE);
        entityData.define(MAX_SPEED, 0.25f);
        entityData.define(TIME_SINCE_HIT, 0);
        entityData.define(DAMAGE_TAKEN, 0f);
        entityData.define(THROTTLE, 0.0f);
        entityData.define(PITCH_UP, (byte) 0);
        entityData.define(YAW_RIGHT, (byte) 0);
    }



    //Set Methods
    public void setQ(Quaternion q) {
        entityData.set(Q, q);
    }
    public void setQ_Client(Quaternion q) {
        Q_Client = q;
    }
    public void setQ_prev(Quaternion q) {
        Q_Prev = q;
    }
    public void setHealth(int health) {
        entityData.set(HEALTH, Math.max(health, 0));
    }
    public void setYawRight(byte yawRight) {
        entityData.set(YAW_RIGHT, yawRight);
    }
    public void setTimeSinceHit(int timeSinceHit) {
        entityData.set(TIME_SINCE_HIT, timeSinceHit);
    }
    public void setDamageTaken(float damageTaken) {
        entityData.set(DAMAGE_TAKEN, damageTaken);
    }
    public void setThrottle(float value) {
        entityData.set(THROTTLE, value);
    }
    public void setPitchUp(byte pitchUp) {
        entityData.set(PITCH_UP, pitchUp);
    }



    //Get Methods
    @Override
    public ItemStack getPickedResult(HitResult target) {
        return getItemStack();
    }
    public float getMaxSpeed() {
        return entityData.get(MAX_SPEED);
    }
    public Quaternion getQ() {
        return new Quaternion(entityData.get(Q));
    }
    public Quaternion getQ_Client() {
        return new Quaternion(Q_Client);
    }
    public Quaternion getQ_Prev() {
        return Q_Prev.copy();
    }
    public int getHealth() {
        return entityData.get(HEALTH);
    }
    public int getMaxHealth() {
        return entityData.get(MAX_HEALTH);
    }
    protected float getRotationSpeedMultiplier() {
        return 1.0f;
    }
    public int getTimeSinceHit() {
        return entityData.get(TIME_SINCE_HIT);
    }
    public float getDamageTaken() {
        return entityData.get(DAMAGE_TAKEN);
    }
    public byte getYawRight() {
        return entityData.get(YAW_RIGHT);
    }
    public float getThrottle() {
        return entityData.get(THROTTLE);
    }
    public byte getPitchUp() {
        return entityData.get(PITCH_UP);
    }



    //Passengers Methods
    @Override
    protected boolean canAddPassenger(Entity passenger) {
        List<Entity> passengers = getPassengers();
        return passengers.size() < 2;
    }
    @Nullable
    public Entity getControllingPassenger() {
        List<Entity> list = getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }
    @Override
    public void positionRider(Entity passenger) {
        positionRiderGeneric(passenger);
        int index = getPassengers().indexOf(passenger);
        if (index == 0){
            Vector3f pos = transformPos(new Vector3f(-0.3f, 0.4f, 0));
            passenger.setPos(getX() + pos.x(), getY() + pos.y(), getZ() + pos.z());
        } else if (index == 1) {
            Vector3f pos = transformPos(new Vector3f(0.2f, 0.4f, 0));
            passenger.setPos(getX() + pos.x(), getY() + pos.y(), getZ() + pos.z());
        }
    }
    protected void positionRiderGeneric(Entity passenger) {
        super.positionRider(passenger);
        boolean local = (passenger instanceof Player) && ((Player) passenger).isLocalPlayer();

        if (hasPassenger(passenger) && !local) {
            applyYawToEntity(passenger);
        }
    }
    public void applyYawToEntity(Entity entityToUpdate) {
        entityToUpdate.setYHeadRot(entityToUpdate.getYHeadRot() + deltaRotation);

        entityToUpdate.yRotO += deltaRotation;

        entityToUpdate.setYBodyRot(getYRot()-180);

        entityToUpdate.setYHeadRot(entityToUpdate.getYRot());
    }
    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
        if (getPassengers().size() == 0) {
            setThrottle((byte) 0);
            setPitchUp((byte) 0);
            setYawRight((byte) 0);
        }

        return super.getDismountLocationForPassenger(livingEntity);
    }
    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (isControlledByLocalInstance()) {
            if (lerpSteps > 0) {
                lerpSteps = 0;
                absMoveTo(lerpX, lerpY, lerpZ, getYRot(), getXRot());
            }
        }
    }



    //Miscellaneous Methods
    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (player.isShiftKeyDown() && itemStack.isEmpty()) {
            return InteractionResult.SUCCESS;
        }
        if (!level.isClientSide) {
            return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.FAIL;
        } else {
            return player.getRootVehicle() == getRootVehicle() ? InteractionResult.FAIL : InteractionResult.SUCCESS;
        }
    }
    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity entity = source.getDirectEntity();
        if (entity == getControllingPassenger() && entity instanceof Player player) {
            return false;
        }

        if (getOnGround() && entity instanceof Player) {
            amount *= 3;
        }

        if (source.getDirectEntity().getType() == EntityType.ARROW){
            source.getDirectEntity().kill();
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
            dropItem();
        } else if (source == MilitaryElements.DAMAGE_SOURCE_JEEP_CRASH) {
            explode();
            kill();
            if (level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                dropItem();
            }
        } else if (getOnGround() && getHealth() <= 0) {
            kill();
            if (level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                dropItem();
            }
        }
        return super.hurt(source, amount);
    }
    private void explode() {
        ((ServerLevel) level).sendParticles(ParticleTypes.SMOKE,
                getX(),
                getY(),
                getZ(),
                5, 1, 1, 1, 2);
        ((ServerLevel) level).sendParticles(ParticleTypes.POOF,
                getX(),
                getY(),
                getZ(),
                10, 1, 1, 1, 1);
        level.explode(this, getX(), getY(), getZ(), 4.0F, Explosion.BlockInteraction.BREAK);
    }
    protected void dropItem() {
        ItemStack itemStack = getItemStack();
        spawnAtLocation(itemStack).setInvulnerable(true);
    }
    public Vector3f transformPos(Vector3f relPos) {
        MathUtil.EulerAngles angles = toEulerAngles(getQ_Client());
        angles.yaw = -angles.yaw;
        angles.roll = -angles.roll;
        relPos.transform(toQuaternion(angles.yaw, angles.pitch, angles.roll));
        return relPos;
    }
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        if (x == getX() && y == getY() && z == getZ()) {
            return;
        }
        lerpX = x;
        lerpY = y;
        lerpZ = z;
        lerpSteps = 10;
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

        yRotO = getYRot();
        xRotO = getXRot();
    }
    public void changeThrottle(ChangeThrottlePacket.Type type) {
        float throttle = getThrottle();
        if (type == ChangeThrottlePacket.Type.UP) {
            if (throttle < MAX_THROTTLE) {
                setThrottle(throttle + 0.1f);
            }
        } else if (throttle > 0) {
            setThrottle(throttle - 0.1f);
        }
        if (throttle<0){
            setThrottle(0);
        }
    }



    //Ticks
    @Override
    public void tick() {
        super.tick();

        if (Double.isNaN(getDeltaMovement().length())) {
            setDeltaMovement(Vec3.ZERO);
        }
        yRotO = getYRot();
        xRotO = getXRot();
        prevRotationRoll = rotationRoll;

        if (level.isClientSide && getHealth() <= 0) {
            level.addAlwaysVisibleParticle(ParticleTypes.LARGE_SMOKE, true, getX(), getY(), getZ(), 0.0, 0.005, 0.0);
        }

        if (level.isClientSide && getTimeSinceHit() > 0) {
            setTimeSinceHit(getTimeSinceHit() - 1);
        }

        if (level.isClientSide && !isControlledByLocalInstance()) {
            tickLerp();
            setDeltaMovement(Vec3.ZERO);
            tickDeltaRotation(getQ_Client());
            return;
        }
        markHurt();

        TempMotionVars tempMotionVars = getMotionVars();
        if (isNoGravity()) {
            tempMotionVars.gravity = 0;
            tempMotionVars.maxLift = 0;
            tempMotionVars.push = 0.00f;
            tempMotionVars.passiveEnginePush = 0;
        }
        Entity controllingPassenger = getControllingPassenger();
        if (isOnGround())
            if (controllingPassenger instanceof Player playerEntity) {
                tempMotionVars.moveStrafing = playerEntity.xxa;
            } else {
                tempMotionVars.moveStrafing = 0;
                setSprinting(false);
            }


        Quaternion q;
        if (level.isClientSide) {
            q = getQ_Client();
        } else {
            q = getQ();
        }

        EulerAngles anglesOld = toEulerAngles(q).copy();

        Vec3 oldMotion = getDeltaMovement();

        tempMotionVars.push = 0.00625f * getThrottle();

        //motion and rotation interpolation + lift.
        if (getDeltaMovement().length() > 0.05) {
            q = tickRotateMotion(tempMotionVars, q, getDeltaMovement());
        }
        boolean doPitch = true;
        //pitch + movement speed
        if (getOnGround()) {
            doPitch = tickOnGround(tempMotionVars);
        } else {
            onGroundTicks--;
        }



        tickMotion(tempMotionVars);
        tickRoll(tempMotionVars);

        //made so plane fully stops when moves slow, removing the slipperiness effect
        if (onGroundTicks > -50 && oldMotion.length() < 0.002 && getDeltaMovement().length() < 0.002) {
            setDeltaMovement(Vec3.ZERO);
        }

        reapplyPosition();

        if (!onGround || getHorizontalDistanceSqr(getDeltaMovement()) > (double) 1.0E-5F || (tickCount + getId()) % 4 == 0) {
            double speedBefore = Math.sqrt(getHorizontalDistanceSqr(getDeltaMovement()));
            boolean onGroundOld = onGround;
            Vec3 motion = getDeltaMovement();
            if (motion.lengthSqr() > 0.25 || getPitchUp() != 0) {
                onGround = true;
            }
            move(MoverType.SELF, motion);
            onGround = ((motion.y()) == 0.0) ? onGroundOld : onGround;

        }

        //back to q
        q.mul(new Quaternion(Vector3f.ZP, ((float) (rotationRoll - anglesOld.roll)), true));
        q.mul(new Quaternion(Vector3f.XN, ((float) (getXRot() - anglesOld.pitch)), true));
        q.mul(new Quaternion(Vector3f.YP, ((float) (getYRot() - anglesOld.yaw)), true));

        q = normalizeQuaternion(q);

        setQ_prev(getQ_Client());
        setQ(q);
        tickDeltaRotation(q);

        if (level.isClientSide && isControlledByLocalInstance()) {
            setQ_Client(q);
        } else {
            ServerPlayer player = (ServerPlayer) getPlayer();
            if (player != null) {
            }
        }
        if (damageTimeout > 0) {
            --damageTimeout;
        }
        if (getDamageTaken() > 0.0F) {
            setDamageTaken(getDamageTaken() - 1.0F);
        }
        if (!level.isClientSide && getHealth() > getMaxHealth() & goldenHeartsTimeout > (getOnGround() ? 300 : 100)) {
            setHealth(getHealth() - 1);
            goldenHeartsTimeout = 0;
        }

        tickLerp();
    }
    protected void tickRoll(TempMotionVars tempMotionVars) {
        if (getHealth() <= 0) {
            rotationRoll += getId() % 2 == 0 ? 10.0f : -10.0f;
            return;
        }

        double turn = 0;

        if (getOnGround()) {
            turn = tempMotionVars.moveStrafing > 0 ? 3 : tempMotionVars.moveStrafing == 0 ? 0 : -3;
            rotationRoll = lerpAngle(0.1f, rotationRoll, 0);

        } else {
            if (tempMotionVars.moveStrafing > 0.0f) {
                rollSpeed += 0.5f;
            } else if (tempMotionVars.moveStrafing < 0.0f) {
                rollSpeed -= 0.5f;
            } else {
                if (rollSpeed < 0) {
                    rollSpeed += 0.5f;
                } else if (rollSpeed > 0) {
                    rollSpeed -= 0.5f;
                }
            }

            rollSpeed = Mth.clamp(rollSpeed, -5.0f, 5.0f);
            rotationRoll += rollSpeed;
        }

        setYRot((float) (getYRot() - turn));
    }
    private void tickLerp() {
        if (isControlledByLocalInstance()) {
            lerpSteps = 0;
            lerpStepsQ = 0;
            syncPacketPositionCodec(getX(), getY(), getZ());
            return;
        }

        if (lerpSteps > 0) {
            double d0 = getX() + (lerpX - getX()) / (double) lerpSteps;
            double d1 = getY() + (lerpY - getY()) / (double) lerpSteps;
            double d2 = getZ() + (lerpZ - getZ()) / (double) lerpSteps;
            --lerpSteps;
            setPos(d0, d1, d2);
        }
        if (lerpStepsQ > 0) {
            setQ_prev(getQ_Client());
            setQ_Client(lerpQ(1f / lerpStepsQ, getQ_Client(), getQ()));
            --lerpStepsQ;
        } else if (lerpStepsQ == 0) {
            setQ_prev(getQ_Client());
            setQ_Client(getQ());
            --lerpStepsQ;
        }
    }
    protected void tickDeltaRotation(Quaternion q) {
        EulerAngles angles = toEulerAngles(q);
        setYRot((float) angles.yaw);
        rotationRoll = (float) angles.roll;

        float d = (float) wrapSubtractDegrees(yRotO, getYRot());
        if (rotationRoll >= 90 && prevRotationRoll <= 90) {
            d = 0;
        }
        int diff = 3;

        deltaRotationTicks = Math.min(10, Math.max((int) Math.abs(deltaRotationLeft) * 5, deltaRotationTicks));
        deltaRotationLeft *= 0.7;
        deltaRotationLeft += d;
        deltaRotationLeft = wrapDegrees(deltaRotationLeft);
        deltaRotation = Math.min(Math.abs(deltaRotationLeft), diff) * Math.signum(deltaRotationLeft);
        deltaRotationLeft -= deltaRotation;
        if (!(deltaRotation > 0)) {
            deltaRotationTicks--;
        }
    }
    protected boolean tickOnGround(TempMotionVars tempMotionVars) {
        if (getDeltaMovement().lengthSqr() < 0.01 && getOnGround()) {
            notMovingTime += 1;
        } else {
            notMovingTime = 0;
        }
        if (notMovingTime > 200 && getHealth() < getMaxHealth() && getPlayer() != null) {
            setHealth(getHealth() + 1);
            notMovingTime = 100;
        }

        boolean speedingUp = true;
        if (onGroundTicks < 0) {
            onGroundTicks = 5;
        } else {
            onGroundTicks--;
        }


        if (degreesDifferenceAbs(getXRot(), 0) > 1 && getDeltaMovement().length() < 0.1) {
            tempMotionVars.push /= 5; //runs while the plane is taking off
        }
        if (getDeltaMovement().length() < tempMotionVars.takeOffSpeed) {
            //                rotationPitch = lerpAngle(0.2f, rotationPitch, pitch);
            speedingUp = false;
            //                push = 0;
        }
        if (getPitchUp() < 0) {
            tempMotionVars.push = -tempMotionVars.groundPush;
        } else if (getPitchUp() > 0 && tempMotionVars.push < tempMotionVars.groundPush) {
            tempMotionVars.push = tempMotionVars.groundPush;
        }
        float f;
        BlockPos pos = new BlockPos(getX(), getY() - 1.0D, getZ());
        f = level.getBlockState(pos).getFriction(level, pos, this);
        tempMotionVars.dragMul *= 20 * (3 - f);
        return speedingUp;
    }
    protected Quaternion tickRotateMotion(TempMotionVars tempMotionVars, Quaternion q, Vec3 motion) {
        float yaw = MathUtil.getYaw(motion);
        float pitch = MathUtil.getPitch(motion);
        if (degreesDifferenceAbs(yaw, getYRot()) > 5 && (getOnGround())) {
            setDeltaMovement(motion.scale(0.98));
        }

        float d = (float) degreesDifferenceAbs(pitch, getXRot());
        if (d > 180) {
            d = d - 180;
        }
        //            d/=3600;
        d /= 60;
        d = Math.min(1, d);
        d *= d;
        d = 1 - d;
        //            speed = getMotion().length()*(d);
        double speed = getDeltaMovement().length();
        double lift = Math.min(speed * tempMotionVars.liftFactor, tempMotionVars.maxLift) * d;
        if (getHealth() <= 0) {
            lift = 0;
        }
//        double cosRoll = (1 + 4 * Math.max(Math.cos(Math.toRadians(degreesDifferenceAbs(rotationRoll, 0))), 0)) / 5;
//        lift *= cosRoll;
//        d *= cosRoll;

        setDeltaMovement(rotationToVector(lerpAngle180(0.1f, yaw, getYRot()),
                lerpAngle180(tempMotionVars.pitchToMotion * d, pitch, getXRot()) + lift,
                speed));
        return q;
    }
    protected void tickMotion(TempMotionVars tempMotionVars) {
        Vec3 motion;
        if (!isPowered()) {
            tempMotionVars.push = 0;
        }
        motion = getDeltaMovement();
        double throttle = getThrottle();

        motion = getForward().scale(-throttle);

        motion = motion.add(0, tempMotionVars.gravity, 0);

        setDeltaMovement(motion);
    }
    protected Vector3f getTickPush(TempMotionVars tempMotionVars) {
        return transformPos(new Vector3f(0, 0, tempMotionVars.push));
    }


    //Mandatory Methods
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
        setQ(new Quaternion(getXRot(), getYRot(), 0, true));
    }
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("health", entityData.get(HEALTH));
        compound.putInt("max_health", entityData.get(MAX_HEALTH));
        compound.putFloat("max_speed", entityData.get(MAX_SPEED));
    }
    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
    }
    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
    }
    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (Q.equals(key) && level.isClientSide() && !isControlledByLocalInstance()) {
            if (firstTick) {
                lerpStepsQ = 0;
                setQ_Client(getQ());
                setQ_prev(getQ());
            } else {
                lerpStepsQ = 10;
            }
        }
    }


    protected JeepEntity.TempMotionVars getMotionVars() {
        TEMP_MOTION_VARS.reset();
        TEMP_MOTION_VARS.maxPushSpeed = getMaxSpeed() * 10;
        return TEMP_MOTION_VARS;
    }
    protected static class TempMotionVars {
        public float moveForward; //TODO: move to HelicopterEntity?
        public double turnThreshold;
        public float moveStrafing;
        double maxSpeed;
        double maxPushSpeed;
        double takeOffSpeed;
        float maxLift;
        double liftFactor;
        double gravity;
        double drag;
        double dragMul;
        double dragQuad;
        float push;
        float groundPush;
        float passiveEnginePush;
        float motionToRotation;
        float pitchToMotion;
        float yawMultiplayer;

        public TempMotionVars() {
            reset();
        }

        public void reset() {
            moveForward = 0;
            turnThreshold = 0;
            moveStrafing = 0;
            maxSpeed = 3;
            takeOffSpeed = 0.3;
            maxLift = 2;
            liftFactor = 10;
            gravity = -0.03;
            drag = 0.001;
            dragMul = 0.0005;
            dragQuad = 0.001;
            push = 0.0f;
            groundPush = 0.01f;
            passiveEnginePush = 0.025f;
            motionToRotation = 0.05f;
            pitchToMotion = 0.2f;
            yawMultiplayer = 0.5f;
        }
    }
}

