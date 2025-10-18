package com.weconnect.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthDTOs {
    public static class Register {
        @NotBlank
        public String name;
        @NotBlank
        public String phone;
        @NotBlank
        public String password;
    }

    public static class Login {
        @NotBlank
        public String phone;
        @NotBlank
        public String password;
    }
}
