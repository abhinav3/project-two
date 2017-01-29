package example.com.projectone.util;

/**
 * Created by Abhinav Ravi on 28/10/16.
 */
public enum KEY_EXTRA {
    Result("Result"),
    IS_FAV("IS_FAV"),
    KEY("24c92c0158254535df5f48cf5f8b6db2");

    private String description;

    KEY_EXTRA(String description) {
        this.description = description;
    }

    public void setDescription(String description) {
        this.description =  description;
    }

    public String getDescription() {
        return description;
    }
}

