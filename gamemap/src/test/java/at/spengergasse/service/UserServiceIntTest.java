package at.spengergasse.service;

import at.spengergasse.domain.GameMap;
import at.spengergasse.domain.GameMode;
import at.spengergasse.domain.User;
import at.spengergasse.service.command.AddMapCommand;
import at.spengergasse.service.command.UpdateMapCommand;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;

import static at.spengergasse.domain.Size.LARGE;
import static at.spengergasse.domain.Size.SMALL;
import static org.assertj.core.api.Assertions.assertThat;


// TODO How to avoid loading WebConfig ?
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceIntTest {

    @Autowired
    private UserService userService;

    private User user;
    private GameMap map;


    @Before
    public void setup() {

        System.out.println( "SETUP --------" );

        map = GameMap.builder()
            .name( "DE Dust" )
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

        System.out.println( "SETUP END --------" );
    }


    @After
    public void teardown() {
        userService.deleteUsers();
    }


    @Test
    public void ensure_saving_and_retrieving_of_a_user_works() {

        // GIVEN
            // user with a map
        user.addMaps( map );
           // saved to db
        userService.saveUser( user );


        // WHEN
            // retrieve user back
        User retrievedUser = userService.findUser_withEagerFetchingMaps( user.getId() );


        // THEN
            // retrieved & saved users are equal
        assertUser( retrievedUser, user );
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
