package ru.netology.sql.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    private SelenideElement heading = $("h1");

    public DashboardPage() {
        heading.shouldHave(Condition.text("Ваши карты"), Condition.visible);
    }
}
