package com.codebind;
import java.util.ArrayList;
import java.util.ListIterator;

public class Integrate extends EquationParser{
    private double leftBound;
    private double rightBound;
    private boolean doLogging = false;
    private ArrayList<Double> values;
    private boolean constant = false;

    public Integrate(String equation, double a, double b)
    {
        super(equation);
        if (findAllVariableIndex().equals(new ArrayList<>()))
        {
            constant = true;
        }
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

        this.values = y;
        return averageArray(y);
    }

    public boolean isContinuous()
    {
        ArrayList<Double> linear = linearSubDivision(leftBound, rightBound, 10000);
        double dx = 0.00001;
        ArrayList<Integer> indexes = findAllVariableIndex();

        double y1;
        double y2;

        for (double val : linear) {
            replaceVariables(indexes, val);
            y1 = this.evaluate();
            logger.log(String.format("y1: %f", y1), doLogging);

            replaceVariables(indexes, val + dx);
            y2 = this.evaluate();
            logger.log(String.format("y2: %f", y2), doLogging);

            double dy = Math.abs(y1/y2);
            logger.log(String.format("dy: %f\n", dy), doLogging);

            if (dy > 1.2) {
                return false;
            }
        }
        return true;
    }

    public static double standardDeviation(ArrayList<Double> x)
    {
        double sum = 0.0;
        double avg = averageArray(x);

        for (double n : x)
        {
            sum += Math.pow(n - avg, 2);
        }

        return Math.sqrt(sum/x.size());
    }

    public double monteCarloError()
    {
        return standardDeviation(this.values) / Math.sqrt(this.values.size());
    }

    private void replaceVariables(ArrayList<Integer> indexes, double value)
    {
        for (Integer index : indexes)
        {
            this.getRPN().get(index).setValue(value);
        }
        logger.log(String.format("Replaced variables with value: %f", value), doLogging);
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
        logger.log(String.format("Integration Variable located at %s", indexes));
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

    public String toString()
    {
        return super.toString() + String.format("Left Bound: %f\nRight Bound: %f\n", leftBound, rightBound);
    }

    public boolean isConstant()
    {
        return constant;
    }
}
