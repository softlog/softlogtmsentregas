package br.eti.softlog.utils;

/**
 * Created by Paulo Sérgio Alves on 2018/03/15.
 */

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
public class AppSingleton {
    private static AppSingleton mAppSingletonInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    private AppSingleton(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized AppSingleton getInstance(Context context) {
        if (mAppSingletonInstance == null) {
            mAppSingletonInstance = new AppSingleton(context);
        }
        return mAppSingletonInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(tag);
        req.setRetryPolicy(new DefaultRetryPolicy(120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }
}