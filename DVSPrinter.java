import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;

/**
 * DVSPrinter
 * This class is used mainly due to usability and user experience on command line
 *
 * @author Francis Obiagwu
 * @version 1
 * @date 5/24/2018
 */

public class DVSPrinter implements DVSPrinterInterface {
    //create a color printer object
    private ColoredPrinter coloredPrinter = new ColoredPrinter.Builder(1, false).foreground(Ansi.FColor.WHITE).background(Ansi.BColor.BLACK).build();

    public DVSPrinter() {
        coloredPrinter.clear();
    }

    /**
     * Used to print a bold string
     *
     * @param str
     */

    @Override
    public void printBold(String str) {
        System.out.println("\u001B[1m" + str); //format for printing bold string
        this.printerReset(); // reset the color, otherwise the terminal continue to use it
    }

    /**
     * Used to print a string with blue font color
     *
     * @param str
     */

    @Override
    public void printWithBlue(String str) {
        coloredPrinter.print(str, Ansi.Attribute.BOLD, Ansi.FColor.BLUE, Ansi.BColor.BLACK);
        this.printerReset();
    }

    /**
     * Used to print a string with red font color
     *
     * @param str
     */

    @Override
    public void printWithRed(String str) {
        coloredPrinter.print(str, Ansi.Attribute.BOLD, Ansi.FColor.RED, Ansi.BColor.BLACK);
        this.printerReset();
    }

    /**
     * Used to print a string with yellow font color
     *
     * @param str
     */

    @Override
    public void printWithYellow(String str) {
        coloredPrinter.print(str, Ansi.Attribute.BOLD, Ansi.FColor.YELLOW, Ansi.BColor.BLACK);
        this.printerReset();
    }

    /**
     * Used to print a string with green font color
     *
     * @param str
     */

    @Override
    public void printWithGreen(String str) {
        coloredPrinter.print(str, Ansi.Attribute.BOLD, Ansi.FColor.GREEN, Ansi.BColor.BLACK);
        this.printerReset();



    }

    public void printWithMagenta(String str){
        coloredPrinter.print(str, Ansi.Attribute.BOLD, Ansi.FColor.MAGENTA, Ansi.BColor.BLACK);
        this.printerReset();

    }

    public void printWithCyan(String str){
        coloredPrinter.print(str, Ansi.Attribute.BOLD, Ansi.FColor.CYAN, Ansi.BColor.BLACK);
        this.printerReset();

    }




    /**
     * Used to reset the terminal color
     */

    @Override
    public void printerReset() {
        coloredPrinter.clear();
    }

    /**
     * Used to return a bold String
     *
     * @param str
     * @return bold string
     */
    @Override
    public String getBold(String str) {
        return "\u001B[1m" + str;

    }

}

