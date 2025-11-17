package io.github.blueknight137.fpamod.render;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joml.Vector3f;

import java.util.Set;

public class KeyFrame {
    public final Transformation transformation;
    public final float timeStamp;
    public final Set<String> tags;

    @JsonIgnore
    public KeyFrame next;

    // default constructor is required for deserialization
    public KeyFrame() {
        this(new Transformation(), 0f);
    }

    public KeyFrame(Transformation transformation, float timeStamp, String... tags) {
        this.transformation = transformation;
        this.timeStamp = timeStamp;
        this.tags = Set.of(tags);
    }

    public Transformation getTransformation() {
        return transformation;
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

    public RenderData getRenderData() {
        return new RenderData(transformation);
    }

    public float getTimeStamp() {
        return timeStamp;
    }

    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }
}
