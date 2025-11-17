package io.github.blueknight137.fpamod.render.tweening.tweeners;

import io.github.blueknight137.fpamod.Identifiable;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TweenerType implements Identifiable {
    public final Identifier id;
    private final Function<List<Float>, BiFunction<List<Float>, List<Float>, Tweener>> tweenerFactory;

    public TweenerType(Identifier id, Function<List<Float>, BiFunction<List<Float>, List<Float>, Tweener>> tweenerFactory) {
        this.id = id;
        this.tweenerFactory = tweenerFactory;
    }

    public Tweener constructTweener(List<Float> arguments, List<Float> timeStamps, List<Float> values) {
        return tweenerFactory.apply(arguments).apply(timeStamps, values);
    }

    @Override
    public Identifier getIdentifier() {
        return id;
    }
}
