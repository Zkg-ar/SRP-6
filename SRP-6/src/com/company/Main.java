package com.company;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class Main {
    private final static SecureRandom random = new SecureRandom();
    private final static BigInteger one     = new BigInteger("1");
    private final static BigInteger two     = new BigInteger("2");
    private final static BigInteger three     = new BigInteger("3");
    public static void main(String[] args) throws Exception {
        BigInteger q = BigInteger.probablePrime(5, random);
        BigInteger N =two.multiply(q).add(one);
        BigInteger g = BigInteger.valueOf(2);
        BigInteger k =three;

        Server server = new Server(N, g, k);

        while (true) {
            System.out.println("1. Зарегестрироваться");
            System.out.println("2. Авторизироваться");
            Scanner input = new Scanner(System.in);
            int choice = input.nextInt();
            switch (choice) {
                case 1: {
                    System.out.println("Введите логин: ");
                    String login = input.next();

                    System.out.println("Введите пароль ");
                    String password = input.next();

                    Client client = new Client(N, g, k, login, password);

                    client.set_s_x_v();
                    String s = client.get_s();
                    BigInteger v = client.get_v();
                    try {
                        server.set_I_s_v(login, s, v);
                    } catch (Exception e) {
                        System.out.println("Имя пользователя уже занято!");
                    }
                    break;
                }

                case 2: {
                    System.out.println("Введите логин: ");
                    String login = input.next();

                    System.out.println("Введите пароль: ");
                    String password = input.next();

                    Client client = new Client(N, g, k, login, password);


                    BigInteger A = client.generate_A();
                    try {
                        server.set_A(A);

                    } catch (Exception e) {
                        System.out.println("A = 0");
                        break;
                    }

                    try {
                        String s = server.get_s(login);
                        BigInteger B = server.generate_B();
                        client.checkB(s, B);
                    } catch (Exception e) {
                        System.out.println("Такого пользователя не существует");
                        break;
                    }

                    try {
                        client.generate_u();
                        server.generate_u();
                    } catch (Exception e) {
                        System.out.println("Соединение утеряно!");
                        break;
                    }

                    client.key();
                    server.key();

                    BigInteger server_R = server.create_M(client.confirmation());

                    if (client.checkR(server_R))
                        System.out.println("Соединение установлено");
                    else
                        System.out.println("Неверный пароль");
                    break;
                }
                default:
                    return;
            }
            System.out.println();
        }
    }
}
