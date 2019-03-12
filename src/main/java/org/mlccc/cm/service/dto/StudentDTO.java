package org.mlccc.cm.service.dto;

import org.mlccc.cm.domain.Student;
import org.mlccc.cm.domain.User;

import java.util.HashSet;
import java.util.Set;

/**
 * A DTO representing a user, with his authorities.
 */
public class StudentDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String gender;

    private String birthMonth;

    private String birthYear;

    private Set<UserDTO> associatedAccounts;

    private Set<String> classesTaken;

    public StudentDTO() {
        // Empty constructor needed for Jackson.
    }

    public StudentDTO(Student student){
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.birthMonth = student.getBirthMonth();
        this.birthYear = student.getBirthYear();
        this.gender = student.getGender();
        Set<UserDTO> associatedAccounts = new HashSet<>();
        for(User user : student.getAssociatedAccounts()){
            UserDTO dto = new UserDTO(user.getId(), user.getLogin(), user.getFirstName(), user.getLastName(),null,
                true, null, null, null, null,null, null, null);
            associatedAccounts.add(dto);
        }
        this.associatedAccounts = associatedAccounts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(String birthMonth) {
        this.birthMonth = birthMonth;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public Set<UserDTO> getAssociatedAccounts() {
        return associatedAccounts;
    }

    public void setAssociatedAccounts(Set<UserDTO> associatedAccounts) {
        this.associatedAccounts = associatedAccounts;
    }

    public Set<String> getClassesTaken() {
        return classesTaken;
    }

    public void setClassesTaken(Set<String> classesTaken) {
        this.classesTaken = classesTaken;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", gender='" + gender + '\'' +
            ", birthMonth='" + birthMonth + '\'' +
            ", birthYear=" + birthYear +
            "}";
    }
}
