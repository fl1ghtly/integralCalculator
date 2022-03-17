package com.codebind;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class IntegrateGUI {
    private JPanel mainPanel;
    private JTextField equationInput;
    private JTextField leftBoundInput;
    private JTextField rightBoundInput;
    private JButton calculateButton;
    private JLabel labelEquation;
    private JLabel labelLeftBound;
    private JLabel labelRightBound;
    private JLabel labelOutput;

    private List<JTextField> inputs = List.of(leftBoundInput, rightBoundInput, equationInput);
    private Logger logger = new Logger(true);

    public IntegrateGUI()
    {
        calculateButton.addActionListener(f -> {
            String equation = equationInput.getText();
            String leftBound = leftBoundInput.getText();
            String rightBound = rightBoundInput.getText();

            if (leftBound.equals("") || rightBound.equals("") || equation.equals(""))
            {
                labelOutput.setText("Error: Missing Input");
                return;
            }
            Double left = replaceBoundsOfIntegration(leftBound);
            Double right = replaceBoundsOfIntegration(rightBound);

            if (left == null || right == null)
            {
                labelOutput.setText("Error: Invalid Bounds");
                return;
            }

            try
            {
                Integrate integral = new Integrate(equation, left, right);
                integral.logger.log(integral.toString());
                if (integral.isContinuous())
                {
                    Double v = integral.monteCarloIntegrate(10000);
                    labelOutput.setText("Value: " + v);
                }
                else
                {
                    labelOutput.setText("Error: Non-continuous function");
                }
            }
            catch (Exception e)
            {
                labelOutput.setText("Error: Invalid Input");
                e.printStackTrace();
            }

            emptyInput();
        });
    }

    public static void main(String[] args)
    {
        IntegrateGUI ig = new IntegrateGUI();

        JFrame frame = new JFrame("Integrate");
        frame.setContentPane(ig.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    private Double replaceBoundsOfIntegration(String val)
    {
        logger.log(String.format("Converting Integration Bounds for %s", val));
        EquationParser parser = new EquationParser(val);
        try
        {
            double num = parser.evaluate();
            return num;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private void emptyInput()
    {
        logger.log("Resetting Inputs\n");
        for (JTextField input : inputs)
        {
            input.setText("");
        }
    }
}
