package demo.test.chi.http;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by eve on 2015/8/20.
 */
public class ResultCallBack implements Serializable {
    Request request;
    IOException e;
    Response response;

    public ResultCallBack() {
    }

    public ResultCallBack(Request request, IOException e) {
        this.request = request;
        this.e = e;
    }

    public ResultCallBack(Response response) {

        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public IOException getE() {
        return e;
    }

    public void setE(IOException e) {
        this.e = e;
    }
}
