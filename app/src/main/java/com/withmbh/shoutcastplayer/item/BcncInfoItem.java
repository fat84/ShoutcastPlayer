package com.withmbh.shoutcastplayer.item;

import com.google.gson.annotations.SerializedName;

/**
 * 거래처 정보를 저장하는 객체
 */
public class BcncInfoItem {
    public int seq;
    @SerializedName("member_seq") public int memberSeq;
    public String name;
    public String ceoname;
    public String tel;
    public String addr;
    public String amount;
    public String memo;
    @SerializedName("reg_date") public String regDate;

    @Override
    public String toString() {
        return "BcncInfoItem{" +
                "seq=" + seq +
                ", memberSeq=" + memberSeq +
                ", name='" + name + '\'' +
                ", ceoname='" + ceoname + '\'' +
                ", tel='" + tel + '\'' +
                ", addr='" + addr + '\'' +
                ", amount='" + amount + '\'' +
                ", memo='" + memo + '\'' +
                ", regDate='" + regDate + '\'' +
                '}';
    }
}
