package ru.netology.sql.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.sql.data.User;

import static com.codeborne.selenide.Selenide.*;

public class LoginPage {
    private SelenideElement loginField = $("[data-test-id='login'] input");
    private SelenideElement passwordField = $("[data-test-id='password'] input");
    private SelenideElement loginButton = $("button[data-test-id='action-login']");
    public VerificationPage validLogin(User user) {
        loginField.setValue(user.getLogin());
        passwordField.setValue("qwerty123");
        loginButton.click();
        return new VerificationPage();
    }
}
