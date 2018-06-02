package com.withmbh.shoutcastplayer.item;


import com.google.gson.annotations.SerializedName;

/**
 * 거래처 정보를 저장하는 객체
 */
public class SchdulInfoItem {
    public int seq;
    @SerializedName("member_seq") public int memberSeq;
    @SerializedName("bcnc_seq") public int bcncSeq;
    @SerializedName("bcnc_name") public String bcncName;
    @SerializedName("visit_date") public String visitDate;
    @SerializedName("visit_date_timestamp") public String visitDateTimestamp;
    @SerializedName("visit_result") public String visitResult;
    public String memo;
    @SerializedName("reg_date") public String regDate;

    @Override
    public String toString() {
        return "SchdulInfoItem{" +
                "seq=" + seq +
                ", memberSeq=" + memberSeq +
                ", bcnc_seq=" + bcncSeq +
                ", bcnc_name='" + bcncName + '\'' +
                ", visit_date='" + visitDate + '\'' +
                ", visit_date_timestamp='" + visitDateTimestamp + '\'' +
                ", visit_result='" + visitResult + '\'' +
                ", memo='" + memo + '\'' +
                ", regDate='" + regDate + '\'' +
                '}';
    }
}

