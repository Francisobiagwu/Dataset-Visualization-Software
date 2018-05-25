import javafx.print.Printer;

/**
 * @author Francis Obiagwu
 * @version 1
 * @date 5/25/2018
 */

public class DVSUser {
    private String username = null;
    private String password = null;
    private DVSUsername dvsUsername = new DVSUsername();
    private DVSPassword dvsPassword = new DVSPassword();


    public void getUsernameAndPassword(){
        this.setUsername();
        this.setPassword();

    }

    public void setUsername(){
        while(!dvsUsername.isUsernameReqMet()){ // allow the user to enter their username
            dvsUsername.setUsername();
            this.username = dvsUsername.getUsername();
            if(!dvsUsername.isUsernameReqMet()){
                System.out.println("Username is invalid");
                String callerClassName = new Exception().getStackTrace()[1].getClassName();
//                System.out.println(callerClassName);
//                String calleeClassName = new Exception().getStackTrace()[0].getClassName();
                if (callerClassName.equals(new DVSNewUser().getClassName())){
                    dvsPassword.printRequirementStatus();
                }
            }
        }
    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword() {
        while(!dvsPassword.isPasswordReqMet()){ // allow the user to enter their username
            dvsPassword.setPassword();
            if(!dvsPassword.isPasswordReqMet()){
                System.out.println("Password is invalid");
                String callerClassName = new Exception().getStackTrace()[1].getClassName();
//                System.out.println("callerClassName: "+callerClassName);
                if (callerClassName.equals(new DVSNewUser().getClassName())){
                    dvsPassword.printRequirementStatus();
                }

            }
        }

    }

    static class DVSNewUser extends DVSUser{

        @Override
        public void getUsernameAndPassword() {
            this.setUsername();
            this.setPassword();

        }

        private String getClassName(){
            return this.getClass().getName();
        }

        @Override
        public void setUsername() {
            super.setUsername();
        }

        @Override
        public String getUsername() {
            return super.getUsername();
        }

        @Override
        public String getPassword() {
            return super.getPassword();
        }

        @Override
        public void setPassword() {
            super.setPassword();
        }
    }


}



