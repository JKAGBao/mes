package com.yxst.mes.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.yxst.mes.R;
import com.yxst.mes.database.model.Item;
import com.yxst.mes.database.Manager.LubeImageQuery;
import com.yxst.mes.database.Manager.LubeItemQuery;
import com.yxst.mes.database.model.Record;
import com.yxst.mes.database.model.LubeImage;
import com.yxst.mes.database.model.LubeItem;
import com.yxst.mes.net.RestCreator;
import com.yxst.mes.rx.manager.LubeImageManager;
import com.yxst.mes.util.SharedPreferenceUtil;
import com.yxst.mes.util.TimeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class LubeValueActivity extends BaseActivity {

    @BindView(R.id.gridView) GridView gridView;
    @BindView(R.id.btn_save) Button buttonSave;            //发布按钮
    @BindView(R.id.et_remark)EditText remark;
    @BindView(R.id.et_add) EditText add;
    @BindView(R.id.tv_unit) TextView tvUnit;
    private Context mContext;
    private ExecutorService executor = Executors.newFixedThreadPool(3);
    private final int TAKE_PHOTO = 0;
    private final int IMAGE_OPEN = 1;        //打开图片标记
    private String pathImage;                //选择图片路径
    private Bitmap bmp;                      //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;     //适配器
    private Long itemId;
    private String unit;
    private String lubeName;
    private Long zoneId;
    private LubeItem item;
    private String uploadPath;
    private ArrayList<File> imagePaths = new ArrayList<>();;
    private List<LubeImage> imageList;
    private File tempFile;
    private String PHOTO_FILE = "" ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //不弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_lube_value);
        ButterKnife.bind(this);
        //查询数据
        lubeName = getIntent().getStringExtra("name");
        unit = getIntent().getStringExtra("unit");
        itemId = getIntent().getLongExtra("itemId",1);
        zoneId = getIntent().getLongExtra("zoneId",1);
        item = LubeItemQuery.findByItemID(this,itemId,zoneId);

        setTitle(lubeName);
        tvUnit.setText(unit);
        add.setText(item.getRealNum()==0?"":item.getRealNum()+"");
       // Toast.makeText(this, ""+imageList.size(), Toast.LENGTH_SHORT).show();
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(add.getText().toString())){
                    Toast.makeText(mContext, "增加量不可以为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveValue();//保存巡检项值
                try {
                    saveRecordImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                executor.execute(uploadRunnable);
                finish(); //
            }


        });
        /*
         * 载入默认图片添加图片加号
         * 通过适配器实现
         * SimpleAdapter参数imageItem为数据源 R.layout.griditem_addpic为布局
         */
        //获取资源图片加号
        imageList = LubeImageQuery.getListByItemId(this,itemId,zoneId);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.addpicture);
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        imageItem.add(map);
    //    Toast.makeText(this, "imageIst:"+imageList.size(), Toast.LENGTH_SHORT).show();
        if(imageList.size()!=0){
            for(LubeImage image : imageList){
                HashMap<String, Object> imgMap = new HashMap<String, Object>();
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                 BitmapFactory.decodeFile(image.getLocalURL(), options);
                 options.inSampleSize = calculateInSampleSize(options, 80, 80);//自定义一个宽和高
                options.inJustDecodeBounds = false;
                imgMap.put("itemImage",BitmapFactory.decodeFile(image.getLocalURL(),options));
                imageItem.add(imgMap);
            }
        }
        simpleAdapter = new SimpleAdapter(this,
                imageItem, R.layout.griditem_addpic,
                new String[]{"itemImage"}, new int[]{R.id.imageView1});

        /*
         * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如
         * map.put("itemImage", R.drawable.img);
         * 解决方法:
         *              1.自定义继承BaseAdapter实现
         *              2.ViewBinder()接口实现
         *  参考 http://blog.csdn.net/admin_/article/details/7257901
         */
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {

                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gridView.setAdapter(simpleAdapter);
        /*
         * 监听GridView点击事件
         * 报错:该函数必须抽象方法 故需要手动导入import android.view.View;
         */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (imageItem.size() == 10) { //第一张为默认图片
                    Toast.makeText(LubeValueActivity.this, "图片数9张已满", Toast.LENGTH_SHORT).show();
                } else if (position == 0) { //点击图片位置为+ 0对应0张图片
           //         Toast.makeText(ObserveActivity.this, "添加图片", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//7.0及以上
                        if (ContextCompat.checkSelfPermission(LubeValueActivity.this,
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(LubeValueActivity.this,
                                        new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        }else{
                            takePicture();
                        }
                    }else{
                        takePicture();
                     // takePicturePhoneorPicture();
                    }
                } else {
                    dialog(position);
                }
            }
        });
    }
    //计算图片的缩放值
     public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
         final int height = options.outHeight;//获取图片的高
         final int width = options.outWidth;//获取图片的框
         int inSampleSize = 4;
         if (height > reqHeight || width > reqWidth) {
             final int heightRatio = Math.round((float) height / (float) reqHeight);
             final int widthRatio = Math.round((float) width / (float) reqWidth);
             inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
             Log.e("imageSample",height+","+heightRatio+","+width+","+widthRatio);
         }
         return inSampleSize;//求出缩放值
     }

    private void saveRecordImage() throws Exception {
        List<LubeImage> imageList = new ArrayList<>();
        for(int i = 0;i<imagePaths.size();i++) {
            //上传图片,
            LubeImage lubeImage = new LubeImage();
            lubeImage.setImgURL(uploadPath + imagePaths.get(i).getName());
            lubeImage.setLocalURL(imagePaths.get(i).getPath());
            lubeImage.setZoneID(item.getZoneID());
            lubeImage.setPlanID(item.getEquipmentID());
            lubeImage.setLubricationItemID(item.getLubricationItemID());
            lubeImage.setBeginTime(item.getBeginTime());
            lubeImage.setEndTime(item.getEndTime());
            lubeImage.setUserId(SharedPreferenceUtil.getId(this, "User"));
            LubeImageQuery.insertImage(lubeImage);
            imageList.add(lubeImage);
            Log.e("lubeImage", imagePaths.get(i).getPath() + "," + uploadPath);
        }
        LubeImageManager.postInpsectImageToServer(mContext,imageList);


    }
    /** * 判断sdcard是否被挂载 */
    public boolean hasSdcard() {
        return Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED);
    }

    public void takePicture() {
         File dirPath = getExternalFilesDir("MesImg");
         PHOTO_FILE = "Item"+item.getLubricationItemID()+"_"+TimeUtil.dateNoFormat(new Date())+".jpg";
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            if (!dirPath.exists()) {
                dirPath.mkdir();
            }
            tempFile = new File(dirPath, PHOTO_FILE);
            //从文件中创建uri
            Uri uri = Uri.fromFile(tempFile);
            Intent intent = new Intent();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(intent.CATEGORY_DEFAULT);
            // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
            startActivityForResult(intent, 0);
        } else {
            Toast.makeText(this, "未找到存储卡，无法拍照！", Toast.LENGTH_SHORT).show();
        }
    }
    /*
    上传图片
     */
    Runnable uploadRunnable = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < imagePaths.size(); i++) {
                LubeImageManager.upLoadImageByPath(imagePaths.get(i).getPath(),uploadPath);

            }
        }
    };
    /*
    获取图片路径
     */
    Runnable pathRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                uploadPath = RestCreator.getLubeImagePath();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
        //获取图片路径 响应startActivityForResult
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            //打开图片
            if(resultCode!=RESULT_OK ){return;}
             if(requestCode == TAKE_PHOTO){
                if (tempFile != null) {
                    imagePaths.add(tempFile);
                    Glide.with(getApplicationContext()).load(tempFile.getPath()).asBitmap().into(target);
                    executor.execute(pathRunnable);

                } else {
                    Toast.makeText(this, "相机异常请稍后再试！", Toast.LENGTH_SHORT).show();
                }

             }
             else if(requestCode==IMAGE_OPEN) {
                    Uri uri = data.getData();
                    if (!TextUtils.isEmpty(uri.getAuthority())) {
                    //查询选择图片
                    Cursor cursor = getContentResolver().query(
                            uri,
                            new String[] { MediaStore.Images.Media.DATA },
                            null,
                            null,
                            null);
                    //返回 没找到选择图片
                    if (null == cursor) {
                        return;
                    }
                    //光标移动至开头 获取图片路径
                    cursor.moveToFirst();
                    pathImage = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                }
            }  //end if 打开图片
    }

    private SimpleTarget target = new SimpleTarget<Bitmap>(80,80) {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            //这里我们拿到回掉回来的bitmap，可以加载到我们想使用到的地方
            try {
                HashMap<String,Object> map = new HashMap<>();
                map.put("itemImage", bitmap);
                imageItem.add(map);
                if(simpleAdapter!=null){
                    simpleAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /*
     * 保存巡检项
     */
    private void saveValue(){
        String num = add.getText().toString();

        item.setRealNum(Integer.valueOf(num));
        LubeItemQuery.update(item);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("AddValue",add.getText().toString());
        params.put("UpdateUser",SharedPreferenceUtil.getName(mContext,"User"));
        params.put("Remark",remark.getText().toString());
        params.put("PlanID",item.getPlanID());
        params.put("ZoneID",zoneId);
        params.put("LubricationItemID",itemId);
        params.put("FatID",item.getFatID());
        params.put("BeginTime",TimeUtil.dateTimeFormat2(item.getBeginTime()));
        params.put("EndTime",TimeUtil.dateTimeFormat2(item.getEndTime()));
        Log.e("asf",new Gson().toJson(params).toString());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), new Gson().toJson(params));
        Observable statusValue = RestCreator.getRxRestService().postLubRecord(requestBody);
        statusValue.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if(Integer.valueOf(o.toString())>0){
                            Toast.makeText(mContext, "提交成功！" + o.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(mContext, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("asd",throwable.getMessage());
                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    if (i == 0){
                        //takePicturePhoneorPicture();
                        takePicture();
                    }
                } else {
                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void recordSave(Record record, Item item) {

    }
    /*
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LubeValueActivity.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                simpleAdapter.notifyDataSetChanged();
                if(imagePaths.size()!=0){
                    imagePaths.remove(position);
                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /*
     * 选择是拍照还是本地图库获取图片的选择框
     */
    Uri imgUri;
    File imgPath;
    private void takePicturePhoneorPicture(){
        String[] selection = {"拍照","图库"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setItems(selection, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                File dir = getExternalFilesDir("MesImages");
                                if (!dir.exists()) {
                                    dir.mkdir();
                                }
                                PHOTO_FILE = "Item"+item.getLubricationItemID()+"_"+TimeUtil.dateNoFormat(new Date())+".jpg";
                                imgPath = new File(dir,PHOTO_FILE);
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                //API>=24 android 7.0
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                                    if (intent.resolveActivity(getPackageManager()) != null){
                                        //7.0以上 的拍照文件必须在storage/emulated/0/Android/data/包名/files/pictures文件夹
                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                                        imgUri = FileProvider.getUriForFile(LubeValueActivity.this,
                                                getApplicationContext().getPackageName() + ".provider", imgPath);
                                    }
                                }else {//<24
                                    imgUri = Uri.fromFile(imgPath);
                                }
                                intent.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);
                                startActivityForResult(intent,TAKE_PHOTO);
                                break;
                            case 1:
                                //选择图片
                                Intent intent2 = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent2, IMAGE_OPEN);
                                //通过onResume()刷新数据
                                break;
                        }
                    }
                });
        builder.show();
    }

}

