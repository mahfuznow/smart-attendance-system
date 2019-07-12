package just.cse.mahfuz.eas.model;

public class Teacher {
   String name,designation,image,shortName,email;

    public Teacher() {
    }
    public Teacher(String name, String designation, String image, String shortName, String email) {
        this.name = name;
        this.designation = designation;
        this.image = image;
        this.shortName = shortName;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
