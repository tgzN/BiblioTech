package bibliotech;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.time.format.DateTimeFormatter;

public class NovoEmprestimo extends JFrame {

    private JTextField txtIdLivro;
    private JTextField txtCpfLeitor;
    private JTextField txtDiasEmprestimo;
    private EmprestimosFrame emprestimosFrame; // Adicionado aqui

    public NovoEmprestimo(EmprestimosFrame emprestimosFrame) { // Modificado aqui
        this.emprestimosFrame = emprestimosFrame; // Adicionado aqui

        setTitle("Novo Empréstimo");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0x272727));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblIdLivro = new JLabel("ID Livro:");
        lblIdLivro.setForeground(new Color(0xFFA217));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblIdLivro, gbc);
        txtIdLivro = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        panel.add(txtIdLivro, gbc);
        gbc.weightx = 0.0;

        JLabel lblCpfLeitor = new JLabel("CPF Leitor:");
        lblCpfLeitor.setForeground(new Color(0xFFA217));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblCpfLeitor, gbc);
        txtCpfLeitor = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        panel.add(txtCpfLeitor, gbc);
        gbc.weightx = 0.0;

        ((AbstractDocument) txtCpfLeitor.getDocument()).setDocumentFilter(new CPFDocumentFilter());

        JLabel lblDiasEmprestimo = new JLabel("Dias de empréstimo:");
        lblDiasEmprestimo.setForeground(new Color(0xFFA217));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblDiasEmprestimo, gbc);
        txtDiasEmprestimo = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        panel.add(txtDiasEmprestimo, gbc);
        gbc.weightx = 0.0;

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(new Color(0xFFA217));
        btnSalvar.setForeground(Color.BLACK);
        btnSalvar.setPreferredSize(new Dimension(100, 30));
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarEmprestimo();
                emprestimosFrame.carregarEmprestimosDoBanco(); // Adicionado aqui
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

    private void salvarEmprestimo() {
        String idLivro = txtIdLivro.getText();
        String cpfLeitor = txtCpfLeitor.getText();
        String diasEmprestimo = txtDiasEmprestimo.getText();

        if (idLivro.isEmpty() || cpfLeitor.isEmpty() || diasEmprestimo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.");
            return;
        }

        try {
            int dias = Integer.parseInt(diasEmprestimo);
            LocalDate dataEmprestimo = LocalDate.now();
            LocalDate devolucaoEsperada = dataEmprestimo.plusDays(dias);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dataEmprestimoString = dataEmprestimo.format(formatter);
            String devolucaoEsperadaString = devolucaoEsperada.format(formatter);


            try (Connection conexao = DBConnection.conectar();
                 PreparedStatement statement = conexao.prepareStatement("INSERT INTO emprestimos (id_livro, cpf_leitor, data_emprestimo, devolucao_esperada) VALUES (?, ?, ?, ?)")) {

                statement.setInt(1, Integer.parseInt(idLivro));
                statement.setString(2, cpfLeitor);
                statement.setString(3, dataEmprestimoString);
                statement.setString(4, devolucaoEsperadaString);

                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Empréstimo cadastrado com sucesso!");
                NovoEmprestimo.this.dispose();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar empréstimo: " + ex.getMessage());
                ex.printStackTrace();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Dias de empréstimo deve ser um número inteiro.");
        }
    }

    static class CPFDocumentFilter extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            String newString = formatCPF(fb.getDocument().getText(0, fb.getDocument().getLength()) + string);
            fb.replace(0, fb.getDocument().getLength(), newString, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            String newString = formatCPF(fb.getDocument().getText(0, fb.getDocument().getLength()).substring(0, offset) + text + fb.getDocument().getText(0, fb.getDocument().getLength()).substring(offset + length));
            fb.replace(0, fb.getDocument().getLength(), newString, attrs);
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            String newString = formatCPF(fb.getDocument().getText(0, fb.getDocument().getLength()).substring(0, offset) + fb.getDocument().getText(0, fb.getDocument().getLength()).substring(offset + length));
            fb.replace(0, fb.getDocument().getLength(), newString, null);
        }

        private String formatCPF(String text) {
            String digitsOnly = text.replaceAll("[^0-9]", "");
            if (digitsOnly.length() <= 3) {
                return digitsOnly;
            } else if (digitsOnly.length() <= 6) {
                return digitsOnly.substring(0, 3) + "." + digitsOnly.substring(3);
            } else if (digitsOnly.length() <= 9) {
                return digitsOnly.substring(0, 3) + "." + digitsOnly.substring(3, 6) + "." + digitsOnly.substring(6);
            } else if (digitsOnly.length() <= 11) {
                return digitsOnly.substring(0, 3) + "." + digitsOnly.substring(3, 6) + "." + digitsOnly.substring(6, 9) + "-" + digitsOnly.substring(9);
            } else {
                return digitsOnly.substring(0, 3) + "." + digitsOnly.substring(3, 6) + "." + digitsOnly.substring(6, 9) + "-" + digitsOnly.substring(9, 11);
            }
        }
    }
}