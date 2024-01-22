public final class FacultyMember extends LibraryUser {
    public FacultyMember(boolean isPunctual) {
        super(isPunctual);
        this.maxLoanLength = new int[]{14, 7, 2}; // books, journals, movies
        this.maxLoanAmount = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}; // books, journals, movies
    }
}
