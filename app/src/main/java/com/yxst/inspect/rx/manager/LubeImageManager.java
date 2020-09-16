package com.yxst.inspect.rx.manager;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.yxst.inspect.ConfigInfo;
import com.yxst.inspect.database.model.InspectImage;
import com.yxst.inspect.database.Manager.InspectImageQueryUtil;
import com.yxst.inspect.database.Manager.LubeImageQuery;
import com.yxst.inspect.database.model.LubeImage;
import com.yxst.inspect.net.RestCreator;
import com.yxst.inspect.util.BitmapUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created By YuanCheng on 2019/7/5 16:48
 */
public class LubeImageManager {

    public static boolean postInpsectImageToServer( final Context ctx,final List<LubeImage> imageList){
        Gson gson = new Gson();
        String jsonStr = gson.toJson(imageList);
        Observable<String> upImgRes = RestCreator.getRxRestService().postLubeImage(jsonStr);
        upImgRes.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if(s!=null || !s.equals("")){
                            int resVal = Integer.valueOf(s).intValue();
                            if(resVal>0){
                               for(LubeImage image:imageList){
                                   //上传成功后，更改字段值为1
                                  LubeImage dbImage = LubeImageQuery.getImageByURL(image.getLocalURL());
                                  dbImage.setIsUploadServer(ConfigInfo.UPLOAD_IMAGE);
                                  LubeImageQuery.updateImage(dbImage);
                               }
                            }
                        }
                      // Toast.makeText(ctx, "上传成功" + s, Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                  //      ThrowableUtil.exceptionManager(throwable,ctx);
                    }
                });
        return true;
    }
    public static boolean upLoadImageByStatus(final Context ctx,final List<InspectImage> inspectImages){
        for (int i = 0; i < inspectImages.size(); i++) {
            Log.e("image",inspectImages.get(i).getImgURL());
            String path = BitmapUtil.compressImage(inspectImages.get(i).getLocalURL());
            try {
                String result = RestCreator.postImageUp(path, inspectImages.get(i).getUploadPath());
                if(result.equals(true)){
                    InspectImage image = InspectImageQueryUtil.getImageByURL(path);
                    image.setIsUploadImage(ConfigInfo.UPLOAD_IMAGE);
                    InspectImageQueryUtil.updateImage(image);
                }
//                Looper.prepare();
//                Toast.makeText(ctx, "图片已上传!", Toast.LENGTH_SHORT).show();
//                Looper.loop();
                return true;
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return false;
    }
    public static boolean upLoadImageByPath(String urlPath,String uploadPath){

        String path = BitmapUtil.compressImage(urlPath);
        try {
            String result = RestCreator.postImageUp(path, uploadPath);
            if(result.equals("true")){
                LubeImage image = LubeImageQuery.getImageByURL(path);
                image.setIsUploadImage(ConfigInfo.UPLOAD_IMAGE);
                LubeImageQuery.updateImage(image);
            }
//                        Looper.prepare();
//                        Toast.makeText(ObserveActivity.this, "图片已上传!", Toast.LENGTH_SHORT).show();
//                        Looper.loop();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }
}
