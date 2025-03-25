/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech;

/**
 *
 * @author Tiago
 */
public class Relatorio {
    private int id;
    private String tipoRelatorio;
    private String dataGeracao;
    private String conteudo;

    public Relatorio(int id, String tipoRelatorio, String dataGeracao, String conteudo) {
        this.id = id;
        this.tipoRelatorio = tipoRelatorio;
        this.dataGeracao = dataGeracao;
        this.conteudo = conteudo;
    }

    public int getId() {
        return id;
    }

    public String getTipoRelatorio() {
        return tipoRelatorio;
    }

    public String getDataGeracao() {
        return dataGeracao;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public void setDataGeracao(String dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
}

