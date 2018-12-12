package com.ebrightmoon.dclient.page;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ebrightmoon.dclient.R;
import com.ebrightmoon.dclient.bean.ResponseRepair;
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
        mBtn = findViewById(R.id.btn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getData();
            }
        });


    }

    /**
     *  联合登录接口
     */
    private void getData()
    {
        HashMap<String,String> params=new HashMap<>();
        HashMap<String,String> paramsarr=new HashMap<>();
        params.put("AgentId","102");
        params.put("UserName","test1");
        params.put("Timestamp",(int) (System.currentTimeMillis() / 1000)+"");
        params.put("UniqueCode", SystemUtils.getUUID(this));
        params.put("ExpireTime",((int) (System.currentTimeMillis() / 1000+3000))+"");
        paramsarr.putAll(params);
        paramsarr.put("SecretKey","60a78c69d89");
        StringBuffer requestParams = HttpUtils.spellPostParams(paramsarr);
        params.put("SecCode", MD5.encode(requestParams.toString()));
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final String requestBody= GsonUtil.gson().toJson(params);
        Request request = new Request.Builder()
                .url("http://192.168.5.19:9094/api/unite/LoginAPP")
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
                startActivity(new Intent(mContext,RepairPushActivity.class));
            }
        });

    }
}
