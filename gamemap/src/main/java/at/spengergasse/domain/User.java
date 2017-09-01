package at.spengergasse.domain;

import lombok.*;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter @ToString
public class User extends BaseDomain {

    @NotNull
    @NonNull
    @Column(unique = true)
    @Length(min = 3, max = 9)
    private String nickname;

    @NotNull
    @NonNull
    @Length(min = 8)
    private String password;

    @NotNull
    @NonNull
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    @NonNull
    @CreditCardNumber
    private String creditCard;

    @NonNull
    @NotNull
    @Min(0)
    private Integer realMoney;

    @NonNull
    @NotNull
    @Min(0)
    private Integer gameMoney;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "creater")
    private final List<GameMap> maps = new ArrayList<>();

    public List<GameMap> getMaps() {
        //return Collections.unmodifiableList( maps );
        return maps;
    }

    public User addMaps( @NonNull GameMap... maps ) {
        Stream.of( maps ).forEach( this::addMap );
        return this;
    }

    public User addMap( @NonNull GameMap map ) {
        maps.add( map );
        map.setCreater( this );
        return this;
    }

    public User removeMap( @NotNull GameMap map ) {
        maps.remove( map );
        map.setCreater( null );
        return this;
    }

    public User updateMap( @NonNull GameMap map ) {
        GameMap oldMap = maps.get( maps.indexOf( map ) );
        oldMap.setName( map.getName() );
        oldMap.setSize( map.getSize() );
        oldMap.setGameMode( map.getGameMode() );
        oldMap.setPrice( map.getPrice() );
        return this;
    }
}
