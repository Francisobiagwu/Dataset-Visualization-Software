/**
 * @author Francis Obiagwu
 * @version 1
 * @date 5/24/2018
 */
public interface DVSPrinterInterface {
    void printBold(String str);
    void printWithBlue(String str);
    void printWithRed(String str);
    void printWithYellow(String str);
    void printWithGreen(String str);
    void printWithCyan(String str);
    void printWithMagenta(String str);
    void printerReset();
    String getBold(String str);

}
