package at.spengergasse.service;

import at.spengergasse.domain.GameMap;
import at.spengergasse.domain.GameMode;
import at.spengergasse.domain.User;
import at.spengergasse.service.command.AddMapCommand;
import at.spengergasse.service.command.UpdateMapCommand;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static at.spengergasse.domain.Size.LARGE;
import static at.spengergasse.domain.Size.SMALL;
import static org.assertj.core.api.Assertions.assertThat;


// TODO How to avoid loading WebConfig ?
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class MapServiceIntTest {

    @Autowired
    private UserService userService;

    @Autowired
    private MapService mapService;

    private User user;
    private GameMap map;

    private AddMapCommand addMapCommand;
    private UpdateMapCommand updateMapCommand;


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

        addMapCommand = AddMapCommand.builder()
            .name( "DE Ultra" )
            .size( SMALL )
            .gameMode( GameMode.DEATH_MATCH )
            .price( 10 )
            .build();

        updateMapCommand = UpdateMapCommand.builder()
            .name( "CN Dead" )
            .size( LARGE )
            .gameMode( GameMode.FREE_FOR_ALL )
            .price( 99 )
            .build();

        System.out.println( "SETUP END --------" );
    }


    @After
    public void teardown() {
        userService.deleteUsers();
    }


    @Test
    public void ensure_adding_a_map_to_a_user_works() {

        // GIVEN
            // user with no maps saved to db
        userService.saveUser( user );
            // add map command
        addMapCommand.setUserId( user.getId() );


        // WHEN
            // map added to db
        GameMap addedMap = mapService.addMap( addMapCommand );


        // THEN
            // retrieved user back
        User retrievedUser = userService.findUser_withEagerFetchingMaps( user.getId() );
            // has exactly one map
        assertThat( retrievedUser.getMaps() ).isNotNull().hasSize( 1 );
            // and map
        GameMap retrievedMap = retrievedUser.getMaps().iterator().next();
            // is persisted with correct attributes
        assertThat( retrievedMap.getId() ).isNotNull();
        assertThat( retrievedMap.getVersion() ).isEqualTo( 0 );
        assertThat( retrievedMap.getName() ).isEqualTo( addMapCommand.getName() );
        assertThat( retrievedMap.getSize() ).isEqualTo( addMapCommand.getSize() );
        assertThat( retrievedMap.getGameMode() ).isEqualTo( addMapCommand.getGameMode() );
        assertThat( retrievedMap.getPrice() ).isEqualTo( addMapCommand.getPrice() );
            // and returned map from service equals retrieved map
        assertMap( retrievedMap, addedMap );
    }


    @Test(expected = ServiceException.class)
    public void ensure_adding_a_map_to_a_non_existing_user_fails() {

        // GIVEN
            // user with no maps saved to db
        userService.saveUser( user );
            // add map command but non-existing id
        addMapCommand.setUserId( -1L  );


        // WHEN
            // map saved to db
        mapService.addMap( addMapCommand );


        // THEN
        // throw EntityNotFoundException
    }


    @Test
    public void ensure_updating_a_map_from_a_user_works() {

        // GIVEN
            // user with 1 map
        user.addMaps( map );
            // saved to db
        userService.saveUser( user );
            // update map command
        updateMapCommand.setId( map.getId() );
        updateMapCommand.setVersion( map.getVersion() );


        // WHEN
            // map updated to db
        GameMap updatedMap = mapService.updateMap( updateMapCommand );


        // THEN
            // retrieved user back
        User retrievedUser = userService.findUser_withEagerFetchingMaps( user.getId() );
            // has exactly one map
        assertThat( retrievedUser.getMaps() ).isNotNull().hasSize( 1 );
            // and map
        GameMap retrievedMap = retrievedUser.getMaps().iterator().next();
            // is updated with correct attributes
        assertThat( retrievedMap.getId() ).isNotNull();
        assertThat( retrievedMap.getVersion() ).isEqualTo( 1 );
        assertThat( retrievedMap.getName() ).isEqualTo( updateMapCommand.getName() );
        assertThat( retrievedMap.getSize() ).isEqualTo( updateMapCommand.getSize() );
        assertThat( retrievedMap.getGameMode() ).isEqualTo( updateMapCommand.getGameMode() );
        assertThat( retrievedMap.getPrice() ).isEqualTo( updateMapCommand.getPrice() );
            // and returned map from service equals retrieved map
        assertMap( retrievedMap, updatedMap );
    }


    @Test(expected = ServiceException.class)
    public void ensure_updating_a_map_from_a_non_existing_user_fails() {

        // GIVEN
            // user with 1 map
        user.addMaps( map );
            // saved to db
        userService.saveUser( user );
            // update command but non-existing id
        updateMapCommand.setId( -1L );
        updateMapCommand.setVersion( map.getVersion() );


        // WHEN
            // map updated to db
        mapService.updateMap( updateMapCommand );


        // THEN
        // throw EntityNotFoundException
    }


    @Test
    public void ensure_delete_a_map_from_a_user_works() {

        // GIVEN
            // user with 1 map
        user.addMaps( map );
            // saved to db
        userService.saveUser( user );


        // WHEN
        // map is removed
        GameMap deletedMap = mapService.removeMap( map.getId() );


        // THEN
            // retrieved user
        User retrievedUser = userService.findUser_withEagerFetchingMaps( user.getId() );
            // has no maps
        assertThat( retrievedUser.getMaps() ).isNotNull().hasSize( 0 );
            // and returned map from service equals initial map
        assertMap( map, deletedMap );
    }


    private void assertMap( GameMap actualMap, GameMap excpectedMap ) {
        assertThat( actualMap.getId() ).isEqualTo( excpectedMap.getId() );
        assertThat( actualMap.getVersion() ).isEqualTo( excpectedMap.getVersion() );
        assertThat( actualMap.getCreatedAt() ).isEqualTo( excpectedMap.getCreatedAt() );
        assertThat( actualMap.getName() ).isEqualTo( excpectedMap.getName() );
        assertThat( actualMap.getGameMode() ).isEqualTo( excpectedMap.getGameMode() );
        assertThat( actualMap.getSize() ).isEqualTo( excpectedMap.getSize() );
    }
}
