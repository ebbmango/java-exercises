import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException {
        Library library = new Library("./src/movies.csv", "./src/journals.csv", "./src/books.csv", 20, 80, 67);

        var book = library.getInventory().getLast();


        Class<?> bookClassA = Class.forName("Book");
        Class<?> bookClassB = book.getClass();
        Class<?> bookClassC = Book.class;

        System.out.println("1) Listing all methods with their access modifiers in the Book class:");
        for (Method method : bookClassB.getMethods()) {
            System.out.format("METHOD = \"%s\" / RETURN_TYPE = %s / MODIFIERS = %s\n", method.getName(), method.getReturnType(), method.getModifiers());
        }

        System.out.println();

        // Using reflection to modify the value of the pagesNr private field
        Field pagesNumber = bookClassB.getDeclaredField("pagesNr");
        pagesNumber.setAccessible(true);
        pagesNumber.set(book, 150);

        // Listing all fields of the book class
        System.out.println("2) Listing all fields with their access modifiers in the Book class:");
        for (Field field : bookClassB.getDeclaredFields()) {
            field.setAccessible(true);
            System.out.format("FIELD = \"%s\" / VALUE = %s / MODIFIERS = %s\n", field.getName(), field.get(book), field.getModifiers());
        }

        System.out.println();

        // Getting the superclass of the Book class (using Reflection) and inspecting all of its methods and fields
        Class<?> superclass = bookClassB.getSuperclass();
        System.out.println("3) Listing all fields and methods of the Book's superclass:");
        for (Method method : superclass.getDeclaredMethods()) {
            System.out.format("METHOD = \"%s\" / RETURN_TYPE = %s / MODIFIERS = %s\n", method.getName(), method.getReturnType(), method.getModifiers());
        }
        for (Field field : superclass.getDeclaredFields()) {
            field.setAccessible(true);
            System.out.format("FIELD = \"%s\" / VALUE = %s / MODIFIERS = %s\n", field.getName(), field.get(book), field.getModifiers());
        }

        System.out.println();

        // Finding information about the constructors of the class using reflection
        System.out.println("4) Finding information about the constructors of the class:");
        for (Constructor<?> constructor : bookClassB.getDeclaredConstructors()) {
            var parameters = constructor.getParameters();

            System.out.format("CONSTRUCTOR [%s parameter(s)]\n", parameters.length);
            System.out.println(" - NAME: \"" + constructor.getName() + "\"");
            System.out.println(" - ACCESS MODIFIERS: " + constructor.getModifiers());
            System.out.println(" - PARAMETERS: " + Arrays.stream(parameters).map(Object::toString).collect(Collectors.joining(" / ")));
            System.out.println();
        }
//        constructor.

    }

}

