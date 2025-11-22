package io.github.blueknight137.fpamod.render.tweening.tweenings;

import io.github.blueknight137.fpamod.render.Keyframe;
import io.github.blueknight137.fpamod.render.RenderData;
import io.github.blueknight137.fpamod.render.tweening.tweeners.Tweener;
import io.github.blueknight137.fpamod.render.tweening.tweeners.TweenerType;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ValueTweening extends Tweening {

    private final BiConsumer<RenderData, Float> renderDataModifier;
    private final Tweener tweener;

    protected ValueTweening(
            Keyframe start,
            Keyframe end,
            List<Float> arguments,
            TweenerType tweenerType,
            Function<RenderData, Float> valueExtractor,
            BiConsumer<RenderData, Float> renderDataModifier
    ) {
        super(start, end);
        this.renderDataModifier = renderDataModifier;
        List<Float> timeStamps = keyframes.stream().map(Keyframe::getTimestamp).toList();
        List<Float> values = keyframes.stream().map(Keyframe::getRenderData).map(valueExtractor).toList();
        tweener = tweenerType.constructTweener(
                arguments,
                timeStamps,
                values
        );
    }

    @Override
    public void applyTransition(RenderData data, Keyframe lastKeyframe, float progress) {
        renderDataModifier.accept(data, tweener.tween(progress));
    }
}
