package ru.se.ifmo;


import ru.se.ifmo.model.User;
import ru.se.ifmo.module.PromptScanner;
import ru.se.ifmo.table.UsersTable;

import java.util.Arrays;
import java.util.Scanner;

public class AuthenticationController {
    private final Scanner scanner = PromptScanner.getUserScanner();
    private final UsersTable usersTable;

    public AuthenticationController(UsersTable usersTable) {
        this.usersTable = usersTable;
    }

    public User auth() {
        boolean repeat = false;
        System.out.println("Привет пользователь! Выбери способ авторизации:");
        User user;
        while (true) {
            if (repeat) {
                System.out.println("\n\nПопробуйте еще раз! Выберите способ авторизации:");
            }
            repeat = true;
            System.out.println("1. Войти в аккаунт");
            System.out.println("2. Зарегистрировать новый аккаунт");
            System.out.print("Введите номер варианта чтобы продолжить: ");
            int number;
            try {
                number = scanner.nextInt();
            } catch (ClassCastException e) {
                System.out.println("Введите число!");
                continue;
            }
            if (number == 1) {
                System.out.print("Введите ваш юзернейм: ");
                String username = scanner.next();
                String password = getPassword();
                if (password == null) {
                    continue;
                }
                user = usersTable.getUser(username);
                if (user == null || !MD2Encoder.verify(password, user.getPassword())) {
                    System.out.println("Введены неверные данные попробуйте еще раз или зарегистрируйте нового пользователя");
                } else {
                    user.setPassword(MD2Encoder.encode(password));
                    System.out.printf("Вы успешно авторизовались под юзернеймом - %s. Вам открыт доступ к программе%n", user.getUsername());
                    break;
                }
            } else if (number == 2) {
                System.out.print("Введите юзернейм для вашего пользователя: ");
                String username = scanner.next();
                if (username == null || username.isEmpty()) {
                    System.out.println("Юзернейм не может быть пустым");
                    continue;
                }
                if (usersTable.isUnique(username)) {
                    System.out.println("Данный юзернейм уже существует в базе данных, используйте другой юзернейм");
                    continue;
                }
                String password = createPassword();
                if (password == null) {
                    continue;
                }
                user = new User(username, MD2Encoder.encode(password));
                usersTable.insertUser(user);
                break;
            } else {
                System.out.println("Введите 1 либо 2 чтобы продолжить");
            }
        }
        return user;
    }

    private String getPassword() {
        String password;
        if (System.console() != null) {
            password = Arrays.toString(System.console().readPassword("Введите ваш пароль: "));
        } else {
            System.out.print("Введите ваш пароль: ");
            password = scanner.next();
        }
        if (password == null || password.isEmpty() || password.isBlank()) {
            System.out.println("Пароль не может быть пустым");
            return null;
        }
        return password;
    }

    private String createPassword() {
        String password;
        if (System.console() != null) {
            password = Arrays.toString(System.console().readPassword("Введите ваш пароль: "));
        } else {
            System.out.print("Введите ваш пароль: ");
            password = scanner.next();
        }
        if (password == null || password.isEmpty() || password.isBlank()) {
            System.out.println("Пароль не может быть пустым");
            return null;
        }
        if (password.length() < 6) {
            System.out.println("Пароль должен содержать минимум 6 символов");
            return null;
        }
        if (!password.matches(".*\\d.*")) {
            System.out.println("Пароль должен содержать цифры");
            return null;
        }
        String password2;
        if (System.console() != null) {
            password2 = Arrays.toString(System.console().readPassword("Введите ваш пароль еще раз: "));
        } else {
            System.out.print("Введите ваш пароль еще раз: ");
            password2 = scanner.next();
        }
        if (!password.equals(password2)) {
            System.out.println("Пароли не совпадают!");
            return null;
        }
        return password;
    }
}
