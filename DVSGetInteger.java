import java.util.Scanner;

/**
 * @author Francis Obiagwu
 * @version 1
 * @date 5/23/2018
 */

public class DVSGetInteger {


    public static int getInt() {
        while(true){
            try {
                return Integer.parseInt(new Scanner(System.in).next());
            } catch(NumberFormatException ne) {
                System.out.println("Only whole numbers are accepted.\n");
            }
        }
    }
}
