import java.util.*;

public class Main {
    public static void main(String[] args) {
        Session session = new Session();
        Random random = new Random();

        for (int i = 1; i <= 20; i++) {
            if (i % 2 == 0) {
                FullTimeStudent student = new FullTimeStudent("FullTimeStudent" + i, random.nextInt(100), random.nextInt(100));
                for (int j = 0; j < 15; j++) {
                    student.addQuizScore(random.nextInt(100));
                }
                session.addStudent(student);
            } else {
                PartTimeStudent student = new PartTimeStudent("PartTimeStudent" + i);
                for (int j = 0; j < 15; j++) {
                    student.addQuizScore(random.nextInt(100));
                }
                session.addStudent(student);
            }
        }

        System.out.println("Class Average Quiz Score: " + session.getClassAverageQuizScore());
        session.printSortedQuizScores();
        session.printPartTimeStudentNames();
        session.printFullTimeExamScores();
    }
}