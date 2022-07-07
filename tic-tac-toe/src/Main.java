import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        /* Prepare the grid */
        char[][] grid = {
                {' ', ' ', ' '},
                {' ', ' ', ' '},
                {' ', ' ', ' '}
        };

        /* Print the grid */
        printGameGrid(grid);

        /* Scan user's move */
        int x, y;
        int i = 1;
        do {
            System.out.print("Enter the coordinates: ");
            if (!scanner.hasNextInt()) {
                scanner.nextLine();
                System.out.println("You should enter numbers!");
                continue;
            }
            x = scanner.nextInt();

            if (!scanner.hasNextInt()) {
                scanner.nextLine();
                System.out.println("You should enter numbers!");
                continue;
            }
            y = scanner.nextInt();

            if ((x < 1 || x > 3) || (y < 1 || y > 3)) {
                System.out.println("Coordinates shout be from 1 to 3!");
                continue;
            }

            if (grid[x - 1][y - 1] != ' ') {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            }

            // Move
            if (i % 2 == 1)
                grid[x - 1][y - 1] = 'X';
            else
                grid[x - 1][y - 1] = 'O';
            i++;

            printGameGrid(grid);
            if (isFinish(grid))
                break;
        } while (true);
    }

    public static void printGameGrid(char[][] grid) {
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            System.out.print("|");
            for (int j = 0; j < 3; j++) {
                System.out.print(" " + grid[i][j]);
            }
            System.out.println(" |");
        }
        System.out.println("---------");
    }

    public static boolean isFinish(char[][] grid) {
        /* Check on empty fields */
        boolean hasEmptyFields = false;
        for (int i = 0; i < 3; i++) {
            if (!hasEmptyFields) {
                for (int j = 0; j < 3; j++) {
                    if (grid[i][j] == ' ') {
                        hasEmptyFields = true;
                        break;
                    }
                }
            } else {
                break;
            }
        }

        /* Analyze the grid */
        int numberOfWinsX = 0;
        int numberOfWinsO = 0;

        // Check horizontal wins
        for (int i = 0; i < 3; i++) {
            if (grid[i][0] == 'X' && grid[i][1] == 'X' && grid[i][2] == 'X')
                numberOfWinsX++;
            if (grid[i][0] == 'O' && grid[i][1] == 'O' && grid[i][2] == 'O')
                numberOfWinsO++;
        }

        // Check vertical wins
        for (int j = 0; j < 3; j++) {
            boolean vertWinX = true;
            boolean vertWinO = true;
            for (int i = 0; i < 3; i++) {
                if (grid[i][j] != 'X')
                    vertWinX = false;
                if (grid[i][j] != 'O')
                    vertWinO = false;
            }
            if (vertWinX)
                numberOfWinsX++;
            if (vertWinO)
                numberOfWinsO++;
        }

        // Check diagonal wins
        boolean diag1X = true;
        boolean diag2X = true;
        boolean diag1O = true;
        boolean diag2O = true;
        if (grid[1][1] == ' ') {
            diag1X = false;
            diag2X = false;
            diag1O = false;
            diag2O = false;
        } else if (grid[1][1] == 'X') {
            diag1O = false;
            diag2O = false;
            if (grid[0][0] != 'X' || grid[2][2] != 'X')
                diag1X = false;
            if (grid[0][2] != 'X' || grid[2][0] != 'X')
                diag2X = false;
        } else if (grid[1][1] == 'O') {
            diag1X = false;
            diag2X = false;
            if (grid[0][0] != 'O' || grid[2][2] != 'O')
                diag1O = false;
            if (grid[0][2] != 'O' || grid[2][0] != 'O')
                diag2O = false;
        }
        numberOfWinsX += (diag1X && diag2X) ? 2 : ((diag1X || diag2X) ? 1 : 0);
        numberOfWinsO += (diag1O && diag2O) ? 2 : ((diag1O || diag2O) ? 1 : 0);

        /* Print result */
        if (numberOfWinsX == 0 && numberOfWinsO == 0 && !hasEmptyFields)
            System.out.println("Draw");
        else if (numberOfWinsX >= 1)
            System.out.println("X wins");
        else if (numberOfWinsO >= 1)
            System.out.println("O wins");
        else
            return false;

        return true;
    }
}
