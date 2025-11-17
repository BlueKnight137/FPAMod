package io.github.blueknight137.fpamod.render.tweening.tweenings;

import io.github.blueknight137.fpamod.render.KeyFrame;
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
            KeyFrame start,
            KeyFrame end,
            List<Float> arguments,
            TweenerType tweenerType,
            Function<RenderData, Float> valueExtractor,
            BiConsumer<RenderData, Float> renderDataModifier
    ) {
        super(start, end);
        this.renderDataModifier = renderDataModifier;
        List<Float> timeStamps = keyFrames.stream().map(KeyFrame::getTimeStamp).toList();
        List<Float> values = keyFrames.stream().map(KeyFrame::getRenderData).map(valueExtractor).toList();
        tweener = tweenerType.constructTweener(
                arguments,
                timeStamps,
                values
        );
    }

    @Override
    public void applyTransition(RenderData data, KeyFrame lastKeyFrame, float progress) {
        renderDataModifier.accept(data, tweener.tween(progress));
    }
}
