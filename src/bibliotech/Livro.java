/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech;

/**
 *
 * @author Tiago
 */
 
public class Livro {
        private int id;
        private String titulo;
        private String autor;
        private String genero;
        private String status;

        public Livro(int id, String titulo, String autor, String genero, String status) {
            this.id = id;
            this.titulo = titulo;
            this.autor = autor;
            this.genero = genero;
            this.status = status;
        }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getGenero() {
        return genero;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
