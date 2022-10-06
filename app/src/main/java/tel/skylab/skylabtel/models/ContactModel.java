package tel.skylab.skylabtel.models;

public class ContactModel {

    private String Id;
    private String Name;
    private String Number;
    private String Image;
    private boolean isSelected;

    public ContactModel(String id, String name, String number, String image, boolean isSelected) {
        Id = id;
        Name = name;
        Number = number;
        Image = image;
        this.isSelected = isSelected;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
