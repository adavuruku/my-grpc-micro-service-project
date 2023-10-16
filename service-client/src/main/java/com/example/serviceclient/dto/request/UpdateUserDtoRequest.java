package com.example.serviceclient.dto.request;

import com.example.serviceclient.dto.response.FileResponse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.common.value.qual.MinLen;

@Data
@Builder
public class UpdateUserDtoRequest {

    @NotEmpty(message = "First Name cannot be empty")
    @NotBlank(message = "First Name cannot be blank")
    @Pattern(regexp = "^[a-zA-Z]+(?:\\s+[a-zA-Z]+)*$", message = "Name must contain only alphabets along with white spaces.")
    private String firstName;

    @NotEmpty(message = "Last Name cannot be empty")
    @NotBlank(message = "Last Name cannot be blank")
    @Pattern(regexp = "^[a-zA-Z]+(?:\\s+[a-zA-Z]+)*$", message = "Name must contain only alphabets along with white spaces.")
    private String lastName;

    @NotEmpty(message = "Phone Number cannot be empty")
    @NotBlank(message = "Phone Number cannot be blank")
    private String phoneNumber;

    private FileResponse profileImage;

    @NotEmpty(message = "Contact address cannot be empty")
    @NotBlank(message = "Contact address cannot be blank")
    private String contactAddress;
}
