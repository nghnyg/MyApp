package nagihan.myapp.models;

import java.io.Serializable;



public class Item implements Serializable {

    public int id;
    public String text;
    public String img;

    public Item(int id, String text, String image) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
