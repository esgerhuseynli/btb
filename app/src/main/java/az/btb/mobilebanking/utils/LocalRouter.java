package az.btb.mobilebanking.utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.Router;

public class LocalRouter {

    private final Map<String, Cicerone<Router>> containers;

    @Inject
    public LocalRouter() {
        containers = new HashMap<>();
    }

    public Cicerone<Router> getCicerone(String containerTag) {
        if (!containers.containsKey(containerTag)) {
            containers.put(containerTag, Cicerone.create());
        }
        return containers.get(containerTag);
    }
}
