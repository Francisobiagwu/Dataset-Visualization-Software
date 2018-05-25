import com.diogonunes.jcdp.bw.Printer;

/**
 * DVSInstructions
 * This class is used to retrieve common instructions for the user
 *
 * @author Francis Obiagwu
 * @version 1
 * @date 5/23/2018
 */
public class DVSInstructions {


    String line = "---------------------------------------------------";


    String introduction = "\n---Welcome to Dataset Visualization Software 1.0---\n";

    String features = "\nHere are the highlights of this release:\n" +
            " - Dataset uploading\n" +
            " - Dataset downloading\n" +
            " - Dataset defintion\n";


    String openingInstruction = this.line +
            "\nTo select option, type in the option #\n" +
            "1. Sign in\n" +
            "2. Sign up\n" +
            "3. Reset password or retrieve username\n" +
            "4. Quit";


    String visualizationInstruction = "1. Notepad\n" +
            "2. Excel\n" +
            "3. Graph\n" +
            "4. Return to the main menu\n";


}
