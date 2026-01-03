package com.showtime.dto.response;


import lombok.Data;

@Data
public class AuthResponse {
   private String token;
   private String type = "Bearer";
   private Long id;
   private String name;
   private String email;
   private String phone;
   private String role;
   
   public AuthResponse(String token, Long id, String name, String email, String phone, String role) {
       this.token = token;
       this.id = id;
       this.name = name;
       this.email = email;
       this.phone = phone;
       this.role = role;
   }
}
