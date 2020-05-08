package model;

import model.Entities.Answer;
import model.Entities.Mark;
import model.Entities.Student;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

//@Entity(name="TESTRESULTS")
public class TestResult {

    @ManyToOne()
    private Mark mark;
    @ManyToOne()
    private Student student;
    @ManyToOne
    private Answer answer;

//    private Question question;

}
