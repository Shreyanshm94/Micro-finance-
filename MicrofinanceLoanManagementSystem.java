import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MicrofinanceLoanManagementSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createLoginGUI());
    }

    private static void createLoginGUI() {
        JFrame frame = new JFrame("Microfinance Loan Management System - Login");
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeLoginComponents(panel, frame);

        frame.setVisible(true);
    }

    private static void placeLoginComponents(JPanel panel, JFrame frame) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 40, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(140, 40, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 80, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(140, 80, 165, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(50, 130, 100, 25);
        panel.add(loginButton);

        JButton signupButton = new JButton("Sign Up");
        signupButton.setBounds(170, 130, 100, 25);
        panel.add(signupButton);

        loginButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());

            if (validateLogin(username, password)) {
                JOptionPane.showMessageDialog(frame, "Login successful!");
                frame.dispose();
                showMicrofinanceLoanManagementSystem();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials.");
            }
        });

        signupButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());

            if (registerUser(username, password)) {
                JOptionPane.showMessageDialog(frame, "Sign up successful! You can now log in.");
            } else {
                JOptionPane.showMessageDialog(frame, "Sign up failed. Username might already exist.");
            }
        });
    }

    private static boolean validateLogin(String username, String password) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean registerUser(String username, String password) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private static void showMicrofinanceLoanManagementSystem() {
        JFrame frame = new JFrame("Microfinance Loan Management System");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        frame.add(panel);

        JButton addLoanButton = new JButton("Add Loan");
        JButton repayLoanButton = new JButton("Repay Loan");
        JButton viewLoansButton = new JButton("View Loans");
        JButton exitButton = new JButton("Exit");

        panel.add(addLoanButton);
        panel.add(repayLoanButton);
        panel.add(viewLoansButton);
        panel.add(exitButton);

        addLoanButton.addActionListener(e -> addLoan());
        repayLoanButton.addActionListener(e -> repayLoan());
        viewLoansButton.addActionListener(e -> viewLoans());
        exitButton.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    private static void addLoan() {
        JFrame frame = new JFrame("Add Loan");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(null);

        JLabel nameLabel = new JLabel("Borrower's Name:");
        nameLabel.setBounds(10, 20, 150, 25);
        panel.add(nameLabel);

        JTextField nameText = new JTextField(20);
        nameText.setBounds(170, 20, 165, 25);
        panel.add(nameText);

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setBounds(10, 60, 150, 25);
        panel.add(phoneLabel);

        JTextField phoneText = new JTextField(20);
        phoneText.setBounds(170, 60, 165, 25);
        panel.add(phoneText);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(10, 100, 150, 25);
        panel.add(addressLabel);

        JTextField addressText = new JTextField(50);
        addressText.setBounds(170, 100, 165, 25);
        panel.add(addressText);

        JLabel amountLabel = new JLabel("Loan Amount:");
        amountLabel.setBounds(10, 140, 150, 25);
        panel.add(amountLabel);

        JTextField amountText = new JTextField(20);
        amountText.setBounds(170, 140, 165, 25);
        panel.add(amountText);

        JLabel interestLabel = new JLabel("Interest Rate (% p.a.):");
        interestLabel.setBounds(10, 180, 150, 25);
        panel.add(interestLabel);

        JTextField interestText = new JTextField(20);
        interestText.setBounds(170, 180, 165, 25);
        panel.add(interestText);

        JLabel durationLabel = new JLabel("Repayment Duration (months):");
        durationLabel.setBounds(10, 220, 200, 25);
        panel.add(durationLabel);

        JTextField durationText = new JTextField(20);
        durationText.setBounds(210, 220, 125, 25);
        panel.add(durationText);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(130, 270, 120, 30);
        panel.add(submitButton);

        submitButton.addActionListener(e -> {
            String borrowerName = nameText.getText();
            String phoneNumber = phoneText.getText();
            String address = addressText.getText();

            double loanAmount;
            double interestRate;
            int durationMonths;

            try {
                loanAmount = Double.parseDouble(amountText.getText());
                interestRate = Double.parseDouble(interestText.getText());
                durationMonths = Integer.parseInt(durationText.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount, interest rate, or duration.");
                return;
            }

            double durationYears = durationMonths / 12.0;
            double totalDue = loanAmount + (loanAmount * interestRate * durationYears) / 100;

            try (Connection conn = DBConnection.getConnection()) {
                String query = "INSERT INTO loans (borrower_name, phone_number, address, loan_amount, interest_rate, duration_months, due_amount) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, borrowerName);
                pstmt.setString(2, phoneNumber);
                pstmt.setString(3, address);
                pstmt.setDouble(4, loanAmount);
                pstmt.setDouble(5, interestRate);
                pstmt.setInt(6, durationMonths);
                pstmt.setDouble(7, totalDue);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Loan added successfully!");
                frame.dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error adding loan.");
            }
        });

        frame.setVisible(true);
    }

    private static void repayLoan() {
        JFrame frame = new JFrame("Repay Loan");
        frame.setSize(350, 250);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(null);

        JLabel nameLabel = new JLabel("Borrower's Name:");
        nameLabel.setBounds(10, 20, 150, 25);
        panel.add(nameLabel);

        JTextField nameText = new JTextField(20);
        nameText.setBounds(150, 20, 165, 25);
        panel.add(nameText);

        JLabel amountLabel = new JLabel("Repay Amount:");
        amountLabel.setBounds(10, 60, 150, 25);
        panel.add(amountLabel);

        JTextField amountText = new JTextField(20);
        amountText.setBounds(150, 60, 165, 25);
        panel.add(amountText);

        JButton repayButton = new JButton("Repay");
        repayButton.setBounds(100, 100, 120, 25);
        panel.add(repayButton);

        repayButton.addActionListener(e -> {
            String borrowerName = nameText.getText();
            double repayAmount;

            try {
                repayAmount = Double.parseDouble(amountText.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount.");
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                String selectQuery = "SELECT due_amount FROM loans WHERE borrower_name = ?";
                PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
                selectStmt.setString(1, borrowerName);
                ResultSet rs = selectStmt.executeQuery();

                if (rs.next()) {
                    double currentDue = rs.getDouble("due_amount");
                    double newDue = currentDue - repayAmount;

                    if (newDue <= 0) {
                        String deleteQuery = "DELETE FROM loans WHERE borrower_name = ?";
                        PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
                        deleteStmt.setString(1, borrowerName);
                        deleteStmt.executeUpdate();
                        JOptionPane.showMessageDialog(frame, "Loan fully repaid! Loan record removed.");
                    } else {
                        String updateQuery = "UPDATE loans SET due_amount = ? WHERE borrower_name = ?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                        updateStmt.setDouble(1, newDue);
                        updateStmt.setString(2, borrowerName);
                        updateStmt.executeUpdate();
                        JOptionPane.showMessageDialog(frame, "Partial repayment successful! New due: " + newDue);
                    }
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Borrower not found!");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error repaying loan.");
            }
        });

        frame.setVisible(true);
    }

    private static void viewLoans() {
        JFrame frame = new JFrame("View Loans");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT borrower_name, phone_number, address, loan_amount, interest_rate, duration_months, due_amount FROM loans";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String borrower = rs.getString("borrower_name");
                String phone = rs.getString("phone_number");
                String address = rs.getString("address");
                double loanAmount = rs.getDouble("loan_amount");
                double interestRate = rs.getDouble("interest_rate");
                int durationMonths = rs.getInt("duration_months");
                double dueAmount = rs.getDouble("due_amount");

                JLabel loanLabel = new JLabel("Borrower: " + borrower + ", Phone: " + phone + ", Address: " + address + ", Amount: " + loanAmount +
                        ", Interest Rate: " + interestRate + "%, Duration: " + durationMonths + " months, Due: " + dueAmount);
                panel.add(loanLabel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Error retrieving loans.");
            panel.add(errorLabel);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        frame.add(scrollPane);
        frame.setVisible(true);
    }
}

class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/loan_management_system";
        String username = "root";
        String password = "Root@1234";

        return DriverManager.getConnection(url, username, password);
    }
}




// javac -cp .:/Users/shreyanshmishra/Downloads/mysql-connector-j-9.3.0/mysql-connector-j-9.3.0.jar MicrofinanceLoanManagementSystem.java
// java -cp .:/Users/shreyanshmishra/Downloads/mysql-connector-j-9.3.0/mysql-connector-j-9.3.0.jar MicrofinanceLoanManagementSystem.java