
package seller.in.mesway.response.todayMeal;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessImage {

    @SerializedName("mess_id")
    @Expose
    private String messId;
    @SerializedName("big_image")
    @Expose
    private List<String> bigImage;
    @SerializedName("created_timestamp")
    @Expose
    private String createdTimestamp;
    @SerializedName("updated_timestamp")
    @Expose
    private String updatedTimestamp;

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }

    public List<String> getBigImage() {
        return bigImage;
    }

    public void setBigImage(List<String> bigImage) {
        this.bigImage = bigImage;
    }

    public String getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(String createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(String updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MessImage.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("messId");
        sb.append('=');
        sb.append(((this.messId == null)?"<null>":this.messId));
        sb.append(',');
        sb.append("bigImage");
        sb.append('=');
        sb.append(((this.bigImage == null)?"<null>":this.bigImage));
        sb.append(',');
        sb.append("createdTimestamp");
        sb.append('=');
        sb.append(((this.createdTimestamp == null)?"<null>":this.createdTimestamp));
        sb.append(',');
        sb.append("updatedTimestamp");
        sb.append('=');
        sb.append(((this.updatedTimestamp == null)?"<null>":this.updatedTimestamp));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
