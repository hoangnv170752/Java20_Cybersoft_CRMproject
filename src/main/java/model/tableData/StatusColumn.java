package model.tableData;

public enum StatusColumn {
    ID("id"),
    NAME("name");
    private String value;
    StatusColumn(String value){
        this.value = value;
    }
    public String getValue(){
        return value;
    }
}
