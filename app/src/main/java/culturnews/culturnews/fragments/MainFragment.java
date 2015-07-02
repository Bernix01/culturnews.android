package culturnews.culturnews.fragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import culturnews.culturnews.MainActivity;
import culturnews.culturnews.R;
import culturnews.culturnews.adapter.MrEventAdapter;
import culturnews.culturnews.object.MrEvent;
import culturnews.culturnews.util.RestApiInterface;
import yalantis.com.sidemenu.interfaces.ScreenShotable;

public class MainFragment extends Fragment implements ScreenShotable {
    public static final String CLOSE = "Close";
    public static final String BUILDING = "Building";
    public static final String BOOK = "Book";
    public static final String MOVIE = "Movie";
    public static final String MORE = "More";
    LinearLayout fcontainer;
    private MrEventAdapter adapt;
    private ArrayList<MrEvent> itemList;
    private ArrayList<MrEvent> garbageMan = new ArrayList<MrEvent>();
    private MrEvent bean;
    private View containerView;
    private Bitmap bitmap;
    private SuperRecyclerView eventsList;
    private int max_pages;
    private int[] act_types = new int[6];
    private int explorer;
    private Boolean isFull = true;

    public static MainFragment newInstance() {
        MainFragment contentFragment = new MainFragment();
        Bundle bundle = new Bundle();
        contentFragment.setArguments(bundle);
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
        View rootView = inflater.inflate(R.layout.list_events, container, false);
        this.eventsList = (SuperRecyclerView) rootView.findViewById(R.id.listv);
        fcontainer = (LinearLayout) rootView.findViewById(R.id.filters);
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
        eventsList.setLayoutManager(layoutManager);
        try {
            getEvents(0);
        } catch (JSONException e) {
            Log.e("Error event json", e.toString());
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
                    ((MainActivity) getActivity()).setActionBarTitle("Eventos " + RestApiInterface.getHumanCategory(c));
                    c--;
                    filter(c);
                }
            });
        }
    }


    private void changeBackground(int pos, LinearLayout root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            (root.getChildAt(i)).setBackgroundColor(Color.parseColor("#5b5b5b"));
        }
        pos++;
        root.getChildAt(pos).setBackgroundColor(Color.parseColor("#292929"));
    }

    public void getEvents(final int offset) throws JSONException {
        RequestParams params = new RequestParams();
        params.put("api_key", RestApiInterface.getKey());
        params.put("catid", RestApiInterface.events);
        params.put("limit", RestApiInterface.stdLimit);
        params.put("orderby", "created");
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
                        getEvents(offset);
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                eventsList.hideRecycler();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getString("status").equals("ok")) {
                        max_pages = response.getInt("pages_total");
                        itemList = new ArrayList<MrEvent>();
                        JSONArray events = response.getJSONArray("articles");
                        Log.i("size", String.valueOf(events.length()));
                        explorer += events.length();
                        for (int i = 0; i < events.length(); i++) {
                            JSONObject foo = events.getJSONObject(i);
                            JSONObject metadata = foo.getJSONObject("metadata");
                            JSONObject dats = new JSONObject(metadata.getString("xreference"));
                            Log.d("pos", String.valueOf(i));
                            Log.e("end", foo.getString("title") + " " + dats.getString("e"));
                            MrEvent temp = new MrEvent(foo.getInt("id"),
                                    foo.getString("title"), foo.getString("metadesc"),
                                    foo.getString("content"),
                                    todate(dats.getString("s")),
                                    todate(dats.getString("e")),
                                    getType(foo.getString("category_title")),
                                    dats.getDouble("x"),
                                    dats.getDouble("y"),
                                    dats.getString("fb"),
                                    dats.getString("tw"),
                                    dats.getString("ig"),
                                    dats.getString("w"),
                                    RestApiInterface.tryGetImageUrl(foo.getJSONObject("images")));
                            if (temp.getEndtDate().after(new Date()))
                                itemList.add(temp);
                        }
                        Log.e("Final size", itemList.size() + "");
                        Collections.sort(itemList);

                        adapt = new MrEventAdapter(itemList, getActivity().getApplicationContext());
                        eventsList.setAdapter(adapt);
                        Log.e("Final size", adapt.getItemCount() + "");
                        assignClickHandler(fcontainer);
                        eventsList.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                try {
                                    getNewEvents(0);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        eventsList.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
                        if (max_pages > 1)
                            eventsList.setupMoreListener(new OnMoreListener() {
                                @Override
                                public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
                                    try {
                                        getMoreEvents();
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

    @Override
    public void onDetach() {
        super.onDetach();
        RestApiInterface.CancelR(getActivity().getApplicationContext(), true);
    }

    public void getMoreEvents() throws JSONException {
        RequestParams params = new RequestParams();
        params.put("api_key", RestApiInterface.getKey());
        params.put("catid", RestApiInterface.events);
        params.put("limit", RestApiInterface.stdLimit);
        params.put("orderby", "created");
        params.put("maxsubs", 5);
        params.put("offset", Math.round(explorer / RestApiInterface.stdLimit));
        RestApiInterface.get(getActivity().getApplicationContext(), "get/content/articles/", params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e("error", throwable.toString());
            }

            @Override
            public void onRetry(int retryNo) {
                if (retryNo < 3) {
                    try {
                        getMoreEvents();
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                eventsList.hideRecycler();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getString("status").equals("ok")) {
                        max_pages = response.getInt("pages_total");
                        itemList = new ArrayList<MrEvent>();

                        JSONArray events = response.getJSONArray("articles");
                        Log.i("size", String.valueOf(events.length()));
                        for (int i = 0; i < events.length(); i++) {
                            JSONObject foo = events.getJSONObject(i);
                            JSONObject metadata = foo.getJSONObject("metadata");
                            JSONObject dats = new JSONObject(metadata.getString("xreference"));
                            MrEvent temp = new MrEvent(foo.getInt("id"), foo.getString("title"), foo.getString("metadesc"), foo.getString("content"), todate(dats.getString("s")),
                                    todate(dats.getString("e")), getType(foo.getString("category_title")), dats.getDouble("x"), dats.getDouble("y"), dats.getString("fb"), dats.getString("tw"),
                                    dats.getString("ig"), dats.getString("w"),
                                    RestApiInterface.tryGetImageUrl(foo.getJSONObject("images")));
                            if (temp.getEndtDate().after(new Date()))
                                adapt.add(temp);
                        }
                        Collections.sort(itemList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    public void getNewEvents(final int offset) throws JSONException {
        RequestParams params = new RequestParams();
        params.put("api_key", RestApiInterface.getKey());
        params.put("catid", RestApiInterface.events);
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
                        getNewEvents(offset);
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
                        max_pages = response.getInt("pages_total");
                        JSONArray events = response.getJSONArray("articles");
                        for (int i = 0; i < events.length(); i++) {
                            Boolean alreadyin = false;
                            JSONObject foo = events.getJSONObject(i);
                            Log.e("ids", "last: " + adapt.getItemId(0) + " new first: " + foo.getInt("id"));
                            JSONObject metadata = foo.getJSONObject("metadata");
                            int id = foo.getInt("id");
                            JSONObject dats = new JSONObject(metadata.getString("xreference"));
                            MrEvent temp = new MrEvent(id, foo.getString("title"), foo.getString("metadesc"), foo.getString("content"), todate(dats.getString("s")), todate(dats.getString("e")),
                                    getType(foo.getString("category_title")), dats.getDouble("x"), dats.getDouble("y"), dats.getString("fb"), dats.getString("tw"), dats.getString("ig"),
                                    dats.getString("w"),
                                    RestApiInterface.tryGetImageUrl(foo.getJSONObject("images")));
                            for (int j = 0; j < adapt.getItemCount(); j++) {
                                if (id == adapt.getItemId(j)) {
                                    alreadyin = true;
                                    break;
                                }
                            }
                            if (temp.getEndtDate().after(new Date()) && !alreadyin) {
                                adapt.insert(temp, 0);
                                adapt.notifyItemInserted(0);
                            }

                        }
                        if (max_pages > 1)
                            eventsList.setupMoreListener(new OnMoreListener() {
                                @Override
                                public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
                                    try {
                                        getNewEvents(offset);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, 10);
                        Collections.sort(itemList);
                        adapt.notifyDataSetChanged();
                        if (max_pages > 1 && max_pages > offset) {
                            int a = offset;
                            a++;
                            getNewEvents(a);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    public Date todate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public int getType(String type) {
        switch (type) {
            case "TEATRO":
                return 0;
            case "MUSICA CONTEMPORANEA":
                return 1;
            case "MUSICA CLASICA":
                return 2;
            case "DANZA":
                return 3;
            case "PINTURA":
                return 4;
            case "LITERATURA":
                return 5;
            default:
                return 0;
        }
    }

    public void filter(int type) {
        if (type == -1 && !isFull) {
            eventsList.showProgress();
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
            eventsList.showProgress();
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
        eventsList.showRecycler();
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
                            MainFragment.this.bitmap = bitmap;
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

