package culturnews.culturnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import culturnews.culturnews.Detail_new;
import culturnews.culturnews.R;
import culturnews.culturnews.object.MrNews;
import culturnews.culturnews.util.RestApiInterface;

/**
 * Created by gbern_000 on 5/16/2015.
 */
public class MrNewsAdapter extends RecyclerView.Adapter<MrNewsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<MrNews> items;

    public MrNewsAdapter(ArrayList<MrNews> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noticia_item, parent, false);
        return new ViewHolder(view);
    }

    public int getItemType(int pos) {
        return items.get(pos).getType();
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    public void add(MrNews string) {
        insert(string, items.size());
    }

    public void insert(MrNews event, int position) {
        items.add(position, event);
        notifyItemInserted(position);
    }

    public void remove(int pos) {
        items.remove(pos);
        notifyItemRemoved(pos);
    }

    public void reorder() {
        Collections.sort(items);

        notifyDataSetChanged();
    }

    public MrNews get(int pos) {
        return items.get(pos);
    }

    public void clear() {
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void addAll(ArrayList<MrNews> all) {
        for (int i = 0; i < all.size(); i++) {
            insert(all.get(i), items.size());
            notifyItemInserted(i);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder headerViewHolder, final int position) {
        final MrNews event = items.get(position);
        headerViewHolder.title.setText(event.getTitle());
        headerViewHolder.content.setText(html2text(event.getContent()));
        headerViewHolder.date.setText((new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault()).format(event.getDate())));
        headerViewHolder.cnt.setBackgroundColor(RestApiInterface.getColor(position));
        if(!event.getImgurl().equals(""))
            Picasso.with(context).load(event.getImgurl()).fit().centerCrop().into(headerViewHolder.header);
        headerViewHolder.cnt.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Log.d("content", items.get(position).getContent());
                Intent i = new Intent(context, Detail_new.class);
                i.putExtra("content", event.getContent());
                i.putExtra("title", event.getTitle());
                i.putExtra("date", event.gethdate());
                i.putExtra("imgurl",event.getImgurl());
                i.putExtra("type", event.getType());
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

        TextView title, content;
        ImageView header;
        TextView date;
        RelativeLayout cnt;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.ntitle_txtv);
            header = (ImageView) itemView.findViewById(R.id.nheader_imv);
            date = (TextView) itemView.findViewById(R.id.ndate_txtv);
            cnt = (RelativeLayout) itemView.findViewById(R.id.new_container);
            content = (TextView) itemView.findViewById(R.id.ncontent);
        }
    }
}
