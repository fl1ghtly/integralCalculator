package com.codebind;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

    public IntegrateGUI()
    {
        calculateButton.addActionListener(e -> {
            String equation = equationInput.getText();
            equationInput.setText("");

            String leftBound = leftBoundInput.getText();
            Double left = replaceBoundsOfIntegration(leftBound);
            leftBoundInput.setText("");

            String rightBound = rightBoundInput.getText();
            Double right = replaceBoundsOfIntegration(rightBound);
            rightBoundInput.setText("");

            EquationParser parser = new EquationParser(equation);
            Double v = Integrate.monteCarloIntegrate(parser, 10000, left, right);
            labelOutput.setText("Value: " + v);
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
        // TODO add check to make sure it is a real number not a function
        EquationParser parser = new EquationParser(val);
        return parser.evaluate();
    }
}
