package az.btb.mobilebanking.models;

public class WelcomePagerModel {

    private final int image;
    private final CharSequence title;
    private final CharSequence description;

    public WelcomePagerModel(int image, CharSequence title, CharSequence description) {
        this.image = image;
        this.title = title;
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public CharSequence getTitle() {
        return title;
    }

    public CharSequence getDescription() {
        return description;
    }
}
