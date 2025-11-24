package io.github.blueknight137.fpamod;

import io.github.blueknight137.fpamod.render.*;
import io.github.blueknight137.fpamod.render.tweening.tweeners.TweenerType;
import io.github.blueknight137.fpamod.render.tweening.tweeners.TweenerTypes;
import io.github.blueknight137.fpamod.render.tweening.tweenings.TweeningSelector;
import io.github.blueknight137.fpamod.render.tweening.tweenings.TweeningTypes;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FPAMod implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("FPAMod");
    public static final String MODID = "fpamod";
    private static AnimationManager animationManager;
    public static ItemInspectManager itemInspectManager;
    private static AnimationResourceManager animationResourceManager;
    public static ClientRegistry<TweeningSelector> tweeningSelectorClientRegistry;
    public static ClientRegistry<TweenerType> tweenerTypeClientRegistry;
    public static AnimationTypeClientRegistry animationTypeClientRegistry;

    @Override
    public void onInitializeClient() {
        animationTypeClientRegistry = new AnimationTypeClientRegistry();
        tweeningSelectorClientRegistry = new ClientRegistry<>();
        tweenerTypeClientRegistry = new ClientRegistry<>();
        TweeningTypes.registerTweeningTypes(tweeningSelectorClientRegistry);
        TweenerTypes.registerTweenerTypes(tweenerTypeClientRegistry);
        AnimationTypes.registerAnimationTypes();
        animationResourceManager = new AnimationResourceManager();
        itemInspectManager = new ItemInspectManager();
        animationManager = new AnimationManager(itemInspectManager, animationResourceManager, animationTypeClientRegistry);
    }
}
