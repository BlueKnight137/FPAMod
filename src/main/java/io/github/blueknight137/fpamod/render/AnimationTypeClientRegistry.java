package io.github.blueknight137.fpamod.render;

import io.github.blueknight137.fpamod.ClientRegistry;

import java.util.*;

public class AnimationTypeClientRegistry extends ClientRegistry<AnimationType> {

    private final TreeSet<AnimationType> orderedAnimationTypes = new TreeSet<>(
            (o1, o2) -> {
                if(o1.priority == o2.priority) {
                    return o1.identifier.compareTo(o2.identifier);
                }
                return Integer.compare(o2.priority, o1.priority);
            }
    );

    @Override
    public AnimationType register(AnimationType object) {
        super.register(object);
        orderedAnimationTypes.add(object);
        return object;
    }

    public Iterator<AnimationType> getIterableAnimationTypes() {
        return orderedAnimationTypes.iterator();
    }
}
