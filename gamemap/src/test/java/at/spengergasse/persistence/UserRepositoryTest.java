package at.spengergasse.persistence;

import at.spengergasse.Application;
import at.spengergasse.config.PersistenceConfig;
import at.spengergasse.domain.BaseDomain;
import at.spengergasse.domain.GameMap;
import at.spengergasse.domain.GameMode;
import at.spengergasse.domain.User;
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
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;
    private GameMap map1, map2;


    @Before
    public void setup() {

        map1 = GameMap.builder()
            .name( "DE Dust" )
            .size( SMALL )
            .gameMode( GameMode.DEATH_MATCH )
            .price( 10 )
            .build();

        map2 = GameMap.builder()
            .name( "GB Dust" )
            .size( SMALL )
            .gameMode( GameMode.DEATH_MATCH )
            .price( 10 )
            .build();

        user = User.builder()
            .nickname( "Max" )
            .password( "12345678" )
            .email( "max@gmx.at" )
            .creditCard( "340000000000009" )
            .realMoney( 10 )
            .gameMoney( 5 )
            .build();
    }


    @Test
    public void ensure_transient_persistent_of_user_works() {

        // GIVEN
            // user with 2 maps
        user.addMaps( map1, map2 );

        // WHEN
            // saved to db
        userRepository.save( user );

        // THEN
          // user is persisted
        assertDomain( user );
          // maps are persisted
        assertThat( user.getMaps() ).isNotNull().hasSize( 2 );
        user.getMaps().forEach( this::assertDomain );
    }


    @Test
    public void ensure_find_existing_user_works() {

        // GIVEN
            // user with 2 maps
        user.addMaps( map1, map2 );
            // saved to db
        userRepository.save( user );

        // WHEN
            // find user just saved to db
        User retrievedUser = userRepository.findOne( user.getId() );

        // THEN
            // retrieved & saved users are equal
        assertUser( retrievedUser, user );
    }


    @Test
    public void ensure_find_non_existing_user_fails() {

        // GIVEN
            // user saved to db
        userRepository.save( user );

        // WHEN
            // find user with non-existing-id
        User retrievedUser = userRepository.findOne( -1L );

        // THEN
            // retrieved user cannot be found
        assertThat( retrievedUser ).isEqualTo( null );
    }


    private <T extends BaseDomain> void assertDomain( T model ) {
        assertThat( model ).isNotNull();
        assertThat( model.getId() ).isNotNull();
        assertThat( model.getVersion() ).isNotNull();
        assertThat( model.getCreatedAt() ).isNotNull();
    }


    private void assertUser( User actualUser, User expectedUser ) {
        assertThat( actualUser.getId() ).isEqualTo( expectedUser.getId() );
        assertThat( actualUser.getVersion() ).isEqualTo( expectedUser.getVersion() );
        assertThat( actualUser.getCreatedAt() ).isEqualTo( expectedUser.getCreatedAt() );
        assertThat( actualUser.getNickname() ).isEqualTo( expectedUser.getNickname() );
        assertThat( actualUser.getPassword() ).isEqualTo( expectedUser.getPassword() );
        assertThat( actualUser.getEmail() ).isEqualTo( expectedUser.getEmail() );
        assertThat( actualUser.getCreditCard() ).isEqualTo( expectedUser.getCreditCard() );
        assertThat( actualUser.getRealMoney() ).isEqualTo( expectedUser.getRealMoney() );
        assertThat( actualUser.getGameMoney() ).isEqualTo( expectedUser.getGameMoney() );
        assertThat( actualUser.getMaps().size() ).isEqualTo( expectedUser.getMaps().size() );
        assertThat( actualUser.getMaps() ).containsExactlyElementsOf( expectedUser.getMaps() );
    }
}
