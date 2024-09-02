package me.mmtr.noted.data.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotEmpty(message = "Username must be specified.")
    private String username;

    @NotEmpty(message = "Password cannot be empty.")
    private String password;

}
