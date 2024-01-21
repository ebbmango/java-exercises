public final class Student extends LibraryUser {
    public Student(boolean isPunctual) {
        super(isPunctual);
        this.maxLoanLength = new int[]{14, 3, 2}; // books, journals, movies
        this.maxLoanAmount = new int[]{ 3, 3, 1}; // books, journals, movies
    }
}
