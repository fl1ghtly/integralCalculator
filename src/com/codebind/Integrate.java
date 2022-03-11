package com.codebind;
import java.util.ArrayList;
import java.util.ListIterator;

public class Integrate {
    public static double monteCarloIntegrate(EquationParser ps, Integer n, Double a, Double b)
    {
        ArrayList<Double> x = randomUniform(a, b, n);
        ArrayList<Integer> indexes = new ArrayList<>();
        ArrayList<Token> eqn = ps.getRPN();
        ListIterator<Token> enumerateEqn = eqn.listIterator();
        while (enumerateEqn.hasNext())
        {
            if (enumerateEqn.next().getTxt().equals(Token.Variable))
            {
                indexes.add(enumerateEqn.previousIndex());
            }
        }

        ArrayList<Double> y = new ArrayList<>();
        ListIterator<Double> enumerateX = x.listIterator();
        while (enumerateX.hasNext())
        {
            for (Integer index : indexes)
            {
                eqn.get(index).setValue(enumerateX.next());
            }
            y.add(ps.evaluate() * (b - a));
        }

        return averageArray(y);
    }

    private static Double averageArray(ArrayList<Double> arr)
    {
        Double sum = 0.0;
        for (Double val : arr)
        {
            sum += val;
        }
        return sum / arr.size();
    }
    private static ArrayList<Double> randomUniform(Double a, Double b, Integer n)
    {
        ArrayList<Double> randArr = new ArrayList<>();
        for (int i = 0; i < n; i++)
        {
            randArr.add(Math.random() * (b - a) + a);
        }
        return randArr;
    }
/*
    public static void main(String[] args) {
        String equation = "3x-5";
        Double left = 0.0;
        Double right = 3.0;
        ArrayList<Token> tokens = EquationParser.tokenize(equation);
        tokens = EquationParser.changeUnaryOp(tokens);
        tokens = EquationParser.addImplicitMultiplication(tokens);
        ArrayList<Token> rpn = EquationParser.convertEquation(tokens);
        Double v = Integrate.monteCarloIntegrate(rpn, 10000, left, right);
    }*/
}
