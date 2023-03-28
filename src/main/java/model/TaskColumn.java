package model;

public enum TaskColumn {
    ID("id"),
    NAME("name"),
    START_DATE("start_date"),
    END_DATE("end_date"),
    USER_ID("user_id"),
    PROJECT_ID("project_id"),
    STATUS_ID("status_id");

    private String value;
    TaskColumn(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
