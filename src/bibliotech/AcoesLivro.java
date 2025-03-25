package bibliotech;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AcoesLivro extends JFrame {

    private int idLivro;
    private String cpfLeitor;
    private String diasEmprestimo;
    private EmprestimosFrame emprestimosFrame; // Adicionado aqui

    public AcoesLivro(int idLivro, String cpfLeitor, String diasEmprestimo, EmprestimosFrame emprestimosFrame) { // Modificado aqui
        this.idLivro = idLivro;
        this.cpfLeitor = cpfLeitor;
        this.diasEmprestimo = diasEmprestimo;
        this.emprestimosFrame = emprestimosFrame; // Adicionado aqui
        setTitle("Ações do Empréstimo");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0x272727));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnDarBaixa = new JButton("Dar Baixa");
        btnDarBaixa.setBackground(new Color(0xFFA217));
        btnDarBaixa.setForeground(Color.BLACK);
        btnDarBaixa.setPreferredSize(new Dimension(100, 30));
        btnDarBaixa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection conexao = DBConnection.conectar();
                    PreparedStatement statement = conexao.prepareStatement("UPDATE emprestimos SET data_devolucao = CURRENT_DATE WHERE id_livro = ? AND cpf_leitor = ?")) {

                    statement.setInt(1, idLivro);
                    statement.setString(2, cpfLeitor);
                    statement.executeUpdate();

                    JOptionPane.showMessageDialog(AcoesLivro.this, "Baixa do empréstimo do livro ID " + idLivro + " realizada com sucesso!");
                    emprestimosFrame.carregarEmprestimosDoBanco(); // Atualiza a tabela
                    AcoesLivro.this.dispose();

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(AcoesLivro.this, "Erro ao dar baixa no empréstimo: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        JButton btnDeletar = new JButton("Deletar");
        btnDeletar.setBackground(new Color(0xFFA217));
        btnDeletar.setForeground(Color.BLACK);
        btnDeletar.setPreferredSize(new Dimension(100, 30));
        btnDeletar.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        try (Connection conexao = DBConnection.conectar();
             PreparedStatement statement = conexao.prepareStatement("DELETE FROM emprestimos WHERE id_livro = ? AND cpf_leitor = ?")) {

            statement.setInt(1, idLivro);
            statement.setString(2, cpfLeitor);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(AcoesLivro.this, "Empréstimo do livro ID " + idLivro + " deletado com sucesso!");
            emprestimosFrame.carregarEmprestimosDoBanco(); // Atualiza a tabela
            AcoesLivro.this.dispose();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(AcoesLivro.this, "Erro ao deletar empréstimo: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
});


        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnDarBaixa, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnDeletar, gbc);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(50, 50, 50, 50));
        contentPane.add(panel, BorderLayout.CENTER);
        contentPane.setBackground(new Color(0x272727));

        add(contentPane);
        setVisible(true);
    }
}