package step.learning.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import java.util.Random;

/**
 * Модуль конфігурації інжектора - тут зазначаються співвідношення
 * інтерфейсів та їх реалізацій, а також інші засоби постачання
 */
public class ConfigModule extends AbstractModule {
    @Override
    protected void configure() {
        // зв'язування за типом
        bind( GreetingService.class ).to( HiService.class ) ;
        bind( GreetingService.class )
                .annotatedWith( Names.named( "hello" ) )
                .to( HelloService.class ) ;
        // іменоване зв'язування (кілька реалізацій одного типу)
        bind( PartingService.class )
                .annotatedWith( Names.named( "bye" ) )
                .to( ByeService.class ) ;
        bind( PartingService.class )
                .annotatedWith( Names.named( "goodbye" ) )
                .to( GoodbyeService.class ) ;
        bind(MyHash.class).annotatedWith(Names.named("md5")).to(MD5Hash.class);
        bind(MyHash.class).annotatedWith(Names.named("sha256")).to(SHA256Hash.class);
        bind(MyHash.class).annotatedWith(Names.named("keccak")).to(Keccak256.class);
        bind(MyHash.class).annotatedWith(Names.named("whirlpool")).to(Whirlpool.class);
        bind(String.class).annotatedWith(Names.named("connect")).toInstance("connect");
        bind(String.class).annotatedWith(Names.named("logFile")).toInstance("logFile");
    }
    private Random _random;
    @Provides @Named("java.util")
    Random randomProvides(){
        // return new Random();// Transient
        if(_random==null){
            _random = new Random(); //Singleton
        }
        return _random;
    }

}
