import java.util.*;

class Session {
    private List<Student> students;

    public Session() {
        students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        if (students.size() < 20) {
            students.add(student);
        }
    }

    public double getClassAverageQuizScore() {
        if (students.isEmpty()) {
            return 0.0;
        }
    
        double totalScore = 0.0;
        for (Student student : students) {
            totalScore += student.getAverageQuizScore();
        }
    
        return totalScore / students.size();
    }
    

    public void printSortedQuizScores() {
        List<Integer> allScores = new ArrayList<>();
        for (Student student : students) {
            allScores.addAll(student.getQuizScores());
        }
        Collections.sort(allScores);
        System.out.println("Sorted quiz scores: " + allScores);
    }

    public void printPartTimeStudentNames() {
        System.out.println("Part-Time Students:");
        for (Student student : students) {
            if (student instanceof PartTimeStudent) {
                System.out.println(student.getName());
            }
        }
    }

    public void printFullTimeExamScores() {
        System.out.println("Full-Time Students Exam Scores:");
        for (Student student : students) {
            if (student instanceof FullTimeStudent) {
                FullTimeStudent ftStudent = (FullTimeStudent) student;
                System.out.println(ftStudent.getName() + " - Exam1: " + ftStudent.getExam1() + ", Exam2: " + ftStudent.getExam2());
            }
        }
    }
}