package io.github.blueknight137.fpamod.render.tweening.tweeners;

import io.github.blueknight137.fpamod.ClientRegistry;
import io.github.blueknight137.fpamod.FPAMod;
import net.minecraft.util.Identifier;

public class TweenerTypes {

    public static TweenerType STATIC;

    public static void registerTweenerTypes(ClientRegistry<TweenerType> clientRegistry) {
        STATIC = clientRegistry.register(new TweenerType(
                Identifier.of(FPAMod.MODID, "static"),
                arguments -> StaticTweener::new
        ));
        clientRegistry.register(new TweenerType(
                Identifier.of(FPAMod.MODID, "linear"),
                arguments -> LinearTweener::new
        ));
        clientRegistry.register(new TweenerType(
                Identifier.of(FPAMod.MODID, "quadratic"),
                arguments -> (timeStamps, values) -> {
                    float startGradient = arguments.getFirst();
                    return new QuadraticTweener(timeStamps, values, startGradient);
                }
        ));
        clientRegistry.register(new TweenerType(
                Identifier.of(FPAMod.MODID, "clamped_cubic"),
                arguments -> (timeStamps, values) -> {
                    float startGradient = arguments.getFirst();
                    float endGradient = arguments.get(1);
                    return new ClampedCubicTweener(timeStamps, values, startGradient, endGradient);
                }
        ));
        clientRegistry.register(new TweenerType(
                Identifier.of(FPAMod.MODID, "natural_cubic"),
                arguments -> NaturalCubicTweener::new
        ));
    }
}
