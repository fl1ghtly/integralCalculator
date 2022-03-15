package com.codebind;

import java.util.List;

public class HelperFunctions {
    public static boolean in(String s, List<String> sArr)
    {
        for (String str : sArr)
        {
            if (s.equals(str))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean in(TokenType t, List<TokenType> tArr)
    {
        for (TokenType token : tArr)
        {
            if (t.equals(token))
            {
                return true;
            }
        }
        return false;
    }

    public static double factorial(double n)
    {
        return gamma(n + 1);
    }

    public static double gamma(double n)
    {
        double p1 = Math.sqrt(2 * Math.PI / n);
        double p2 = (1 / Math.E) * (n + 1/(12 * n - (1/(10*n)) ) );
        return p1 * Math.pow(p2, n);
    }
}

