public abstract sealed class LibraryUser permits Student, FacultyMember {
    protected int id;
    protected boolean isPunctual;

    protected int[] maxLoanLength; // books, journals, movies
    protected int[] maxLoanAmount; // books, journals, movies

    LibraryUser(boolean isPunctual) {
        this.isPunctual = isPunctual;
    }

}
