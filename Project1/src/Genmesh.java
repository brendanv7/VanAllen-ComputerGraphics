import java.util.Scanner;

/**
 *
 * @author Brendan Van Allen
 * @version Spring 2019
 */
public class Genmesh {
    private static Scanner keyboard = new Scanner(System.in);
    private static String shape;
    private static int divisionsU = 32;
    private static int divisionsV = 16;
    private static String outFile;

    public static void main(String args[]) {
        System.out.println("Welcome to Genmesh");
        System.out.println("Enter a command in the following format to produce a .obj file:");
        System.out.println("genmesh -g <sphere|cylinder> [-n <divisionsU>] [-m <divisionsV>] -o <outfile.obj>");

        String input = keyboard.nextLine();
        while(!parseInput(input)) {
            System.out.println("Invalid input. Please check your command syntax and try again.");
        }

    }

    /**
     * Parses the user input command to determine the values to be used
     * for mesh generation.
     *
     * @param input The command given by the user
     * @return True if the command is valid, false if it is invalid
     */
    private static boolean parseInput(String input) {
        String[] tokens = input.split(" ");

        // Try/catch is used to protect against ArrayIndexOutOfBounds and NumberFormatException
        try {
            // Command must start with "genmesh"
            if (!tokens[0].equalsIgnoreCase("genmesh")) {
                return false;
            }

            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].equalsIgnoreCase("-g")) {
                    if (tokens[i + 1].equalsIgnoreCase("sphere") || tokens[i + 1].equalsIgnoreCase("cylinder")) {
                        shape = tokens[i + 1];
                        i++;
                    } else {
                        return false;
                    }
                } else if (tokens[i].equalsIgnoreCase("-n")) {
                    try {
                        divisionsU = Integer.parseInt(tokens[i + 1]);
                        i++;
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        return false;
                    }
                } else if (tokens[i].equalsIgnoreCase("-m")) {
                    try {
                        divisionsV = Integer.parseInt(tokens[i + 1]);
                        i++;
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        return false;
                    }
                } else if (tokens[i].equalsIgnoreCase("-o")) {
                    if (tokens[i + 1].substring(tokens[i + 1].length() - 4).equals(".obj")) {
                        outFile = tokens[i + 1];
                    } else {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }

        // shape and outFile are the only required information
        if(shape != null && outFile != null) {
            return true;
        }

        return false;
    }
}
