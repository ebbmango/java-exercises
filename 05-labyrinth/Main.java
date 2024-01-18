import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void LoadMaze (String mazeFilePath) {
        try (var bufferedReader = new BufferedReader(new FileReader(mazeFilePath))) {
            System.out.println("File read successfully");
        } catch (IOException e) {
            System.out.println("Could not read from file \"" + mazeFilePath + "\"");
        }
    }

    public static void main(String[] args) {
        LoadMaze(args[0]);
    }
}