/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech;

/**
 *
 * @author Tiago
 */

import javax.swing.*;
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
import javax.swing.border.EmptyBorder;
import java.util.List;

public class LivrosFrame extends JFrame {

    private JTextField buscaField;
    private JTable livrosTable;
    private DefaultTableModel livrosTableModel;
    private List<Livro> livros;
    
    private JButton emprestimosButton;
    private JButton cadastrarButton;
    private JButton excluirButton;


    public LivrosFrame() {
        initComponents();
        setTitle("Livros - BiblioTech");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        carregarLivrosDoBanco();
    }

    @Override
public void setVisible(boolean b) {
    super.setVisible(b);
    if (b) {
        verificarPermissoes();
    }
}

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(0x272727)); 
        
       
        JPanel tituloPanel = new JPanel();
        tituloPanel.setBackground(new Color(0x272727));
        tituloPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel titleLabel = new JLabel("Livros: BiblioTech");
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

        JButton emprestimosButton = new JButton("Empréstimos");
        
        emprestimosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (!temPermissao("bibliotecario")) {
                    JOptionPane.showMessageDialog(LivrosFrame.this, "Você não tem permissão para acessar empréstimos.");
                    return;
                }
                LivrosFrame.this.dispose();
                EmprestimosFrame emprestimosFrame = new EmprestimosFrame();
                emprestimosFrame.setVisible(true);
            }
        });
        emprestimosButton.setBackground(new Color(0xFFA217));
        emprestimosButton.setForeground(Color.BLACK);
        emprestimosButton.setPreferredSize(new Dimension(200, 35)); 
        botoesPanel.add(emprestimosButton);

        JButton cadastrarButton = new JButton("Cadastrar novo");        
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!temPermissao("bibliotecario")) {
                    JOptionPane.showMessageDialog(LivrosFrame.this, "Você não tem permissão para cadastrar livros.");
                    return;
                }
                TelaCadastroLivro cadastroLivro = new TelaCadastroLivro(LivrosFrame.this);
                cadastroLivro.setVisible(true);
            }
        });
        cadastrarButton.setBackground(new Color(0xFFA217));
        cadastrarButton.setForeground(Color.BLACK);
        cadastrarButton.setPreferredSize(new Dimension(200, 35));
        botoesPanel.add(cadastrarButton);

        JButton excluirButton = new JButton("Excluir");
        excluirButton.setBackground(new Color(0x4F0303));
        excluirButton.setForeground(Color.BLACK);
        excluirButton.setPreferredSize(new Dimension(200, 35));
        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!temPermissao("admin")) {
                    JOptionPane.showMessageDialog(LivrosFrame.this, "Você não tem permissão para excluir livros.");
                    return;
                }
                excluirLivroSelecionado();
            }
        });
        botoesPanel.add(excluirButton);

        
    System.out.println("emprestimosButton: " + emprestimosButton);
    System.out.println("cadastrarButton: " + cadastrarButton);
    System.out.println("excluirButton: " + excluirButton);
    
        mainPanel.add(tituloBuscaPanel, BorderLayout.NORTH);
        mainPanel.add(livrosScrollPane, BorderLayout.CENTER);
        mainPanel.add(botoesPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        
        buscaField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                buscarLivros(buscaField.getText());
            }
        });
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
    
    private void buscarLivros(String termoBusca) {
        
        atualizarTabelaLivros(termoBusca);
        
    }
    
    void carregarLivrosDoBanco() {
        livrosTableModel = new DefaultTableModel(new Object[]{"ID", "Título", "Autor", "Gênero", "Status"}, 0);
        livrosTable.setModel(livrosTableModel);

        try (Connection conexao = DBConnection.conectar();
             PreparedStatement statement = conexao.prepareStatement("SELECT * FROM livros");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                livrosTableModel.addRow(new Object[]{
                        resultSet.getInt("id"),
                        resultSet.getString("titulo"),
                        resultSet.getString("autor"),
                        resultSet.getString("genero"),
                        resultSet.getString("status")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar livros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void atualizarTabelaLivros(String termoBusca) {
        livrosTableModel.setRowCount(0);

        try (Connection conexao = DBConnection.conectar();
             PreparedStatement statement = conexao.prepareStatement("SELECT * FROM livros WHERE titulo LIKE ?")) {

            statement.setString(1, "%" + termoBusca + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                livrosTableModel.addRow(new Object[]{
                        resultSet.getInt("id"),
                        resultSet.getString("titulo"),
                        resultSet.getString("autor"),
                        resultSet.getString("genero"),
                        resultSet.getString("status")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar livros: " + e.getMessage());
        }
    }

    private boolean temPermissao(String cargoNecessario) {
        String cargo = UsuarioLogado.getCargo();
        if (cargo == null) {
            return false;
        }
        if (cargoNecessario.equals("admin")) {
            return cargo.equals("admin");
        } else {
            return cargo.equals("admin") || cargo.equals("bibliotecario");
        }
    }
    
    private void excluirLivroSelecionado() {
        int linhaSelecionada = livrosTable.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para excluir.");
            return;
        }

        int idLivro = (int) livrosTableModel.getValueAt(linhaSelecionada, 0);

        try (Connection conexao = DBConnection.conectar();
             PreparedStatement statement = conexao.prepareStatement("DELETE FROM livros WHERE id = ?")) {

            statement.setInt(1, idLivro);
            statement.executeUpdate();
            livrosTableModel.removeRow(linhaSelecionada);
            JOptionPane.showMessageDialog(this, "Livro excluído com sucesso.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir livro: " + e.getMessage());
        }
        
    }

    private void verificarPermissoes() {
    String cargo = UsuarioLogado.getCargo();

    System.out.println("Cargo do usuário: " + cargo);

    if (cargo == null || (!cargo.equals("admin") && !cargo.equals("bibliotecario"))) {
        System.out.println("Cargo não é admin nem bibliotecario");
        if (emprestimosButton != null) {
            System.out.println("Desativando botão emprestimos");
            emprestimosButton.setEnabled(false);
        }
        if (cadastrarButton != null) {
            System.out.println("Desativando botão cadastrar");
            cadastrarButton.setEnabled(false);
        }
    }
    if (cargo == null || !cargo.equals("admin")) {
        System.out.println("Cargo não é admin");
        if (excluirButton != null) {
            System.out.println("Desativando botão excluir");
            excluirButton.setEnabled(false);
        }
    }
}
}