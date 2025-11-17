package io.github.blueknight137.fpamod.render.tweening.tweenings;

import io.github.blueknight137.fpamod.FPAMod;
import io.github.blueknight137.fpamod.render.KeyFrame;
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
 * {@link TweeningType} that construct the Tweening into {@link FPAMod#tweeningTypeClientRegistry}.
 * Examples of this can be found in {@link TweeningTypes}.
 */
public abstract class Tweening {
    public final KeyFrame start;
    public final KeyFrame end;
    protected final List<KeyFrame> keyFrames;

    private void ensureKeyFramesInOrder(KeyFrame start, KeyFrame end) {
        KeyFrame keyFrame = start.next;
        float timeStamp = start.getTimeStamp();
        while (keyFrame != null && keyFrame != end) {
            keyFrame = keyFrame.next;
            if(keyFrame != null) {
                if(timeStamp > keyFrame.getTimeStamp()) {
                    throw new IllegalArgumentException("KeyFrame timestamps are not aligned!");
                }
                timeStamp = keyFrame.getTimeStamp();
            }
        }
        if(keyFrame == null) {
            throw new IllegalArgumentException("Start and end keyframes are not in order!");
        }
    }

    protected Tweening(KeyFrame start, KeyFrame end) {
        ensureKeyFramesInOrder(start, end);
        this.start = start;
        this.end = end;
        keyFrames = new ArrayList<>();
        KeyFrame keyFrame = start;
        while (keyFrame != end.next) {
            keyFrames.add(keyFrame);
            keyFrame = keyFrame.next;
        }
    }


    public boolean isApplicable(float progress) {
        return start.getTimeStamp() <= progress && progress <= end.getTimeStamp();
    }

    public abstract void applyTransition(RenderData data, KeyFrame lastKeyFrame, float progress);
}
