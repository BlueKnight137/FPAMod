package io.github.blueknight137.fpamod.render.tweening.tweenings;

import io.github.blueknight137.fpamod.ClientRegistry;
import io.github.blueknight137.fpamod.FPAMod;
import io.github.blueknight137.fpamod.render.RenderData;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;

public class TweeningTypes {

    public static void registerTweeningTypes(ClientRegistry<TweeningSelector> clientRegistry) {
        clientRegistry.register(new TweeningSelector(
                Identifier.of(FPAMod.MODID, "full"),
                (tweenerType, arguments) -> (start, end) -> new FullTweening(start, end, arguments, tweenerType)
        ));
        clientRegistry.register(new TweeningSelector(
                Identifier.of(FPAMod.MODID, "translation"),
                (tweenerType, arguments) -> (start, end) ->
                        new TriValueTweening(
                                start,
                                end,
                                arguments,
                                tweenerType,
                                RenderData::getPosition,
                                RenderData::setPosition
                        )
        ));
        clientRegistry.register(new TweeningSelector(
                Identifier.of(FPAMod.MODID, "rotation"),
                (tweenerType, arguments) -> (start, end) ->
                        new TriValueTweening(
                                start,
                                end,
                                arguments,
                                tweenerType,
                                RenderData::getRotation,
                                RenderData::setRotation
                        )
        ));
        clientRegistry.register(new TweeningSelector(
                Identifier.of(FPAMod.MODID, "scale"),
                (tweenerType, arguments) -> (start, end) ->
                        new TriValueTweening(
                                start,
                                end,
                                arguments,
                                tweenerType,
                                RenderData::getScale,
                                RenderData::setScale
                        )
        ));
        clientRegistry.register(new TweeningSelector(
                Identifier.of(FPAMod.MODID, "x"),
                (tweenerType, arguments) -> (start, end) ->
                        new ValueTweening(
                                start,
                                end,
                                arguments,
                                tweenerType,
                                RenderData::getPositionX,
                                RenderData::setPositionX
                        )
        ));
        clientRegistry.register(new TweeningSelector(
                Identifier.of(FPAMod.MODID, "y"),
                (tweenerType, arguments) -> (start, end) ->
                        new ValueTweening(
                                start,
                                end,
                                arguments,
                                tweenerType,
                                RenderData::getPositionY,
                                RenderData::setPositionY
                        )
        ));
        clientRegistry.register(new TweeningSelector(
                Identifier.of(FPAMod.MODID, "z"),
                (tweenerType, arguments) -> (start, end) ->
                        new ValueTweening(
                                start,
                                end,
                                arguments,
                                tweenerType,
                                RenderData::getPositionZ,
                                RenderData::setPositionZ
                        )
        ));
        clientRegistry.register(new TweeningSelector(
                Identifier.of(FPAMod.MODID, "yaw"),
                (tweenerType, arguments) -> (start, end) ->
                        new ValueTweening(
                                start,
                                end,
                                arguments,
                                tweenerType,
                                RenderData::getYaw,
                                RenderData::setYaw
                        )
        ));
        clientRegistry.register(new TweeningSelector(
                Identifier.of(FPAMod.MODID, "pitch"),
                (tweenerType, arguments) -> (start, end) ->
                        new ValueTweening(
                                start,
                                end,
                                arguments,
                                tweenerType,
                                RenderData::getPitch,
                                RenderData::setPitch
                        )
        ));
        clientRegistry.register(new TweeningSelector(
                Identifier.of(FPAMod.MODID, "roll"),
                (tweenerType, arguments) -> (start, end) ->
                        new ValueTweening(
                                start,
                                end,
                                arguments,
                                tweenerType,
                                RenderData::getRoll,
                                RenderData::setRoll
                        )
        ));
        clientRegistry.register(new TweeningSelector(
                Identifier.of(FPAMod.MODID, "scale_x"),
                (tweenerType, arguments) -> (start, end) ->
                        new ValueTweening(
                                start,
                                end,
                                arguments,
                                tweenerType,
                                RenderData::getScaleX,
                                RenderData::setScaleX
                        )
        ));
        clientRegistry.register(new TweeningSelector(
                Identifier.of(FPAMod.MODID, "scale_y"),
                (tweenerType, arguments) -> (start, end) ->
                        new ValueTweening(
                                start,
                                end,
                                arguments,
                                tweenerType,
                                RenderData::getScaleY,
                                RenderData::setScaleY
                        )
        ));
        clientRegistry.register(new TweeningSelector(
                Identifier.of(FPAMod.MODID, "scale_z"),
                (tweenerType, arguments) -> (start, end) ->
                        new ValueTweening(
                                start,
                                end,
                                arguments,
                                tweenerType,
                                RenderData::getScaleZ,
                                RenderData::setScaleZ
                        )
        ));
        clientRegistry.register(new TweeningSelector(
                Identifier.of(FPAMod.MODID, "xy"),
                (tweenerType, arguments) -> (start, end) ->
                        new BiValueTweening(
                                start,
                                end,
                                arguments,
                                tweenerType,
                                data -> new Vector2f(data.getPositionX(), data.getPositionY()),
                                (data, vector) -> data.setPositionX(vector.x).setPositionY(vector.y)
                        )
        ));
        clientRegistry.register(new TweeningSelector(
                Identifier.of(FPAMod.MODID, "yz"),
                (tweenerType, arguments) -> (start, end) ->
                        new BiValueTweening(
                                start,
                                end,
                                arguments,
                                tweenerType,
                                data -> new Vector2f(data.getPositionY(), data.getPositionZ()),
                                (data, vector) -> data.setPositionY(vector.x).setPositionZ(vector.y)
                        )
        ));
        clientRegistry.register(new TweeningSelector(
                Identifier.of(FPAMod.MODID, "xz"),
                (tweenerType, arguments) -> (start, end) ->
                        new BiValueTweening(
                                start,
                                end,
                                arguments,
                                tweenerType,
                                data -> new Vector2f(data.getPositionX(), data.getPositionZ()),
                                (data, vector) -> data.setPositionX(vector.x).setPositionZ(vector.y)
                        )
        ));
    }
}
