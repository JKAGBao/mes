
package com.yxst.inspect.nfc;

import android.Manifest;
import android.app.AlertDialog;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yxst.inspect.ConfigInfo;
import com.yxst.inspect.R;
import com.yxst.inspect.activity.BaseActivity;
import com.yxst.inspect.database.model.InspectImage;
import com.yxst.inspect.database.model.Item;
import com.yxst.inspect.database.Manager.InspectImageQueryUtil;
import com.yxst.inspect.database.Manager.ItemQueryUtil;
import com.yxst.inspect.database.Manager.RecordQueryUtil;
import com.yxst.inspect.database.model.Record;
import com.yxst.inspect.net.RestCreator;
import com.yxst.inspect.rx.manager.InspectImageManager;
import com.yxst.inspect.rx.manager.RecordManager;
import com.yxst.inspect.util.SharedPreferenceUtil;
import com.yxst.inspect.util.TimeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 观察型巡检项界面
 */
public class ObservedActivity extends BaseActivity {

    @BindView(R.id.gridView) GridView gridView;//照片展示框
    @BindView(R.id.btn_save) Button buttonSave;//保存
    @BindView(R.id.btn_next) Button buttonNext;//下一项
    @BindView(R.id.radioGroup_gender)RadioGroup radio;//单选框
    @BindView(R.id.rb_no)RadioButton no;//单选按钮no
    @BindView(R.id.rb_yes)RadioButton yes;//单选yes
    @BindView(R.id.et_remark)EditText remark;//编辑框
    @BindView(R.id.tv_observe_name) TextView tvName;//巡检项名称
    private ExecutorService executor = Executors.newFixedThreadPool(3);//线程池
    private final int TAKE_PHOTO = 0;
    private final int IMAGE_OPEN = 1;        //打开图片标记
    private String pathImage;                //选择图片路径
    private Bitmap bmp;                      //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;     //适配器
    private Long itemId;//巡检项ID
    private Long placeId;//部位ID
    private String placeName;//巡检部位名称
    private Long lineId;//巡检线ID
    private Item item;//巡检项
    //图片路径
    private ArrayList<File> imagePaths = new ArrayList<>();
    private List<InspectImage> imageList;
    private File tempFile;
    private String PHOTO_FILE = "" ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //不弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_inspect_observed);
        ButterKnife.bind(this);
        //设置标题名称
        placeName = getIntent().getStringExtra("name");
        setTitle(placeName);
        //获取参数，查询数据
        itemId = getIntent().getLongExtra("itemId",1);
        placeId = getIntent().getLongExtra("placeId",1);
        lineId = getIntent().getLongExtra("lineId",1);
        item =  ItemQueryUtil.getItemById(this,itemId,lineId);
        tvName.setText(item==null?"":item.getCheckContent());
        //获取之前数据
        Record record = RecordQueryUtil.getRecordByItemId(this,itemId,lineId);
        if(record!=null){
        //    Toast.makeText(this, ""+record.getCheckValue(), Toast.LENGTH_SHORT).show();
            remark.setText(record.getRemark()!=null?record.getRemark():"");;
            if(record.getCheckValue().equals(ConfigInfo.ITEM_CHECK_NORMAL)){
                yes.setChecked(true);
            }else {
                no.setChecked(true);
            }
        }
        //显示保存还是下一项判断
        List<Item> itemListStatus = ItemQueryUtil.getItemByGEStatus(this,placeId,lineId,ConfigInfo.CHECKED_STATUS);
        List<Item> itemList = ItemQueryUtil.getItemByPlaceId(this,placeId,lineId);
        if(itemList.size() == itemListStatus.size()+1 || itemList.size() ==1){
            buttonSave.setText("保存");
        }else{
            buttonSave.setText("下一项");

        }
//        buttonNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveValue();//保存巡检项值
//                try {
//                    saveRecordImage();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                executor.execute(uploadRunnable);
//                nextInspect();
//            }
//        });
       // Toast.makeText(this, ""+imageList.size(), Toast.LENGTH_SHORT).show();
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( uploadRunnable==null){
                    executor.execute(pathRunnable);
                    AlertDialog.Builder builder = new AlertDialog.Builder(ObservedActivity.this)
                            .setMessage("上传照片异常，请重新保存！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                }else {
                    saveValue();//保存巡检项值
                    try {
                        if(!"null".equals(RestCreator.UPLOAD_PATH)){
                            saveRecordImage();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    executor.execute(uploadRunnable);
                    nextInspect();
//                setResult(Activity.RESULT_OK,getIntent());
//                finish();
                }
            }


        });
        /*
         * 载入默认图片添加图片加号
         * 通过适配器实现
         * SimpleAdapter参数imageItem为数据源 R.layout.griditem_addpic为布局
         */
        //获取资源图片加号
        imageList = InspectImageQueryUtil.getListByItemId(this,itemId,lineId);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.addpicture);
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        imageItem.add(map);
    //    Toast.makeText(this, "imageIst:"+imageList.size(), Toast.LENGTH_SHORT).show();
        if(imageList.size()!=0){
            for(InspectImage image : imageList){
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
                    Toast.makeText(ObservedActivity.this, "图片数9张已满", Toast.LENGTH_SHORT).show();
                } else if (position == 0) { //点击图片位置为+ 0对应0张图片
           //         Toast.makeText(ObserveActivity.this, "添加图片", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//7.0及以上
                        if (ContextCompat.checkSelfPermission(ObservedActivity.this,
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(ObservedActivity.this,
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
    /*
  上传图片
   */
    private  Runnable uploadRunnable = new Runnable() {
        @Override
        public void run() {

            for (int i = 0; i < imagePaths.size(); i++) {
                InspectImageManager.upLoadImageByPath(imagePaths.get(i).getPath(),RestCreator.UPLOAD_PATH);
            }
        }
    };
    /*
    计算图片的缩放值
     */
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
    /*
    图片存储路径，上传到后台
     */
    private void saveRecordImage() throws Exception {
        List<InspectImage> imageList = new ArrayList<>();
        for(int i = 0;i<imagePaths.size();i++) {
            //上传图片,
            InspectImage inspectImage = new InspectImage();
            inspectImage.setUploadPath(RestCreator.UPLOAD_PATH);
            inspectImage.setImgURL(RestCreator.UPLOAD_PATH + imagePaths.get(i).getName());
            inspectImage.setLocalURL(imagePaths.get(i).getPath());
            inspectImage.setLineID(item.getLineID());
            inspectImage.setEquipmentID(item.getEquipmentID());
            inspectImage.setInspectionItemID(item.getItemID());
            inspectImage.setPlanID(item.getPlanID());
            inspectImage.setBeginTime(item.getBeginTime());
            inspectImage.setEndTime(item.getEndTime());
            inspectImage.setUserId(SharedPreferenceUtil.getId(this, "User"));
            InspectImageQueryUtil.insertImage(inspectImage);
            imageList.add(inspectImage);
            Log.e("imagePaths", imagePaths.get(i).getPath() + "," + RestCreator.UPLOAD_PATH);
        }
        InspectImageManager.postInpsectImageToServer(ObservedActivity.this,imageList);


    }
    /** * 判断sdcard是否被挂载 */
    public boolean hasSdcard() {
        return Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED);
    }

    public void takePicture() {
        executor.execute(pathRunnable);
         File dirPath = getExternalFilesDir("MesImg");
         PHOTO_FILE = "Item"+item.getItemID()+"_"+TimeUtil.dateNoFormat(new Date())+".jpg";
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
    请求webService接口，获取图片路径
     */
   private Runnable pathRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                RestCreator.UPLOAD_PATH = RestCreator.getImagePath();
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
                    if("null".equals(RestCreator.UPLOAD_PATH)|| RestCreator.UPLOAD_PATH == ""){
                        executor.execute(pathRunnable);
                    }

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
    //下一个巡检项逻辑

    private void saveValue(){
        String checkValue = radio.getCheckedRadioButtonId() == R.id.rb_no ? ConfigInfo.ITEM_CHECK_ABNORMAL:ConfigInfo.ITEM_CHECK_NORMAL;
       //查询这条Record是否存在
        Record haveRecord = RecordQueryUtil.getRecordByItemId(this,itemId,lineId);
        //不存在的话创建，存在的话更新
        if(haveRecord==null){
            //上传后台项
            Record record = new Record();
            recordSave(record, item);
            RecordQueryUtil.insertRecord(record);

        }else{
            recordSave(haveRecord,item);
            RecordQueryUtil.updateRecord(haveRecord);
        }
        //保存item
        Item item = ItemQueryUtil.getItemById(this,itemId,lineId);
        item.setCheckValue(checkValue);
        item.setCheckStatus(ConfigInfo.ITME_CHECKED_STATUS);
        ItemQueryUtil.updateItem(item);



    }
    /*
   巡检值保存到数据库，上传到服务器
    */
    private void recordSave(final Record record, final Item item) {
        String checkValue = radio.getCheckedRadioButtonId() == R.id.rb_no ? ConfigInfo.ITEM_CHECK_ABNORMAL:ConfigInfo.ITEM_CHECK_NORMAL;
        record.setCheckValue(checkValue);
        record.setCheckConclusion(item.getStandardValue().equals(checkValue) ? 5 : 1); //5正常1异常
        record.setCheckUser(SharedPreferenceUtil.getName(this,"User"));
        record.setUserId(SharedPreferenceUtil.getId(this,"User"));
        record.setRemark(remark.getText().toString());
        record.setInputDate(TimeUtil.dateFormat(new Date()));
        record.setDangerTitle(placeName+item.getCheckContent());
        record.setEquipmentID(item.getEquipmentID());
        record.setPlanId(item.getPlanID());
        record.setLineID(item.getLineID());
        record.setInspectionItemID(item.getItemID());
        record.setBeginTime(TimeUtil.dateTimeFormat(item.getBeginTime()));
        record.setEndTime(TimeUtil.dateTimeFormat(item.getEndTime()));
        record.setCheckStatus(ConfigInfo.ITEM_UNCHECK_STATUS);//1待2漏3已巡检
        List<Record> records = new ArrayList<>();
        records.add(record);
        //上传到服务器
        RecordManager.realPostRecord(ObservedActivity.this,records);

    }
    /*
    下一个巡检项
     */
    private void nextInspect() {
        //查询要巡检的项
        List<Item> itemList = ItemQueryUtil.getItemByStatus(this,placeId,lineId,ConfigInfo.ITEM_UNCHECK_STATUS);
        if(itemList.size()!=0){
            Item localItem = itemList.get(0);
            int checkType = localItem.getCheckType();//.NumberFormatException: Invalid int: ""
            switch (checkType){
                case (ConfigInfo.ITME_TYPE_VIBRATE):
                case(ConfigInfo.ITME_TYPE_TEMPERATURE):
                    Intent intent = new Intent(ObservedActivity.this,MainActivity.class);
                    intent.putExtra("itemId",localItem.getItemID());
                    intent.putExtra("lineId",localItem.getLineID());
                    intent.putExtra("placeId",localItem.getPlaceID());
                    intent.putExtra("name",placeName);
                    startActivity(intent);
                    finish();
                    // startActivityForResult(intent,ConfigInfo.ITME_TYPE_OBSERVE);
                    break;
                case (ConfigInfo.ITME_TYPE_OBSERVE):
                    intent = new Intent(ObservedActivity.this,ObservedActivity.class);
                    intent.putExtra("itemId",localItem.getItemID());
                    intent.putExtra("lineId",localItem.getLineID());
                    intent.putExtra("placeId",localItem.getPlaceID());
                    intent.putExtra("name",placeName);
                    startActivity(intent);
                    finish();
                    break;

                case(ConfigInfo.ITME_TYPE_READMETER):

                    break;
                case(ConfigInfo.ITME_TYPE_VIBRATE_TEMP):
                    break;
                default:
                    break;
            }
        }else{
            finish();
        }
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

    /*
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ObservedActivity.this);
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
                                PHOTO_FILE = "Item"+item.getItemID()+"_"+TimeUtil.dateNoFormat(new Date())+".jpg";
                                imgPath = new File(dir,PHOTO_FILE);
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                //API>=24 android 7.0
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                                    if (intent.resolveActivity(getPackageManager()) != null){
                                        //7.0以上 的拍照文件必须在storage/emulated/0/Android/data/包名/files/pictures文件夹
                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                                        imgUri = FileProvider.getUriForFile(ObservedActivity.this,
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

