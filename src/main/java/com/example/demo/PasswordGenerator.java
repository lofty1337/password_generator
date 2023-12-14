package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.security.SecureRandom;

public class PasswordGenerator extends Application {

    private static final String CHARACTERS_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String CHARACTERS_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHARACTERS_DIGITS = "0123456789";
    private static final String CHARACTERS_SPECIAL = "!@#$%^&*()-_=+";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Password Generator");

        // UI
        Label passwordLabel = new Label("Your Password: ");
        Label strengthLabel = new Label("Password Strength: ");
        Button generateButton = new Button("Generate Password");
        Button copyButton = new Button("Copy to Clipboard");
        CheckBox showPasswordCheckBox = new CheckBox("Show Password");

        // TextFields
        TextField lengthTextField = new TextField();
        lengthTextField.setPromptText("Length");

        CheckBox uppercaseCheckBox = new CheckBox("Uppercase");
        CheckBox lowercaseCheckBox = new CheckBox("Lowercase");
        CheckBox digitsCheckBox = new CheckBox("Digits");
        CheckBox specialCheckBox = new CheckBox("Special Characters");

        // Clipboard
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();

        // Event handling
        generateButton.setOnAction(e -> {
            try {
                int passwordLength = Integer.parseInt(lengthTextField.getText());

                if (passwordLength <= 0) {
                    passwordLabel.setText("Your Password: Invalid Input");
                    return;
                }

                if (!uppercaseCheckBox.isSelected() && !lowercaseCheckBox.isSelected() &&
                        !digitsCheckBox.isSelected() && !specialCheckBox.isSelected()) {
                    passwordLabel.setText("Your Password: Select at least one character type");
                    return;
                }

                String password = generatePassword(passwordLength, uppercaseCheckBox.isSelected(),
                        lowercaseCheckBox.isSelected(), digitsCheckBox.isSelected(), specialCheckBox.isSelected());

                if (showPasswordCheckBox.isSelected()) {
                    passwordLabel.setText("Your Password: " + password);
                    clipboardContent.putString(password);
                } else {
                    passwordLabel.setText("Your Password: " + maskPassword(password));
                    clipboardContent.putString(password);
                }

                // Evaluate password strength
                int strength = evaluatePasswordStrength(password);
                String strengthText = getStrengthText(strength);
                strengthLabel.setText("Password Strength: " + strengthText);

            } catch (NumberFormatException ex) {
                passwordLabel.setText("Your Password: Invalid Input");
            }
        });

        copyButton.setOnAction(e -> {
            // Use the content from the clipboard
            clipboard.setContent(clipboardContent);
        });

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().addAll(passwordLabel, generateButton, showPasswordCheckBox, lengthTextField,
                uppercaseCheckBox, lowercaseCheckBox, digitsCheckBox, specialCheckBox, copyButton, strengthLabel);

        // Scene
        Scene scene = new Scene(layout, 300, 400);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private String generatePassword(int length, boolean useUppercase, boolean useLowercase, boolean useDigits, boolean useSpecial) {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        String allCharacters = "";

        if (useUppercase) {
            allCharacters += CHARACTERS_UPPERCASE;
        }
        if (useLowercase) {
            allCharacters += CHARACTERS_LOWERCASE;
        }
        if (useDigits) {
            allCharacters += CHARACTERS_DIGITS;
        }
        if (useSpecial) {
            allCharacters += CHARACTERS_SPECIAL;
        }

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allCharacters.length());
            password.append(allCharacters.charAt(randomIndex));
        }

        return password.toString();
    }

    private String maskPassword(String password) {
        return "*".repeat(password.length());
    }

    private int evaluatePasswordStrength(String password) {
        // Evaluate password strength based on length and character types
        int strength = 0;

        // Length
        if (password.length() >= 8) {
            strength++;
        }
        if (password.length() >= 12) {
            strength++;
        }

        // Character types
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean hasDigits = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[" + CHARACTERS_SPECIAL + "].*");

        if (hasUppercase) {
            strength++;
        }
        if (hasLowercase) {
            strength++;
        }
        if (hasDigits) {
            strength++;
        }
        if (hasSpecial) {
            strength++;
        }

        return strength;
    }

    private String getStrengthText(int strength) {
        switch (strength) {
            case 0:
            case 1:
                return "Weak";
            case 2:
            case 3:
                return "Moderate";
            case 4:
            case 5:
                return "Strong";
            case 6:
            case 7:
                return "Very Strong";
            default:
                return "Exceptional";
        }
    }
}