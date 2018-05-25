/**
 * DVSPassword
 * This class is used to create a valid password implementing security quality attribute
 *
 * @author Francis Obiagwu
 * @version 1
 * @date 5/22/2018
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
    private boolean isPasswordReqMet = false;   // main flag, sets to true if all requirements are met
    private String password = null;            // valid password
    private String userEnteredPassword = null; // user entered password


    /**
     * Used to obtain password from the user
     *
     * @return boolean
     */
    public boolean setPassword() {
        System.out.print("Password: ");
        this.userEnteredPassword = DVSGetString.getString();

        // verify all the requirements
        this.hasLetterVerify();
        this.hasDigitVerify();
        this.hasSpecialCharVerify();
        this.containSpaceVerify();
        this.sameCharUsedTwiceVerify();
        this.verifyPasswordLen();

        if (this.hasLetter && this.hasDigit && this.hasSpecialChar && !this.noSpace && !this.sameCharUsedTwice && this.passwordLenMet) {
            // if all requirement are met
            this.isPasswordReqMet = true;
            this.password = this.userEnteredPassword;
            return this.isPasswordReqMet;
        } else {
            // if the user didn't meet the requirement
            this.isPasswordReqMet = false;
            return this.isPasswordReqMet;
        }
    }

    /**
     * Get password
     *
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
        for (String str1 : str_array) {
            int count = 0;
            for (String str2 : str_array) {
                if (str1.equalsIgnoreCase(str2)) {
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
    private void verifyPasswordLen() {
        if ((this.userEnteredPassword.length() >= 8) && (this.userEnteredPassword.length() <= 15)) { // length checker
            this.passwordLenMet = true;
        }
    }

    public boolean isPasswordReqMet() {
        return isPasswordReqMet;
    }

    public void setPasswordReqMet(boolean passwordReqMet) {
        isPasswordReqMet = passwordReqMet;
    }

    public void printRequirementStatus() {
        String str = "hasLetter:" + hasLetter +
                "    \nhasDigit: " + hasDigit +
                "    \nhasSpecialChar: " + hasSpecialChar +
                "    \nsameCharUsedTwice: " + sameCharUsedTwice +
                "    \nnoSpace: " + noSpace;
        new DVSPrinter().printWithRed(str);
        System.out.println();
    }

    public void printAcceptablePasswordInstruction() {
        System.out.println("\nAt least 8 character long but not more than 15" +
                "\n Must contain at least one Upper case alphabet" +
                "\n Must contan at least one lower case alphabet" +
                "\n No character must appear more than once");


    }

    public void newUserPassword(){
        while(!this.isPasswordReqMet()){ // allow the user to enter their username
            this.setPassword();
            if(!this.isPasswordReqMet){
                System.out.println("Password is invalid");
                this.printRequirementStatus();
            }
        }

    }
}
