[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/tangsiyuan/maven/myokhttp/images/download.svg) ](https://bintray.com/tangsiyuan/maven/myokhttp/_latestVersion)
# RepairPush
  此Demo是第三方需要嵌入爱推修H5示例。
  
 ## Usage
 **Step1**
  调联合登录接口  接口地址：https://91bihu.gitbooks.io/bihu_uniteapp_doc/chapter1/lian-he-deng-lu-jie-kou.html
  如下
  
 >   HashMap<String,String> params=new HashMap<>();
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
                //可传对象，也可以本地保存读取
                Intent intent=new Intent(mContext,RepairPushActivity.class);
                //标题
                intent.putExtra(Constant.PUSH_REPAIR_TITLE,"预约爱推修");
                //爱推修地址
                intent.putExtra(Constant.URL_REPAIR_URL,"http://192.168.5.19:7776");
                startActivity(intent);
            }
        }); 
        
   **Step2**
   在RepairPushActivity类配置跳转地址
   >
         /**
         * 返回首页方法
         */
        @JavascriptInterface
        public void goHome() {
            //替换成第三方产品主页
            startActivity(new Intent(mContext, HomeActivity.class));
            finish();
        }

        /**
         * 返回登录页方法
         */
        @JavascriptInterface
        public void goLogin() {
            //替换成第三方产品登录页
            startActivity(new Intent(mContext, LoginActivity.class));
            finish();
        }
      
 
 
 ## Technical Support 
QQ:2315813288  
Email:jinsedeyuzhou@sina.com  

## License

** Copyright (C) dou361, The Framework Open Source Project**

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

(Frequently Asked Questions)FAQ
##Bugs Report and Help

If you find any bug when using project, please report here. Thanks for helping us building a better one.
