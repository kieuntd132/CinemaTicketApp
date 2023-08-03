package trong.fpt.duan1_nhom4_cp17310.models;

import java.io.Serializable;

public class Users implements Serializable {

    private String idUser;
    private String tenTaiKhoan;
    private String matKhau;
    private int loaiTaiKhoan;

    public Users(String tenTaiKhoan) {
        this.tenTaiKhoan = tenTaiKhoan;
    }


    public Users( String tenTaiKhoan, String matKhau, int loaiTaiKhoan) {
        this.tenTaiKhoan = tenTaiKhoan;
        this.matKhau = matKhau;
        this.loaiTaiKhoan = loaiTaiKhoan;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getTenTaiKhoan() {
        return tenTaiKhoan;
    }

    public void setTenTaiKhoan(String tenTaiKhoan) {
        this.tenTaiKhoan = tenTaiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public int getLoaiTaiKhoan() {
        return loaiTaiKhoan;
    }

    public void setLoaiTaiKhoan(int loaiTaiKhoan) {
        this.loaiTaiKhoan = loaiTaiKhoan;
    }
}
