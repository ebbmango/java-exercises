import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    private static void printRepresentation (int[][] mazeMatrix) {
        for (int[] row : mazeMatrix) {
            for (int integer : row) {
                    switch (integer) {
                        case -1:
                            System.out.print("■ ");
                            break;
                        case -2:
                            System.out.print("྾ ");
                            break;
                        case -3:
                        case 0:
                            System.out.print("  ");
                            break;
                        default:
                            System.out.print("× ");
                            break;
                }
            }
            System.out.println();
        }
    }

    public static int[][] LoadMaze (String mazeFilePath) {
//        1) READ ALL LINES FROM FILE
        ArrayList<String> unfilteredMaze = new ArrayList<>();
        try (var bufferedReader = new BufferedReader(new FileReader(mazeFilePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                unfilteredMaze.add(line);
            }
        } catch (IOException e) {
            System.out.println("Could not read from file \"" + mazeFilePath + "\"");
        }

//        2) GET RID OF ALL WHITESPACES
        ArrayList<String> filteredMaze = new ArrayList<>();
        for (String row : unfilteredMaze) {
            filteredMaze.add(row.replaceAll("\\s+", ""));
        }

//        3) MAKE STRING ARRAY INTO INTEGER MATRIX
        int height = filteredMaze.size();
        int width = filteredMaze.getFirst().length();

        int[][] mazeMatrix = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                char character = filteredMaze.get(i).charAt(j);
                switch (character) {
                    case 'W':
                        mazeMatrix[i][j] = -1;
                        break;
                    case 'C':
                        mazeMatrix[i][j] = 0;
                        break;
                    case 'S':
                        mazeMatrix[i][j] = 1;
                        break;
                    case 'F':
                        mazeMatrix[i][j] = -2;
                        break;
                }
            }
        }

        return mazeMatrix;
    }

    public static int[] getAdjacentSquares(int[][] maze, int[] coordinates) {

        int top;
        try { top = maze[coordinates[0] - 1][coordinates[1]]; }
        catch (Exception error) { top = -1; }

        int right;
        try { right = maze[coordinates[0]][coordinates[1] + 1]; }
        catch (Exception error) { right = -1; }

        int bot;
        try { bot = maze[coordinates[0] + 1][coordinates[1]]; }
        catch (Exception error) { bot = -1; }

        int left;
        try { left = maze[coordinates[0]][coordinates[1] - 1]; }
        catch (Exception error) { left = -1; }

        return new int[] {top, right, bot, left};
    }

    private static int findIndex (int[] array, int integer) {
        int index = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == integer) {
                index = i;
                break;
            }
        }
        return index;
    }

    private static int[] findInAdjacent (int[] adjacentSquares, int[] coordinates, int target) {
        int currentY = coordinates[0];
        int currentX = coordinates[1];

        return switch (findIndex(adjacentSquares, target)) {
            case 0  -> new int[]{ currentY - 1, currentX     };
            case 1  -> new int[]{ currentY    , currentX + 1 };
            case 2  -> new int[]{ currentY + 1, currentX     };
            case 3  -> new int[]{ currentY    , currentX - 1 };
            default -> new int[]{ -1 }; // if there are no more adjacent nodes marked with the current search level
        };
    }

    public static void backtrace (int[][] maze, int level, int[] coordinates) {
        int currentY = coordinates[0];
        int currentX = coordinates[1];

        maze[currentY][currentX] = -3; // so we mark the current node as REJECTED

        int[] adjacentSquares = getAdjacentSquares(maze, coordinates);

        // we check for the next adjacent square that has been marked with the current level
        int[] newCoordinates = findInAdjacent(adjacentSquares, coordinates, level);

        if (newCoordinates[0] == -1) { // if there are none...
            // it's because we have already successfully backtracked to the last bifurcation!
            maze[currentY][currentX] = -3; // so we mark the current node as REJECTED

            // and instead, look for the next adjacent square that has been marked at one level lower
            newCoordinates = findInAdjacent(adjacentSquares, coordinates, level - 1);

            walk(maze, level - 1, newCoordinates); // and resume walking from there!
        } else { // else, we need to backtrack some more!
            backtrace(maze, level, newCoordinates);
        }
    }

    public static void walk (int[][] maze, int level, int[] coordinates) {
        int currentY = coordinates[0];
        int currentX = coordinates[1];

        int[] adjacentSquares = getAdjacentSquares(maze, coordinates);
        maze[currentY][currentX] = level;

        // we check if the end is in sight
        if (findIndex(adjacentSquares, -2) != -1) {
            printRepresentation(maze);
        } else { // we resume our recursion

            // we look for the next adjacent square that contains a zero
            int[] newCoordinates = findInAdjacent(adjacentSquares, coordinates, 0);

            if (newCoordinates[0] == -1) { // if there are NO free spaces (zeroes) adjacent to the current node...
                // it's because we have hit a DEAD-END!
                backtrace(maze, level, coordinates);
            } else {
                // if THERE ARE free spaces adjacent to the current node, we need to know just exactly how many:
                int untroddenPaths = 0;
                for (int square : adjacentSquares) {
                    untroddenPaths += (square == 0 ? 1 : 0);
                }
                // depending on the amount of free spaces:
                if (untroddenPaths > 1) { // if there is more than one free space, we have hit a BIFURCATION
                    walk(maze, level + 1, newCoordinates); //  WALK CALL (with amplified level)
                } else { // if there is only one free space, THE PATH IS CLEAR
                    walk(maze, level, newCoordinates); // WALK CALL (with the same level)
                }
                // in either case, the next node to be traversed will be chosen from the available ones in the clockwise
                // order of priority starting from the top/ (top -> right -> bot -> left)
            }
        }
    }

    public static void main(String[] args) {

        int[][] maze = LoadMaze(args[0]);

        walk(maze, 1, new int[]{1, 0});
    }
}
