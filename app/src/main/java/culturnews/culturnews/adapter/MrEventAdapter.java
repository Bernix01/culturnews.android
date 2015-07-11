package culturnews.culturnews.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import culturnews.culturnews.Detail_event;
import culturnews.culturnews.R;
import culturnews.culturnews.object.MrEvent;
import culturnews.culturnews.util.RestApiInterface;

/**
 * Created by gbern_000 on 5/15/2015.
 */
public class MrEventAdapter extends RecyclerView.Adapter<MrEventAdapter.ViewHolder>{

    private ArrayList<MrEvent> items;
    private Context context;

    public MrEventAdapter(ArrayList<MrEvent> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.evento_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    public int getItemType(int pos) {
        return items.get(pos).getType();
    }

    public void add(MrEvent string) {
        insert(string, items.size());
    }

    public void insert(MrEvent event, int position) {
        items.add(position, event);
        notifyItemInserted(position);
    }

    public void remove(int pos) {
        items.remove(pos);
        notifyItemRemoved(pos);
    }

    public void addAll(ArrayList<MrEvent> all) {
        for (int i = 0; i < all.size(); i++) {
            insert(all.get(i), items.size());
            notifyItemInserted(i);
        }
    }

    public MrEvent get(int pos) {
        return items.get(pos);
    }

    public void clear() {
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }
    @Override
    public void onBindViewHolder(final ViewHolder headerViewHolder, final int position) {
        final MrEvent event = items.get(position);
        headerViewHolder.title.setText(event.getTitle());
        headerViewHolder.detail.setText(event.getDetail());
        if (event.getImgUrl() != null && !event.getImgUrl().equals(""))
            Picasso.with(context).load(event.getImgUrl()).fit().centerCrop().into(headerViewHolder.headerimg);
        headerViewHolder.cnt.setBackgroundColor(RestApiInterface.getColor(position));
        switch (event.getType()){
            case 0:
                headerViewHolder.type.setImageResource(R.drawable.n004);
                break;
            case 1:
                headerViewHolder.type.setImageResource(R.drawable.n001);
                break;
            case 2:
                headerViewHolder.type.setImageResource(R.drawable.n001);
                break;
            case 3:
                headerViewHolder.type.setImageResource(R.drawable.n005);
                break;
            case 4:
                headerViewHolder.type.setImageResource(R.drawable.n003);
                break;
            case 5:
                headerViewHolder.type.setImageResource(R.drawable.n002);
                break;
            default:
                headerViewHolder.type.setImageResource(R.drawable.n001);
                break;
        }
        headerViewHolder.cnt.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Detail_event.class);
                i.putExtra("content", event.getContent());
                i.putExtra("title", event.getTitle());
                i.putExtra("X", event.getX());
                i.putExtra("Y", event.getY());
                i.putExtra("fb", event.getFb());
                i.putExtra("imgurl", event.getImgUrl());
                i.putExtra("tw", event.getTw());
                i.putExtra("ig", event.getIg());
                i.putExtra("wb", event.getWb());
                i.putExtra("desc", items.get(position).getDetail());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }

        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void reorder() {
        //Collections.sort(items);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout cnt;
        TextView title, detail;
        ImageView type, headerimg;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.event_txtv);
            type = (ImageView) itemView.findViewById(R.id.etype_imgv);
            detail = (TextView) itemView.findViewById(R.id.event_detail_txtv);
            cnt = (RelativeLayout) itemView.findViewById(R.id.event_container);
            headerimg = (ImageView) itemView.findViewById(R.id.himgv_evento_item);
        }
    }
}
