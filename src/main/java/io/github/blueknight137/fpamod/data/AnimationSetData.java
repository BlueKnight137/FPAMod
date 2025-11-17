package io.github.blueknight137.fpamod.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnimationSetData {
    @JsonProperty
    String version;
    @JsonProperty
    public List<String> items = new ArrayList<>();
    public List<AnimationSetEntry> animations;

    public static class AnimationSetEntry {
        @JsonProperty
        public String type;
        @JsonProperty
        public String animation;
    }
}
