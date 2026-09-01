package schoolcalculator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

public final class CalculatorApp {
    private static final Color BACKGROUND = new Color(28, 25, 23);
    private static final Color DISPLAY = new Color(44, 30, 22);
    private static final Color NUMBER = new Color(255, 247, 237);
    private static final Color NUMBER_TEXT = new Color(28, 25, 23);
    private static final Color OPERATION = new Color(154, 52, 18);
    private static final Color EQUALS = new Color(29, 78, 216);
    private static final Color TEXT = Color.WHITE;

    private final CalculatorEngine engine = new CalculatorEngine();
    private final JLabel history = new JLabel("Ready for some maths?", SwingConstants.RIGHT);
    private final JLabel display = new JLabel("0", SwingConstants.RIGHT);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                new CalculatorApp().show();
            }
        });
    }

    private void show() {
        setSystemLookAndFeel();
        JFrame frame = new JFrame("Pocket Math");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(createContent());
        frame.setMinimumSize(new Dimension(360, 520));
        frame.setSize(390, 590);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    private JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setBackground(BACKGROUND);
        content.setBorder(new EmptyBorder(18, 18, 18, 18));
        content.add(createDisplay(), BorderLayout.NORTH);
        content.add(createKeypad(), BorderLayout.CENTER);
        installKeyboardShortcuts(content);
        return content;
    }

    private JPanel createDisplay() {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(DISPLAY);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 50)),
                new EmptyBorder(12, 14, 12, 14)));
        history.setForeground(new Color(254, 215, 170));
        history.setFont(new Font("SansSerif", Font.PLAIN, 15));
        display.setForeground(TEXT);
        display.setFont(new Font("SansSerif", Font.BOLD, 36));
        display.getAccessibleContext().setAccessibleName("Calculator display");
        panel.add(history, BorderLayout.NORTH);
        panel.add(display, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createKeypad() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        addButton(panel, "C", "Clear the calculation", 0, 0, 1, 1, NUMBER, NUMBER_TEXT, new Runnable() {
            @Override public void run() { engine.clear(); }
        });
        addButton(panel, "⌫", "Delete the last digit", 1, 0, 1, 1, NUMBER, NUMBER_TEXT, new Runnable() {
            @Override public void run() { engine.backspace(); }
        });
        addOperation(panel, "÷", '/', 2, 0);
        addOperation(panel, "×", '*', 3, 0);
        addDigitRow(panel, 7, 1);
        addOperation(panel, "−", '-', 3, 1);
        addDigitRow(panel, 4, 2);
        addOperation(panel, "+", '+', 3, 2);
        addDigitRow(panel, 1, 3);
        addButton(panel, "0", "Digit zero", 0, 4, 2, 1, NUMBER, NUMBER_TEXT, new Runnable() {
            @Override public void run() { engine.inputDigit(0); }
        });
        addButton(panel, ".", "Decimal point", 2, 4, 1, 1, NUMBER, NUMBER_TEXT, new Runnable() {
            @Override public void run() { engine.inputDecimal(); }
        });
        addButton(panel, "=", "Calculate the answer", 3, 3, 1, 2, EQUALS, TEXT, new Runnable() {
            @Override public void run() { engine.equals(); }
        });
        return panel;
    }

    private void addDigitRow(JPanel panel, final int firstDigit, int row) {
        for (int column = 0; column < 3; column++) {
            final int digit = firstDigit + column;
            addButton(panel, String.valueOf(digit), "Digit " + digit, column, row, 1, 1,
                    NUMBER, NUMBER_TEXT, new Runnable() {
                        @Override public void run() { engine.inputDigit(digit); }
                    });
        }
    }

    private void addOperation(JPanel panel, String label, final char operation, int column, int row) {
        addButton(panel, label, "Operation " + operationName(operation), column, row, 1, 1,
                OPERATION, TEXT, new Runnable() {
                    @Override public void run() { engine.chooseOperation(operation); }
                });
    }

    private void addButton(JPanel panel, String text, String accessibleName, int x, int y,
                           int width, int height, Color background, Color foreground,
                           final Runnable action) {
        JButton button = new JButton(text);
        button.setUI(new BasicButtonUI());
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setFont(new Font("SansSerif", Font.BOLD, 24));
        button.setForeground(foreground);
        button.setBackground(background);
        button.setFocusPainted(true);
        button.setToolTipText(accessibleName);
        button.getAccessibleContext().setAccessibleName(accessibleName);
        button.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(ActionEvent event) {
                action.run();
                refreshDisplay();
            }
        });
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        constraints.weightx = width;
        constraints.weighty = height;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(4, 4, 4, 4);
        panel.add(button, constraints);
    }

    private void installKeyboardShortcuts(JPanel panel) {
        for (int digit = 0; digit <= 9; digit++) {
            final int selectedDigit = digit;
            panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(
                    javax.swing.KeyStroke.getKeyStroke("typed " + digit), "digit" + digit);
            panel.getActionMap().put("digit" + digit, new javax.swing.AbstractAction() {
                @Override public void actionPerformed(ActionEvent event) {
                    engine.inputDigit(selectedDigit); refreshDisplay();
                }
            });
        }
        bind(panel, "typed .", "decimal", new Runnable() { @Override public void run() { engine.inputDecimal(); } });
        bind(panel, "typed +", "add", new Runnable() { @Override public void run() { engine.chooseOperation('+'); } });
        bind(panel, "typed -", "subtract", new Runnable() { @Override public void run() { engine.chooseOperation('-'); } });
        bind(panel, "typed *", "multiply", new Runnable() { @Override public void run() { engine.chooseOperation('*'); } });
        bind(panel, "typed /", "divide", new Runnable() { @Override public void run() { engine.chooseOperation('/'); } });
        bind(panel, "ENTER", "equals", new Runnable() { @Override public void run() { engine.equals(); } });
        bind(panel, "BACK_SPACE", "backspace", new Runnable() { @Override public void run() { engine.backspace(); } });
        bind(panel, "ESCAPE", "clear", new Runnable() { @Override public void run() { engine.clear(); } });
    }

    private void bind(JPanel panel, String key, String name, final Runnable action) {
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(javax.swing.KeyStroke.getKeyStroke(key), name);
        panel.getActionMap().put(name, new javax.swing.AbstractAction() {
            @Override public void actionPerformed(ActionEvent event) { action.run(); refreshDisplay(); }
        });
    }

    private void refreshDisplay() {
        display.setText(engine.getDisplay());
        String detail = engine.getExpression();
        history.setText(detail.isEmpty() ? "Ready for some maths?" : detail);
    }

    private static String operationName(char operation) {
        switch (operation) {
            case '+': return "addition";
            case '-': return "subtraction";
            case '*': return "multiplication";
            case '/': return "division";
            default: return "";
        }
    }

    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }
    }
}
