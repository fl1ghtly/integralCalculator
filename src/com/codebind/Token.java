package com.codebind;
import java.util.*;

public class Token {
    private int verbosity = 1;
    private Logger logger = new Logger(false);

    public static String Variable = "x";
    private String txt;
    private int precedence;
    private int assoc;
    private double value;
    private TokenType type;

    public Token(String s)
    {
        if (s != null)
        {
            this.txt = s;
            this.type = checkType(this.txt);
            this.precedence = 0;
            this.assoc = 0;

            if (this.type == TokenType.OP)
            {
                this.setPrecedence();
                this.setAssociation();
            }
            else if (this.type == TokenType.NUM)
            {
                this.setValue();
            }

            logger.log(this.toString());
        }
    }

    public static TokenType checkType(String s)
    {
        if (isOperator(s))
        {
            return TokenType.OP;
        }
        else if (isFunction(s))
        {
            return TokenType.FUNC;
        }
        else if (isNumber(s))
        {
            return TokenType.NUM;
        }
        else if (s.equals(Variable))
        {
            return TokenType.SYMBOL;
        }
        else if (s.equals("("))
        {
            return TokenType.LPAR;
        }
        else if (s.equals(")"))
        {
            return TokenType.RPAR;
        }

        return TokenType.NONE;
    }

    private void setValue()
    {
        HashMap<String, Double> values = new HashMap<>();
        values.put("pi", Math.PI);
        values.put("e", Math.E);
        values.put(Variable, 0.0);

        if (this.txt.matches("[a-zA-Z]+"))
        {
            this.value = values.get(this.txt);
        }
        else
        {
            this.value = Float.parseFloat(this.txt);
        }
    }

    private void setPrecedence()
    {
        HashMap<String, Integer> precedence = new HashMap<>();
        precedence.put("^", 4);
        precedence.put("*", 3);
        precedence.put("/", 3);
        precedence.put("+", 2);
        precedence.put("-", 2);

        this.precedence = precedence.get(this.txt);
    }

    private void setAssociation()
    {
        HashMap<String, Integer> associativity = new HashMap<>();
        associativity.put("^", 1);
        associativity.put("*", 0);
        associativity.put("/", 0);
        associativity.put("+", 0);
        associativity.put("-", 0);

        this.assoc = associativity.get(this.txt);
    }

    private static boolean isOperator(String s)
    {
        String[] operators = {"+", "-", "^", "*", "/"};
        for (String operator : operators)
        {
            if (s.equals(operator))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean isFunction(String s)
    {
        String[] funcs = {"sin", "cos", "tan", "arcsin", "arccos", "arctan",
                "csc", "sec", "cot", "arccsc", "arcsec", "arccot",
                "sinh", "cosh", "tanh", "arcsinh", "arccosh", "arctanh",
                "csch", "sech", "coth", "arccsch", "arcsech", "arccoth",
                "ln", "log", "sqrt", "abs"};

        for (String func : funcs)
        {
            if (s.equals(func))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean isNumber(String s)
    {
        String[] specials = {"pi", "e", "."};
        try
        {
            for (String special : specials)
            {
                if (s.equals(special))
                {
                    return true;
                }
            }
            Float.parseFloat(s);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public TokenType getType()
    {
        return this.type;
    }

    public void setType(TokenType t)
    {
        this.type = t;
    }

    public int getPrecedence()
    {
        return this.precedence;
    }
    public int getAssoc()
    {
        return this.assoc;
    }

    public double getValue()
    {
        return this.value;
    }

    public void setValue(Double v)
    {
        this.value = v;
    }

    public String getTxt()
    {
        return this.txt;
    }

    public void setVerbosity(int verb)
    {
        verbosity = verb;
    }

    public void setLogger(boolean b)
    {
        logger.setShowLog(b);
    }

    public String toString()
    {
        if (verbosity == 0)
        {
            return txt;
        }
        else if (verbosity == 1)
        {
            return String.format("Text: %s\n", txt);
        }
        else if (verbosity == 2)
        {
            return String.format("Text: %s\nType: %s\n", txt, type);
        }
        else
        {
            return String.format("Text: %s\nType: %s\nPrecedence: %d\nAssociation: %d\nValue: %f\n", txt, type, precedence, assoc, value);
        }
    }
}