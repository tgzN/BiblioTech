/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech;

/**
 *
 * @author Tiago
 */
public class Emprestimo {
    private int id;
    private int idLivro;
    private int cpfLeitor;
    private String dataEmprestimo;
    private String dataDevolucaoPrevista;
    private String dataDevolucaoReal;

    public Emprestimo(int id, int idLivro, int cpfLeitor, String dataEmprestimo, String dataDevolucaoPrevista, String dataDevolucaoReal) {
        this.id = id;
        this.idLivro = idLivro;
        this.cpfLeitor = cpfLeitor;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    public int getId() {
        return id;
    }

    public int getIdLivro() {
        return idLivro;
    }

    public int getIdLeitor() {
        return cpfLeitor;
    }

    public String getDataEmprestimo() {
        return dataEmprestimo;
    }

    public String getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public String getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public void setIdLeitor(int cpfLeitor) {
        this.cpfLeitor = cpfLeitor;
    }

    public void setDataEmprestimo(String dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public void setDataDevolucaoPrevista(String dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public void setDataDevolucaoReal(String dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
    }
}
