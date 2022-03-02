package com.codebind;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class IntegrateGUI {
    private JPanel mainPanel;
    private JButton test;
    private JTextField equationInput;

    public IntegrateGUI()
    {

        test.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Hello World");
            }
        });
    }

    public static void main(String[] args)
    {
        IntegrateGUI ig = new IntegrateGUI();

        String equation = "sin(x)";
        Double left = 0.0;
        Double right = 1.0;
        ArrayList<Token> tokens = EquationParser.tokenize(equation);
        ArrayList<Token> rpn = EquationParser.convertEquation(tokens);
        Double v = Integrate.monteCarloIntegrate(rpn, 1000, left, right);
        System.out.println(v);

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
