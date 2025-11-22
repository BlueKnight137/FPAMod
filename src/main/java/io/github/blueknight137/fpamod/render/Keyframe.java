package io.github.blueknight137.fpamod.render;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joml.Vector3f;

import java.util.Set;

public class Keyframe {
    public final Transformation transformation;
    public final float timestamp;
    public final Set<String> tags;

    @JsonIgnore
    public Keyframe next;

    // default constructor is required for deserialization
    public Keyframe() {
        this(new Transformation(), 0f);
    }

    public Keyframe(Transformation transformation, float timestamp, String... tags) {
        this.transformation = transformation;
        this.timestamp = timestamp;
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

    public float getTimestamp() {
        return timestamp;
    }

    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }
}
