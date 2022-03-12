package com.codebind;
import java.util.ArrayList;
import java.util.ListIterator;

public class Integrate extends EquationParser{
    private double leftBound;
    private double rightBound;

    public Integrate(String equation, double a, double b)
    {
        super(equation);
        this.leftBound = a;
        this.rightBound = b;
    }

    public double monteCarloIntegrate(Integer n)
    {
        ArrayList<Double> x = randomUniform(leftBound, rightBound, n);
        ArrayList<Integer> indexes = findAllVariableIndex();

        ArrayList<Double> y = new ArrayList<>();
        ListIterator<Double> enumerateX = x.listIterator();
        while (enumerateX.hasNext())
        {
            for (Integer index : indexes)
            {
                this.getRPN().get(index).setValue(enumerateX.next());
            }
            y.add(this.evaluate() * (rightBound - leftBound));
        }

        return averageArray(y);
    }

    public boolean isContinuousStrict()
    {
        ArrayList<Double> linear = linearSubDivision(leftBound, rightBound, 10000);
        double dx = 0.00001;
        ArrayList<Integer> indexes = findAllVariableIndex();

        double y1 = 0.0;
        double y2 = 0.0;

        ListIterator<Double> enumerateX = linear.listIterator();
        while (enumerateX.hasNext())
        {
            double val = enumerateX.next();

            replaceVariables(indexes, val);
            y1 = this.evaluate();

            replaceVariables(indexes, val + dx);
            y2 = this.evaluate();

            if (Math.abs(y2 - y1) > 0.001)
            {
                return false;
            }
        }
        return true;
    }

    private void replaceVariables(ArrayList<Integer> indexes, double value)
    {
        for (Integer index : indexes)
        {
            this.getRPN().get(index).setValue(value);
        }
    }

    private ArrayList<Integer> findAllVariableIndex()
    {
        ArrayList<Integer> indexes = new ArrayList<>();
        ListIterator<Token> enumerateEqn = this.getRPN().listIterator();
        while (enumerateEqn.hasNext())
        {
            if (enumerateEqn.next().getTxt().equals(Token.Variable))
            {
                indexes.add(enumerateEqn.previousIndex());
            }
        }

        return indexes;
    }



    public static ArrayList<Double> linearSubDivision(double start, double end, int subDivisions)
    {
        ArrayList<Double> lin = new ArrayList<>();
        double dx = (end - start) / (subDivisions - 1);
        double num = start;
        for (int i  = 0; i < subDivisions; i++)
        {
            lin.add(num);
            num += dx;
        }
        return lin;
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

    public double getLeftBound() {
        return leftBound;
    }

    public double getRightBound() {
        return rightBound;
    }

    public void setLeftBound(double leftBound) {
        this.leftBound = leftBound;
    }

    public void setRightBound(double rightBound) {
        this.rightBound = rightBound;
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
