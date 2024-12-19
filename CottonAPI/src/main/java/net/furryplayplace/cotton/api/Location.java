package net.furryplayplace.cotton.api;

import com.google.common.base.Preconditions;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import java.util.Vector;;

/**
 * Represents a 3-dimensional position in a world.
 * <br>
 * No constraints are placed on any angular values other than that they be
 * specified in degrees. This means that negative angles or angles of greater
 * magnitude than 360 are valid, but may be normalized to any other equivalent
 * representation by the implementation.
 *
 * This class was originally made for Bukkit but adapted for CottonFramework as a stub
 */
@Getter
@Setter
public class Location implements Cloneable { // Paper
    private Reference<World> world;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    /**
     * Constructs a new Location with the given coordinates
     *
     * @param world The world in which this location resides
     * @param x The x-coordinate of this new location
     * @param y The y-coordinate of this new location
     * @param z The z-coordinate of this new location
     */
    public Location(final World world, final double x, final double y, final double z) { // Paper
        this(world, x, y, z, 0, 0);
    }

    /**
     * Constructs a new Location with the given coordinates and direction
     *
     * @param world The world in which this location resides
     * @param x The x-coordinate of this new location
     * @param y The y-coordinate of this new location
     * @param z The z-coordinate of this new location
     * @param yaw The absolute rotation on the x-plane, in degrees
     * @param pitch The absolute rotation on the y-plane, in degrees
     */
    public Location(final World world, final double x, final double y, final double z, final float yaw, final float pitch) { // Paper
        if (world != null) {
            this.world = new WeakReference<>(world);
        }

        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    /**
     * Sets the world that this location resides in
     *
     * @param world New world that this location resides in
     */
    public void setWorld(@Nullable World world) {
        this.world = (world == null) ? null : new WeakReference<>(world);
    }

    public World getWorld() {
        if (this.world == null) {
            return null;
        }

        World world = this.world.get();
        Preconditions.checkArgument(world != null, "World unloaded");
        return world;
    }

    /**
     * Gets the chunk at the represented location
     *
     * @return Chunk at the represented location
     */
    @NotNull
    public Chunk getChunk() {
        return getWorld().getChunk((int) this.x, (int) this.z);
    }

    /**
     * Gets the block at the represented location
     *
     * @return Block at the represented location
     */
    @NotNull
    public Block getBlock() {
        return getWorld().getBlockState(BlockPos.ofFloored(this.x, this.y, this.z)).getBlock();
    }

    /**
     * Gets a unit-vector pointing in the direction that this Location is
     * facing.
     *
     * @return a vector pointing the direction of this location's {@link
     *     #getPitch() pitch} and {@link #getYaw() yaw}
     */
    @NotNull
    public Vector3d getDirection() {
        Vector3d vector = new Vector3d();

        double rotX = this.getYaw();
        double rotY = this.getPitch();

        vector.y = (-Math.sin(Math.toRadians(rotY)));

        double xz = Math.cos(Math.toRadians(rotY));

        vector.x = (-xz * Math.sin(Math.toRadians(rotX)));
        vector.z = (xz * Math.cos(Math.toRadians(rotX)));

        return vector;
    }

    /**
     * Adds the location by another.
     *
     * @param vec The other location
     * @return the same location
     * @throws IllegalArgumentException for differing worlds
     * @see Vector
     */
    @NotNull
    public Location add(@NotNull Location vec) {
        if (vec == null || vec.getWorld() != getWorld()) {
            throw new IllegalArgumentException("Cannot add Locations of differing worlds");
        }

        x += vec.x;
        y += vec.y;
        z += vec.z;
        return this;
    }

    /**
     * Adds the location by a vector.
     *
     * @param vec Vector to use
     * @return the same location
     * @see Vector
     */
    @NotNull
    public Location add(@NotNull Vector3d vec) {
        this.x += vec.x();
        this.y += vec.y();
        this.z += vec.z();
        return this;
    }

    /**
     * Adds the location by another. Not world-aware.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return the same location
     * @see Vector
     */
    @NotNull
    public Location add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    /**
     * Subtracts the location by another.
     *
     * @param vec The other location
     * @return the same location
     * @throws IllegalArgumentException for differing worlds
     * @see Vector
     */
    @NotNull
    public Location subtract(@NotNull Location vec) {
        if (vec == null || vec.getWorld() != getWorld()) {
            throw new IllegalArgumentException("Cannot add Locations of differing worlds");
        }

        x -= vec.x;
        y -= vec.y;
        z -= vec.z;
        return this;
    }

    /**
     * Subtracts the location by a vector.
     *
     * @param vec The vector to use
     * @return the same location
     * @see Vector
     */
    @NotNull
    public Location subtract(@NotNull Vector3d vec) {
        this.x -= vec.x();
        this.y -= vec.y();
        this.z -= vec.z();
        return this;
    }

    /**
     * Subtracts the location by another. Not world-aware and
     * orientation independent.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return the same location
     * @see Vector
     */
    @NotNull
    public Location subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    /**
     * Performs scalar multiplication, multiplying all components with a
     * scalar. Not world-aware.
     *
     * @param m The factor
     * @return the same location
     * @see Vector
     */
    @NotNull
    public Location multiply(double m) {
        x *= m;
        y *= m;
        z *= m;
        return this;
    }

    /**
     * Zero this location's components. Not world-aware.
     *
     * @return the same location
     * @see Vector
     */
    @NotNull
    public Location zero() {
        x = 0;
        y = 0;
        z = 0;
        return this;
    }

    /**
     * Sets the position of this Location and returns itself
     * <p>
     * This mutates this object, clone first.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return self (not cloned)
     */
    @NotNull
    public Location set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /**
     * Takes the x/y/z from base and adds the specified x/y/z to it and returns self
     * <p>
     * This mutates this object, clone first.
     *
     * @param base The base coordinate to modify
     * @param x X coordinate to add to base
     * @param y Y coordinate to add to base
     * @param z Z coordinate to add to base
     * @return self (not cloned)
     */
    @NotNull
    public Location add(@NotNull Location base, double x, double y, double z) {
        return this.set(base.x + x, base.y + y, base.z + z);
    }

    /**
     * Takes the x/y/z from base and subtracts the specified x/y/z to it and returns self
     * <p>
     * This mutates this object, clone first.
     *
     * @param base The base coordinate to modify
     * @param x X coordinate to subtract from base
     * @param y Y coordinate to subtract from base
     * @param z Z coordinate to subtract from base
     * @return self (not cloned)
     */
    @NotNull
    public Location subtract(@NotNull Location base, double x, double y, double z) {
        return this.set(base.x - x, base.y - y, base.z - z);
    }

    @Override
    public Location clone() {
        try {
            return (Location) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
