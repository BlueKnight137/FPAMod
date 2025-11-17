package io.github.blueknight137.fpamod;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ClientRegistry<T extends Identifiable> {

    private final Map<Identifier, T> objectIDMap = new HashMap<>();

    public T register(T object) {
        objectIDMap.put(object.getIdentifier(), object);
        return object;
    }

    public T get(Identifier id) {
        if(!objectIDMap.containsKey(id)) {
            FPAMod.LOGGER.error("Attempted to access object with id [{}] in a client registry that is not present!", id);
            return null;
        }
        return objectIDMap.get(id);
    }

    public T get(String idStr) {
        return get(Identifier.of(idStr));
    }
}
