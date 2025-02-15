import java.util.*;

abstract class Student {
    protected String name;
    protected List<Integer> quizScores;

    public Student(String name) {
        this.name = name;
        this.quizScores = new ArrayList<>();
    }

    public void addQuizScore(int score) {
        if (quizScores.size() < 15) {
            quizScores.add(score);
        }
    }

    public double getAverageQuizScore() {
        if (quizScores.isEmpty()) {
            return 0.0;
        }
    
        int total = 0;
        for (int score : quizScores) {
            total += score;
        }
    
        return (double) total / quizScores.size();
    }
    

    public String getName() {
        return name;
    }

    public List<Integer> getQuizScores() {
        return quizScores;
    }
}
