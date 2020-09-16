package com.yxst.inspect.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.bumptech.glide.Glide;
import com.yxst.inspect.R;
import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.dao.UserDao;
import com.yxst.inspect.database.model.User;
import com.yxst.inspect.util.SharedPreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By YuanCheng on 2019/12/20 10:29
 */
public class UserInfoActivity extends BaseActivity {
    @BindView(R.id.iv_head) ImageView ivHead;
    @BindView(R.id.tv_name) TextView tvName;
    @BindView(R.id.tv_account) TextView tvAccount;
    @BindView(R.id.tv_dept) TextView tvDept;
    @BindView(R.id.tv_role) TextView tvRole;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        String headImg = SharedPreferenceUtil.getHeadImg(this,"User");
        String url = "http://60.164.211.4:9800/FilesServer"+headImg;
        Glide.with(this).load(url)
                .thumbnail(0.2f)
                .error(R.drawable.user)
                .override(70,70)
                .into(ivHead);
        initView();
    }

    private void initView() {
        String loginAccount = SharedPreferenceUtil.getName(this,"User");
        User user = DatabaseManager.getInstance().getUserDao().queryBuilder().where(UserDao.Properties.LoginName.eq(loginAccount)).list().get(0);
        tvAccount.setText(user.getLoginName());
        tvName.setText(user.getRealName());
        tvDept.setText(user.getDeptName());
        tvRole.setText(user.getRoleNameCN());

    }



}
