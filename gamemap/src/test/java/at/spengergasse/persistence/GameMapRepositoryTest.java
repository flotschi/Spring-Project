package at.spengergasse.persistence;

import at.spengergasse.Application;
import at.spengergasse.config.PersistenceConfig;
import at.spengergasse.domain.BaseDomain;
import at.spengergasse.domain.GameMap;
import at.spengergasse.domain.GameMode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static at.spengergasse.domain.Size.SMALL;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = { Application.class, PersistenceConfig.class })
public class GameMapRepositoryTest {

    @Autowired
    private GameMapRepository gameMapRepository;

    private GameMap map;


    @Before
    public void setup() {

        map = GameMap.builder()
            .name( "DE Dust" )
            .size( SMALL )
            .gameMode( GameMode.DEATH_MATCH )
            .price( 10 )
            .build();
    }


    @Test
    public void ensure_transient_persistent_of_user() {

        // WHEN
            // map saved to db
        gameMapRepository.save( map );

        // THEN
            // map persisted?
        assertDomain( map );
    }


    private static <T extends BaseDomain> void assertDomain( T model ) {
        assertThat( model ).isNotNull();
        assertThat( model.getId() ).isNotNull();
        assertThat( model.getVersion() ).isNotNull();
        assertThat( model.getCreatedAt() ).isNotNull();
    }
}
