package service;

import model.Student;
import java.util.ArrayList;

public class StudentService {
    private final ArrayList<Student> studentList = new ArrayList<>();

    public void addStudent(Student student) {
        studentList.add(student);
    }

    public ArrayList<Student> getAllStudents() {
        return studentList;
    }

    public void updateStudent(int id, String newName, String newCourse) {
        for (Student s : studentList) {
            if (s.getId() == id) {
                s.setName(newName);
                s.setCourse(newCourse);
                break;
            }
        }
    }

    public void deleteStudent(int id) {
        studentList.removeIf(s -> s.getId() == id);
    }
}
