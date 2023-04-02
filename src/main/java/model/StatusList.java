package model;

public enum StatusList {
    UNBEGUN(1),
    DOING(2),
    FINISH(3);

    private int value;
    StatusList(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
