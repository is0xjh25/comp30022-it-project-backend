package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class LoginRequest {
    @NonNull
    private String email;
    @NonNull
    private String password;
}
