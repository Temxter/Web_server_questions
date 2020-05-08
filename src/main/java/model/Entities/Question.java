package model.Entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "QUESTIONS")
public class Question implements Serializable {

    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String question;
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private List<Answer> answers;
    @ManyToOne
    private Test test;

    @Transient
    final private String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    public Question() {
    }

    public Question(int id, String question, Test test) {
        this.id = id;
        this.question = question;
        this.test = test;
        answers = new ArrayList<>();
    }

    public String getRightAnswer(){
        int counter = 0;
        for (Answer a: answers){
            if(a.isRight())
                return String.valueOf(ALPHABET.charAt(counter));
            counter++;
        }
        return String.valueOf(ALPHABET.charAt(counter));
    }

    public void addAnswer(Answer answer){
        answers.add(answer);
    }

    public List<Answer> getAnswers(){
        return answers;
    }

    public String getQuestion() {
        return question;
    }

    public int getId() {
        return id;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public String getAnswersString(){
        StringBuilder stringBuilder = new StringBuilder();
        int counter = 0;
        for (Answer a : answers){
            stringBuilder.append(ALPHABET.charAt(counter) + ") " + a.toString() + "; ");
            counter++;
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return id + ") " + question + "\n" + "\t" +
                getAnswersString();
    }
}
