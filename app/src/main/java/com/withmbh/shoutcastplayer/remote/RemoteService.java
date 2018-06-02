package com.withmbh.shoutcastplayer.remote;


import com.withmbh.shoutcastplayer.item.BcncInfoItem;
import com.withmbh.shoutcastplayer.item.MemberInfoItem;
import com.withmbh.shoutcastplayer.item.SchdulInfoItem;
import com.withmbh.shoutcastplayer.item.UserInfoItem;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 서버에 호출할 메소드를 선언하는 인터페이스
 */
public interface RemoteService {
    String BASE_URL = "http://hp.rush79.com:3000";
    String MEMBER_ICON_URL = BASE_URL + "/member/";
    String IMAGE_URL = BASE_URL + "/img/";

    //사용자 정보
    @GET("/member/{phone}")
    Call<MemberInfoItem> selectMemberInfo(@Path("phone") String phone);

    @POST("/member/info")
    Call<String> insertMemberInfo(@Body MemberInfoItem memberInfoItem);

    @FormUrlEncoded
    @POST("/member/phone")
    Call<String> insertMemberPhone(@Field("phone") String phone);

    @Multipart
    @POST("/member/icon_upload")
    Call<ResponseBody> uploadMemberIcon(@Part("member_seq") RequestBody memberSeq,
                                        @Part MultipartBody.Part file);


    @GET("/user/list")
    Call<ArrayList<UserInfoItem>> listUserInfo(@Query("member_seq") int memberSeq,
                                               @Query("order_type") String orderType,
                                               @Query("current_page") int currentPage);

    @GET("/bcnc/list")
    Call<ArrayList<BcncInfoItem>> listBcncInfo(@Query("member_seq") int memberSeq,
                                               @Query("order_type") String orderType,
                                               @Query("current_page") int currentPage);
    //거래처 정보
    @GET("/bcnc/info/{info_seq}")
    Call<BcncInfoItem> selectBcncInfo(@Path("info_seq") int bcncInfoSeq,
                                      @Query("member_seq") int memberSeq);

    @POST("/bcnc/info")
    Call<String> insertBcncInfo(@Body BcncInfoItem infoItem);

    @DELETE("/bcnc/{info_seq}/{member_seq}")
    Call<String> deleteBcncInfo(@Path("member_seq") int memberSeq, @Path("info_seq") int infoSeq);

    @PUT("/bcnc/{info_seq}/{member_seq}")
    Call<String> updateBcncInfo(@Body BcncInfoItem infoItem, @Path("info_seq") int infoSeq, @Path("member_seq") int memberSeq);


    @GET("/schdul/list")
    Call<ArrayList<SchdulInfoItem>> listSchdulInfo(@Query("member_seq") int memberSeq,
                                                   @Query("bcnc_seq") int bcncSeq,
                                                   @Query("order_type") String orderType,
                                                   @Query("search_memo") String searchMemo,
                                                   @Query("current_page") int currentPage);

    @GET("/schdul/list/date")
    Call<ArrayList<SchdulInfoItem>> listSchdulInfoDate(@Query("member_seq") int memberSeq, @Query("date") String date);

    @GET("/schdul/calender")
    Call<ArrayList<SchdulInfoItem>> listSchdulCalender(@Query("member_seq") int memberSeq,
                                                       @Query("start_date") String startDate,
                                                       @Query("end_date") String endDate);


    //거래처 정보
    @GET("/schdul/info/{info_seq}")
    Call<SchdulInfoItem> selectSchdulInfo(@Path("info_seq") int schdulInfoSeq,
                                          @Query("member_seq") int memberSeq);

    @POST("/schdul/info")
    Call<String> insertSchdulInfo(@Body SchdulInfoItem infoItem);

    @DELETE("/schdul/{info_seq}/{member_seq}")
    Call<String> deleteSchdulInfo(@Path("info_seq") int infoSeq, @Path("member_seq") int memberSeq);

    @PUT("/schdul/{info_seq}/{member_seq}")
    Call<String> updateSchdulInfo(@Body SchdulInfoItem infoItem, @Path("info_seq") int infoSeq, @Path("member_seq") int memberSeq);



    @Multipart
    @POST("/food/info/image")
    Call<ResponseBody> uploadFoodImage(@Part("info_seq") RequestBody infoSeq,
                                       @Part("image_memo") RequestBody imageMemo,
                                       @Part MultipartBody.Part file);



//
//    //지도
//    @GET("/food/map/list")
//    Call<ArrayList<FoodInfoItem>> listMap(@Query("member_seq") int memberSeq,
//                                          @Query("latitude") double latitude,
//                                          @Query("longitude") double longitude,
//                                          @Query("distance") int distance,
//                                          @Query("user_latitude") double userLatitude,
//                                          @Query("user_longitude") double userLongitude);


    //즐겨찾기
    @POST("/keep/{member_seq}/{info_seq}")
    Call<String> insertKeep(@Path("member_seq") int memberSeq, @Path("info_seq") int infoSeq);

    @DELETE("/keep/{member_seq}/{info_seq}")
    Call<String> deleteKeep(@Path("member_seq") int memberSeq, @Path("info_seq") int infoSeq);

//    @GET("/keep/list")
//    Call<ArrayList<KeepItem>> listKeep(@Query("member_seq") int memberSeq,
//                                       @Query("user_latitude") double userLatitude,
//                                       @Query("user_longitude") double userLongitude);
}