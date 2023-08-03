package trong.fpt.duan1_nhom4_cp17310.models;

import java.io.Serializable;

public class Film implements Serializable {

    private String idFilm;
    private String tenFilm;
    private String ngayChieu;
    private String giaVe;
    private String linkAnh;
    private String details;

    public Film(String tenFilm, String ngayChieu, String giaVe, String linkAnh, String details) {
        this.tenFilm = tenFilm;
        this.ngayChieu = ngayChieu;
        this.giaVe = giaVe;
        this.linkAnh = linkAnh;
        this.details = details;
    }

    public String getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(String idFilm) {
        this.idFilm = idFilm;
    }

    public String getTenFilm() {
        return tenFilm;
    }

    public void setTenFilm(String tenFilm) {
        this.tenFilm = tenFilm;
    }

    public String getNgayChieu() {
        return ngayChieu;
    }

    public void setNgayChieu(String ngayChieu) {
        this.ngayChieu = ngayChieu;
    }

    public String getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(String giaVe) {
        this.giaVe = giaVe;
    }

    public String getLinkAnh() {
        return linkAnh;
    }

    public void setLinkAnh(String linkAnh) {
        this.linkAnh = linkAnh;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
