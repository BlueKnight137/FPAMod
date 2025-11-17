package io.github.blueknight137.fpamod.render;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.blueknight137.fpamod.FPAMod;
import io.github.blueknight137.fpamod.data.AnimationSetData;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

public class AnimationResourceManager implements SimpleSynchronousResourceReloadListener {

    private final Map<Identifier, Animation> animationIDMap = new HashMap<>();
    private final Map<Item, AnimationSet> animationSetMap = new HashMap<>();
    // item tags are not registered when the resource manager loads, so we have to resolve them later
    private final Queue<Entry<String, AnimationSet>> itemTagResolutionQueue = new LinkedList<>();
    private static final String ANIMATIONS_PATH = "animations";
    private static final String ANIMATION_SETS_PATH = "animation_sets";

    public AnimationResourceManager() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(this);
        // we resolve the item tags when we change worlds
        ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((client, world) -> this.resolveItemTags());
    }

    public AnimationSet getAnimationForItem(Item item) {
        return animationSetMap.get(item);
    }

    public void clear() {
        animationIDMap.clear();
        animationSetMap.clear();
        itemTagResolutionQueue.clear();
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.of(FPAMod.MODID, "animation_resources");
    }

    private void reloadAnimations(ResourceManager manager, ObjectMapper mapper) {
        FPAMod.LOGGER.info("Reloading animations");
        for(var id : manager.findResources(ANIMATIONS_PATH, path -> path.toString().endsWith("json")).keySet()) {
            try(InputStream stream = manager.getResource(id).orElseThrow().getInputStream()) {
                Animation data = mapper.readValue(stream, Animation.class);
                animationIDMap.put(id, data);
            } catch (IOException e) {
                FPAMod.LOGGER.error("Could not load animation with id [{}]! It is potentially invalid!", id);
                FPAMod.LOGGER.debug("Animation error!", e);
            }
        }
    }

    private void reloadAnimationSets(ResourceManager manager, ObjectMapper mapper) {
        FPAMod.LOGGER.info("Reloading animation sets");
        for(var id : manager.findResources(ANIMATION_SETS_PATH, path -> path.toString().endsWith("json")).keySet()) {
            try(InputStream stream = manager.getResource(id).orElseThrow().getInputStream()) {
                AnimationSetData data = mapper.readValue(stream, AnimationSetData.class);
                AnimationSet animationSet = new AnimationSet();
                for(var entry : data.animations) {
                    Identifier suffixedID = Identifier.of(entry.animation).withSuffixedPath(".json");
                    if(!animationIDMap.containsKey(suffixedID)) {
                        FPAMod.LOGGER.warn("Could not find animation with id [{}] needed to construct animation set with id [{}]!", entry.animation, id.toString());
                        continue;
                    }
                    animationSet.upsertAnimation(
                            FPAMod.animationTypeClientRegistry.get(Identifier.of(entry.type)),
                            animationIDMap.get(suffixedID)
                    );
                }
                for(String itemID : data.items) {
                    if(itemID.startsWith("#")) {
                        itemID = itemID.substring(1);
                        itemTagResolutionQueue.add(Map.entry(itemID, animationSet));
                        return;
                    }
                    Item item = Registries.ITEM.get(Identifier.of(itemID));
                    animationSetMap.put(item, animationSet);
                }
            } catch (IOException e) {
                FPAMod.LOGGER.error("Could not load animation set with id [{}]! It is potentially invalid!", id);
                FPAMod.LOGGER.debug("Animation set error!", e);
            }
        }
    }

    private void resolveItemTags() {
        while(!itemTagResolutionQueue.isEmpty()) {
            Entry<String, AnimationSet> entry = itemTagResolutionQueue.poll();
            // fixme there must be a better way to do this
            Registries.ITEM.stream()
                    .filter(item -> item.getRegistryEntry().streamTags().anyMatch(tag -> tag.id().toString().equals(entry.getKey())))
                    .forEach(item -> animationSetMap.put(item, entry.getValue()));
        }
    }

    @Override
    public void reload(ResourceManager manager) {
        FPAMod.LOGGER.info("Reloading AnimationResourceManager");
        clear();
        ObjectMapper mapper = new ObjectMapper();
        reloadAnimations(manager, mapper);
        reloadAnimationSets(manager, mapper);
        // if we are not in a world we should not call this since the reload was probably called from the initial loading
        // reloading the resourcepacks from the world should allow the resolution to happen
        if(MinecraftClient.getInstance().world != null) resolveItemTags();
    }
}
