package io.github.blueknight137.fpamod.render;

import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector3f;

/**
 * Seems pointless for now, but I plan on expanding it later with custom render data.
 * Doing this now won't break the API in the future.
 */
public class RenderData {

    private final Transformation transformation;

    public RenderData(Transformation transformation) {
        this.transformation = transformation;
    }

    public RenderData() {
        this(new Transformation());
    }

    public Vector3f getPosition() {
        return transformation.getPosition();
    }

    public Vector3f getRotation() {
        return transformation.getRotation();
    }

    public Vector3f getScale() {
        return transformation.getScale();
    }

    public float getPositionX() {
        return transformation.getPositionX();
    }

    public float getPositionY() {
        return transformation.getPositionY();
    }

    public float getPositionZ() {
        return transformation.getPositionZ();
    }

    public float getYaw() {
        return transformation.getYaw();
    }

    public float getPitch() {
        return transformation.getPitch();
    }

    public float getRoll() {
        return transformation.getRoll();
    }

    public float getScaleX() {
        return transformation.getScaleX();
    }

    public float getScaleY() {
        return transformation.getScaleY();
    }

    public float getScaleZ() {
        return transformation.getScaleZ();
    }

    public RenderData setPosition(Vector3f position) {
        transformation.setPosition(position);
        return this;
    }

    public RenderData setRotation(Vector3f rotation) {
        transformation.setRotation(rotation);
        return this;
    }

    public RenderData setScale(Vector3f scale) {
        transformation.setScale(scale);
        return this;
    }

    public RenderData setPositionX(float v) {
        transformation.setPositionX(v);
        return this;
    }

    public RenderData setPositionY(float v) {
        transformation.setPositionY(v);
        return this;
    }

    public RenderData setPositionZ(float v) {
        transformation.setPositionZ(v);
        return this;
    }

    public RenderData setYaw(float v) {
        transformation.setYaw(v);
        return this;
    }

    public RenderData setPitch(float v) {
        transformation.setPitch(v);
        return this;
    }

    public RenderData setRoll(float v) {
        transformation.setRoll(v);
        return this;
    }

    public RenderData setScaleX(float v) {
        transformation.setScaleX(v);
        return this;
    }

    public RenderData setScaleY(float v) {
        transformation.setScaleY(v);
        return this;
    }

    public RenderData setScaleZ(float v) {
        transformation.setScaleZ(v);
        return this;
    }

    public void applyTransformation(MatrixStack matrixStack) {
        transformation.apply(matrixStack);
    }
}
