package br.com.spring.placeti.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonPostDTO {
    @NotBlank(message = "Name must not be blank")
    @Schema(description = "This is the Person's name", example = "Eduardo Alves")
    private String name;

    @NotBlank(message = "Profession must not be blank")
    @Schema(description = "This is the Person's profession", example = "Desenvolvedor")
    private String profession;

    @NotNull(message = "The person must have a valid age")
    @Min(value = 18, message = "Age should not be less than 18")
    @Schema(description = "This is the Person's age", example = "22")
    private int age;
}
