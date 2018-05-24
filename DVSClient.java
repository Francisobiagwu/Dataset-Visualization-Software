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
        DVSInstructions dvsInstruction = new DVSInstructions();

        DVSSecurityQuestions securityQuestions = new DVSSecurityQuestions();
        System.out.println(dvsInstruction.openingInstruction);
        int userSelection = DVSGetInteger.getInt();
        DVSUsername dvsUsername = new DVSUsername();
        DVSPassword dvsPassword = new DVSPassword();
        DVSSecurityQuestions dvsSecurityQuestionsObj = new DVSSecurityQuestions();

        switch (userSelection){
            case 1:
                dvsUsername.setUsername(); // allow the user to enter their username
                dvsPassword.setPassword(); // allow the user to enter their password

                // verify if the user input meets the username and password requirement
                if(dvsPassword.isPasswordReqMet() && dvsUsername.isUsernameReqMet()){
                    // now interact with the database to verify if the credentials are valid
                    //if credentials are valid
                    System.out.println(dvsInstruction.visualizationInstruction);
                }
                else{
                    dvsUsername.printRequirementStatus();
                    dvsPassword.printRequirementStatus();
                    System.out.println("Credentials are invalid");
                }

                break;

            case 2:
                dvsUsername.setUsername();
                System.out.println("--------------------------");
                dvsPassword.setPassword();
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
