/**
 * DVSClient
 * This class is the commandline version of the client side.
 *
 * @author Francis Obiagwu
 * @version 1
 * @date 5/23/2018
 */

public class DVSClient {

    public static void main(String[] args) {

        DVSPrinter printer = new DVSPrinter();
        DVSInstructions dvsInstruction = new DVSInstructions();
        DVSSecurityQuestions securityQuestions = new DVSSecurityQuestions();

        printer.printWithGreen(dvsInstruction.introduction);
        System.out.println(dvsInstruction.features);
        System.out.println(dvsInstruction.openingInstruction);

        System.out.print("\n> ");
        int userSelection = DVSGetInteger.getInt();
        DVSUsername dvsUsername = new DVSUsername();
        DVSUser user = new DVSUser();
        DVSUser.DVSNewUser newUser = new DVSUser.DVSNewUser();
        DVSPassword dvsPassword = new DVSPassword();
        DVSSecurityQuestions dvsSecurityQuestionsObj = new DVSSecurityQuestions();

        switch (userSelection){
            case 1:
                user.getUsernameAndPassword();
                // now interact with the database to verify if the credentials are valid
                //if credentials are valid
                System.out.println();
                printer.printWithYellow(dvsInstruction.line);
                printer.printWithYellow("\nSelect visualization option");
                System.out.println("\n"+dvsInstruction.visualizationInstruction);

                break;

            case 2:
                printer.printWithCyan(dvsInstruction.line);
                printer.printWithCyan("\nUsername creation requirement");
                dvsUsername.printAcceptableUsernameInstruction();
                newUser.setUsername();
                System.out.println(dvsInstruction.line);
                printer.printWithCyan(dvsInstruction.line);
                printer.printWithCyan("\nAcceptable password");
                dvsPassword.printAcceptablePasswordInstruction();
                newUser.setPassword();
                printer.printWithCyan(dvsInstruction.line);
                System.out.println();
                printer.printWithCyan("Security questions\n");
                dvsSecurityQuestionsObj.setUserSecurityQuestionsAndAnswers();
                break;

            case 3:
                // call function or class that will interact with the database to retrieve user information to enable
                // resetting of password
                break;

            case 4:
                System.exit(0);
                break;
        }

    }
}
