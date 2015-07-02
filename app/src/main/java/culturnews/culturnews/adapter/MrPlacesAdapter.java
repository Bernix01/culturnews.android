package culturnews.culturnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import culturnews.culturnews.Detail_place;
import culturnews.culturnews.R;
import culturnews.culturnews.object.MrPlace;
import culturnews.culturnews.util.RestApiInterface;

/**
 * Created by gbern_000 on 5/23/2015.
 */
public class MrPlacesAdapter extends RecyclerView.Adapter<MrPlacesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<MrPlace> items;

    public MrPlacesAdapter(ArrayList<MrPlace> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.establecimiento_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(MrPlace string) {
        insert(string, items.size());
    }

    public void insert(MrPlace event, int position) {
        items.add(position, event);
        notifyItemInserted(position);
    }

    public void clear() {
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public void onBindViewHolder(ViewHolder headerViewHolder, final int position) {
        final MrPlace event = items.get(position);
        headerViewHolder.title.setText(event.getName());
        headerViewHolder.descr.setText(event.getDescr());
        headerViewHolder.timeto.setText(event.getTimeto());
        headerViewHolder.title.setTextColor(RestApiInterface.getColorE_descr(position));
        headerViewHolder.descr.setTextColor(RestApiInterface.getColorE_descr(position));
        headerViewHolder.cnt.setBackgroundColor(RestApiInterface.getColorE(position));
        if(!event.getHimgurl().equals(""))
            Picasso.with(context).load(event.getHimgurl()).fit().centerCrop().into(headerViewHolder.header);
        headerViewHolder.cnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Detail_place.class);
                i.putExtra("content", event.getContent());
                i.putExtra("title", event.getName());
                i.putExtra("rtimeto", event.getRtimeto());
                i.putExtra("type", event.getType());
                i.putExtra("imgurl",event.getHimgurl());
                i.putExtra("X", event.getX());
                i.putExtra("Y", event.getY());
                i.putExtra("fb", event.getFb());
                i.putExtra("tw", event.getTw());
                i.putExtra("ig", event.getIg());
                i.putExtra("wb", event.getWb());
                Log.e("str", event.getExtra());
                i.putExtra("asd", event.getExtra());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView header;
        TextView timeto, descr;
        RelativeLayout cnt;
        LinearLayout social;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.place_item_name_txtv);
            header = (ImageView) itemView.findViewById(R.id.place_item_h_imv);
            descr = (TextView) itemView.findViewById(R.id.place_item_descr);
            timeto = (TextView) itemView.findViewById(R.id.place_item_timeto_txtv);
            cnt = (RelativeLayout) itemView.findViewById(R.id.place_item_container);
            social = (LinearLayout) itemView.findViewById(R.id.sociallinks);
        }
    }
}
