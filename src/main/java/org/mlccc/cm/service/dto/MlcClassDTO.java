package org.mlccc.cm.service.dto;

import org.mlccc.cm.domain.*;

import javax.persistence.*;
import java.util.Objects;

public class MlcClassDTO {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String className;

    private String textBook;

    private Integer size;

    private Integer minAge;

    private Double tuition;

    private Double registrationFee;

    private Integer seqNumber;

    private String status;

    private String classTime;

    private String teacher;

    private String classRoom;

    private String schoolTerm;

    private String mlcClassCategory;

    public MlcClassDTO() {
        // Empty constructor needed for Jackson.
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTextBook() {
        return textBook;
    }

    public void setTextBook(String textBook) {
        this.textBook = textBook;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Double getTuition() {
        return tuition;
    }

    public void setTuition(Double tuition) {
        this.tuition = tuition;
    }

    public Double getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(Double registrationFee) {
        this.registrationFee = registrationFee;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getSchoolTerm() {
        return schoolTerm;
    }

    public void setSchoolTerm(String schoolTerm) {
        this.schoolTerm = schoolTerm;
    }

    public String getMlcClassCategory() {
        return mlcClassCategory;
    }

    public void setMlcClassCategory(String mlcClassCategory) {
        this.mlcClassCategory = mlcClassCategory;
    }
}
