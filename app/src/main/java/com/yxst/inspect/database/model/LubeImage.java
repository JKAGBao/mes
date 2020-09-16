package com.yxst.inspect.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * Created By YuanCheng on 2019/6/21 10:51
 */
@Entity
public class LubeImage {
    @Id
    private Long id;
    private Long FatID;
    private Long PlanID;
    private Long ZoneID;
    private Long LubricationItemID;
    private Date BeginTime;
    private Date EndTime;
    private String UploadPath;//服务器图片路径
    private String ImgURL;//服务器图片文件名
    private String LocalURL;//本地存储文件名
    private Long userId;
    private int isUploadServer = 0;
    private int isUploadImage = 0;
    @Generated(hash = 1457713038)
    public LubeImage(Long id, Long FatID, Long PlanID, Long ZoneID,
            Long LubricationItemID, Date BeginTime, Date EndTime, String UploadPath,
            String ImgURL, String LocalURL, Long userId, int isUploadServer,
            int isUploadImage) {
        this.id = id;
        this.FatID = FatID;
        this.PlanID = PlanID;
        this.ZoneID = ZoneID;
        this.LubricationItemID = LubricationItemID;
        this.BeginTime = BeginTime;
        this.EndTime = EndTime;
        this.UploadPath = UploadPath;
        this.ImgURL = ImgURL;
        this.LocalURL = LocalURL;
        this.userId = userId;
        this.isUploadServer = isUploadServer;
        this.isUploadImage = isUploadImage;
    }
    @Generated(hash = 665515786)
    public LubeImage() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getFatID() {
        return this.FatID;
    }
    public void setFatID(Long FatID) {
        this.FatID = FatID;
    }
    public Long getPlanID() {
        return this.PlanID;
    }
    public void setPlanID(Long PlanID) {
        this.PlanID = PlanID;
    }
    public Long getZoneID() {
        return this.ZoneID;
    }
    public void setZoneID(Long ZoneID) {
        this.ZoneID = ZoneID;
    }
    public Long getLubricationItemID() {
        return this.LubricationItemID;
    }
    public void setLubricationItemID(Long LubricationItemID) {
        this.LubricationItemID = LubricationItemID;
    }
    public Date getBeginTime() {
        return this.BeginTime;
    }
    public void setBeginTime(Date BeginTime) {
        this.BeginTime = BeginTime;
    }
    public Date getEndTime() {
        return this.EndTime;
    }
    public void setEndTime(Date EndTime) {
        this.EndTime = EndTime;
    }
    public String getUploadPath() {
        return this.UploadPath;
    }
    public void setUploadPath(String UploadPath) {
        this.UploadPath = UploadPath;
    }
    public String getImgURL() {
        return this.ImgURL;
    }
    public void setImgURL(String ImgURL) {
        this.ImgURL = ImgURL;
    }
    public String getLocalURL() {
        return this.LocalURL;
    }
    public void setLocalURL(String LocalURL) {
        this.LocalURL = LocalURL;
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public int getIsUploadServer() {
        return this.isUploadServer;
    }
    public void setIsUploadServer(int isUploadServer) {
        this.isUploadServer = isUploadServer;
    }
    public int getIsUploadImage() {
        return this.isUploadImage;
    }
    public void setIsUploadImage(int isUploadImage) {
        this.isUploadImage = isUploadImage;
    }

   
}
