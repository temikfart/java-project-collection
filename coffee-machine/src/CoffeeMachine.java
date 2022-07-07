import java.util.Scanner;

public class CoffeeMachine {
    // Resources
    int water;
    int milk;
    int coffeeBeans;
    int disposableCups;
    int money;

    // Variants of coffee
    Coffee espresso = new Coffee(250, 0, 16, 4);
    Coffee latte = new Coffee(350, 75, 20, 7);
    Coffee cappuccino = new Coffee(200, 100, 12, 6);

    boolean isFinish = false;
    State state;

    public enum State {
        CHOOSING_ACTION,
        CHOOSING_COFFEE,
        ADDING_WATER,
        ADDING_MILK,
        ADDING_COFFEE_BEANS,
        ADDING_DISPOSABLE_CUPS,
        TAKING_MONEY
    }

    public CoffeeMachine() {}
    public CoffeeMachine(int water, int milk, int coffeeBeans, int disposableCups, int money) {
        this.water = water;
        this.milk = milk;
        this.coffeeBeans = coffeeBeans;
        this.disposableCups = disposableCups;
        this.money = money;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CoffeeMachine coffeeMachine = new CoffeeMachine(400, 540, 120, 9, 550);

        coffeeMachine.chooseAction();
    }

    public void chooseAction() {
        Scanner scanner = new Scanner(System.in);

        this.state = State.CHOOSING_ACTION;

        do {
            System.out.println("Write action (buy, fill, take, remaining, exit):");
            String action = scanner.next();

            System.out.println();
            switch (action) {
                case "buy":
                    this.chooseCoffee();
                    break;
                case "fill":
                    this.fill();
                    break;
                case "take":
                    this.take();
                    break;
                case "remaining":
                    this.printState();
                    break;
                case "exit":
                    this.isFinish = true;
                    break;
                default:
                    System.out.println("Something went wrong");
                    this.isFinish = true;
            }
            if (this.isFinish)
                break;
            System.out.println();
        } while (true);
    }

    public void printState() {
        System.out.println("The coffee machine has:");
        System.out.printf("%d ml of water%n", this.water);
        System.out.printf("%d ml of milk%n", this.milk);
        System.out.printf("%d g of coffee beans%n", this.coffeeBeans);
        System.out.printf("%d disposable cups%n", this.disposableCups);
        System.out.printf("$%d of money%n", this.money);
    }

    public void chooseCoffee() {
        Scanner scanner = new Scanner(System.in);

        this.state = State.CHOOSING_COFFEE;

        System.out.println("What do you want to buy?" +
                " 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
        if (scanner.hasNextInt()) {
            int typeOfCoffee = scanner.nextInt();

            switch (typeOfCoffee) {
                case 1:
                    this.buyCoffee(this.espresso);
                    break;
                case 2:
                    this.buyCoffee(this.latte);
                    break;
                case 3:
                    this.buyCoffee(this.cappuccino);
                    break;
                default:
                    System.out.println("Unknown type of coffee");
            }
        } else {
            String buyAction = scanner.next();
            if (!buyAction.equals("back"))
                System.out.println("Incorrect buy-action");
        }
    }

    public void fill() {
        Scanner scanner = new Scanner(System.in);

        this.state = State.ADDING_WATER;
        System.out.println("Write how many ml of water you want to add:");
        this.water += scanner.nextInt();

        this.state = State.ADDING_MILK;
        System.out.println("Write how many ml of milk you want to add:");
        this.milk += scanner.nextInt();

        this.state = State.ADDING_COFFEE_BEANS;
        System.out.println("Write how many grams of coffee beans you want to add:");
        this.coffeeBeans += scanner.nextInt();

        this.state = State.ADDING_DISPOSABLE_CUPS;
        System.out.println("Write how many disposable cups of coffee you want to add:");
        this.disposableCups += scanner.nextInt();
    }

    public void take() {
        this.state = State.TAKING_MONEY;
        System.out.printf("I gave you $%d%n", this.money);
        this.money = 0;
    }

    public boolean canMakeCoffee(Coffee coffee) {
        if (this.water < coffee.water) {
            System.out.println("Sorry, not enough water!");
            return false;
        }
        if (this.milk < coffee.milk) {
            System.out.println("Sorry, not enough milk!");
            return false;
        }
        if (this.coffeeBeans < coffee.coffeeBeans) {
            System.out.println("Sorry, not enough coffee beans!");
            return false;
        }

        System.out.println("I have enough resources, making you a coffee!");
        return true;
    }

    public void buyCoffee(Coffee coffee) {
        if (!this.canMakeCoffee(coffee))
            return;

        this.water -= coffee.water;
        this.milk -= coffee.milk;
        this.coffeeBeans -= coffee.coffeeBeans;
        this.disposableCups--;
        this.money += coffee.cost;
    }
}

class Coffee {
    int water;
    int milk;
    int coffeeBeans;
    int cost;

    Coffee(int water, int milk, int coffeeBeans, int cost) {
        this.water = water;
        this.milk = milk;
        this.coffeeBeans = coffeeBeans;
        this.cost = cost;
    }
}
