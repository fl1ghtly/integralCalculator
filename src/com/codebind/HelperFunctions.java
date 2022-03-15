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

    public static int factorial(int n)
    {
        int a = 1;
        for(int i =0; i <  n; i++)
        {
            a = a * (i +1);
        }
        return a;
    }
}

