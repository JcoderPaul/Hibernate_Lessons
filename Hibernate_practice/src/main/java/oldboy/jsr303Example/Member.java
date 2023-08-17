package oldboy.jsr303Example;

import javax.validation.constraints.*;
import java.util.Date;

public class Member {

    private String lastName = null;
    private String firstName = null;
    private Gender gender = null;
    private String emailAddress = null;
    private Date dateOfBirth = null;

    public Member() {
    }

    @NotNull(message = "First name is compulsory")
    @NotBlank(message = "First name is compulsory")
    @Pattern(regexp = "[a-z-A-Z]*", message = "First name has invalid characters")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @NotNull(message = "Gender is compulsory")
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @NotNull(message = "Last name is compulsory")
    @NotBlank(message = "Last name is compulsory")
    @Pattern(regexp = "[a-z-A-Z]*", message = "Last name has invalid characters")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Past(message = "Date of Birth must be the past")
    @NotNull
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Min(value = 18, message = "Age must be greater than or equal to 18")
    @Max(value = 150, message = "Age must be less than or equal to 150")
    public Integer getAge() {
        if (this.dateOfBirth != null) {
            // calculate age of member here
        }
        return null;
    }

    @NotNull(message="Email Address is compulsory")
    @NotBlank(message="Email Address is compulsory")
    @Email(message = "Email Address is not a valid format")
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
