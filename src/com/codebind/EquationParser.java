package com.codebind;

import java.util.*;
import java.lang.reflect.*;

public class EquationParser
{
    protected Logger logger = new Logger(true);
    private ArrayList<Token> tokens;
    private ArrayList<Token> rpn;
    private String eqn;

    public EquationParser(String equation)
    {
        this.eqn = equation;
        this.tokens = tokenize();
        this.tokens = changeUnaryOp();
        this.tokens = addImplicitMultiplication();
        this.rpn = convertEquation();
    }

    public ArrayList<Token> tokenize()
    {
        String tkn = "";
        ArrayList<Token> tkns = new ArrayList<>();
        String c = eqn.substring(0, 1);
        TokenType charType = Token.checkType(c);
        for (char letter : eqn.toCharArray())
        {
            if (letter == ' ')
            {
                continue;
            }
            String cNew = Character.toString(letter);
            TokenType newType = Token.checkType(cNew);
            if (newType != charType)
            {
                tkns.add(new Token(tkn));
                tkn = cNew;
                charType = newType;
            }
            else if (HelperFunctions.in(newType, List.of(TokenType.OP, TokenType.LPAR, TokenType.RPAR)))
            {
                if (!tkn.equals(""))
                {
                    tkns.add(new Token(tkn));
                }
                tkn = cNew;
                charType = newType;
            }
            else
            {
                tkn += cNew;
            }
        }

        tkns.add(new Token(tkn));
        return tkns;
    }

    public ArrayList<Token> addImplicitMultiplication()
    {
        ArrayList<Token> changed = new ArrayList<>();
        List<TokenType> conditions = List.of(TokenType.FUNC, TokenType.NUM, TokenType.LPAR, TokenType.SYMBOL);
        List<TokenType> initial = List.of(TokenType.RPAR, TokenType.NUM, TokenType.SYMBOL);
        ListIterator<Token> tokenListIterator = tokens.listIterator();
        while (tokenListIterator.hasNext())
        {
            int i = tokenListIterator.nextIndex();
            Token token = tokenListIterator.next();
            if (i >= tokens.size() - 1)
            {
                changed.add(token);
                break;
            }

            TokenType nextTokenType = tokens.get(i + 1).getType();
            if (!HelperFunctions.in(token.getType(), initial))
            {
                changed.add(token);
                continue;
            }
            if (HelperFunctions.in(nextTokenType, conditions))
            {
                changed.add(token);
                changed.add(new Token("*"));
            }
            else
            {
                changed.add(token);
            }
        }
        return changed;
    }

    public ArrayList<Token> changeUnaryOp()
    {
        ArrayList<Token> changed = new ArrayList<>();
        ListIterator<Token> tokenListIterator = tokens.listIterator();
        while (tokenListIterator.hasNext())
        {
            int i = tokenListIterator.nextIndex();
            Token token = tokenListIterator.next();
            if (HelperFunctions.in(token.getTxt(), List.of("-", "!")))
            {
                if (i == 0)
                {
                    token.setType(TokenType.UNARYOP);
                }
                else if (HelperFunctions.in(tokens.get(i - 1).getType(), List.of(TokenType.LPAR, TokenType.OP, TokenType.RPAR, TokenType.NUM, TokenType.SYMBOL)))
                {
                    token.setType(TokenType.UNARYOP);
                }
            }
            changed.add(token);
        }
        return changed;
    }
    public ArrayList<Token> convertEquation()
    {
        Stack<Token> stack = new Stack<>();
        ArrayList<Token> output = new ArrayList<>();

        for (Token tkn : tokens)
        {
            TokenType t = tkn.getType();
            if (t == TokenType.NUM || t == TokenType.SYMBOL)
            {
                output.add(tkn);
            }
            else if (t == TokenType.UNARYOP)
            {
                if (tkn.getTxt().equals("-"))
                {
                    stack.add(tkn);
                }
                else
                {
                    output.add(tkn);
                }
            }
            else if (t == TokenType.FUNC)
            {
                stack.add(tkn);
            }
            else if (t == TokenType.LPAR)
            {
                stack.add(tkn);
            }
            else if (t == TokenType.RPAR)
            {
                while (!stack.isEmpty())
                {
                    Token op = stack.pop();
                    if (op.getType() == TokenType.LPAR)
                    {
                        break;
                    }
                    output.add(op);
                }
                try
                {
                    Token func = stack.peek();
                    if (func != null && func.getType() == TokenType.FUNC)
                    {
                        output.add(stack.pop());
                    }
                }
                catch (EmptyStackException e)
                {

                }
            }
            else if (t == TokenType.OP)
            {
                if (stack.isEmpty() || stack.peek().getType() == TokenType.LPAR)
                {
                    stack.add(tkn);
                }
                else
                {
                    boolean repeat = true;
                    while (!stack.isEmpty())
                    {
                        int dif = tkn.getPrecedence() - stack.peek().getPrecedence();
                        if (dif > 0 || (dif == 0 && tkn.getAssoc() == 1))
                        {
                            stack.add(tkn);
                            repeat = false;
                            break;
                        }
                        else if (dif < 0 || (dif == 0 && tkn.getAssoc() == 0))
                        {
                            output.add(stack.pop());
                        }
                    }
                    if (repeat)
                    {
                        stack.add(tkn);
                    }
                }
            }
        }
        while (!stack.isEmpty())
        {
            output.add(stack.pop());
        }
        return output;
    }


    private static double evalFunc(String func, Double val)
    {
        Double v = null;
        Method[] methodArray = Math.class.getMethods();
        Map<String, Method> methodMap = new HashMap<>();
        for (Method method : methodArray)
        {
            methodMap.put(method.getName(), method);
        }

        Method m = methodMap.get(func);
        try
        {
            v = (Double) m.invoke(null, val);
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return v;
    }

    private static Double calculate(Double x, Double y, String operator)
    {
        Double v = null;
        switch (operator)
        {
            case "+":
                v = x + y;
                break;
            case "-":
                v = x - y;
                break;
            case "/":
                v = x / y;
                break;
            case "*":
                v = x * y;
                break;
            case "^":
                v = Math.pow(x, y);
                break;
        }
        return v;
    }

    private static Double calculate(Double x, String operator)
    {
        Double v = null;
        switch (operator)
        {
            case "-":
                v = -x;
                break;
            case "!":
                v = HelperFunctions.factorial(x);
                break;
        };
        return v;
    }

    public Double evaluate()
    {
        Stack<Token> stack = new Stack<>();
        for (Token tkn : rpn)
        {
            TokenType t = tkn.getType();
            if (t == TokenType.NUM || t == TokenType.SYMBOL)
            {
                stack.add(tkn);
            }
            else if (t == TokenType.OP)
            {
                double o1 = stack.pop().getValue();
                double o2 = stack.pop().getValue();
                Double v = calculate(o2 ,o1, tkn.getTxt());
                stack.add(new Token(v.toString()));
            }
            else if (t == TokenType.FUNC)
            {
                double o1 = stack.pop().getValue();
                Double v = evalFunc(tkn.getTxt(), o1);
                stack.add(new Token(v.toString()));
            }
            else if (t == TokenType.UNARYOP)
            {
                Double num = stack.pop().getValue();
                String op = tkn.getTxt();
                Double v = calculate(num, op);
                stack.add(new Token(v.toString()));
            }
        }
        return stack.pop().getValue();
    }

    public String getEqn()
    {
        return this.eqn;
    }

    public void setEqn(String equation)
    {
        this.eqn = equation;
        this.tokens = tokenize();
        this.tokens = changeUnaryOp();
        this.tokens = addImplicitMultiplication();
        this.rpn = convertEquation();
    }

    public ArrayList<Token> getTokens()
    {
        return this.tokens;
    }

    public ArrayList<Token> getRPN()
    {
        return this.rpn;
    }

    public void prepareLogging(int verbosity, boolean showLog)
    {
        for (Token t : tokens)
        {
            t.setVerbosity(verbosity);
            t.setLogger(showLog);
        }

        for (Token t : rpn)
        {
            t.setVerbosity(verbosity);
            t.setLogger(showLog);
        }
    }

    public String toString()
    {
        prepareLogging(0, true);
        return String.format("\nEquation: %s\nTokens: %s\nRPN: %s\n", eqn, tokens, rpn);
    }
}
