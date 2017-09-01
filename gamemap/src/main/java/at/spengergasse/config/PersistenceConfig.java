package at.spengergasse.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@Configuration
@EnableJpaAuditing
public class PersistenceConfig {

    // debug
    static { System.out.println( "LOAD: Persistence -------------------" ); }
}
