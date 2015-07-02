package culturnews.culturnews.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

/**
 * Created by gbern_000 on 5/15/2015.
 */
public class RestApiInterface {
    private static final String BASE_URL = "http://next.culturnews.com/endpoint/";
    public static int news = 9;
    public static int events = 8;
    public static int stdLimit = 30;
    public static int places = 10;
    private static String key = "IL5H9IGAWDCCKQQKWUDF";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(context, getAbsoluteUrl(url), params, responseHandler);
        }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static void CancelR(Context context, Boolean foo) {
        client.cancelRequests(context, foo);
    }
    public static String getKey() {
        return key;
    }

    public static String tryGetImageUrl(JSONObject imgs) {
        try {
            return imgs.getString("image_intro");

        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getHumanCategory(int cat) {
        switch (cat) {
            case 1:
                return "de Teatro";
            case 2:
                return "de Música Clásica";
            case 3:
                return "de Música Contemporánea";
            case 4:
                return "de Danza";
            case 5:
                return "de Pintura";
            case 6:
                return "de Literatura";
            default:
                return "";
        }
    }

    public static int getColor(int pos) {
        //a37091   bc683c    90a7af    dd6021  41b89c  c9a477
        //c9a477   dd6021       a37091     b78757       bc683c      90a7af
        //        3a2721  e48950  826335  3c2d2a  b75526  d59e5b
        pos = pos % 6;
        switch (pos) {
            case 0:
                return Color.parseColor("#3a2721");
            case 1:
                return Color.parseColor("#e48950");
            case 2:
                return Color.parseColor("#826335");
            case 3:
                return Color.parseColor("#3c2d2a");
            case 4:
                return Color.parseColor("#b75526");
            case 5:
                return Color.parseColor("#d59e5b");
            default:
                return Color.parseColor("#d59e5b");

        }
    }

    public static int getH(WindowManager wm) {
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    public static int getColorE(int pos) {
        //a37091   bc683c    90a7af    dd6021  41b89c  c9a477
        //4e4136  625343  373530  e1cb9a   5b5040   c5aa72
        pos = pos % 6;
        switch (pos) {
            case 0:
                return Color.parseColor("#4e4136");
            case 1:
                return Color.parseColor("#625343");
            case 2:
                return Color.parseColor("#373530");
            case 3:
                return Color.parseColor("#e1cb9a");
            case 4:
                return Color.parseColor("#5b5040");
            case 5:
                return Color.parseColor("#c5aa72");
            default:
                return Color.parseColor("#c5aa72");

        }
    }

    public static String tryGetStr(JSONObject obj, String name) {
        try {
            return obj.getString(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int tryGetInt(JSONObject obj, String name) {
        try {
            return obj.getInt(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getColorE_descr(int pos) {
        pos = pos % 6;
        switch (pos) {
            default:
                return Color.parseColor("#FFFFFF");
        }
    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }
}
