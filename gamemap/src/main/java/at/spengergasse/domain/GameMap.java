package at.spengergasse.domain;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter @ToString(callSuper = true, exclude = "creater")
public class GameMap extends BaseDomain {

    @Builder
    private GameMap(
        Long id, Integer version,
        String name, Size size, GameMode gameMode, Integer price, User creater ) {
        this( name, size , gameMode, price, creater );
        setId( id );
        setVersion( version );
    }

    @NonNull
    @NotNull
    @Length(min = 3, max = 9)
    @Column(unique = true)
    private String name;

    @NonNull
    @NotNull
    @Enumerated(EnumType.STRING)
    private Size size;

    @NonNull
    @NotNull
    @Enumerated(EnumType.STRING)
    private GameMode gameMode;

    @NonNull
    @NotNull
    @Min(0)
    private Integer price;

    // only called by user
    @ManyToOne
    @Setter(AccessLevel.PROTECTED)
    private User creater;
}
