package pt.psoft.g1.psoftg1.usermanagement.model;

import java.beans.Transient;

public class Password {
    private String password;

    private final String regexValid = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9\\W]).{8,}$";

    public Password(String newPasswd) {
        updatePassword(newPasswd);
    }

    public void updatePassword(String newPasswd) {
        if(newPasswd == null || newPasswd.isEmpty() || !newPasswd.matches(regexValid)) {
            throw new IllegalArgumentException("Given Password is not valid. It must contain at least 8 characters, 1 upper case letter, 1 lower case letter and 1 number or special character.");
        }

        this.password = newPasswd;
    }
}
