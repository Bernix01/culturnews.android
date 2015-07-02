package culturnews.culturnews.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import culturnews.culturnews.MainActivity;
import culturnews.culturnews.R;
import culturnews.culturnews.adapter.MrNewsAdapter;
import culturnews.culturnews.object.MrNews;
import culturnews.culturnews.util.RestApiInterface;
import yalantis.com.sidemenu.interfaces.ScreenShotable;

public class NewsFragment extends Fragment implements ScreenShotable {
    int max_pages = 1;
    LinearLayout fcontainer;
    private MrNews bean;
    private ArrayList<MrNews> itemList;
    private ArrayList<MrNews> garbageMan = new ArrayList<MrNews>();
    private View containerView;
    private Bitmap bitmap;
    private Boolean isFull = true;
    private MrNewsAdapter adapt;
    private SuperRecyclerView newsList;

    public static NewsFragment newInstance() {
        NewsFragment contentFragment = new NewsFragment();
        return contentFragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.containerView = view.findViewById(R.id.container);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_news, container, false);
        this.newsList = (SuperRecyclerView)rootView.findViewById(R.id.listv);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newsList.setLayoutManager(layoutManager);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Noticias");
        fcontainer = (LinearLayout) rootView.findViewById(R.id.filters);
        Log.d("tas", "starting");
        try {
            getNews(0);
        }catch (JSONException e){
            Log.e("Error News json", e.toString());
        }
        return rootView;
    }

    public void assignClickHandler(final LinearLayout root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            final int a = i;
            (root.getChildAt(i)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int c = a;
                    c--;
                    changeBackground(c++, root);
                    ((MainActivity) getActivity()).setActionBarTitle("Noticias " + RestApiInterface.getHumanCategory(c));
                    c--;
                    filter(c);
                }
            });
        }
    }
    public void getNews(final int offset) throws JSONException {
        RequestParams params = new RequestParams();
        params.put("api_key", RestApiInterface.getKey());
        params.put("catid", RestApiInterface.news);
        params.put("limit", RestApiInterface.stdLimit);
        params.put("maxsubs", 5);
        params.put("offset", offset);
        RestApiInterface.get(getActivity().getApplicationContext(), "get/content/articles/", params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e("error", throwable.toString());
            }

            @Override
            public void onRetry(int retryNo) {
                if (retryNo < 3) {
                    try {
                        getNews(offset);
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                newsList.hideRecycler();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("task", "finish");
                try {
                    if (response.getString("status").equals("ok")) {
                        int max_pages = response.getInt("pages_total");
                        itemList = new ArrayList<MrNews>();
                        JSONArray News = response.getJSONArray("articles");
                        Log.i("size", String.valueOf(News.length()));
                        for (int i = 0; i < News.length(); i++) {
                            JSONObject foo = News.getJSONObject(i);
                            MrNews temp = new MrNews(foo.getInt("id"), getType(foo.getString("category_title")), foo.getString("title"), foo.getString("metadesc"), foo.getString("content"), RestApiInterface.tryGetImageUrl(foo.getJSONObject("images")), todate(foo.getString("published_date")));
                            itemList.add(temp);
                        }

                        WindowManager wm = (WindowManager) getActivity().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                        Display display = wm.getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        int width = size.x;
                        int height = (width / 27) * 20;
                        adapt = new MrNewsAdapter(itemList, getActivity().getApplicationContext());
                        newsList.setAdapter(adapt);

                        assignClickHandler(fcontainer);
                        newsList.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                try {
                                    getNewNews(0);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        newsList.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
                        if (max_pages > 1)
                            newsList.setupMoreListener(new OnMoreListener() {
                                @Override
                                public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
                                    try {
                                        getMoreNews(numberOfItems);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, 10);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    public void getMoreNews(final int nitems) throws JSONException {
        RequestParams params = new RequestParams();
        params.put("api_key", RestApiInterface.getKey());
        params.put("catid", RestApiInterface.news);
        params.put("limit", RestApiInterface.stdLimit);
        params.put("maxsubs", 5);
        params.put("offset", Math.round(nitems / RestApiInterface.stdLimit));
        RestApiInterface.get(getActivity().getApplicationContext(), "get/content/articles/", params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e("error", throwable.toString());
            }

            @Override
            public void onRetry(int retryNo) {
                if (retryNo < 3) {
                    try {
                        getMoreNews(nitems);
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                newsList.hideRecycler();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("response", response.toString() + " offset: " + String.valueOf(Math.round(nitems / RestApiInterface.stdLimit)));
                try {
                    if (response.getString("status").equals("ok")) {
                        max_pages = response.getInt("pages_total");
                        itemList = new ArrayList<MrNews>();

                        JSONArray News = response.getJSONArray("articles");
                        Log.i("size", String.valueOf(News.length()));
                        for (int i = 0; i < News.length(); i++) {
                            JSONObject foo = News.getJSONObject(i);
                            adapt.add(new MrNews(foo.getInt("id"), getType(foo.getString("category_title")), foo.getString("title"), foo.getString("metadesc"), foo.getString("content"), RestApiInterface.tryGetImageUrl(foo.getJSONObject("images")), todate(foo.getString("published_date"))));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RestApiInterface.CancelR(getActivity().getApplicationContext(), true);
    }
    public void getNewNews(final int offset) throws JSONException {
        RequestParams params = new RequestParams();
        params.put("api_key", RestApiInterface.getKey());
        params.put("catid", RestApiInterface.news);
        params.put("limit", RestApiInterface.stdLimit);
        params.put("maxsubs", 5);
        params.put("offset", offset);
        RestApiInterface.get(getActivity().getApplicationContext(), "get/content/articles/", params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e("error", throwable.toString());
            }

            @Override
            public void onRetry(int retryNo) {
                if (retryNo < 3) {
                    try {
                        getNewNews(offset);
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getString("status").equals("ok")) {
                        Boolean flaggy = false;
                        max_pages = response.getInt("pages_total");
                        JSONArray News = response.getJSONArray("articles");
                        for (int i = 0; i < News.length(); i++) {
                            Boolean alreadyin = false;
                            JSONObject foo = News.getJSONObject(i);
                            int id = foo.getInt("id");
                            for (int j = 0; j < adapt.getItemCount(); j++) {
                                long tempId = adapt.getItemId(j);
                                if (id == tempId) {
                                    alreadyin = true;
                                    break;
                                }
                            }
                            Log.e("already?", alreadyin + "");
                            if (!alreadyin) {
                                adapt.insert(new MrNews(id, getType(foo.getString("category_title")), foo.getString("title"), foo.getString("metadesc"), foo.getString("content"),
                                        RestApiInterface.tryGetImageUrl(foo.getJSONObject("images")), todate(foo.getString("published_date"))), 0);
                                adapt.notifyItemInserted(0);
                            }
                        }
                        Log.e("already?", "done");
                        if(max_pages>1)
                            newsList.setupMoreListener(new OnMoreListener() {
                                @Override
                                public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
                                    try {
                                        getMoreNews(numberOfItems);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, 10);
                        adapt.notifyDataSetChanged();
                        if(max_pages>1 && max_pages>offset) {
                            int a = offset;
                            a++;
                            getNewNews(a);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    public Date todate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    private void changeBackground(int pos, LinearLayout root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            (root.getChildAt(i)).setBackgroundColor(Color.parseColor("#5b5b5b"));
        }
        pos++;
        root.getChildAt(pos).setBackgroundColor(Color.parseColor("#292929"));
    }
    public int getType(String type) {
        switch (type) {
            case "NOTICIAS TEATRO":
                return 0;
            case "NOTICIAS MUSICA":
                return 1;
            case "NOTICIAS MUSICA2":
                return 2;
            case "DANZA":
                return 3;
            case "NOTICIAS PINTURA":
                return 4;
            case "NOTICIAS LITERATURA":
                return 5;
            default:
                return 0;
        }
    }

    public void filter(int type) {
        Log.e("context", "isFull: " + isFull + " type: " + type);
        Log.e("size", "" + itemList.size());
        if (type == -1 && !isFull) {
            newsList.showProgress();
            adapt.addAll(garbageMan);
            isFull = true;
            garbageMan.clear();
        }
        if (isFull && type != -1) {
            Log.e("adapt size", "" + adapt.getItemCount());
            for (int i = adapt.getItemCount() - 1; i >= 0; i--)
                if (adapt.getItemType(i) != type)
                    doTheThing(i);
        }
        if (!isFull && type != -1) {
            newsList.showProgress();
            adapt.addAll(garbageMan);
            garbageMan.clear();
            isFull = true;
            filter(type);
            return;
        }
        if (type != -1)
            isFull = false;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                adapt.reorder();
            }
        }, 300);
        newsList.showRecycler();
    }

    public void doTheThing(int pos) {
        Log.e("deleted pos", "" + pos);
        garbageMan.add(adapt.get(pos));
        adapt.remove(pos);
    }
    @Override
    public void takeScreenShot() {
        runThread();

    }

    private void runThread() {

        new Thread() {
            public void run() {
                try {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Bitmap bitmap = Bitmap.createBitmap(containerView.getWidth(),
                                    containerView.getHeight(), Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            containerView.draw(canvas);
                            NewsFragment.this.bitmap = bitmap;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }
}

