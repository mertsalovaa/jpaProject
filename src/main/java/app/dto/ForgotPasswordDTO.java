package app.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class ForgotPasswordDTO {

    @NotBlank(message = "Поле не може бути пустим")
    @Email(message = "Не коректно вказано пошту")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
