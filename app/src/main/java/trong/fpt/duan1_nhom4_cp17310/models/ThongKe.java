package trong.fpt.duan1_nhom4_cp17310.models;

public class ThongKe {

    private String tenPhim;
    private int tongDoanhThu;

    public ThongKe(String tenPhim, int tongDoanhThu) {
        this.tenPhim = tenPhim;
        this.tongDoanhThu = tongDoanhThu;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public int getTongDoanhThu() {
        return tongDoanhThu;
    }

    public void setTongDoanhThu(int tongDoanhThu) {
        this.tongDoanhThu = tongDoanhThu;
    }
}
