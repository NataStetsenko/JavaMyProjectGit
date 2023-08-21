package step.learning.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 * Модуль конфігурації інжектора - тут зазначаються співвідношення
 * інтерфейсів та їх реалізацій, а також інші засоби постачання
 */
public class ConfigModule extends AbstractModule {
    @Override
    protected void configure() {
        // зв'язування за типом
        bind( GreetingService.class ).to( HiService.class ) ;

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
    }
}
