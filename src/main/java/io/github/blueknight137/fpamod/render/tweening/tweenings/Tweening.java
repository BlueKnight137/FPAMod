package io.github.blueknight137.fpamod.render.tweening.tweenings;

import io.github.blueknight137.fpamod.FPAMod;
import io.github.blueknight137.fpamod.render.Keyframe;
import io.github.blueknight137.fpamod.render.RenderData;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines what to do between the defined keyframes.
 * <br/><br/>
 * It can apply to only certain parts of the keyframes like position or rotation and even
 * individual coordinates (defined in its subtypes).
 * <br/><br/>
 * Creating a new Tweening is simple. Extend this abstract class then register a
 * {@link TweeningSelector} that construct the Tweening into {@link FPAMod#tweeningSelectorClientRegistry}.
 * Examples of this can be found in {@link TweeningTypes}.
 */
public abstract class Tweening {
    public final Keyframe start;
    public final Keyframe end;
    protected final List<Keyframe> keyframes;

    private void ensureKeyFramesInOrder(Keyframe start, Keyframe end) {
        Keyframe keyFrame = start.next;
        float timeStamp = start.getTimestamp();
        while (keyFrame != null && keyFrame != end) {
            keyFrame = keyFrame.next;
            if(keyFrame != null) {
                if(timeStamp > keyFrame.getTimestamp()) {
                    throw new IllegalArgumentException("KeyFrame timestamps are not aligned!");
                }
                timeStamp = keyFrame.getTimestamp();
            }
        }
        if(keyFrame == null) {
            throw new IllegalArgumentException("Start and end keyframes are not in order!");
        }
    }

    protected Tweening(Keyframe start, Keyframe end) {
        ensureKeyFramesInOrder(start, end);
        this.start = start;
        this.end = end;
        keyframes = new ArrayList<>();
        Keyframe keyFrame = start;
        while (keyFrame != end.next) {
            keyframes.add(keyFrame);
            keyFrame = keyFrame.next;
        }
    }


    public boolean isApplicable(float progress) {
        return start.getTimestamp() <= progress && progress <= end.getTimestamp();
    }

    public abstract void applyTransition(RenderData data, Keyframe lastKeyframe, float progress);
}
