package model.Entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "STUDENTS")
public class Student implements Serializable {

    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToOne(mappedBy = "student")
    private User user;
    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    private List<Mark> marks;

//    private String surname;
//    private int course;


    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }

    public void setMark(Mark m){
        if (marks == null)
            marks = new ArrayList<>();
        else
            marks.add(m);
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", name='" + name + "'";
    }

    public List<Mark> getUnfilledMarks() {
        List<Mark> negativeMarks = new ArrayList<>();
        for (Mark m: marks){
            if(m.getMark() == -1){
                negativeMarks.add(m);
            }
        }
        return negativeMarks;
    }
}
