package mtr.entity;

import mtr.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class EntityBase extends Entity {

	private int clientInterpolationSteps;
	private double clientX;
	private double clientY;
	private double clientZ;

	public EntityBase(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public void lerpTo(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
		clientX = x;
		clientY = y;
		clientZ = z;
		clientInterpolationSteps = interpolationSteps;
	}

	@Override
	public final Packet<?> getAddEntityPacket() {
		return Registry.createAddEntityPacket(this);
	}

	@Override
	protected final void readAdditionalSaveData(CompoundTag compoundTag) {
	}

	@Override
	protected final void addAdditionalSaveData(CompoundTag compoundTag) {
	}

	protected final void setClientPosition() {
		if (clientInterpolationSteps > 0) {
			final double x = getX() + (clientX - getX()) / clientInterpolationSteps;
			final double y = getY() + (clientY - getY()) / clientInterpolationSteps;
			final double z = getZ() + (clientZ - getZ()) / clientInterpolationSteps;
			--clientInterpolationSteps;
			absMoveTo(x, y, z);
		} else {
			reapplyPosition();
		}
	}
}
