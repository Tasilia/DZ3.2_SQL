package ru.netology.sql;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.sql.data.User;
import ru.netology.sql.page.LoginPage;

import java.sql.DriverManager;

import static com.codeborne.selenide.Selenide.open;

public class TestAppDeadline {
    Faker faker = new Faker();
    String id = faker.internet().uuid();

    @BeforeEach
    @SneakyThrows
    void setUp() {
        //var faker = new Faker();
        var runner = new QueryRunner();
        var dataUserSQL = "INSERT INTO users(id, login, password, status) VALUES (?, ?, ?, ?);";
        var dataCodeSQL = "INSERT INTO auth_codes(id, user_id, code) VALUES (?, ?, ?);";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );

        ) {
            runner.update(conn, dataUserSQL, id, faker.name().username(),
                    "$2a$10$vOI8z98kD.sqR4Duck9C4epThpi.6s8nQ0b3GpaIO1/zk4OadalEK", "active");
            runner.update(conn, dataCodeSQL, faker.internet().uuid(), id,
                    faker.numerify("######"));
        }
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @SneakyThrows
    void stubTest() {
        var userSQL = "SELECT * FROM users WHERE id = ?;";
        var codeSQL = "SELECT code FROM auth_codes WHERE user_id = ?;";
        var runner = new QueryRunner();
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            var user = runner.query(conn, userSQL, id, new BeanHandler<>(User.class));
            var code = runner.query(conn, codeSQL, id, new ScalarHandler<>());
            var loginPage = new LoginPage();
            var verificationPage = loginPage.validLogin(user);
            var dashboardPage = verificationPage.validVerify(code.toString());
        }
    }
}
