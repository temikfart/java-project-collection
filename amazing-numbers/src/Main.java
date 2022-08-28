import java.util.Scanner;
import java.util.*;

public class Main {
    long number;
    ArrayList<Integer> digits = new ArrayList<>();

    ArrayList<String> propertiesAsStrings = new ArrayList<>();
    ArrayList<Boolean> propertiesAsBoolean = new ArrayList<>();

    enum Property {
        BUZZ,
        DUCK,
        PALINDROMIC,
        GAPFUL,
        SPY,
        SQUARE,
        SUNNY,
        JUMPING,
        HAPPY,
        SAD,
        EVEN,
        ODD
    }

    enum Mode {
        NUMBER,
        LIST,
        SPECIAL_PROPERTIES
    }

    Main() {}
    Main(long number) {
        this.number = number;
        digits = getDigits(number);
        buzz();
        duck();
        palindromic();
        gapful();
        spy();
        square();
        sunny();
        jumping();
        happy();
        even();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printWelcome();
        do {
            System.out.println();
            System.out.print("Enter a request: ");

            String request = scanner.nextLine();
            String[] parameters = request.split(" ");
            int argNum = parameters.length;

            System.out.println();

            long number = Long.parseLong(parameters[0]);
            if (number == 0) {
                System.out.println("Goodbye!");
                break;
            } else if (number < 1) {
                System.out.println("The first parameter should be a natural number or zero.");
                continue;
            }

            Long count = argNum > 1 ? Long.parseLong(parameters[1]) : null;
            if (count != null && count < 1) {
                System.out.println("The second parameter should be a natural number");
                continue;
            }

            ArrayList<String> properties = new ArrayList<>();
            if (argNum > 2) {
                properties = new ArrayList<>(List.of(parameters));
                properties.remove(0);
                properties.remove(0);

                var invalidProps = getIncorrectProperties(properties);
                if (!invalidProps.isEmpty()) {
                    printInvalidPropertiesError(invalidProps);
                    continue;
                }

                var exclusivePair = getExclusiveProperties(properties);
                if (exclusivePair != null) {
                    System.out.printf("The request contains mutually exclusive properties: [%s, %s]\n",
                            exclusivePair[0].toUpperCase(), exclusivePair[1].toUpperCase());
                    System.out.println("The are no numbers with these properties");
                    continue;
                }
            }

            Mode mode;
            if (parameters.length < Mode.values().length)
                mode = Mode.values()[parameters.length - 1];
            else
                mode = Mode.SPECIAL_PROPERTIES;

            if (!mode.equals(Mode.NUMBER) && count == null)
                continue;

            for (int i = 0; i < properties.size(); i++)
                properties.set(i, properties.get(i).toLowerCase());
            switch (mode) {
                case NUMBER:
                    Main n = new Main(number);
                    n.printProperties();
                    break;
                case LIST:
                    for (int i = 0; i < count; i++) {
                        n = new Main(number + i);
                        n.printPropertiesInLine();
                    }
                    break;
                case SPECIAL_PROPERTIES:
                    int j = 0;
                    for (int i = 0; j < count; i++) {
                        n = new Main(number + i);
                        if (isDesired(n, properties)) {
                            n.printPropertiesInLine();
                            j++;
                        }
                    }
                    break;
            }
        } while (true);
    }

    public static void printWelcome() {
        System.out.println("Welcome to Amazing numbers!");
        System.out.println();
        System.out.println("Supported requests:");
        System.out.println("- enter a natural number to know its properties;");
        System.out.println("- enter two natural numbers to obtain the properties of the list:");
        System.out.println("  * the first parameter represents a starting number;");
        System.out.println("  * the second parameter shows " +
                "how many consecutive numbers are to be processed;");
        System.out.println("- two natural numbers and properties to search for;");
        System.out.println("- a property preceded by minus must not be present in numbers;");
        System.out.println("- separate the parameters with one space;");
        System.out.println("- enter 0 to exit");
    }

    public static void printInvalidPropertiesError(ArrayList<String> invalidProps) {
        if (invalidProps.size() == 1) {
            System.out.printf("The property [%s] is wrong.%n", invalidProps.get(0).toUpperCase());
        } else {
            System.out.print("The properties [");
            for (int i = 0; i < invalidProps.size(); i++) {
                System.out.print(invalidProps.get(i).toUpperCase());
                if (i + 1 != invalidProps.size())
                    System.out.print(", ");
            }
            System.out.println("] are wrong.");
        }

        printAvailableProperties();
        System.out.println();
    }

    public static void printAvailableProperties() {
        System.out.print("Available properties: [");
        Property[] availableProperties = Property.values();
        for (int i = 0; i < availableProperties.length; i++) {
            System.out.print(availableProperties[i]);
            if (i < availableProperties.length - 1)
                System.out.print(", ");
        }
        System.out.print("]");
    }

    public void printProperties() {
        System.out.println("Properties of " + number);
        for (int i = 0; i < Property.values().length; i++)
            System.out.printf("%12s: %s%n",
                    Property.values()[i].toString(), propertiesAsBoolean.get(i));
    }

    public void printPropertiesInLine() {
        System.out.printf("%13s%d is ", "", number);
        for (String p : propertiesAsStrings) {
            System.out.print(p);
            if (propertiesAsStrings.indexOf(p) != propertiesAsStrings.size() - 1)
                System.out.print(", ");
        }
        System.out.println();
    }

    public static boolean isCorrectProperty(String property) {
        if ("-".equals(property))
            return false;
        if (property.charAt(0) == '-')
            property = property.substring(1);
        for (Property p : Property.values())
            if (property.equalsIgnoreCase(p.toString()))
                return true;
        return false;
    }

    public static ArrayList<String> getIncorrectProperties(ArrayList<String> properties) {
        ArrayList<String> invalidProps = new ArrayList<>();
        for (String p : properties)
            if (!isCorrectProperty(p))
                invalidProps.add(p);
        return invalidProps;
    }

    public static String[] getExclusiveProperties(ArrayList<String> properties) {
        String[][] exclusiveProperties = new String[][] {
                {"even", "odd"},
                {"duck", "spy"},
                {"square", "sunny"},
                {"happy", "sad"}
        };

        for (String[] pair : exclusiveProperties) {
            if (properties.contains(pair[0]) && properties.contains(pair[1]))
                return pair;
            if (properties.contains("-" + pair[0]) && properties.contains("-" + pair[1])
                    && !"square".equals(pair[0]))
                return new String[] {"-" + pair[0], "-" + pair[1]};
        }
        for (String p : properties)
            if (p.charAt(0) != '-' && properties.contains("-" + p))
                return new String[] {p, "-" + p};
        return null;
    }

    public static boolean isDesired(Main n, ArrayList<String> properties) {
        for (String p : properties) {
            boolean need = p.charAt(0) != '-';
            if (!need)
                p = p.substring(1);
            boolean contains = n.propertiesAsStrings.contains(p);
            if ((need && !contains) || (!need && contains))
                return false;
        }
        return true;
    }

    public void even() {
        boolean isEven = number % 2 == 0;
        String result = isEven ? "even" : "odd";
        propertiesAsStrings.add(result);
        propertiesAsBoolean.add(isEven);
        propertiesAsBoolean.add(!isEven);
    }

    public void buzz() {
        boolean isBuzz = number % 10 == 7 || number % 7 == 0;
        propertiesAsBoolean.add(isBuzz);
        if (isBuzz)
            propertiesAsStrings.add("buzz");
    }

    public void duck() {
        boolean isDuck = digits.contains(0);
        propertiesAsBoolean.add(isDuck);
        if (isDuck)
            propertiesAsStrings.add("duck");
    }

    public void palindromic() {
        int size = digits.size();
        for (int i = 0; i < size / 2; i++) {
            if (!Objects.equals(digits.get(i), digits.get(size - i - 1))) {
                propertiesAsBoolean.add(false);
                return;
            }
        }
        propertiesAsStrings.add("palindromic");
        propertiesAsBoolean.add(true);
    }

    public void gapful() {
        if (digits.size() >= 3) {
            long i = (number / (long)Math.pow(10, digits.size() - 1)) * 10 + (number % 10);
            if (number % i == 0) {
                propertiesAsStrings.add("gapful");
                propertiesAsBoolean.add(true);
                return;
            }
        }
        propertiesAsBoolean.add(false);
    }

    public void spy() {
        long sum = 0;
        long mul = 1;
        for (Integer i : digits) {
            sum += i;
            mul *= i;
        }
        boolean isSpy = sum == mul;
        propertiesAsBoolean.add(isSpy);
        if (isSpy)
            propertiesAsStrings.add("spy");
    }

    public static boolean isSquare(long n) {
        double root = Math.ceil(Math.sqrt(n));
        return (double)n == root * root;
    }

    public void square() {
        boolean isSquareNum = isSquare(number);
        if (isSquareNum)
            propertiesAsStrings.add("square");
        propertiesAsBoolean.add(isSquareNum);
    }

    public void sunny() {
        boolean isSunny = isSquare(number + 1);
        if (isSunny)
            propertiesAsStrings.add("sunny");
        propertiesAsBoolean.add(isSunny);
    }

    public void jumping() {
        if (digits.size() > 1) {
            for (int i = 0; i < digits.size() - 1; i++) {
                int cur = digits.get(i);
                int next = digits.get(i + 1);
                if (cur + 1 != next && cur - 1 != next) {
                    propertiesAsBoolean.add(false);
                    return;
                }
            }
        }
        propertiesAsStrings.add("jumping");
        propertiesAsBoolean.add(true);
    }

    public static ArrayList<Integer> getDigits(long n) {
        ArrayList<Integer> digits = new ArrayList<>();
        while (n > 0) {
            int digit = (int)(n % 10);
            digits.add(digit);
            n /= 10;
        }
        return digits;
    }

    public void happy() {
        long n = 0;
        boolean isHappy = false;
        var newDigits = digits;
        ArrayList<Long> previousNums = new ArrayList<>();
        while (n != 1) {
            n = 0;
            for (Integer d : newDigits)
                n += (long) d * d;
            if (n == 1)
                isHappy = true;
            if (previousNums.contains(n))
                break;
            newDigits = getDigits(n);
            previousNums.add(n);
        }
        propertiesAsBoolean.add(isHappy);
        propertiesAsBoolean.add(!isHappy);
        if (isHappy)
            propertiesAsStrings.add("happy");
        else
            propertiesAsStrings.add("sad");
    }
}
