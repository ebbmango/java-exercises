public class HammingCleared extends Hamming {

    // "HammingCleared class should contain the private method clear which removes from the string all charactes such as whitespaces, tabulators, underlines."
    private static String clear(String string) {
        return string.replace(" ", "").replace("\t", "").replace("_", "");
    }

    // "HammingCleared class should also contain the compare method, that overrides the method from the parent class."
    public int compare(String lineA, String lineB) {
    //  "This method should clear both strings first, and then compare them using the method from the parent class."
        String clearedLine1 = clear(lineA);
        String clearedLine2 = clear(lineB);
    //  "Use the keyword super within the overridden method in the child class to use the parent class' method."
        return super.compare(clearedLine1, clearedLine2);
    }
}
