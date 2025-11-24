package io.github.blueknight137.fpamod.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.github.blueknight137.fpamod.FPAMod;
import io.github.blueknight137.fpamod.render.Animation;
import io.github.blueknight137.fpamod.render.Animation.Builder;
import io.github.blueknight137.fpamod.render.Keyframe;
import io.github.blueknight137.fpamod.render.Transformation;
import io.github.blueknight137.fpamod.render.tweening.tweeners.TweenerType;

import java.io.IOException;
import java.util.List;

public class AnimationDeserializer extends JsonDeserializer<Animation> {

    @Override
    public Animation deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        Animation.Builder builder = new Builder();

        try {
            AnimationData data = jsonParser.readValueAs(AnimationData.class);

            builder.setInitialTransformation(data.initialTransformation);

            for (var keyFrame : data.keyframes) {
                builder.addKeyFrame(keyFrame);
            }

            for (var tweeningData : data.tweenings) {
                TweenerType tweenerType = FPAMod.tweenerTypeClientRegistry.get(tweeningData.tweener);
                for(var selector : tweeningData.selectors) {
                    FPAMod.tweeningSelectorClientRegistry.get(selector).buildInto(builder, tweeningData.startTag, tweeningData.endTag, tweenerType, tweeningData.arguments);
                }
            }
        } catch (Exception e) {
            FPAMod.LOGGER.error("Could not deserialize animation! An animation defined in one of the resourcepacks is invalid or could not be loaded.");
            FPAMod.LOGGER.debug("Deserialization error!", e);
            throw new IOException(e.getMessage());
        }

        return builder.build();
    }

    private static class AnimationData {
        @JsonProperty
        String version;
        @JsonProperty
        Transformation initialTransformation = new Transformation();
        @JsonProperty
        List<Keyframe> keyframes;
        @JsonProperty
        List<TweeningData> tweenings;
    }

    private static class TweeningData {
        @JsonProperty
        public List<String> selectors;
        @JsonProperty
        public String tweener;
        @JsonProperty
        public String startTag;
        @JsonProperty
        public String endTag;
        @JsonProperty
        public List<Float> arguments;
    }

}
