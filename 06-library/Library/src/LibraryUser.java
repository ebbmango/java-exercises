public abstract sealed class LibraryUser permits Student, FacultyMember {
    protected int id;
    private static int nextId = 1;
    protected boolean isPunctual;

    protected int[] maxLoanLength; // books, journals, movies
    protected int[] maxLoanAmount; // books, journals, movies

    LibraryUser(boolean isPunctual) {
        this.id = nextId++;
        this.isPunctual = isPunctual;
    }

}
