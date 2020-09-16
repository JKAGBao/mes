package com.yxst.inspect.rx;

import com.google.gson.JsonObject;
import com.yxst.inspect.database.model.Device;
import com.yxst.inspect.database.model.Grade;
import com.yxst.inspect.database.model.InspectDevice;
import com.yxst.inspect.database.model.Item;
import com.yxst.inspect.database.model.Line;
import com.yxst.inspect.database.model.Monitor;
import com.yxst.inspect.database.model.Place;
import com.yxst.inspect.database.model.Undetect;
import com.yxst.inspect.database.model.InspectLine;
import com.yxst.inspect.database.model.LubeDevice;
import com.yxst.inspect.database.model.LubeItem;
import com.yxst.inspect.database.model.Sample;
import com.yxst.inspect.database.model.UnLube;
import com.yxst.inspect.model.Visit;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RxRestService {

    @GET("webapi/api/v1/inspection/GetInspectionLine")
     Observable<List<Line>> getLine();

    // page访问记录
    @Headers({"Content-Type: application/json"})//需要添加头
    @POST("webapi/api/v1/basic/InsertLog")
    Observable<String> visitRecord( @Body Visit visit);
    //绑定设备code码
    @FormUrlEncoded
    @POST("webapi/api/v1/inspection/updaterfid")
    Observable<String> postDeviceCode(@Field("id")int id,@Field("code")String code);
    //绑定设备code码
    @FormUrlEncoded
    @POST("webapi/api/v1/inspection/updateLineRfid")
    Observable<String> postLineCode(@Field("id")int id,@Field("code")String code);
    //设备停止运行
    @POST("webapi/api/v1/inspection/saveEquipmentRunStates")
    Observable<String> stopStatus(@Body RequestBody body);

    //线路停止运行
    @POST("webapi/api/v1/inspection/saveLineEquipmentRunStates")
    Observable<String> stopLineStatus(@Body RequestBody body);

    //润滑记录提交
    @POST("webapi/api/v1/inspection/saveLubricationRecordone")
    Observable<String> postLubRecord(@Body RequestBody body);

    //用户登录
    @FormUrlEncoded
    @POST("webapi/api/v1/inspection/UserLoginByLine")
    Observable<JsonObject> signInBy(@Field("user") String user, @Field("pwd")String password);

    //根据线路id查询device http://60.164.211.4:9800/webapi/api/v1/inspection/getEquipmentToMobileMenu?EquipmentID=648
    @GET("webapi/api/v1/inspection/getEquipmentToMobileMenu")
    Observable<List<Monitor>> monitorByEquipmentID(@Query("EquipmentID") long id);  //根据线路id查询device
    @GET("webapi/api/v1/inspection/GetDeviceByLine")
    Observable<List<Device>> getDeviceByLineId(@Query("lineid") int id);    //根据线路id查询device
    @GET("webapi/api/v1/quality/GetSPBindByCID")
    Observable<List<Sample>> getSample(@Query("rFIDid") long id);
    @GET("webapi/api/v1/quality/UpdateSPByCheckDate1")
    Observable<String> submitSample(@Query("ID") String name,@Query("sampingPerson")String user,@Query("checkTime")String date);
//    Observable<String> submitSample(@Query("dataBaseName") String name,@Query("sampingPerson")String user,@Query("checkTime")String date);
    //获取绑定设备的列表
//    @GET("webapi/api/v1/inspection/GetInspectionEquipment")
    @GET("webapi/api/v1/inspection/getEquipmentAll")
    Observable<List<Device>> getDeviceList();
    //获取所有的部位
    @GET("webapi/api/v1/inspection/GetDevicePlace")
    Observable<List<Place>> getPlace();
    //警告等级
    @GET("webapi/api/v1/inspection/GetDangerGrade")
    Observable<List<Grade>> getDangerGrade();
    //提交巡检记录
    @FormUrlEncoded
    @POST("webapi/api/v1/inspection/saveInspectionRecord")
    Observable<String> postInspectItemRecord( @Field("param") String token);
    //提交巡检记录图片地址
    @FormUrlEncoded
    @POST("webapi/api/v1/inspection/saveInspectionRecordImg")
    Observable<String> postRecordImage(  @Field("param") String token);
    //提交巡检记录图片地址
    @FormUrlEncoded
    @POST("webapi/api/v1/inspection/saveLubricationRecordImg")
    Observable<String> postLubeImage(  @Field("param") String token);
    //获取未巡检的设备
    @GET("webapi/api/v1/inspection/getMissedDetectionByUserID")
    Observable<List<Undetect>> geUndectDeviceByUserId(@Query("id") Long id);
    //获取line
    @GET("webapi/api/v1/inspection/getisLineByUser")
    Observable<List<InspectLine>> getLineByUser(@Query("id") Long id);
    //根据用户Id查询，获取巡检设备
    @GET("webapi/api/v1/inspection/GetEquipmentByUserID")
    Observable<List<InspectDevice>> getInspectDeviceByUserId(@Query("id") Long id);
    //获取部位列表
    @GET("webapi/api/v1/inspection/GetDevicePlaceByUser")
    Observable<List<Place>> getPlaceByUserId(@Query("id") Long id);
    //根据Userid获取Item巡检项
    @GET("webapi/api/v1/inspection/GetInspectionItemByUserID")
    Observable<List<Item>> getItemByUserId(@Query("id") Long id);
    //根据LineId获取Item
    @GET("webapi/api/v1/inspection/GetItemByLine")
    Observable<List<Item>> getItemByLineId(@Query("lineid") int id);

    //待润滑设备
    @GET("webapi/api/v1/inspection/getstayLubricationByEquipment")
    Observable<List<LubeDevice>> getLubeDevice(@Query("id")Long id);
    //获取润滑列表
    @GET("webapi/api/v1/inspection/getstayLubricationRecord")
    Observable<List<LubeItem>> getLubeRecord(@Query("id")Long id);
    //漏润滑设备
    @GET("webapi/api/v1/inspection/getMissLubricationRecord")
    Observable<List<UnLube>> getMissLube(@Query("id")Long id);

    @GET("webapi/api/v1/inspection/saveLubricationRecordone")
    Observable<List<LubeItem>> saveRecordone(@Query("id")Long id);





}
