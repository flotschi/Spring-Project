package at.spengergasse.service.command;

import at.spengergasse.domain.GameMode;
import at.spengergasse.domain.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddMapCommand {

    @NotNull
    private Long userId;

    @NotNull
    @Length(min = 3, max = 9, message = "name must have between {min} and {max} characters")
    private String name;

    @NotNull
    private Size size;

    @NotNull
    private GameMode gameMode;

    @NotNull
    @Min(value = 0, message = "price must be posetive")
    private Integer price;
}
