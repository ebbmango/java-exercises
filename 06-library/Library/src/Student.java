public final class Student extends User {
    public Student() {
        this.maxLoanLength = new int[]{14, 3, 2}; // books, journals, movies
        this.maxLoanAmount = new int[]{ 3, 3, 1}; // books, journals, movies
    }
}
