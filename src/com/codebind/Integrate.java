package com.codebind;
import java.util.ArrayList;
import java.util.ListIterator;

public class Integrate {
    public static double monteCarloIntegrate(ArrayList<Token> eqn, Integer n, Double a, Double b)
    {
        ArrayList<Double> x = randomUniform(a, b, n);
        ArrayList<Integer> indexes = new ArrayList<>();
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
            y.add(EquationParser.evaluate(eqn) * (b - a));
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
}
