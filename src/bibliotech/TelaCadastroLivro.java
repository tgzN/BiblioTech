package bibliotech;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TelaCadastroLivro extends JFrame {

    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtGen;
    private LivrosFrame livrosFrame; // Adicionado

    
    
    public TelaCadastroLivro(LivrosFrame livrosFrame) { // Modificado
        this.livrosFrame = livrosFrame; // Adicionado
        setTitle("Cadastro de Novo Livro");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0x272727));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Título:");
        lblTitulo.setForeground(new Color(0xFFA217));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblTitulo, gbc);
        txtTitulo = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        panel.add(txtTitulo, gbc);
        gbc.weightx = 0.0;

        JLabel lblAutor = new JLabel("Autor:");
        lblAutor.setForeground(new Color(0xFFA217));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblAutor, gbc);
        txtAutor = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        panel.add(txtAutor, gbc);
        gbc.weightx = 0.0;

        JLabel lblGen = new JLabel("Gênero:");
        lblGen.setForeground(new Color(0xFFA217));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblGen, gbc);
        txtGen = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        panel.add(txtGen, gbc);
        gbc.weightx = 0.0;

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(new Color(0xFFA217));
        btnSalvar.setForeground(Color.BLACK);
        btnSalvar.setPreferredSize(new Dimension(100, 30));
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titulo = txtTitulo.getText();
                String autor = txtAutor.getText();
                String gen = txtGen.getText();

                try (Connection conexao = DBConnection.conectar();
                     PreparedStatement statement = conexao.prepareStatement("INSERT INTO livros (titulo, autor, genero, status) VALUES (?, ?, ?, 'Disponível')")) {

                    statement.setString(1, titulo);
                    statement.setString(2, autor);
                    statement.setString(3, gen);
                    statement.executeUpdate();
                    
                    if (livrosFrame != null) {
                        livrosFrame.carregarLivrosDoBanco();
                    }

                    JOptionPane.showMessageDialog(TelaCadastroLivro.this, "Livro " + titulo + " cadastrado com sucesso!");
                    TelaCadastroLivro.this.dispose();

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(TelaCadastroLivro.this, "Erro ao cadastrar livro: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnSalvar, gbc);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(50, 50, 50, 50));
        contentPane.add(panel, BorderLayout.CENTER);
        contentPane.setBackground(new Color(0x272727));

        add(contentPane);
        setVisible(true);
        
    }
    
}