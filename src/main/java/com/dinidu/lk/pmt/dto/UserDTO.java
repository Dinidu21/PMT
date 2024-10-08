package com.dinidu.lk.pmt.dto;
import lombok.*;

@NoArgsConstructor
@ToString
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
}
