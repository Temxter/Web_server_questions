package model.Dao;

import model.Entities.Student;

public class DaoStudent extends DaoAbstract<Student> {
    public DaoStudent() {
        super(Student.class);
    }
}
