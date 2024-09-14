package ru.itmo.common.utility;

public interface UserVerificationService {
    /**
     * Метод для проверки пароля пользователя.
     * @param username имя пользователя.
     * @param password пароль пользователя.
     * @return true, если пароль верный, иначе false.
     */
    boolean verifyUserPassword(String username, String password);
}
