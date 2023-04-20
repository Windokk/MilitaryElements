package com.windokkstudio.militaryelements.entities.vehicles;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.windokkstudio.militaryelements.MilitaryElements;
import com.windokkstudio.militaryelements.init.ItemInit;
import com.windokkstudio.militaryelements.init.DataSerializersInit;
import com.windokkstudio.militaryelements.init.networking.ChangeThrottlePacket;
import com.windokkstudio.militaryelements.init.networking.NetworkingInit;
import com.windokkstudio.militaryelements.init.networking.RotationPacket;
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
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

import static com.windokkstudio.militaryelements.misc.MathUtil.*;
import static net.minecraft.util.Mth.wrapDegrees;


public class JeepEntityold extends Entity implements IEntityAdditionalSpawnData {

    //Entity Data Vars
    public static final EntityDataAccessor<Integer> MAX_HEALTH = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> HEALTH = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> MAX_SPEED = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> TIME_SINCE_HIT = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> DAMAGE_TAKEN = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Quaternion> Q = SynchedEntityData.defineId(JeepEntity.class, DataSerializersInit.QUATERNION_SERIALIZER_ENTRY.get());
    public static final EntityDataAccessor<Integer> SPEED = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Byte> PITCH_UP = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.BYTE);
    public static final EntityDataAccessor<Byte> YAW_RIGHT = SynchedEntityData.defineId(JeepEntity.class, EntityDataSerializers.BYTE);
    public static final int MAX_THROTTLE = 5;

    //Vars
    public Quaternion Q_Client = new Quaternion(Quaternion.ONE);
    public Quaternion Q_Prev = new Quaternion(Quaternion.ONE);
    public float rotationRoll;
    private int onGroundTicks;
    private int damageTimeout;
    public int notMovingTime;
    public float prevRotationRoll;
    private float deltaRotation;
    private float deltaRotationLeft;
    private int deltaRotationTicks;
    private int lerpSteps;
    private int lerpStepsQ;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    protected float yawSpeed = 0;
    protected float rollSpeed = 0;
    protected float pitchSpeed = 0;
    public int goldenHeartsTimeout = 0;
    @Override
    public boolean canCollideWith(Entity p_20303_){
        return true;
    }
    @Override
    public boolean canBeCollidedWith() {return true;}
    protected float getRotationSpeedMultiplier() {return 1.0f;}
    protected float getGroundPitch() {return 5;}
    public boolean isPickable() {
        return true;
    }
    public boolean isPowered() {
        return isAlive() && (isCreative());
    }
    public boolean isCreative() {
        return getControllingPassenger() instanceof Player && ((Player) getControllingPassenger()).isCreative();
    }


    public JeepEntityold(EntityType<JeepEntity> type, Level world) {
        super(type, world);
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
        if (this.isVehicle()) {
            Entity controllingPassenger = getFirstPassenger();
            if (controllingPassenger instanceof Player playerEntity) {

                tempMotionVars.moveForward = getMoveForward(playerEntity);
                tempMotionVars.moveStrafing = playerEntity.xxa;
            } else {
                tempMotionVars.moveForward = 0;
                tempMotionVars.moveStrafing = 0;
                setSprinting(false);
            }
        }

        if (Math.abs(tempMotionVars.moveStrafing) < tempMotionVars.turnThreshold) {
            tempMotionVars.moveStrafing = 0;
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
        if (doPitch) {
            tickPitch(tempMotionVars);
        }

        tickYaw();

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
            if (horizontalCollision && !level.isClientSide && onGroundTicks <= 0) {
                if (getHealth() <= 0) {
                    crash(16);
                } else {
                    double speedAfter = Math.sqrt(getHorizontalDistanceSqr(getDeltaMovement()));
                    double speedDiff = speedBefore - speedAfter;
                    float f2 = (float) (speedDiff * 10.0D - 5.0D);
                    if (f2 > 5.0F) {
                        crash(f2);
                    }
                }
            }
        }

        if (getHealth() <= 0 && onGround && !isRemoved()) {
            crash(16);
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

            NetworkingInit.INSTANCE.sendToServer(new RotationPacket(getQ()));
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
        if (goldenHeartsTimeout < 1000 && isPowered()) {
            goldenHeartsTimeout++;
        }

        tickLerp();
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
        if (!getOnGround() && motion.length() > 0.1) {

            if (degreesDifferenceAbs(pitch, getXRot()) > 90) {
                pitch = wrapDegrees(pitch + 180);
            }
            if (Math.abs(getXRot()) < 85) {

                yaw = MathUtil.getYaw(getDeltaMovement());
                if (degreesDifferenceAbs(yaw, getYRot()) > 90) {
                    yaw = yaw - 180;
                }
                Quaternion q1 = toQuaternion(yaw, pitch, rotationRoll);
                q = lerpQ(tempMotionVars.motionToRotation, q, q1);
            }

        }
        return q;
    }
    protected void tickMotion(TempMotionVars tempMotionVars) {
        Vec3 motion;
        motion = getDeltaMovement();
        double speed = motion.length();
        double brakesMul = getThrottle() == 0 ? 5.0 : 1.0;
        speed -= (speed * speed * tempMotionVars.dragQuad + speed * tempMotionVars.dragMul + tempMotionVars.drag) * brakesMul;
        speed = Math.max(speed, 0);
        if (speed > tempMotionVars.maxSpeed) {
            speed = Mth.lerp(0.2, speed, tempMotionVars.maxSpeed);
            speed = Mth.lerp(0.2, speed, tempMotionVars.maxSpeed);
        }

        if (speed == 0) {
            motion = Vec3.ZERO;
        }
        if (motion.length() > 0) {
            motion = motion.scale(speed / motion.length());
        }

        Vec3 pushVec = new Vec3(getTickPush(tempMotionVars));
        if (pushVec.length() != 0 && motion.length() > 0.1) {
            double dot = normalizedDotProduct(pushVec, motion);
            pushVec = pushVec.scale(Mth.clamp(1 - dot * speed / (tempMotionVars.maxPushSpeed * (tempMotionVars.push + 0.05)), 0, 2));
        }

        motion = motion.add(pushVec);

        motion = motion.add(0, tempMotionVars.gravity, 0);

        setDeltaMovement(motion);
    }
    protected void tickYaw() {
        float yaw;
        if (getHealth() <= 0) {
            yaw = 10.0f;
        } else {
            if (getYawRight() > 0) {
                yawSpeed += 0.5f * getRotationSpeedMultiplier();
            } else if (getYawRight() < 0) {
                yawSpeed -= 0.5f * getRotationSpeedMultiplier();
            } else {
                if (yawSpeed < 0) {
                    yawSpeed += 0.5f * getRotationSpeedMultiplier();
                } else if (yawSpeed > 0) {
                    yawSpeed -= 0.5f * getRotationSpeedMultiplier();
                }
            }
            yawSpeed = Mth.clamp(yawSpeed, -2.5f * getRotationSpeedMultiplier(), 2.5f * getRotationSpeedMultiplier());
            yaw = yawSpeed;
        }
        setYRot(getYRot() + yaw);
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
    protected Vector3f getTickPush(TempMotionVars tempMotionVars) {
        return transformPos(new Vector3f(0, 0, tempMotionVars.push));
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
        float pitch = getGroundPitch();
        if ((getPitchUp() > 0)) {
            pitch = 0;
        } else if (getDeltaMovement().length() > tempMotionVars.takeOffSpeed) {
            pitch /= 2;
        }
        setXRot(lerpAngle(0.1f, getXRot(), pitch));

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
    protected void tickPitch(TempMotionVars tempMotionVars) {
        float pitch;
        if (getHealth() <= 0) {
            pitch = 10.0f;
        } else {
            if (getPitchUp() > 0) {
                pitchSpeed += 0.5f * getRotationSpeedMultiplier();
            } else if (getPitchUp() < 0) {
                pitchSpeed -= 0.5f * getRotationSpeedMultiplier();
            } else {
                if (pitchSpeed < 0) {
                    pitchSpeed += 0.5f * getRotationSpeedMultiplier();
                } else if (pitchSpeed > 0) {
                    pitchSpeed -= 0.5f * getRotationSpeedMultiplier();
                }
            }
            pitchSpeed = Mth.clamp(pitchSpeed, -5.0f * getRotationSpeedMultiplier(), 5.0f * getRotationSpeedMultiplier());
            pitch = pitchSpeed;
        }
        setXRot(getXRot() + pitch);
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
        setXRot((float) angles.pitch);
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



    //Miscellaneous Methods
    public Vector3f transformPos(Vector3f relPos) {
        EulerAngles angles = toEulerAngles(getQ_Client());
        angles.yaw = -angles.yaw;
        angles.roll = -angles.roll;
        relPos.transform(toQuaternion(angles.yaw, angles.pitch, angles.roll));
        return relPos;
    }
    protected void dropItem() {
        ItemStack itemStack = getItemStack();
        spawnAtLocation(itemStack).setInvulnerable(true);
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
    public void crash(float damage) {
        if (!level.isClientSide && isAlive()) {
            for (Entity entity : getPassengers()) {
                float damageMod = Math.min(1, 1 - ((float) getHealth() / getMaxHealth()));
                entity.hurt(MilitaryElements.DAMAGE_SOURCE_JEEP_CRASH, damage * damageMod);
            }
            hurt(MilitaryElements.DAMAGE_SOURCE_JEEP_CRASH, damage + 2);
        }
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


    //Passengers Methods
    @Override
    protected boolean canAddPassenger(Entity passenger) {
        List<Entity> passengers = getPassengers();
        return passengers.size() < 2;
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

        entityToUpdate.setYBodyRot(getYRot());

        entityToUpdate.setYHeadRot(entityToUpdate.getYRot());
    }
    public void tryAddPassenger(Player player){
            if (canAddPassenger(player))
                player.startRiding(this);
        }


    //Interactions Methods
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
    @Override
    public InteractionResult interact(Player player, InteractionHand interactionHand) {
        if (interactionHand == InteractionHand.MAIN_HAND){
            if (player.getUseItem() == ItemStack.EMPTY){
                tryAddPassenger(player);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }



    //TempMotion Vars Set() Methods
    public void setQ(Quaternion q) {
        entityData.set(Q, q);
    }
    public void setQ_Client(Quaternion q) {
        Q_Client = q;
    }
    public void setQ_prev(Quaternion q) {
        Q_Prev = q;
    }
    public void setTimeSinceHit(int timeSinceHit) {
        entityData.set(TIME_SINCE_HIT, timeSinceHit);
    }
    public void setDamageTaken(float damageTaken) {
        entityData.set(DAMAGE_TAKEN, damageTaken);
    }
    public void setHealth(int health) {
        entityData.set(HEALTH, Math.max(health, 0));
    }
    public void setThrottle(int value) {
        entityData.set(SPEED, value);
    }
    public void setPitchUp(byte pitchUp) {
        entityData.set(PITCH_UP, pitchUp);
    }
    public void setYawRight(byte yawRight) {
        entityData.set(YAW_RIGHT, yawRight);
    }
    public void changeThrottle(ChangeThrottlePacket.Type type) {
        int throttle = getThrottle();
        if (type == ChangeThrottlePacket.Type.UP) {
            if (throttle < MAX_THROTTLE) {
                setThrottle(throttle + 1);
            }
        } else if (throttle > 0) {
            setThrottle(throttle - 1);
        }
    }


    //Temp Motion Vars Get() Methods
    private static final TempMotionVars TEMP_MOTION_VARS = new TempMotionVars();
    public byte getYawRight() {
        return entityData.get(YAW_RIGHT);
    }
    public int getThrottle() {
        return entityData.get(SPEED);
    }
    public float getMaxSpeed() {
        return entityData.get(MAX_SPEED);
    }
    public int getHealth() {
        return entityData.get(HEALTH);
    }
    public byte getPitchUp() {
        return entityData.get(PITCH_UP);
    }
    public int getMaxHealth() {
        return entityData.get(MAX_HEALTH);
    }
    public float getDamageTaken() {
        return entityData.get(DAMAGE_TAKEN);
    }
    public int getTimeSinceHit() {
        return entityData.get(TIME_SINCE_HIT);
    }
    public Quaternion getQ_Prev() {
        return Q_Prev.copy();
    }
    public Quaternion getQ() {
        return new Quaternion(entityData.get(Q));
    }
    public Quaternion getQ_Client() {
        return new Quaternion(Q_Client);
    }


    // Miscellaneous Get() Methods
    protected TempMotionVars getMotionVars() {
        TEMP_MOTION_VARS.reset();
        TEMP_MOTION_VARS.maxPushSpeed = getMaxSpeed() * 10;
        return TEMP_MOTION_VARS;
    }
    protected float getMoveForward(Player player) {return player.zza;}
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
    public boolean getOnGround() {return onGround || onGroundTicks > 1;}
    public Player getPlayer() {
        if (getControllingPassenger() instanceof Player) {
            return (Player) getControllingPassenger();
        }
        return null;
    }


    //Mandatory Methods
    @Override
    protected void defineSynchedData() {
        entityData.define(MAX_HEALTH, 10);
        entityData.define(HEALTH, 10);
        entityData.define(Q, Quaternion.ONE);
        entityData.define(MAX_SPEED, 0.25f);
        entityData.define(TIME_SINCE_HIT, 0);
        entityData.define(DAMAGE_TAKEN, 0f);
        entityData.define(SPEED, 0);
        entityData.define(PITCH_UP, (byte) 0);
        entityData.define(YAW_RIGHT, (byte) 0);
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
    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {

    }
    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
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

