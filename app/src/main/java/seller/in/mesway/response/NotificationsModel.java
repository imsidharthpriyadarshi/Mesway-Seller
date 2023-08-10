package seller.in.mesway.response;

public class NotificationsModel {
    private String notification_title;
    private String time;

    public NotificationsModel(String notification_title, String time) {
        this.notification_title = notification_title;
        this.time = time;
    }

    public String getNotification_title() {
        return notification_title;
    }

    public void setNotification_title(String notification_title) {
        this.notification_title = notification_title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
