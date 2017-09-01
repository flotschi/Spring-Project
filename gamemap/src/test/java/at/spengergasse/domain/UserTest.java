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


public class UserTest {

    private static Validator validator;

    private User user;


    @BeforeClass
    public static void setupOnce() {

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }


    @Before
    public void setup() {

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

        user = User.builder()
            .nickname( "Max" )
            .password( "12345678" )
            .email( "max@gmx.at" )
            .creditCard( "340000000000009" )
            .realMoney( 10 )
            .gameMoney( 5 )
            .build()
            .addMaps( map1, map2 );
    }


    @Test
    public void ensure_user_is_valid() {

        // GIVEN
            // user with two maps

        // WHEN
        Set<ConstraintViolation<User>> violations =
            validator.validate( user );

        // THEN
        assertThat( violations ).hasSize( 0 );
        assertThat( user.getMaps() ).hasSize( 2 );
    }


    @Test
    public void ensure_nickname_has_correct_length() {

        // WHEN
        Set<ConstraintViolation<User>> violations =
            validateValues( "nickname", "12", "1234567890" );

        // THEN
        assertThat( violations ).hasSize( 2);
    }


    @Test
    public void ensure_password_has_correct_length() {

        // WHEN
        Set<ConstraintViolation<User>> violations =
            validateValues( "password", "1234567", "123456" );

        // THEN
        assertThat( violations ).hasSize( 2 );
    }



    @Test
    public void ensure_email_has_correct_form() {

        // WHEN
        Set<ConstraintViolation<User>> violations =
            validateValues( "email", "max", "max@" );

        // THEN
        assertThat( violations ).hasSize( 2 );
    }


    @Test
    public void ensure_creditcard_has_correct_form() {

        // WHEN
        Set<ConstraintViolation<User>> violations =
            validateValues( "creditCard", "number123", "1234567890" );

        // THEN
        assertThat( violations ).hasSize( 2 );
    }


    @Test
    public void ensure_realMoney_has_correct_value() {

        // WHEN
        Set<ConstraintViolation<User>> violations =
            validateValues( "realMoney", -1, 0 );

        // then
        assertThat( violations ).hasSize( 1 );
    }


    @Test
    public void ensure_gameMoney_has_correct_value() {

        // WHEN
        Set<ConstraintViolation<User>> violations =
            validateValues( "gameMoney", -1, 0 );

        // THEN
        assertThat( violations ).hasSize( 1 );
    }


    private Set<ConstraintViolation<User>> validateValues( String attribute, Object... values ) {
        return Stream.of( values )
            .map( value ->  validator.validateValue( User.class, attribute, value ) )
            .flatMap( violations -> violations.stream() )
            .collect( toSet() );
    }
}
