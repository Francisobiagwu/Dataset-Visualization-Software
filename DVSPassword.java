import java.util.*;

/**
 * DVSPassword
 * This class is used to create a valid password implementing security quality attribute
 * @author Francis Obiagwu
 * @date 5/22/2018
 * @version 1
 */
public class DVSPassword {
    private static final int MIN_PASSWORD_LEN = 8;      // minimum password length
    private static final int MAX_PASSWORD_LEN = 15;     // maximum password length
    private boolean hasLetter = false;         // letter flag
    private boolean hasDigit = false;          // digit flag
    private boolean hasSpecialChar = false;    // special character flag
    private boolean sameCharUsedTwice = false; // same character flag
    private boolean noSpace = false;           // space checker
    private boolean passwordLenMet = false;    // password length requirement checker
    private boolean isPasswordValid = false;   // main flag, sets to true if all requirements are met
    private String password = null;            // valid password
    private String userEnteredPassword = null; // user entered password


    /**
     * Used to obtain password from the user
     * @return boolean
     */
    public boolean setPassword() {
        System.out.print("Password: ");
        Scanner scanner = new Scanner(System.in);
        String user_password = scanner.nextLine();
        this.userEnteredPassword = user_password;

        // verify all the requirements
        this.hasLetterVerify();
        this.hasDigitVerify();
        this.hasSpecialCharVerify();
        this.containSpaceVerify();
        this.sameCharUsedTwiceVerify();
        this.verifyPasswordLen();

        if (this.hasLetter && this.hasDigit && this.hasSpecialChar && !this.noSpace && !this.sameCharUsedTwice && this.passwordLenMet){
            // if all requirement are met
            this.isPasswordValid = true;
            this.password = this.userEnteredPassword;
            return this.isPasswordValid;
        }
        else{
            // if the user didn't meet the requirement
            return this.isPasswordValid;
        }
    }

    /**
     * Get password
     * @return user's password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Verify if the user entered password contains a letter
     */
    private void hasLetterVerify() {
        if (this.userEnteredPassword.matches(".*[a-z].*")) { // check for lowercase
            this.hasLetter = true;
        } else if (userEnteredPassword.matches(".*[A-Z].*")) { // check for uppercase
            this.hasLetter = true;
        }
    }

    /**
     * Check if the user entered password contains a digit
     */

    private void hasDigitVerify() {
        if (this.userEnteredPassword.matches(".*[0-9].*")) { // check for digit
            this.hasDigit = true;

        }
    }

    /**
     * Check if the user entered password contains special character
     */
    private void hasSpecialCharVerify() {
        if (this.userEnteredPassword.matches(".*[!@#$%^&*()_+|<>?{}\\[\\]~-].*")) { // check for special character
            this.hasSpecialChar = true;
        }
    }

    /**
     * Check if the user entered password contains space
     */

    private void containSpaceVerify() {
        if (this.userEnteredPassword.matches(".*[\\s].*")) { // check for space
            this.noSpace = true;
        }
    }

    /**
     * Verify if the user used the same character more than once
     */

    private void sameCharUsedTwiceVerify() {
        String str_array[] = this.userEnteredPassword.split("");

        outerLoop:
        for (int i = 0; i < str_array.length; i++) {
            int count = 0;
            for (int j = 0; j < str_array.length; j++) {
                if (str_array[i].equalsIgnoreCase(str_array[j])) {
                    count += 1;
                    if (count > 1) {
                        this.sameCharUsedTwice = true;
                        break outerLoop;
                    }
                }
            }
        }
    }

    /**
     * Verify if the user met the password length requirement
     */
    private void verifyPasswordLen(){
        if ((this.userEnteredPassword.length()>= 8) && (this.userEnteredPassword.length()<=15) ){ // length checker
            this.passwordLenMet = true;
        }
    }


}
