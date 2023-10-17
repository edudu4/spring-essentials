package br.com.spring.placeti.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PersonPostDTO {
    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Profession must not be blank")
    private String profession;

    @NotNull(message = "The person must have to be a valid age")
    @Min(value = 18, message = "Age should not be less than 18")
    private int age;
}
