import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;


public class Calculator {

    public List<String> infixToRPN(List<String> valuation) {
        List<String> RPN = new ArrayList<String>();
        Stack<String> stack = new Stack<String>();

        for (String s : valuation) {
            if (isFloat(s)) {
                RPN.add(s);
            } else if (s.equals("(")) {
                stack.push(s);
            } else if (s.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    RPN.add(stack.pop());
                }
                stack.pop();
            } else if (isOperator(s)) {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(s)) {
                    RPN.add(stack.pop());
                }
                stack.push(s);
            }
        }
        while (!stack.isEmpty()) {
            RPN.add(stack.pop());
        }
        return RPN;
    }

    public float RPNcalculations(List<String> RPN){
        Stack<Float> stack = new Stack<Float>();
        for (String s: RPN){
            if (isFloat(s)){
                stack.push(Float.parseFloat(s));
            }
            else{
                Float b = stack.pop();
                Float a = stack.pop();
                switch (s){
                    case "+":
                        stack.push(a + b);
                        break;
                    case "-":
                        stack.push(a - b);
                        break;
                    case "x":
                        stack.push(a * b);
                        break;
                    case "/":
                        stack.push(a / b);
                        break;
                    case "%":
                        stack.push(a % b);
                        break;
                    case "^":
                        stack.push((float) Math.pow(a, b));
                        break;
                    default:
                        break;
                }
            }
        }
        return stack.pop();
    }

    public boolean isOperator(String s) {
        return s.equals("+") || s.equals("-") 
        || s.equals("x") || s.equals("/") 
        || s.equals("%") || s.equals("^");
    }
    public int precedence(String s) {
        HashMap <String, Integer> precedence = new HashMap<String, Integer>();
        precedence.put("+", 1);
        precedence.put("-", 1);
        precedence.put("x", 2);
        precedence.put("/", 2);
        precedence.put("%", 2);
        precedence.put("^", 3);
        try{
            return precedence.get(s);
        } catch (Exception e){
            return 0;
        }
    }
    public boolean isFloat(String s) {
        try {
            Float.parseFloat(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Calculator");
        frame.setSize(620, 1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JTextField textField = new JTextField();
        textField.setBounds(0, 0, 600, 200);
        textField.setFont(new Font("Arial", Font.PLAIN, 40));
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.setBackground(Color.gray);
        textField.setForeground(Color.white);
        frame.add(textField);

        String[] buttonLabels = 
        {    "1", "2", "3", "+",
            "4", "5", "6", "-",
            "7", "8", "9", "x",
            "0", ".", "e", "/",
             "(", ")", "^", "%",
             "π", "C", "Del", "="
        };

        List <String> valuation = new ArrayList<String>();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                int index = i * 4 + j;
                JButton button = new JButton(buttonLabels[index]);
                button.setFont(new Font("Arial", Font.PLAIN, 40));   
                button.setBounds(150 * j, 100* i + 200, 150, 100);
                button.setBackground(Color.black);
                button.setForeground(Color.white);

                frame.add(button);

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String buttonText = button.getText();
                        int length = valuation.size();
                        if (buttonText.equals("e")){
                            buttonText = "" + Math.E;
                        }
                        else if (buttonText.equals("π")){
                            buttonText = "" + Math.PI;
                        }

                        if (!(buttonText.equals("C") || buttonText.equals("=") 
                        || buttonText.equals("Del"))){
                            if (length > 0) {
                                String last = valuation.get(length - 1);
                                
                                if (new Calculator().isOperator(last) || new Calculator().isOperator(buttonText)
                                || last.equals("(") || buttonText.equals(")")) {
                                    valuation.add(buttonText);
                                } else{
                                    if (buttonText.equals("" + Math.PI) || buttonText.equals("" + Math.E)){
                                        valuation.add("x");
                                        valuation.add(buttonText);

                                    } else {
                                    valuation.set(length - 1, last + buttonText);
                                }}
                            } else {
                                valuation.add(buttonText);

                            } 
                     }

                        if (buttonText.equals("C")) {
                            valuation.clear();
                            textField.setText("");
                        } else if (buttonText.equals("=")) {
                            try {
                                List <String> RPN = new Calculator().infixToRPN(valuation);
                                float result = new Calculator().RPNcalculations(RPN);
                                textField.setText("" + result);
                                valuation.clear();
                                valuation.add("" + result);
                            } catch (Exception ex) {
                                textField.setText("Error");
                            }
                        } else if (buttonText.equals("Del")) {
                            if (valuation.size() > 0) {
                                length = valuation.size();
                                String last = valuation.get(length - 1);
                                if (last.length() > 1) {
                                    valuation.set(length - 1, last.substring(0, last.length() - 1));
                                } else {
                                    valuation.remove(length - 1);
                                }
                                textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
                            }
                            
                        } else {
                            if (buttonText.equals("" + Math.PI)){
                                textField.setText(textField.getText() + "π");
                            } else if (buttonText.equals("" + Math.E)){
                                textField.setText(textField.getText() + "e");
                            } else {
                            textField.setText(textField.getText() + buttonText);
                            }   
                        }
                    }
                });
            }
        }

        frame.setVisible(true);
    }
}
