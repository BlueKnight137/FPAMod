package io.github.blueknight137.fpamod.render.tweening.tweeners;

import io.github.blueknight137.fpamod.FPAMod;
import io.github.blueknight137.fpamod.render.tweening.tweenings.Tweening;

/**
 * Defines the behavior of a {@link Tweening}.
 * <br/><br/>
 * Creating a new Tweener is simple. Extend this abstract class then register a
 * {@link TweenerType} that construct the Tweener into {@link FPAMod#tweenerTypeClientRegistry}.
 * Examples of this can be found in {@link TweenerTypes}.
 */
public interface Tweener {

    float tween(float progress);
}
