package trong.fpt.duan1_nhom4_cp17310.models;

public class News {

    private String linkAnh;
    private String title;
    private String linkWeb;

    public News(String linkAnh, String title, String linkWeb) {
        this.linkAnh = linkAnh;
        this.title = title;
        this.linkWeb = linkWeb;
    }

    public String getLinkAnh() {
        return linkAnh;
    }

    public void setLinkAnh(String linkAnh) {
        this.linkAnh = linkAnh;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLinkWeb() {
        return linkWeb;
    }

    public void setLinkWeb(String linkWeb) {
        this.linkWeb = linkWeb;
    }
}
