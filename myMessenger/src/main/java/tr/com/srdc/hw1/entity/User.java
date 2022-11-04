package tr.com.srdc.hw1.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    private String userName;
    private String password;
    private String name;
    private String surname;
    private Date birthDate;
    private String gender;
    private String email;
    private String address;
    private String userType = "Regular";
    private boolean isActive = false;

}
