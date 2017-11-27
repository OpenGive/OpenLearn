package org.openlearn.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    // Regex for acceptable logins
	// ^$ : Checks whole string
	// (?=.*[]) : Must contain at least one (letter, digit, and special character)
	// {8,100} : Must be between 8 and 100 characters in length, inclusive
	public static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d!@#$%^&*()_+]{8,100}$";

    public static final String SYSTEM_ACCOUNT = "system";

    private Constants() {
    }
}
