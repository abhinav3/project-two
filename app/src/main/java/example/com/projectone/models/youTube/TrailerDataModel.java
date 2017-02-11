package example.com.projectone.models.youTube;

/**
 * Created by 16545 on 12/02/17.
 */
public class TrailerDataModel {
    private String name;
    private String image;

    public TrailerDataModel(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "TrailerDataModel{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
