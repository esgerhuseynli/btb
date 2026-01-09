package az.btb.mobilebanking.di.modules;

import az.btb.mobilebanking.api.AuthService;
import retrofit2.Retrofit;
import toothpick.config.Module;

public class RetrofitServiceModule extends Module {

    public RetrofitServiceModule(Retrofit retrofit){
        bind(AuthService.class).toInstance(retrofit.create(AuthService.class));
    }

}
