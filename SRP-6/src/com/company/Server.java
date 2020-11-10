package com.company;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Server {
    private BigInteger N;
    private BigInteger g;
    private BigInteger k;
    private BigInteger v;
    private BigInteger A;
    private BigInteger b;
    private BigInteger B;
    private BigInteger u;
    private BigInteger K;
    private BigInteger M;
    private String I;
    private String s;

    private Map<String, Pair<String, BigInteger>> BD = new HashMap<>();

    public Server(BigInteger N, BigInteger g, BigInteger k) {
        this.N = N;
        this.g = g;
        this.k = k;
    }
    public void set_I_s_v(String I, String s, BigInteger v) throws Exception {
        if (!BD.containsKey(I)) {
            BD.put(I, new Pair<>(s, v));
        } else
            throw new Exception();
    }
    public void set_A(BigInteger A) throws IllegalAccessException {
        if (A.equals(BigInteger.ZERO))
            throw new IllegalAccessException();
        else
            this.A = A;
    }

    public BigInteger generate_B() {

        b = new BigInteger(1024, new Random());

        B = (k.multiply(v).add(g.modPow(b, N))).mod(N);
        return B;
    }

    public void generate_u() throws Exception {
        u = SHA256.hash(A, B);
        if (u.equals(BigInteger.ZERO))
            throw new Exception();
    }

    public String get_s(String I) throws Exception {
        if (BD.containsKey(I)) {
            this.I = I;
            s = BD.get(this.I).first;
            v = BD.get(this.I).second;
            return s;
        } else
            throw new Exception();
    }

    public void key() {
        BigInteger S = A.multiply(v.modPow(u, N)).modPow(b, N);
        K = SHA256.hash(S);
    }

    public BigInteger create_M(BigInteger M) {
        BigInteger M_S = SHA256.hash(SHA256.hash(N).xor(SHA256.hash(g)), SHA256.hash(I), s, A, B, K);
        if (M_S.equals(M))
            return SHA256.hash(A, M_S, K);
        else
            return BigInteger.ZERO;
    }

    private class Pair<A, B> {
        A first;
        B second;

        Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }
    }
}

