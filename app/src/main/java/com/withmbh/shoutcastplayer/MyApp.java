package com.withmbh.shoutcastplayer;

import android.app.Application;
import android.os.StrictMode;

import com.withmbh.shoutcastplayer.item.BcncInfoItem;
import com.withmbh.shoutcastplayer.item.MemberInfoItem;
import com.withmbh.shoutcastplayer.item.SchdulInfoItem;
import com.withmbh.shoutcastplayer.item.UserInfoItem;

/**
 * 앱 전역에서 사용할 수 있는 클래스
 */
public class MyApp extends Application {
    private MemberInfoItem memberInfoItem;
    private BcncInfoItem bcncInfoItem;
    private SchdulInfoItem schdulInfoItem;
    private UserInfoItem userInfoItem;

    private String streamURL = "";




    @Override
    public void onCreate() {
        super.onCreate();

        // FileUriExposedException 문제를 해결하기 위한 코드
        // 관련 설명은 책의 [참고] 페이지 참고
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public MemberInfoItem getMemberInfoItem() {
        if (memberInfoItem == null) memberInfoItem = new MemberInfoItem();
        return memberInfoItem;
    }
    public void setMemberInfoItem(MemberInfoItem item) {
        this.memberInfoItem = item;
    }

    public int getMemberSeq() {
        return memberInfoItem.seq;
    }

    public BcncInfoItem getBcncInfoItem() {
        if (bcncInfoItem == null) bcncInfoItem = new BcncInfoItem();
        return bcncInfoItem;
    }
    public void setBcncInfoItem(BcncInfoItem bcncInfoItem) {
        this.bcncInfoItem = bcncInfoItem;
    }

    public SchdulInfoItem getSchdulInfoItem() {
        if (schdulInfoItem == null) schdulInfoItem = new SchdulInfoItem();
        return schdulInfoItem;
    }
    public void setSchdulInfoItem(SchdulInfoItem schdulInfoItem) {
        this.schdulInfoItem = schdulInfoItem;
    }

    public UserInfoItem getUserInfoItem() {
        if (userInfoItem == null) userInfoItem = new UserInfoItem();
        return userInfoItem;
    }
    public void setUserInfoItem(UserInfoItem userInfoItem) {
        this.userInfoItem = userInfoItem;
    }


    public String getStreamURL() {
        if (streamURL == null) streamURL = new String();
        return streamURL;
    }
    public void setStreamURL(String streamURL) {
        this.streamURL = streamURL;
    }
}
