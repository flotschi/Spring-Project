package at.spengergasse.domain;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Stream;

import static at.spengergasse.domain.Size.SMALL;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Java6Assertions.assertThat;


public class GameMapTest {

    private static Validator validator;

    private GameMap map;


    @BeforeClass
    public static void setupOnce() {

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }


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
    public void ensure_map_is_valid() {

        // when
        Set<ConstraintViolation<GameMap>> violations = validator.validate( map );

        // then
        assertThat( violations ).hasSize( 0 );
    }


    @Test
    public void ensure_name_has_correct_length() {

        // when
        Set<ConstraintViolation<GameMap>> violations = validateValues( "name", "12", "1234567890" );

        // then
        assertThat( violations ).hasSize( 2 );
    }


    private Set<ConstraintViolation<GameMap>> validateValues( String attribute, Object... values ) {
        return Stream.of( values )
            .map( value ->  validator.validateValue( GameMap.class, attribute, value ) )
            .flatMap( violations -> violations.stream() )
            .collect( toSet() );
    }
}
