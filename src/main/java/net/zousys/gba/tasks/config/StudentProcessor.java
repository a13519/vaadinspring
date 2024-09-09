package net.zousys.gba.tasks.config;

import net.zousys.gba.tasks.student.Student;
import org.springframework.batch.item.ItemProcessor;

public class StudentProcessor implements ItemProcessor<Student,Student> {

    @Override
    public Student process(Student student) {
        return student;
    }
}
