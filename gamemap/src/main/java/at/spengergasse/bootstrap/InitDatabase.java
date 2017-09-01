package at.spengergasse.bootstrap;

import at.spengergasse.domain.GameMap;
import at.spengergasse.domain.GameMode;
import at.spengergasse.domain.User;
import at.spengergasse.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import static at.spengergasse.domain.Size.SMALL;


@Service
@Transactional
@RequiredArgsConstructor
@Profile("development")
public class InitDatabase {

    private final UserRepository userRepository;


    @PostConstruct
    public void init() {

        GameMap map1 = GameMap.builder()
            .name( "DE Dust" )
            .size( SMALL )
            .gameMode( GameMode.DEATH_MATCH )
            .price( 10 )
            .build();

        GameMap map2 = GameMap.builder()
            .name( "GB Dust" )
            .size( SMALL )
            .gameMode( GameMode.DEATH_MATCH )
            .price( 10 )
            .build();

        GameMap map3 = GameMap.builder()
            .name( "AUT Dust" )
            .size( SMALL )
            .gameMode( GameMode.DEATH_MATCH )
            .price( 10 )
            .build();

        User user = User.builder()
            .nickname( "Max" )
            .password( "12345678" )
            .email( "max@gmx.at" )
            .creditCard( "340000000000009" )
            .realMoney( 10 )
            .gameMoney( 5 )
            .build()
            .addMaps( map1, map2, map3 );

        userRepository.deleteAll();
        userRepository.save( user );
        //userRepository.flush();
    }
}
