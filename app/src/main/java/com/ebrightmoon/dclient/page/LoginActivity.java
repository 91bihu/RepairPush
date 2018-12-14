package com.ebrightmoon.dclient.page;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.ebrightmoon.dclient.R;
import com.ebrightmoon.dclient.bean.ResponseRepair;
import com.ebrightmoon.dclient.util.Constant;
import com.ebrightmoon.dclient.util.GsonUtil;
import com.ebrightmoon.dclient.util.HttpUtils;
import com.ebrightmoon.dclient.util.MD5;
import com.ebrightmoon.dclient.util.PermissionUtil;
import com.ebrightmoon.dclient.util.PrefUtils;
import com.ebrightmoon.dclient.util.SystemUtils;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 作者：create by  Administrator on 2018/12/11
 * 邮箱：2315813288@qq.com
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private Context mContext;
    private Button mBtn;
    private TextInputLayout usernameWrapper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext=this;
        PermissionUtil.getPerInstans().requestPermssions(this,
                new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_WIFI_STATE},
                new PermissionUtil.OnPermissionRequestListener() {
                    @Override
                    public void onRequestSuccess() {

                    }

                    @Override
                    public void onRequestFail() {

                    }
                });
        usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        usernameWrapper.setHint("用户名");
        mBtn = findViewById(R.id.btn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                String username = usernameWrapper.getEditText().getText().toString();
                if(TextUtils.isEmpty(username))
                {
                    usernameWrapper.setError("Not a valid phone!");
                }else {
                    getData(username);
                }
            }
        });


    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    /**
     *  联合登录接口
     */
    private void getData(String username)
    {
        HashMap<String,String> params=new HashMap<>();
        HashMap<String,String> paramsarr=new HashMap<>();
        params.put("AgentId","9121");
        params.put("UserName",username);
        params.put("Timestamp",(int) (System.currentTimeMillis() / 1000)+"");
        params.put("UniqueCode", SystemUtils.getUUID(this));
        params.put("ExpireTime",((int) (System.currentTimeMillis() / 1000+3000))+"");
        paramsarr.putAll(params);
        paramsarr.put("SecretKey","94fee470b43270a912c27d56c27b3211");
        StringBuffer requestParams = HttpUtils.spellPostParams(paramsarr);
        params.put("SecCode", MD5.encode(requestParams.toString()));
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final String requestBody= GsonUtil.gson().toJson(params);
        Request request = new Request.Builder()
                .url("http://wx.91bihu.com/api/unite/LoginAPP")
                .post(RequestBody.create(mediaType, requestBody))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("", "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.protocol() + " " +response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d(TAG, headers.name(i) + ":" + headers.value(i));
                }
                ResponseRepair responseRepair =GsonUtil.gson().fromJson(response.body().string(),ResponseRepair.class);
                PrefUtils.saveString(getApplicationContext(),PrefUtils.KEY.USER_NAME, responseRepair.getUserName());
                PrefUtils.saveInt(getApplicationContext(),PrefUtils.KEY.AGENT_ID, responseRepair.getAgentId());
                PrefUtils.saveInt(getApplicationContext(),PrefUtils.KEY.LOGIN_STATUS, responseRepair.getLoginStatus());
                PrefUtils.saveString(getApplicationContext(),PrefUtils.KEY.TOKEN, responseRepair.getToken());
                PrefUtils.saveString(getApplicationContext(),PrefUtils.KEY.AGENT_NAME, responseRepair.getAgentName());
                PrefUtils.saveInt(getApplicationContext(),PrefUtils.KEY.TOP_AGENT_ID, responseRepair.getTopAgentId());
                PrefUtils.saveString(getApplicationContext(),PrefUtils.KEY.REPEAT_QUOTE, responseRepair.getRepeatQuote());
                Intent intent=new Intent(mContext,RepairPushActivity.class);
                intent.putExtra(Constant.PUSH_REPAIR_TITLE,"预约爱推修");
                intent.putExtra(Constant.URL_REPAIR_URL,"http://wx.91bihu.com/index.html");
                startActivity(intent);
            }
        });

    }
}
