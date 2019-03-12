package org.mlccc.cm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A MlcClass.
 */
@Entity
@Table(name = "mlc_class")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MlcClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "class_name")
    private String className;

    @Column(name = "text_book")
    private String textBook;

    @Column(name = "jhi_size")
    private Integer size;

    @Column(name = "min_age")
    private Integer minAge;

    @Column(name = "tuition")
    private Double tuition;

    @Column(name = "registration_fee")
    private Double registrationFee;

    @Column(name = "seq_number")
    private Integer seqNumber;

    @ManyToOne
    private ClassStatus status;

    @ManyToOne
    private ClassTime classTime;

    @ManyToOne
    private Teacher teacher;

    @ManyToOne
    private ClassRoom classRoom;

    @ManyToOne
    private SchoolTerm schoolTerm;

    @ManyToOne
    private MlcClassCategory mlcClassCategory;

    /*
    @OneToMany(
            mappedBy = "mlcClass",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Registration> registrations = new HashSet<>();*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public MlcClass className(String className) {
        this.className = className;
        return this;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTextBook() {
        return textBook;
    }

    public MlcClass textBook(String textBook) {
        this.textBook = textBook;
        return this;
    }

    public void setTextBook(String textBook) {
        this.textBook = textBook;
    }

    public Integer getSize() {
        return size;
    }

    public MlcClass size(Integer size) {
        this.size = size;
        return this;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public MlcClass minAge(Integer minAge) {
        this.minAge = minAge;
        return this;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Double getTuition() {
        return tuition;
    }

    public MlcClass tuition(Double tuition) {
        this.tuition = tuition;
        return this;
    }

    public void setTuition(Double tuition) {
        this.tuition = tuition;
    }

    public Double getRegistrationFee() {
        return registrationFee;
    }

    public MlcClass registrationFee(Double registrationFee) {
        this.registrationFee = registrationFee;
        return this;
    }

    public void setRegistrationFee(Double registrationFee) {
        this.registrationFee = registrationFee;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public MlcClass seqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
        return this;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public ClassStatus getStatus() {
        return status;
    }

    public MlcClass status(ClassStatus classStatus) {
        this.status = classStatus;
        return this;
    }

    public void setStatus(ClassStatus classStatus) {
        this.status = classStatus;
    }

    public ClassTime getClassTime() {
        return classTime;
    }

    public MlcClass classTime(ClassTime classTime) {
        this.classTime = classTime;
        return this;
    }

    public void setClassTime(ClassTime classTime) {
        this.classTime = classTime;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public MlcClass teacher(Teacher teacher) {
        this.teacher = teacher;
        return this;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public MlcClass classRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
        return this;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public SchoolTerm getSchoolTerm() {
        return schoolTerm;
    }

    public MlcClass schoolTerm(SchoolTerm schoolTerm) {
        this.schoolTerm = schoolTerm;
        return this;
    }

    public void setSchoolTerm(SchoolTerm schoolTerm) {
        this.schoolTerm = schoolTerm;
    }

    public MlcClassCategory getMlcClassCategory() {
        return mlcClassCategory;
    }

    public void setMlcClassCategory(MlcClassCategory mlcClassCategory) {
        this.mlcClassCategory = mlcClassCategory;
    }

   /* public Set<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(Set<Registration> registrations) {
        this.registrations = registrations;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MlcClass mlcClass = (MlcClass) o;
        if (mlcClass.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mlcClass.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MlcClass{" +
            "id=" + getId() +
            ", className='" + getClassName() + "'" +
            ", textBook='" + getTextBook() + "'" +
            ", size='" + getSize() + "'" +
            ", minAge='" + getMinAge() + "'" +
            ", tuition='" + getTuition() + "'" +
            ", registrationFee='" + getRegistrationFee() + "'" +
            ", seqNumber='" + getSeqNumber() + "'" +
            "}";
    }
}
