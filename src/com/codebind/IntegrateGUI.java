package com.codebind;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class IntegrateGUI {
    private JPanel mainPanel;
    private JTextField equationInput;
    private JTextField leftBoundInput;
    private JTextField rightBoundInput;
    private JButton calculateButton;

    public IntegrateGUI()
    {

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String equation = equationInput.getText();
                equationInput.setText("");

                String leftBound = leftBoundInput.getText();
                Double left = replaceBoundsOfIntegration(leftBound);
                leftBoundInput.setText("");

                String rightBound = rightBoundInput.getText();
                Double right = replaceBoundsOfIntegration(rightBound);
                rightBoundInput.setText("");

                ArrayList<Token> tokens = EquationParser.tokenize(equation);
                ArrayList<Token> rpn = EquationParser.convertEquation(tokens);
                Double v = Integrate.monteCarloIntegrate(rpn, 10000, left, right);
                System.out.println(v);
            }
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
        ArrayList<Token> tkns = EquationParser.tokenize(val);
        ArrayList<Token> rpn = EquationParser.convertEquation(tkns);
        return EquationParser.evaluate(rpn);
    }
}
