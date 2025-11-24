package io.github.blueknight137.fpamod.render.tweening.tweenings;

import io.github.blueknight137.fpamod.Identifiable;
import io.github.blueknight137.fpamod.render.Animation.Builder;
import io.github.blueknight137.fpamod.render.Keyframe;
import io.github.blueknight137.fpamod.render.tweening.tweeners.TweenerType;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.BiFunction;

public class TweeningSelector implements Identifiable {
    public final Identifier id;
    private final BiFunction<TweenerType, List<Float>, BiFunction<Keyframe, Keyframe, Tweening>> tweeningFactory;

    public TweeningSelector(Identifier id, BiFunction<TweenerType, List<Float>, BiFunction<Keyframe, Keyframe, Tweening>> tweeningFactory) {
        this.id = id;
        this.tweeningFactory = tweeningFactory;
    }

    public void buildInto(Builder builder, String startTag, String endTag, TweenerType tweenerType, List<Float> arguments) {
        builder.addTweening(tweeningFactory.apply(tweenerType, arguments), startTag, endTag);
    }

    @Override
    public Identifier getIdentifier() {
        return id;
    }
}
