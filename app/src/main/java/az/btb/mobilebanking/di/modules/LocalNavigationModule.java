package az.btb.mobilebanking.di.modules;

import az.btb.mobilebanking.utils.LocalRouter;
import toothpick.config.Module;

public class LocalNavigationModule extends Module {
    public LocalNavigationModule() {
        bind(LocalRouter.class).toInstance(new LocalRouter());
    }
}
