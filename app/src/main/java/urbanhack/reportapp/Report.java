package urbanhack.reportapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

/**
 * Created by rohan on 3/10/15.
 */
public class Report implements Parcelable{
    private String title;
    private String content;
    private String author;
    private String authorImgUrl;
    private String id;

    private String addressOfIncident;
    private double lattitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private double longitude;

    private Date dateOfIncident;
    private Date dateOfReport;

    public Date getDateOfReport() {
        return dateOfReport;
    }

    public void setDateOfReport(Date dateOfReport) {
        this.dateOfReport = dateOfReport;
    }

    public Role getRole() {
        return role;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorImgUrl() {
        return authorImgUrl;
    }

    public void setAuthorImgUrl(String authorImgUrl) {
        this.authorImgUrl = authorImgUrl;
    }

    public String getAddressOfIncident() {
        return addressOfIncident;
    }

    public void setAddressOfIncident(String addressOfIncident) {
        this.addressOfIncident = addressOfIncident;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Date getDateOfIncident() {
        return dateOfIncident;
    }

    public void setDateOfIncident(Date dateOfIncident) {
        this.dateOfIncident = dateOfIncident;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    private String category;
    private List<String> imgUrls;
    private List<String> tags;

    private Role role= Role.USER;

    @Override
    public String toString() {
        return "Report{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", authorImgUrl='" + authorImgUrl + '\'' +
                ", addressOfIncident='" + addressOfIncident + '\'' +
                ", lattitude=" + lattitude +
                ", longitude=" + longitude +
                ", dateOfIncident=" + dateOfIncident +
                ", category='" + category + '\'' +
                ", imgUrls=" + imgUrls +
                ", tags=" + tags +
                ", role=" + role +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.author);
        dest.writeString(this.authorImgUrl);
        dest.writeString(this.addressOfIncident);
        dest.writeDouble(this.lattitude);
        dest.writeDouble(this.longitude);
        dest.writeLong(dateOfIncident != null ? dateOfIncident.getTime() : -1);
        dest.writeString(this.category);
        dest.writeStringList(this.imgUrls);
        dest.writeStringList(this.tags);
        dest.writeInt(this.role == null ? -1 : this.role.ordinal());
    }

    public Report() {
    }

    protected Report(Parcel in) {
        this.title = in.readString();
        this.content = in.readString();
        this.author = in.readString();
        this.authorImgUrl = in.readString();
        this.addressOfIncident = in.readString();
        this.lattitude = in.readDouble();
        this.longitude = in.readDouble();
        long tmpDateOfIncident = in.readLong();
        this.dateOfIncident = tmpDateOfIncident == -1 ? null : new Date(tmpDateOfIncident);
        this.category = in.readString();
        this.imgUrls = in.createStringArrayList();
        this.tags = in.createStringArrayList();
        int tmpRole = in.readInt();
        this.role = tmpRole == -1 ? null : Role.values()[tmpRole];
    }

    public static final Creator<Report> CREATOR = new Creator<Report>() {
        public Report createFromParcel(Parcel source) {
            return new Report(source);
        }

        public Report[] newArray(int size) {
            return new Report[size];
        }
    };
}
