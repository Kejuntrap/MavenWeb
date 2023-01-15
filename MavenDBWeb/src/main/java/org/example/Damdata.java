package org.example;

public class Damdata {      // Jsonのために必要なクラス
    public int year;
    public int month;
    public int day;
    public int hour;
    public int waterVolume;
    public float percentage;
    public float diff;
    public String dataCreated;

    public String toString() {
        return "Damdata [year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", hour=" + hour +
                ", waterVolume=" + waterVolume +
                ", percentage=" + percentage +
                ", diff=" + diff +
                ", dataCreated=" + dataCreated +
                "]";
    }

    public String toStringwithid(int id){
        return "Damdata data_id."+id+" [year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", hour=" + hour +
                ", waterVolume=" + waterVolume +
                ", percentage=" + percentage +
                ", diff=" + diff +
                ", dataCreated=" + dataCreated +
                "]";
    }
}