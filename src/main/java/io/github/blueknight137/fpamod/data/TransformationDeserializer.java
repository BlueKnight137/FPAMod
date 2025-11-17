package io.github.blueknight137.fpamod.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.github.blueknight137.fpamod.render.Transformation;
import org.joml.Vector3f;

import java.io.IOException;

public class TransformationDeserializer extends JsonDeserializer<Transformation> {

    @Override
    public Transformation deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        TransformationData data = jsonParser.readValueAs(TransformationData.class);
        Vector3f translation = arrayToVector(data.translation);
        Vector3f rotation = arrayToVector(data.rotation);
        Vector3f scale = arrayToVector(data.scale);

        return new Transformation(translation, rotation, scale);
    }

    private Vector3f arrayToVector(float[] array) {
        return new Vector3f(array[0], array[1], array[2]);
    }

    private static class TransformationData {
        @JsonProperty
        public float[] translation = new float[]{0, 0, 0};

        @JsonProperty
        public float[] rotation = new float[]{0, 0, 0};

        @JsonProperty
        public float[] scale = new float[]{1, 1, 1};
    }
}
