package io.github.blueknight137.fpamod.render.tweening.tweenings;

import io.github.blueknight137.fpamod.render.KeyFrame;
import io.github.blueknight137.fpamod.render.RenderData;
import io.github.blueknight137.fpamod.render.tweening.tweeners.TweenerType;
import io.github.blueknight137.fpamod.render.tweening.tweeners.TweenerVector3;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class TriValueTweening extends Tweening {

    private final BiConsumer<RenderData, Vector3f> renderDataModifier;
    private final TweenerVector3 tweener;

    protected TriValueTweening(
            KeyFrame start,
            KeyFrame end,
            List<Float> arguments,
            TweenerType tweenerType,
            Function<RenderData, Vector3f> vectorExtactor,
            BiConsumer<RenderData, Vector3f> renderDataModifier
    ) {
        super(start, end);
        this.renderDataModifier = renderDataModifier;
        List<Float> timeStamps = keyFrames.stream().map(KeyFrame::getTimeStamp).toList();
        List<Vector3f> vectors = keyFrames.stream().map(KeyFrame::getRenderData).map(vectorExtactor).toList();
        tweener = new TweenerVector3(tweenerType, arguments, timeStamps, vectors);
    }

    @Override
    public void applyTransition(RenderData data, KeyFrame lastKeyFrame, float progress) {
        renderDataModifier.accept(data, tweener.tween(progress));
    }
}
