import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * DVSSecurityQuestions
 * This class is used to setup and retrieve security questions for password reset
 * @author Francis Obiagwu
 * @version 1
 * @date 5/23/2018
 */
public class DVSSecurityQuestions {
    private Map<String, String> userSecurityQuestionsAndAnswers = new HashMap<>();
    private static String question0 = "In what city where you born?";
    private static String question1 = "What is the color of your first car?";
    private static String question2 = "Where did you travel to the first time you flew";
    private static String question3 = "What is the your eldest sibling's middle name";
    private static String[] defaultSecurityQuestions = new String[4];
    private static final int MAX_USER_SECURITY_QUESTION = 2;

    DVSSecurityQuestions() {
        this.updateQuestions();
    }

    public void updateQuestions() {
        this.defaultSecurityQuestions[0] = this.question0;
        this.defaultSecurityQuestions[1] = this.question1;
        this.defaultSecurityQuestions[2] = this.question2;
        this.defaultSecurityQuestions[3] = this.question3;

    }


    public void setUserSecurityQuestions() {
        System.out.println("Setup security question in case you forget your password");
        System.out.println("Enter next if you wish to skip to the next question");
        Scanner scanner = new Scanner(System.in);
        String userInput;

        outerloop:
        for (int i = 0; i < this.defaultSecurityQuestions.length; i++) {
            System.out.println(this.defaultSecurityQuestions[i]);
            userInput = scanner.nextLine();

            // if the user enters next, skip to the next question
            while (userInput.equalsIgnoreCase("next")) {
                i += 1;
                if (i >= this.defaultSecurityQuestions.length) {
                    break outerloop;
                }
                System.out.println(this.defaultSecurityQuestions[i]);
                userInput = scanner.nextLine();
            }

            // update the user's security question with answers provided
            this.userSecurityQuestionsAndAnswers.put(this.defaultSecurityQuestions[i], userInput);

            if (this.userSecurityQuestionsAndAnswers.size() == this.MAX_USER_SECURITY_QUESTION) {
                // if the user have entered up to 2 security question answers then break
                break;
            } else {
                continue;
            }
        }

        if (this.userSecurityQuestionsAndAnswers.size() != this.MAX_USER_SECURITY_QUESTION) {
            System.out.println("Setup your personalized security question");
            while (this.userSecurityQuestionsAndAnswers.size() != this.MAX_USER_SECURITY_QUESTION) {
                System.out.print("Question: ");
                String userQuestion = scanner.nextLine();
                System.out.print("Answer: ");
                String userAnswer = scanner.nextLine();

                if (userQuestion.length() >= 1 && userAnswer.length() >= 1) {
                    this.addSecurityQuestionAndAnswers(userQuestion, userAnswer);

                } else {
                    System.out.println("Please provide a valid entry");
                    continue;
                }

            }
        }
    }

    /**
     * Allow the user to specify a security question and the answer
     *
     * @param securityQuestion
     * @param answer
     */

    public void addSecurityQuestionAndAnswers(String securityQuestion, String answer) {
        this.userSecurityQuestionsAndAnswers.put(securityQuestion, answer);

    }

    /**
     * Get the user's security questions and the answers
     *
     * @return hashMap of user's security Q and A
     */

    public Map<String, String> getUserSecurityQuestionsAndAnswers() {
        return this.userSecurityQuestionsAndAnswers;
    }

    /**
     * Allow the calling program to create security questions and answers for the user
     *
     * @param userSecurityQuestionsAndAnswers
     */

    public void setUserSecurityQuestionsAndAnswers(Map<String, String> userSecurityQuestionsAndAnswers) {
        this.userSecurityQuestionsAndAnswers = userSecurityQuestionsAndAnswers;
    }


    /**
     * Get the first question
     *
     * @return first question
     */

    public String getQuestion0() {
        return this.question0;
    }


    /**
     * Allows the calling program to modify the first question
     */

    public void setQuestion0(String question0) {
        this.question0 = question0;
        this.updateQuestions(); // update the default question array to reflect the change
    }

    /**
     * Get the second security question
     *
     * @return 2nd security question
     */

    public String getQuestion1() {
        return this.question1;

    }

    /**
     * change the second security question
     *
     * @param question1
     */

    public void setQuestion1(String question1) {
        this.question1 = question1;
        this.updateQuestions();     // update the default question array to reflect the change
    }

    /**
     * Get the third security question
     *
     * @return 3rd security question
     */

    public String getQuestion2() {
        return this.question2;
    }

    /**
     * Change the 3rd security question
     * @param question2
     */

    public void setQuestion2(String question2) {
        this.question2 = question2;
        this.updateQuestions(); // update the default question array to reflect the change
    }

    /**
     * Get the 4th security question
     * @return 4th security question
     */

    public String getQuestion3() {
        return this.question3;
    }

    /**
     * Change the 4th security question
     * @param question3
     */

    public void setQuestion3(String question3) {
        this.question3 = question3;
        this.updateQuestions();
    }


    /**
     * Get the all the security questions
     * @return array of default security questions
     */

    public String[] getDefaultSecurityQuestions() {
        return this.defaultSecurityQuestions;
    }

    /**
     * Set all the default security question at once
     * @param defaultSecurityQuestions
     */

    public void setDefaultSecurityQuestions(String[] defaultSecurityQuestions) {
        this.defaultSecurityQuestions = defaultSecurityQuestions;
    }
}
