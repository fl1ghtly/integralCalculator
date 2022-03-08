package com.codebind;

import java.util.*;
import java.lang.reflect.*;

public class EquationParser
{
    public static ArrayList<Token> tokenize(String s)
    {
        String tkn = "";
        ArrayList<Token> tkns = new ArrayList<>();
        Token emptyToken = new Token(null);
        String c = s.substring(0, 1);
        TokenType charType = emptyToken.checkType(c);
        for (char letter : s.toCharArray())
        {
            if (letter == ' ')
            {
                continue;
            }
            String cNew = Character.toString(letter);
            TokenType newType = emptyToken.checkType(cNew);
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

    public static ArrayList<Token> addImplicitMultiplication(ArrayList<Token> tokens)
    {
        ArrayList<Token> changed = new ArrayList<>();
        List<TokenType> conditions = List.of(TokenType.FUNC, TokenType.NUM, TokenType.LPAR);
        List<TokenType> initial = List.of(TokenType.RPAR, TokenType.NUM);
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

    public static ArrayList<Token> changeUnaryOp(ArrayList<Token> tokens)
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
                else if (HelperFunctions.in(tokens.get(i - 1).getTxt(), List.of("(", "*", "/")))
                {
                    token.setType(TokenType.UNARYOP);
                }
            }
            changed.add(token);
        }
        return changed;
    }
    public static ArrayList<Token> convertEquation(ArrayList<Token> tokens)
    {
        Stack<Token> stack = new Stack<>();
        ArrayList<Token> output = new ArrayList<>();

        for (Token tkn : tokens)
        {
            TokenType t = tkn.getType();
            if (t == TokenType.NUM)
            {
                output.add(tkn);
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
                if (stack.peek().getType() == TokenType.FUNC)
                {
                    output.add(stack.pop());
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
        Double v = switch (operator) {
            case "+" -> x + y;
            case "-" -> x - y;
            case "/" -> x / y;
            case "*" -> x * y;
            case "^" -> Math.pow(x, y);
            default -> null;
        };
        return v;
    }

    private static Double calculate(Double x, String operator)
    {
        Double v = switch (operator) {
            case "-" -> -x;
            default -> null;
        };
        return v;
    }

    public static Double evaluate(ArrayList<Token> eqn)
    {
        Stack<Token> stack = new Stack<>();
        for (Token tkn : eqn)
        {
            TokenType t = tkn.getType();
            if (t == TokenType.NUM)
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
}
