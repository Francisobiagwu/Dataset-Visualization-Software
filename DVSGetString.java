import java.util.Scanner;

/**
 * @author Francis Obiagwu
 * @version 1
 * @date 5/23/2018
 */

public class DVSGetString {


    public static String getString(){
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();
        while(userInput.length()<1){
            System.out.println("Invalid input. Try again!!!");
            scanner.nextLine();
        }

        return userInput;
    }
}
