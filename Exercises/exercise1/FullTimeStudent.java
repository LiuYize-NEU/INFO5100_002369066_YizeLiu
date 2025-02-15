class FullTimeStudent extends Student {
    private int exam1;
    private int exam2;

    public FullTimeStudent(String name, int exam1, int exam2) {
        super(name);
        this.exam1 = exam1;
        this.exam2 = exam2;
    }

    public int getExam1() {
        return exam1;
    }

    public int getExam2() {
        return exam2;
    }
}