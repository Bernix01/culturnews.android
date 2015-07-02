package culturnews.culturnews.object;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by gbern_000 on 5/15/2015.
 */
public class MrEvent implements Comparable {
    String title;
    String detail;
    Date dateStart;
    Date dateEnd;
    String fb, tw, wb, ig;
    String content;
    int type,id;
    String imgUrl;
    Double X;
    Double Y;
    Boolean isSpecial;
    public MrEvent(int id, String title, String detail, String content, Date dateStart, Date dateEnd, int type, Double X, Double Y, String fb, String tw, String ig, String wb, String imgUrl) {
       this.id=id;
       this.title=title;
       this.detail=detail;
       this.content=content;
       this.dateStart=dateStart;
       this.dateEnd=dateEnd;
       this.type=type;
        this.X = X;
        this.Y = Y;
        this.fb = fb;
        this.ig = ig;
        this.wb = wb;
        this.tw = tw;
        this.imgUrl = imgUrl;
       setIsSpecial();
   }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    public String getTw() {
        return tw;
    }

    public void setTw(String tw) {
        this.tw = tw;
    }

    public String getWb() {
        return wb;
    }

    public void setWb(String wb) {
        this.wb = wb;
    }

    public String getIg() {
        return ig;
    }

    public void setIg(String ig) {
        this.ig = ig;
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

    public Boolean isSpecial(){
        return this.isSpecial;
    }

    public void setIsSpecial(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.getStartDate());
        int dayOfMonthS = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(this.getEndtDate());
        int dayOfMonthE = cal.get(Calendar.DAY_OF_MONTH);
        this.isSpecial= (dayOfMonthE != dayOfMonthS);
    }

    public Date getStartDate() {
        return this.dateStart;
    }

    public void setStartDate(Date date) {
        this.dateStart = date;
    }

    public Date getEndtDate() {
        return this.dateEnd;
    }

    public void setEndDate(Date date) {
        this.dateEnd = date;
    }

    public String getContent() {
        return this.content;
    }

    public Double getX() {
        return this.X;
    }

    public void setX(Double X) {
        this.X = X;
    }

    public Double getY() {
        return this.Y;
    }

    public void setY(Double Y) {
        this.Y = Y;
    }

   /* public Date getTodayEndDate(Date dateEnd ){
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateEnd);

        return fooDate;
    }
*/
    public int getId(){ return this.id;}

    public void setId(int id){ this.id = id;}

    @Override
    public int compareTo(Object another) {
        MrEvent e = (MrEvent) another;
        if (!e.isSpecial() && !isSpecial()) {
            if (dateStart.after(e.dateStart))
                return 1;
            if (dateStart.before(e.dateStart))
                return -1;
        }
        if (!e.isSpecial() && isSpecial()) {
            if (getDayOfMonth(new Date()) < getDayOfMonth(getEndtDate()) && getDayOfMonth(new Date()) > getDayOfMonth(getStartDate())) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(getEndtDate());
                cal.roll(Calendar.DAY_OF_MONTH, -(1));
            }

        }
        return 0;
    }

    private int getDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
}
