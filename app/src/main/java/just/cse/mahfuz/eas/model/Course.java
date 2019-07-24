package just.cse.mahfuz.eas.model;

public class Course {
   String courseID,courseName,teacher;

    public Course() {
    }

    public Course(String courseID, String courseName, String teacher) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.teacher = teacher;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
