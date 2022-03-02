package com.codebind;
import java.util.*;

public class Token {
    public static String Variable = "x";
    private String txt;
    private int precedence;
    private int assoc;
    private double value;
    private TokenType type;

    public Token(String s)
    {
        this.txt = s;
        this.precedence = 0;
        this.assoc = 0;
        this.setType();
    }

    private void setType()
    {
        if (this.isOperator())
        {
            this.type = TokenType.OP;
            this.setPrecedence();
            this.setAssociation();
        }
        else if (this.isFunction())
        {
            this.type = TokenType.FUNC;
        }
        else if (this.isNumber())
        {
            this.type = TokenType.NUM;
            this.setValue();
        }
        else if (this.txt.equals("("))
        {
            this.type = TokenType.LPAR;
        }
        else if (this.txt.equals(")"))
        {
            this.type = TokenType.RPAR;
        }
    }

    private void setValue()
    {
        HashMap<String, Double> values = new HashMap<>();
        values.put("pi", Math.PI);
        values.put("e", Math.E);
        values.put(this.Variable, 0.0);

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

    private boolean isOperator()
    {
        String[] operators = {"+", "-", "^", "*", "/"};
        for (String operator : operators)
        {
            if (this.txt.equals(operator))
            {
                return true;
            }
        }
        return false;
    }

    private boolean isFunction()
    {
        String[] funcs = {"sin", "cos", "tan", "arcsin", "arccos", "arctan",
                "csc", "sec", "cot", "arccsc", "arcsec", "arccot",
                "sinh", "cosh", "tanh", "arcsinh", "arccosh", "arctanh",
                "csch", "sech", "coth", "arccsch", "arcsech", "arccoth",
                "ln", "log", "sqrt", "abs"};

        for (String func : funcs)
        {
            if (this.txt.equals(func))
            {
                return true;
            }
        }
        return false;
    }

    private boolean isNumber()
    {
        String[] specials = {"pi", "e", this.Variable};
        try
        {
            for (String special : specials)
            {
                if (this.txt.equals(special))
                {
                    return true;
                }
            }
            Float.parseFloat(this.txt);
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
}