package com.company;

import java.math.BigInteger;
import java.util.Random;

public class Client {
    private BigInteger N;
    private BigInteger g;
    private BigInteger k;
    private BigInteger x;
    private BigInteger v;
    private BigInteger u;
    private BigInteger a;
    private BigInteger A;
    private BigInteger B;
    private BigInteger M;
    private BigInteger K;
    private String I;
    private String p;
    private String s;
    private  int lenght = 9;
    public Client(BigInteger N, BigInteger g, BigInteger k, String I, String p) {
        this.N = N;
        this.g = g;
        this.k = k;
        this.I = I;
        this.p = p;
    }

    public void set_s_x_v() {
        s = generate_s(lenght);
        x = SHA256.hash(s, p);
        v = g.modPow(x, N);
    }
    public String generate_s(int lenght) {
        String alphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
        Random rnd = new Random();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < lenght; ++i) {
            s.append(alphabet .charAt(rnd.nextInt(alphabet .length())));
        }
        return s.toString();
    }
    public BigInteger generate_A() {
        a = new BigInteger(1024, new Random());
        A = g.modPow(a, N);
        return A;
    }
    public void checkB(String s, BigInteger B) throws Exception {
        this.s = s;
        this.B = B;
        if (B.equals(BigInteger.ZERO))
            throw new Exception();
    }

    public void generate_u() throws  Exception {
        u = SHA256.hash(A, B);
        if (u.equals(BigInteger.ZERO))
            throw new Exception();
    }
    public void key() {
        x = SHA256.hash(s, p);
        BigInteger S = (B.subtract(k.multiply(g.modPow(x, N)))).modPow(a.add(u.multiply(x)), N);
        K = SHA256.hash(S);
    }
    public BigInteger confirmation() {
        M = SHA256.hash(SHA256.hash(N).xor(SHA256.hash(g)), SHA256.hash(I), s, A, B, K);
        return M;
    }
    public boolean checkR(BigInteger R) {
        BigInteger R_C = SHA256.hash(A, M, K);
        return R_C.equals(R);
    }

    public String get_s() {
        return s;
    }

    public BigInteger get_v() {
        return v;
    }

}
