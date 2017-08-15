package nagihan.myapp.models;

public class Person {

    private String username;
    private String language;
    private String area;
    private int photo;

    public Person(String username, String language, String area, int photo) {
        this.username = username;
        this.language = language;
        this.area = area;
        this.photo = photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
