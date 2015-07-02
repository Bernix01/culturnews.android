package culturnews.culturnews.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import culturnews.culturnews.R;
import culturnews.culturnews.adapter.MrPlacesAdapter;
import culturnews.culturnews.object.MrPlace;
import culturnews.culturnews.util.RestApiInterface;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class RecyclerViewFragment extends Fragment {

    GoogleApiClient mGoogleApiClient;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<MrPlace> mContentItems = new ArrayList<>();
    private Boolean hasLocationEnabled = false;
    private String loc;
    private int catid;
    private boolean isupdatingTimeTo=false;

    public static RecyclerViewFragment newInstance(int catid, String loc) {
        RecyclerViewFragment f = new RecyclerViewFragment();
        Bundle args = new Bundle();
        args.putInt("catid", catid);
        args.putString("loc",loc);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        Bundle bundle = this.getArguments();
        this.loc = bundle.getString("loc");
        try {
            getPlaces(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getPlaces(final int offset) throws JSONException {
        RequestParams params = new RequestParams();
        Bundle bundle = this.getArguments();
        int myInt = bundle.getInt("catid", 11);
        catid=myInt;
        params.put("api_key", RestApiInterface.getKey());
        params.put("catid", myInt);
        params.put("limit", RestApiInterface.stdLimit);
        params.put("maxsubs", 5);
        params.put("orderby", "created");
        params.put("orderdir", "desc");
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
                        getPlaces(offset);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getString("status").equals("ok")) {
                        int max_pages = response.getInt("pages_total");
                        JSONArray News = response.getJSONArray("articles");
                        for (int i = 0; i < News.length(); i++) {
                            JSONObject foo = News.getJSONObject(i);
                            JSONObject metadata = foo.getJSONObject("metadata");
                            JSONObject dats = new JSONObject(metadata.getString("xreference"));
                            MrPlace temp;
                            Log.e("wbrd", RestApiInterface.tryGetStr(dats, "wbrd") + " ");
                            temp = new MrPlace(foo.getString("title"), "", 0, DoubleIt(dats.getString("x")), DoubleIt(dats.getString("y")), foo.getString("content"), dats.getString("fb"), dats.getString("tw"), dats.getString("w"), dats.getString("ig"), (foo.getString("category_title")),
                                    RestApiInterface.tryGetImageUrl(foo.getJSONObject("images")), foo.getString("metadesc"), RestApiInterface.tryGetStr(dats, "wbrd"));
                            mContentItems.add(temp);
                        }

                        mAdapter = new RecyclerViewMaterialAdapter(new MrPlacesAdapter(mContentItems, getActivity().getApplicationContext()));
                        if (offset == max_pages - 1) {
                            if (loc != null)
                                timeto(0);
                            mRecyclerView.setAdapter(mAdapter);
                            MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }


    public Double DoubleIt(String d) {
        if (d.equals(""))
            return 0.0;
        return Double.parseDouble(d);
    }
    public void timeto(final int pos){
        isupdatingTimeTo=true;
        final MrPlace temp = mContentItems.get(pos);
        if(temp.getX() == 0 && temp.getY() == 0) {
            int b=pos;
            b++;
            if (pos == mContentItems.size()-1)
            return;
            timeto(b);
        }

        RequestParams params = new RequestParams();
        params.put("mode","driving");
        params.put("key","AIzaSyCI-hxRj48cwIQVUmQIjDdDdaoEsvkuVT0");
        params.put("origin", loc);
        params.put("destination", String.valueOf(temp.getX())+","+String.valueOf(temp.getY()));
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://maps.googleapis.com/maps/api/directions/json",params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getString("status").equals("OVER_QUERY_LIMIT")) {
                        Log.e("timeto", "over quota");
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // called when response HTTP status is "200 OK"
                Log.i("responseTimeTo", response.toString());
                try {
                    JSONObject foo = response.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("duration");
                    temp.setRtimeto(foo.getInt("value"));
                    temp.setTimeto(foo.getString("text"));
                    mContentItems.set(pos, temp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(pos == mContentItems.size()-1) {
                    mAdapter.notifyDataSetChanged();
                    finishTimeTo();
                    return;
                }
                int a = pos;
                a++;
                timeto(a);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e("error", throwable.toString());
            }

            @Override
            public void onRetry(int retryNo) {
                timeto(pos);
            }
        });
    }

    public void finishTimeTo(){
        this.isupdatingTimeTo=false;
        Log.d("TimeTO", "finished");
    }

}
