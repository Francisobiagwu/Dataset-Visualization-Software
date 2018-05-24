import java.util.Scanner;

/**
 * DVSUsername
 * Use this class to create username object
 * @author Francis Obiagwu
 * @version 1
 * @date 5/23/2018
 */
public class DVSUsername {
    private String username = null;
    private String usernameEntered = null;
    private int USERNAME_LENGTH = 5;                // maximum length for username
    private int NUMBER_OF_LETTERS = 3;              // maximum number of alphabets
    private int NUMBER_OF_DIGITS = 2;               // maximum number of digits
    private boolean isUsernameLengthMet = false;    // flag to to ensure the user enters only the acceptable length for username
    private boolean isFirstThreeCharAlpha = false;  // flag set to ensure that the first 3 character is always alphabet
    private boolean isLastThreeCharDigits = false;  // flag set to ensure that the last 3 characters is always digits
    private boolean isUsernameReqMet = false;         // This flag is set to true when all the requirements are met


    /**
     * Used to obtain and set username for the user
     * @return boolean
     */

    public boolean setUsername() {
        Scanner scanner = new Scanner(System.in);                   // obtain input from the user
        System.out.print("Username: ");
        this.usernameEntered = DVSGetString.getString().toLowerCase();    //convert all input to lowercase

        return this.requirementMet();                               //returns true if requirement is met and false if not
    }

    /**
     * Get the user's username
     * @return username
     */

    public String getUsername() {
        return this.username;
    }

    /**
     * Verify if the user entered the a username whose length is equal to the required length
     */

    private void verifyUsernameLen() {
        if (this.usernameEntered.length() == this.USERNAME_LENGTH) {
            this.isUsernameLengthMet = true;
        } else {
            this.isUsernameLengthMet = false;
        }
    }

    /**
     * Verify if the user's entered value have the first three characters as alphabets
     */
    private void verifyFirstThreeChar() {
        if (this.isUsernameLengthMet) { // perform this action only if the user met the username length requirement
            char ch[] = this.usernameEntered.toCharArray();
            int count = 0;

            for (int i = 0; i < this.NUMBER_OF_LETTERS; i++) {
                if (Character.toString(ch[i]).matches(".*[a-z].*")) { // check for alphabet
                    count += 1;

                } else {
                    break;
                }

            }
            if (count == this.NUMBER_OF_LETTERS) {
                this.isFirstThreeCharAlpha = true;
            } else {
                this.isFirstThreeCharAlpha = false;
            }
        }
    }

    /**
     * Verify if the user's entered value have the last three characters as digits
     */

    private void verifyLastThreeChar() {
        if (this.isUsernameLengthMet) {     // perform this action only if the user met the username length requirement
            char ch[] = this.usernameEntered.toCharArray();
            int count = 0;

            for (int i = 3; i < this.USERNAME_LENGTH; i++) {
                if (Character.toString(ch[i]).matches(".*[0-9].*")) { // check for digit
                    count += 1;
                } else {
                    break;
                }
            }
            if (count == this.NUMBER_OF_DIGITS) {
                this.isLastThreeCharDigits = true;
            } else {
                this.isLastThreeCharDigits = false;
            }
        }
    }

    /**
     * Verify if the user met all the requirements necessary for a valid username to be created.
     * @return boolean
     */

    private boolean requirementMet() {

        // call the methods necessary for valid username verification
        this.verifyUsernameLen();
        this.verifyFirstThreeChar();
        this.verifyLastThreeChar();

        if (this.isUsernameLengthMet && this.isFirstThreeCharAlpha && this.isLastThreeCharDigits) {
            this.isUsernameReqMet = true;
            this.username = this.usernameEntered;
            return this.isUsernameReqMet;
        } else {
            this.isUsernameReqMet = false;
            return this.isUsernameReqMet;
        }
    }

    public boolean isUsernameReqMet() {
        return isUsernameReqMet;
    }
}
