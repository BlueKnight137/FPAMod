package io.github.blueknight137.fpamod.render;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.blueknight137.fpamod.data.TransformationDeserializer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@JsonDeserialize(using = TransformationDeserializer.class)
public class Transformation {
    private Vector3f position;

    // represented as yaw pitch roll
    private Vector3f rotation;

    private Vector3f scale;

    public Transformation(Vector3f position, Vector3f rotation) {
        this(position, rotation, new Vector3f(1));
    }

    public Transformation(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Transformation() {
        this(new Vector3f(), new Vector3f(), new Vector3f(1));
    }

    public void apply(MatrixStack matrixStack) {
        matrixStack.translate(position.x, position.y, position.z);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation.x));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotation.y));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation.z));
        matrixStack.scale(scale.x, scale.y, scale.z);
    }

    public void applyMCStyle(MatrixStack matrixStack) {
        matrixStack.translate(position.x, position.y, position.z);
        matrixStack.multiply(new Quaternionf().rotationXYZ((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y), (float) Math.toRadians(rotation.z)));
        matrixStack.scale(scale.x, scale.y, scale.z);
    }

    public Vector3f getPosition() {
        return new Vector3f(position);
    }

    public Vector3f getRotation() {
        return new Vector3f(rotation);
    }

    public Vector3f getScale() {
        return new Vector3f(scale);
    }

    public float getPositionX() {
        return position.x;
    }

    public float getPositionY() {
        return position.y;
    }

    public float getPositionZ() {
        return position.z;
    }

    public float getYaw() {
        return rotation.x;
    }

    public float getPitch() {
        return rotation.y;
    }

    public float getRoll() {
        return rotation.z;
    }

    public float getScaleX() {
        return scale.x;
    }

    public float getScaleY() {
        return scale.y;
    }

    public float getScaleZ() {
        return scale.z;
    }

    public Transformation setPosition(Vector3f position) {
        this.position = position;
        return this;
    }

    public Transformation setRotation(Vector3f rotation) {
        this.rotation = rotation;
        return this;
    }

    public Transformation setScale(Vector3f scale) {
        this.scale = scale;
        return this;
    }

    public Transformation setPositionX(float v) {
        this.position.x = v;
        return this;
    }

    public Transformation setPositionY(float v) {
        this.position.y = v;
        return this;
    }

    public Transformation setPositionZ(float v) {
        this.position.z = v;
        return this;
    }

    public Transformation setYaw(float v) {
        this.rotation.x = v;
        return this;
    }

    public Transformation setPitch(float v) {
        this.rotation.y = v;
        return this;
    }

    public Transformation setRoll(float v) {
        this.rotation.z = v;
        return this;
    }

    public Transformation setScaleX(float v) {
        this.scale.x = v;
        return this;
    }

    public Transformation setScaleY(float v) {
        this.scale.y = v;
        return this;
    }

    public Transformation setScaleZ(float v) {
        this.scale.z = v;
        return this;
    }
}
