public abstract sealed class User permits Student, FacultyMember {
    protected int id;
    protected int[] maxLoanLength; // books, journals, movies
    protected int[] maxLoanAmount; // books, journals, movies

}
