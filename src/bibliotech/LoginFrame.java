package bibliotech;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginFrame() {
        initComponents();
        setTitle("Login: BiblioTech");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(0x272727)); // Cor de fundo do painel

        JLabel titleLabel = new JLabel("Login: BiblioTech");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0xFFA217)); // Cor do texto Login
        addComponent(mainPanel, titleLabel, 0, 0, 2, 1, GridBagConstraints.CENTER);

        JLabel emailLabel = new JLabel("Usuário:");
        emailLabel.setForeground(Color.WHITE);
        addComponent(mainPanel, emailLabel, 0, 1, 1, 1, GridBagConstraints.WEST);

        emailField = new JTextField(20);
        emailField.setBackground(Color.WHITE);
        emailField.setForeground(Color.BLACK);
        addComponent(mainPanel, emailField, 1, 1, 1, 1, GridBagConstraints.WEST);

        JLabel passwordLabel = new JLabel("Senha:");
        passwordLabel.setForeground(Color.WHITE);
        addComponent(mainPanel, passwordLabel, 0, 2, 1, 1, GridBagConstraints.WEST);
        
        passwordField = new JPasswordField(20);
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.BLACK);
        addComponent(mainPanel, passwordField, 1, 2, 1, 1, GridBagConstraints.WEST);

        JButton enterButton = new JButton("Entrar");
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                
                try (Connection conexao = DBConnection.conectar()) {
                    String query = "SELECT * FROM accounts WHERE username = ?";
                    try (PreparedStatement preparedStatement = conexao.prepareStatement(query)) {
                        preparedStatement.setString(1, email);
                        try (java.sql.ResultSet resultSet = preparedStatement.executeQuery()) {
                            if (resultSet.next()) {
                                String hashedPassword = resultSet.getString("pass");
                                if (PasswordUtils.verifyPassword(password, hashedPassword)) {
                                    
                                    String cargoUsuario = resultSet.getString("cargo");
                                    UsuarioLogado.setCargo(cargoUsuario); 

                                    JOptionPane.showMessageDialog(LoginFrame.this, "Login como " + cargoUsuario + " bem-sucedido!");
                                    LoginFrame.this.dispose();
                                    LivrosFrame livrosFrame = new LivrosFrame();
                                    livrosFrame.setVisible(true);
                                } else {
                                    JOptionPane.showMessageDialog(LoginFrame.this, "Senha incorreta.");
                                }
                            } else {
                                JOptionPane.showMessageDialog(LoginFrame.this, "Usuário não encontrado.");
                            }
                        }
                    }
                    conexao.close();

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Erro ao conectar ao banco de dados: " + e.getMessage());
                }
            }
        });
        enterButton.setBackground(new Color(0xFFA217)); // Cor do botão Entrar
        enterButton.setForeground(Color.BLACK);
        addComponent(mainPanel, enterButton, 0, 3, 2, 1, GridBagConstraints.CENTER);
        

        setContentPane(mainPanel);
    }

    private void addComponent(JPanel panel, Component component, int gridx, int gridy, int gridwidth, int gridheight, int anchor) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.anchor = anchor;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(component, gbc);
    }

}