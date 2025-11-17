package io.github.blueknight137.fpamod.render;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil.Type;
import org.lwjgl.glfw.GLFW;

public class ItemInspectManager {

    private final KeyBinding inspectItem;
    private float inspectTicks;
    private int inspectLength;
    private boolean inspecting = false;

    public ItemInspectManager() {
        inspectItem =
            KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.fpamod.inspectItem",
                    Type.KEYSYM,
                    GLFW.GLFW_KEY_Y,
                    "category.fpamod.fpamod"
            ));
        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            if(inspectItem.wasPressed()) {
                startInspect(120);
            }
        });
        ClientTickEvents.START_CLIENT_TICK.register(mc -> maybeAddInspectionTick());
    }

    public void startInspect(int length) {
        inspectTicks = 0;
        inspectLength = length;
        inspecting = true;
    }

    public void endInspect() {
        inspecting = false;
    }

    public void maybeAddInspectionTick() {
        if(!inspecting) return;
        inspectTicks += 1;
        if(inspectTicks > inspectLength) {
            inspecting = false;
        }
    }

    public boolean isInspecting() {
        return inspecting;
    }

    public float getInspectProgress(float partialTick) {
        return Math.min((inspectTicks + partialTick) / inspectLength, 1f);
    }
}
