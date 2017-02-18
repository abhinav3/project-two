package example.com.projectone.util;

/**
 * Created by Abhinav Ravi on 28/10/16.
 */
public enum KEY_EXTRA {
    Result("Result"),
    IS_FAV("IS_FAV"),
    KEY("XXXXXXXXX");

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

