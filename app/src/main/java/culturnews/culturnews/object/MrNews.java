package culturnews.culturnews.object;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gbern_000 on 5/15/2015.
 */
public class MrNews implements Comparable {
    String title;
    String detail;
    Date date;
    String hdate;
    String content;
    String imgurl;
    int type,id;

    public MrNews(int id, int type, String title, String detail, String content, String imgurl, Date datedate) {
        this.id = id;
        this.type = type;

        this.title = title;

        this.detail = detail;
        this.content = content;
        this.imgurl = imgurl;
        this.date = datedate;
        this.hdate = getHumanDate(datedate);
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent(){ return this.content; }

    public void setContent(String content){ this.content = content;}

    public String gethdate() {
        return this.hdate;
    }

    private String getHumanDate(Date date) {
        DateFormat df = new SimpleDateFormat("dd/MM yyyy");
        return df.format(date);
    }

    @Override
    public int compareTo(Object another) {
        MrNews e = (MrNews) another;
        if (date.after(e.date))
            return 1;
        if (date.before(e.date))
            return -1;
        return 0;
    }
}
