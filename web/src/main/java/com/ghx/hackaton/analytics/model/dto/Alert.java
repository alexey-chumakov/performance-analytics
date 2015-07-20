package com.ghx.hackaton.analytics.model.dto;

/**
 * Created by achumakov on 7/20/2015.
 */
public class Alert {

    private String application;

    private String URL;

    private String message;

    private Status status;

    public Alert(String application, String URL, String message, Status status) {
        this.application = application;
        this.URL = URL;
        this.message = message;
        this.status = status;
    }

    public String getApplication() {
        return application;
    }

    public String getURL() {
        return URL;
    }

    public String getMessage() {
        return message;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Alert alert = (Alert) o;

        if (application != null ? !application.equals(alert.application) : alert.application != null) return false;
        if (URL != null ? !URL.equals(alert.URL) : alert.URL != null) return false;
        return !(message != null ? !message.equals(alert.message) : alert.message != null);

    }

    @Override
    public int hashCode() {
        int result = application != null ? application.hashCode() : 0;
        result = 31 * result + (URL != null ? URL.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "application='" + application + '\'' +
                ", URL='" + URL + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public enum Status {
        warning, danger;
    }

}
