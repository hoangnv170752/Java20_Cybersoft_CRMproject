package model;

public enum ProjectColumn {
    ID("id"),
    NAME("name"),
    START_DATE("start_date"),
    END_DATE("end_date"),
    LEADER_ID("leader_id");
    private String value;
    ProjectColumn(String value) {
        this.value = value;
    }
    public String getValue(){
        return value;
    }
}
