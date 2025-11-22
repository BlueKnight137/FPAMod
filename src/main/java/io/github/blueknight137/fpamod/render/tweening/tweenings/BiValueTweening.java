package io.github.blueknight137.fpamod.render.tweening.tweenings;

import io.github.blueknight137.fpamod.render.Keyframe;
import io.github.blueknight137.fpamod.render.RenderData;
import io.github.blueknight137.fpamod.render.tweening.tweeners.Tweener;
import io.github.blueknight137.fpamod.render.tweening.tweeners.TweenerType;
import org.joml.Vector2f;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BiValueTweening extends Tweening {

    private final BiConsumer<RenderData, Vector2f> renderDataModifier;
    private final Tweener tweener1;
    private final Tweener tweener2;

    protected BiValueTweening(
            Keyframe start,
            Keyframe end,
            List<Float> arguments,
            TweenerType tweenerType,
            Function<RenderData, Vector2f> valueExtractor,
            BiConsumer<RenderData, Vector2f> renderDataModifier
    ) {
        super(start, end);
        this.renderDataModifier = renderDataModifier;
        List<Float> timeStamps = keyframes.stream().map(Keyframe::getTimestamp).toList();
        List<Vector2f> values = keyframes.stream().map(Keyframe::getRenderData).map(valueExtractor).toList();
        tweener1 = tweenerType.constructTweener(
                arguments,
                timeStamps,
                values.stream().map(Vector2f::x).toList()
        );
        tweener2 = tweenerType.constructTweener(
                arguments,
                timeStamps,
                values.stream().map(Vector2f::y).toList()
        );
    }

    @Override
    public void applyTransition(RenderData data, Keyframe lastKeyframe, float progress) {
        renderDataModifier.accept(data, new Vector2f(tweener1.tween(progress), tweener2.tween(progress)));
    }
}
