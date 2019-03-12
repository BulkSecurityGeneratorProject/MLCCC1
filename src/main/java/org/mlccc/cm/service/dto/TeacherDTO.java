package org.mlccc.cm.service.dto;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.mlccc.cm.domain.MlcClass;
import org.mlccc.cm.domain.Teacher;
import org.mlccc.cm.domain.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TeacherDTO{
    private Long id;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    private LocalDate hireDate;

    private Double payRate;

    private User account;

    private String classCategories;

    private String classes;

    private Set<MlcClassDTO> mlcClasses;

    public TeacherDTO() {
        // Empty constructor needed for Jackson.
    }

    public TeacherDTO(Teacher teacher){
        this(teacher.getId(), teacher.getFirstName(), teacher.getLastName(), teacher.getPayRate(),
                teacher.getHireDate(), teacher.getAccount());
        if(teacher.getMlcClasses() != null){
            Set<MlcClassDTO> classes = new HashSet<>();
            teacher.getMlcClasses().forEach(mlcClass->{
                MlcClassDTO classDTO = new MlcClassDTO();
                classDTO.setClassName(mlcClass.getClassName());
                classDTO.setId(mlcClass.getId());
                classes.add(classDTO);
            });
            this.setMlcClasses(classes);
        }
    }
    
    public TeacherDTO(Long id, String firstName, String lastName,
                      Double payRate, LocalDate hireDate, User account) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.payRate = payRate;
        this.hireDate = hireDate;
        this.account = account;
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

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public Double getPayRate() {
        return payRate;
    }

    public void setPayRate(Double payRate) {
        this.payRate = payRate;
    }

    public User getAccount() {
        return account;
    }

    public void setAccount(User account) {
        this.account = account;
    }

    public String getClassCategories() {
        return classCategories;
    }

    public void setClassCategories(String classCategories) {
        this.classCategories = classCategories;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public Set<MlcClassDTO> getMlcClasses() {
        return mlcClasses;
    }

    public void setMlcClasses(Set<MlcClassDTO> mlcClasses) {
        this.mlcClasses = mlcClasses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeacherDTO that = (TeacherDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return true;
    }

    @Override
    public String toString() {
        return "TeacherDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", hireDate=" + hireDate +
                ", payRate=" + payRate +
                ", account=" + account +
                ", classCategories='" + classCategories + '\'' +
                '}';
    }
}
