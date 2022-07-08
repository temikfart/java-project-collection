package cinema;

import java.util.Scanner;

public class Cinema {

    boolean isFinish = false;
    char[][] scheme;
    int rows;
    int seats;

    int purchasedTickets = 0;
    int totalSeats;
    int currentIncome = 0;
    int totalIncome;

    final int priceFront = 10;
    int priceBack = 10;
    int frontRows;

    Cinema(int rows, int seats) {
        this.scheme = new char[rows][seats];
        this.rows = rows;
        this.seats = seats;
        this.totalSeats = rows * seats;

        this.frontRows = rows / 2;
        if (totalSeats > 60)
            priceBack = 8;

        this.totalIncome = frontRows * priceFront + (rows - frontRows) * priceBack;
        this.totalIncome *= seats;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of rows:");
        int rows = scanner.nextInt();
        System.out.println("Enter the number of seats in each row:");
        int seats = scanner.nextInt();

        Cinema cinema = new Cinema(rows, seats);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < seats; j++) {
                cinema.scheme[i][j] = 'S';
            }
        }

        do {
            System.out.println();
            printMenu();
            int choice = scanner.nextInt();
            System.out.println();
            switch (choice) {
                case 0:
                    cinema.isFinish = true;
                    break;
                case 1:
                    printScheme(cinema.scheme);
                    break;
                case 2:
                    cinema.buyTicket(cinema.scheme);
                    break;
                case 3:
                    cinema.printStatistics();
                    break;
                default:
                    System.out.println("Something went wrong");
                    break;
            }
        } while (!cinema.isFinish);
    }

    public static void printMenu() {
        System.out.println("1. Show the seats");
        System.out.println("2. Buy a ticket");
        System.out.println("3. Statistics");
        System.out.println("0. Exit");
    }

    public void buyTicket(char[][] scheme) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a row number:");
        int buyRow = scanner.nextInt();
        System.out.println("Enter a seat number in that row:");
        int buySeat = scanner.nextInt();

        if (buyRow < 1 || buyRow > rows || buySeat < 1 || buySeat > seats) {
            System.out.println();
            System.out.println("Wrong input!");
            System.out.println();
            buyTicket(scheme);
            return;
        }
        if (scheme[buyRow - 1][buySeat - 1] == 'B') {
            System.out.println();
            System.out.println("That ticket has already been purchased!");
            System.out.println();
            buyTicket(scheme);
            return;
        }

        int price;
        if (buyRow <= frontRows)
            price = priceFront;
        else
            price = priceBack;

        System.out.println();
        System.out.printf("Ticket price: $%d%n", price);
        scheme[buyRow - 1][buySeat - 1] = 'B';
        purchasedTickets++;
        currentIncome += price;
    }

    public void printStatistics() {
        System.out.println("Number of purchased tickets: " + purchasedTickets);
        double percentage = purchasedTickets / (double)totalSeats * 100;
        System.out.printf("Percentage: %.2f%%%n", percentage);
        System.out.println("Current income: $" + currentIncome);
        System.out.println("Total income: $" + totalIncome);
    }

    public static void printScheme(char[][] scheme) {
        int rows = scheme.length;
        int seats = scheme[0].length;

        System.out.println("Cinema:");
        for (int i = 0; i <= rows; i++) {
            if (i > 0)
                System.out.print(i);
            for (int j = 0; j < seats; j++) {
                if (i == 0 && j == 0)
                    System.out.print(" ");
                if (i == 0) {
                    System.out.print(" " + (j + 1));
                    continue;
                }
                System.out.print(" " + scheme[i - 1][j]);
            }
            System.out.println();
        }
    }
}