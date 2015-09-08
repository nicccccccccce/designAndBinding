package demo.test.chi.http;

import android.os.AsyncTask;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * Created by eve on 2015/8/20.
 */
public class HttpAsyTask extends AsyncTask<Request, Void, ResultCallBack> {
    @Override
    protected ResultCallBack doInBackground(Request... params) {
        OkHttpUtil.enqueue(params[0], new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                EventBus.getDefault().post(new ResultCallBack(request, e));
            }

            @Override
            public void onResponse(Response response) throws IOException {
                EventBus.getDefault().post(new ResultCallBack(response));
            }
        });
        return null;
    }
}
