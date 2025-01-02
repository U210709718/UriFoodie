package com.example.urifoodie;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.urifoodie.PasswordValidator;

public class PasswordValidatorTest {

    @Test
    public void testPasswordTooShort() {
        assertFalse(PasswordValidator.isPasswordStrong("Ab1!")); // Less than 8 characters
    }

    @Test
    public void testPasswordWithoutUppercase() {
        assertFalse(PasswordValidator.isPasswordStrong("abc123!@#")); // No uppercase letter
    }

    @Test
    public void testPasswordWithoutLowercase() {
        assertFalse(PasswordValidator.isPasswordStrong("ABC123!@#")); // No lowercase letter
    }

    @Test
    public void testPasswordWithoutDigit() {
        assertFalse(PasswordValidator.isPasswordStrong("Abcdef!@#")); // No digit
    }

    @Test
    public void testPasswordWithoutSpecialChar() {
        assertFalse(PasswordValidator.isPasswordStrong("Abcdef123")); // No special character
    }

    @Test
    public void testValidPassword() {
        assertTrue(PasswordValidator.isPasswordStrong("Abc123!@#")); // Meets all criteria
    }

    @Test
    public void testNullPassword() {
        assertFalse(PasswordValidator.isPasswordStrong(null)); // Null input
    }

    @Test
    public void testEmptyPassword() {
        assertFalse(PasswordValidator.isPasswordStrong("")); // Empty input
    }
}
