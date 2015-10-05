package urbanhack.reportapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

/**
 * Created by rohan on 3/10/15.
 */
public class Report implements Parcelable{
    private String id;
    private String title;
    private String content;
    private String author;
    private String authorGender;
    private String authorEmail;

    public String getAuthorGender() {
        return authorGender;
    }

    public void setAuthorGender(String authorGender) {
        this.authorGender = authorGender;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    private String authorImgUrl;
    private String videoUrl;
    private String addressOfIncident;
    private String descriptionOfIncident;
    private String typeOfPlace;
    private double lattitude;
    private double longitude;
    private String cctvFootageAvailable;
    private String policeStation;
    private int victimsAge;
    private String assailantVehicleType;
    private String assailantVehicleModel;
    private String assailatVehicleColor;
    private String assailantVehicleNumber;
    private String assailantWearingHelmet;

    private String timeOfOccurencce;
    private String timeBetween;

    private Date dateOfIncident;
    private Date dateOfReport;

    private String month;
    private String day;
    private String firStatus;
    private String reportId;
    private String category;
    private List<String> imgUrls;
    private List<String> tags;

    private Role role= Role.USER;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getAddressOfIncident() {
        return addressOfIncident;
    }

    public void setAddressOfIncident(String addressOfIncident) {
        this.addressOfIncident = addressOfIncident;
    }

    public String getDescriptionOfIncident() {
        return descriptionOfIncident;
    }

    public void setDescriptionOfIncident(String descriptionOfIncident) {
        this.descriptionOfIncident = descriptionOfIncident;
    }

    public String getTypeOfPlace() {
        return typeOfPlace;
    }

    public void setTypeOfPlace(String typeOfPlace) {
        this.typeOfPlace = typeOfPlace;
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

    public String getCctvFootageAvailable() {
        return cctvFootageAvailable;
    }

    public void setCctvFootageAvailable(String cctvFootageAvailable) {
        this.cctvFootageAvailable = cctvFootageAvailable;
    }

    public String getPoliceStation() {
        return policeStation;
    }

    public void setPoliceStation(String policeStation) {
        this.policeStation = policeStation;
    }

    public int getVictimsAge() {
        return victimsAge;
    }

    public void setVictimsAge(int victimsAge) {
        this.victimsAge = victimsAge;
    }

    public String getAssailantVehicleType() {
        return assailantVehicleType;
    }

    public void setAssailantVehicleType(String assailantVehicleType) {
        this.assailantVehicleType = assailantVehicleType;
    }

    public String getAssailantVehicleModel() {
        return assailantVehicleModel;
    }

    public void setAssailantVehicleModel(String assailantVehicleModel) {
        this.assailantVehicleModel = assailantVehicleModel;
    }

    public String getAssailatVehicleColor() {
        return assailatVehicleColor;
    }

    public void setAssailatVehicleColor(String assailatVehicleColor) {
        this.assailatVehicleColor = assailatVehicleColor;
    }

    public String getAssailantVehicleNumber() {
        return assailantVehicleNumber;
    }

    public void setAssailantVehicleNumber(String assailantVehicleNumber) {
        this.assailantVehicleNumber = assailantVehicleNumber;
    }

    public String getAssailantWearingHelmet() {
        return assailantWearingHelmet;
    }

    public void setAssailantWearingHelmet(String assailantWearingHelmet) {
        this.assailantWearingHelmet = assailantWearingHelmet;
    }

    public String getTimeOfOccurencce() {
        return timeOfOccurencce;
    }

    public void setTimeOfOccurencce(String timeOfOccurencce) {
        this.timeOfOccurencce = timeOfOccurencce;
    }

    public String getTimeBetween() {
        return timeBetween;
    }

    public void setTimeBetween(String timeBetween) {
        this.timeBetween = timeBetween;
    }

    public Date getDateOfIncident() {
        return dateOfIncident;
    }

    public void setDateOfIncident(Date dateOfIncident) {
        this.dateOfIncident = dateOfIncident;
    }

    public Date getDateOfReport() {
        return dateOfReport;
    }

    public void setDateOfReport(Date dateOfReport) {
        this.dateOfReport = dateOfReport;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFirStatus() {
        return firStatus;
    }

    public void setFirStatus(String firStatus) {
        this.firStatus = firStatus;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
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

    @Override
    public String toString() {
        return "Report{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", gender='" + authorGender + '\'' +
                ", email='" + authorEmail + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", authorImgUrl='" + authorImgUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", addressOfIncident='" + addressOfIncident + '\'' +
                ", descriptionOfIncident='" + descriptionOfIncident + '\'' +
                ", typeOfPlace='" + typeOfPlace + '\'' +
                ", lattitude=" + lattitude +
                ", longitude=" + longitude +
                ", cctvFootageAvailable='" + cctvFootageAvailable + '\'' +
                ", policeStation='" + policeStation + '\'' +
                ", victimsAge=" + victimsAge +
                ", assailantVehicleType='" + assailantVehicleType + '\'' +
                ", assailantVehicleModel='" + assailantVehicleModel + '\'' +
                ", assailatVehicleColor='" + assailatVehicleColor + '\'' +
                ", assailantVehicleNumber='" + assailantVehicleNumber + '\'' +
                ", assailantWearingHelmet='" + assailantWearingHelmet + '\'' +
                ", timeOfOccurencce='" + timeOfOccurencce + '\'' +
                ", timeBetween='" + timeBetween + '\'' +
                ", dateOfIncident=" + dateOfIncident +
                ", dateOfReport=" + dateOfReport +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                ", firStatus='" + firStatus + '\'' +
                ", reportId='" + reportId + '\'' +
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
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.author);
        dest.writeString(this.authorGender);
        dest.writeString(this.authorEmail);
        dest.writeString(this.authorImgUrl);
        dest.writeString(this.videoUrl);
        dest.writeString(this.addressOfIncident);
        dest.writeString(this.descriptionOfIncident);
        dest.writeString(this.typeOfPlace);
        dest.writeDouble(this.lattitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.cctvFootageAvailable);
        dest.writeString(this.policeStation);
        dest.writeInt(this.victimsAge);
        dest.writeString(this.assailantVehicleType);
        dest.writeString(this.assailantVehicleModel);
        dest.writeString(this.assailatVehicleColor);
        dest.writeString(this.assailantVehicleNumber);
        dest.writeString(this.assailantWearingHelmet);
        dest.writeString(this.timeOfOccurencce);
        dest.writeString(this.timeBetween);
        dest.writeLong(dateOfIncident != null ? dateOfIncident.getTime() : -1);
        dest.writeLong(dateOfReport != null ? dateOfReport.getTime() : -1);
        dest.writeString(this.month);
        dest.writeString(this.day);
        dest.writeString(this.firStatus);
        dest.writeString(this.reportId);
        dest.writeString(this.category);
        dest.writeStringList(this.imgUrls);
        dest.writeStringList(this.tags);
        dest.writeInt(this.role == null ? -1 : this.role.ordinal());
    }

    public Report() {
    }

    protected Report(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.author = in.readString();
        this.authorGender = in.readString();
        this.authorEmail = in.readString();
        this.authorImgUrl = in.readString();
        this.videoUrl = in.readString();
        this.addressOfIncident = in.readString();
        this.descriptionOfIncident = in.readString();
        this.typeOfPlace = in.readString();
        this.lattitude = in.readDouble();
        this.longitude = in.readDouble();
        this.cctvFootageAvailable = in.readString();
        this.policeStation = in.readString();
        this.victimsAge = in.readInt();
        this.assailantVehicleType = in.readString();
        this.assailantVehicleModel = in.readString();
        this.assailatVehicleColor = in.readString();
        this.assailantVehicleNumber = in.readString();
        this.assailantWearingHelmet = in.readString();
        this.timeOfOccurencce = in.readString();
        this.timeBetween = in.readString();
        long tmpDateOfIncident = in.readLong();
        this.dateOfIncident = tmpDateOfIncident == -1 ? null : new Date(tmpDateOfIncident);
        long tmpDateOfReport = in.readLong();
        this.dateOfReport = tmpDateOfReport == -1 ? null : new Date(tmpDateOfReport);
        this.month = in.readString();
        this.day = in.readString();
        this.firStatus = in.readString();
        this.reportId = in.readString();
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
