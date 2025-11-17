package io.github.blueknight137.fpamod.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class FPAClientPlayerEvents {

    public interface Swing {
        Event<Swing> EVENT = EventFactory.createArrayBacked(Swing.class,
                (listeners) -> (player, hand) -> {
                    for (var listener : listeners) {
                        listener.swing(player, hand);
                    }
                });

        void swing(ClientPlayerEntity player, Hand hand);
    }

    public interface Attack {
        Event<Attack> EVENT = EventFactory.createArrayBacked(Attack.class,
                (listeners) -> (player, target) -> {
                    for (var listener : listeners) {
                        listener.attack(player, target);
                    }
                });

        void attack(PlayerEntity player, Entity target);
    }

    public interface Mine {
        Event<Mine> EVENT = EventFactory.createArrayBacked(Mine.class,
                (listeners) -> () -> {
                    for (var listener : listeners) {
                        listener.mine();
                    }
                });

        void mine();
    }

    public interface InteractBlock {
        Event<InteractBlock> EVENT = EventFactory.createArrayBacked(InteractBlock.class,
                (listeners) -> hand -> {
                    for (var listener : listeners) {
                        listener.interactBlock(hand);
                    }
                });

        void interactBlock(Hand hand);
    }

    public interface InteractItem {
        Event<InteractItem> EVENT = EventFactory.createArrayBacked(InteractItem.class,
                (listeners) -> hand -> {
                    for (var listener : listeners) {
                        listener.interactItem(hand);
                    }
                });

        void interactItem(Hand hand);
    }
}
