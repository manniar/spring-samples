package fi.ari.bootweb.allin.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@Data
@JsonAutoDetect(
    creatorVisibility = NONE,
    fieldVisibility = NONE,
    setterVisibility = NONE
)
public class Person {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Person ID")
    protected int id;
    @NotNull @NotEmpty
    @Schema(description = "Person given name", example = "Chuck")
    protected String firstName;
    @NotNull @NotEmpty
    @Schema(description = "Person family name", example = "Norris")
    protected String lastName;

    @JsonCreator
    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
