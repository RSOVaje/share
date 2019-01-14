package si.fri.pictures.models.entities;

import si.fri.pictures.models.dtos.Picture;

import javax.persistence.*;
import java.util.List;

@Entity(name = "share")
@NamedQueries(value = {
        @NamedQuery(name = "Share.getByIdProfile", query = "SELECT s FROM share s WHERE s.idProfila = :idProfila"),
        @NamedQuery(name = "Share.getByIdSProfile", query = "SELECT s FROM share s WHERE s.idSProfila = :idSProfila"),
        @NamedQuery(name = "Share.getShare", query = "SELECT s FROM share s WHERE s.idSProfila = :idSProfila AND s.idProfila = :idProfila"),
        @NamedQuery(name = "Share.getById", query = "SELECT s FROM share s WHERE s.id = :id"),
        @NamedQuery(name = "Share.getAll", query = "SELECT s FROM share s")
})
public class Share {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer idProfila;

    private Integer idSProfila;

    private Integer idPicture;

    @Transient
    private Picture pictures;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdProfila() {
        return idProfila;
    }

    public void setIdProfila(Integer idProfila) {
        this.idProfila = idProfila;
    }

    public Integer getIdSProfila() {
        return idSProfila;
    }

    public void setIdSProfila(Integer idSProfila) {
        this.idSProfila = idSProfila;
    }

    public Integer getIdPicture() {
        return idPicture;
    }

    public void setIdPicture(Integer idPhoto) {
        this.idPicture = idPhoto;
    }

    public Picture getPictures() {
        return pictures;
    }

    public void setPictures(Picture pictures) {
        this.pictures = pictures;
    }
}
