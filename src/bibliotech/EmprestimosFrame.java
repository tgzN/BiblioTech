/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech;

/**
 *
 * @author Tiago
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Tiago
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmprestimosFrame extends JFrame {

    private JTextField buscaField;
    private JTable livrosTable;
    private DefaultTableModel livrosTableModel;

    public EmprestimosFrame() {
        initComponents();
        setTitle("Empréstimos - BiblioTech");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        carregarEmprestimosDoBanco();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(0x272727));

        JPanel tituloPanel = new JPanel();
        tituloPanel.setBackground(new Color(0x272727));
        tituloPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel titleLabel = new JLabel("Empréstimos: BiblioTech");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0xFFA217));
        tituloPanel.add(titleLabel);

        JPanel titleBorderPanel = new JPanel();
        titleBorderPanel.setBackground(new Color(0x272727));
        titleBorderPanel.setBorder(new EmptyBorder(30, 0, 15, 0));
        titleBorderPanel.add(titleLabel);

        tituloPanel.add(titleBorderPanel, BorderLayout.CENTER);

        JPanel buscaPanel = new JPanel();
        buscaPanel.setBackground(new Color(0x272727));
        buscaPanel.setLayout(new GridBagLayout());
        buscaPanel.setBorder(new EmptyBorder(0, 70, 0, 70));

        JLabel buscaLabel = new JLabel("Busca:  ");
        buscaLabel.setForeground(Color.WHITE);
        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.gridx = 0;
        gbcLabel.gridy = 0;
        gbcLabel.anchor = GridBagConstraints.WEST;
        buscaPanel.add(buscaLabel, gbcLabel);

        buscaField = new JTextField(20);
        GridBagConstraints gbcField = new GridBagConstraints();
        gbcField.gridx = 1;
        gbcField.gridy = 0;
        gbcField.weightx = 1.0;
        gbcField.fill = GridBagConstraints.HORIZONTAL;
        buscaPanel.add(buscaField, gbcField);

        JPanel tituloBuscaPanel = new JPanel();
        tituloBuscaPanel.setLayout(new BorderLayout());
        tituloBuscaPanel.add(tituloPanel, BorderLayout.NORTH);
        tituloBuscaPanel.add(buscaPanel, BorderLayout.CENTER);

        livrosTable = new JTable();
        JScrollPane livrosScrollPane = new JScrollPane(livrosTable);
        livrosScrollPane.setBorder(new EmptyBorder(15, 70, 40, 70));
        livrosScrollPane.setBackground(new Color(0x272727));

        JPanel botoesPanel = new JPanel();
        botoesPanel.setBackground(new Color(0x272727));
        botoesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        botoesPanel.setBorder(new EmptyBorder(0, 0, 40, 0));

        JButton livrosButton = new JButton("Livros");
        livrosButton.setBackground(new Color(0xFFA217));
        livrosButton.setForeground(Color.BLACK);
        livrosButton.setPreferredSize(new Dimension(200, 35));
        livrosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                EmprestimosFrame.this.dispose();
                LivrosFrame livrosFrame = new LivrosFrame();
                livrosFrame.setVisible(true);
            }
        });
        botoesPanel.add(livrosButton);

        JButton cadastrarButton = new JButton("Novo empréstimo");
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NovoEmprestimo novoEmprestimo = new NovoEmprestimo(EmprestimosFrame.this); // Modificado aqui
                novoEmprestimo.setVisible(true);
            }
        });
        cadastrarButton.setBackground(new Color(0xFFA217));
        cadastrarButton.setForeground(Color.BLACK);
        cadastrarButton.setPreferredSize(new Dimension(200, 35));
        botoesPanel.add(cadastrarButton);

        JButton acoesButton = new JButton("Ações");
        acoesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = livrosTable.getSelectedRow();
                if (selectedRow == -1) {
                   JOptionPane.showMessageDialog(EmprestimosFrame.this, "Selecione um empréstimo para realizar ações.");
                } else {
                    int idLivro = (int) livrosTable.getValueAt(selectedRow, 0);
                    String cpfLeitor = (String) livrosTable.getValueAt(selectedRow, 2);
                    String devolucaoEsperada = (String) livrosTable.getValueAt(selectedRow, 4);

                    AcoesLivro acoesLivro = new AcoesLivro(idLivro, cpfLeitor, devolucaoEsperada, EmprestimosFrame.this); // Modificado aqui
                    acoesLivro.setVisible(true);
                }
            }
        });
        acoesButton.setBackground(new Color(0xFFA217));
        acoesButton.setForeground(Color.BLACK);
        acoesButton.setPreferredSize(new Dimension(200, 35));
        botoesPanel.add(acoesButton);

        mainPanel.add(tituloBuscaPanel, BorderLayout.NORTH);
        mainPanel.add(livrosScrollPane, BorderLayout.CENTER);
        mainPanel.add(botoesPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        buscaField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {
                buscarEmprestimos(buscaField.getText());
            }
        });
    }

    void carregarEmprestimosDoBanco() {
        livrosTableModel = new DefaultTableModel(new Object[]{"ID Livro", "Título", "CPF Leitor", "Data Devolução", "Devolução Esperada"}, 0);
        livrosTable.setModel(livrosTableModel);

        try (Connection conexao = DBConnection.conectar();
             PreparedStatement statement = conexao.prepareStatement("SELECT e.id_livro, l.titulo, e.cpf_leitor, e.data_devolucao, e.devolucao_esperada FROM emprestimos e JOIN livros l ON e.id_livro = l.id")) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                livrosTableModel.addRow(new Object[]{
                        resultSet.getInt("id_livro"),
                        resultSet.getString("titulo"),
                        resultSet.getString("cpf_leitor"), // CPF como string
                        resultSet.getString("data_devolucao"),
                        resultSet.getString("devolucao_esperada")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar empréstimos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void buscarEmprestimos(String termoBusca) {
        livrosTableModel.setRowCount(0);
        try (Connection conexao = DBConnection.conectar();
             PreparedStatement statement = conexao.prepareStatement("SELECT e.id_livro, l.titulo, e.cpf_leitor, e.data_devolucao, e.devolucao_esperada FROM emprestimos e JOIN livros l ON e.id_livro = l.id WHERE l.titulo LIKE ?")) {

            statement.setString(1, "%" + termoBusca + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                livrosTableModel.addRow(new Object[]{
                        resultSet.getInt("id_livro"),
                        resultSet.getString("titulo"),
                        resultSet.getString("cpf_leitor"), // CPF como string
                        resultSet.getString("data_devolucao"),
                        resultSet.getString("devolucao_esperada")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar empréstimos: " + e.getMessage());
        }
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