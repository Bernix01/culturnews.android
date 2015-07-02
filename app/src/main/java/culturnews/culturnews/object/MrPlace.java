package culturnews.culturnews.object;

/**
 * Created by gbern_000 on 5/22/2015.
 */
public class MrPlace {
    private String name;
    private String timeto;
    private int rtimeto;
    private double x, y;
    private String himgurl;
    private String content;
    private String descr;
    private String extra;

    private String fb, tw, wb, ig;
    private String type;
    private float distanceto;

    public MrPlace(String name, String timeto, int rtimeto, double x, double y, String content, String fb, String tw, String wb, String ig, String type, String himgurl, String descr, String extra) {

        this.name = name;
        this.timeto = timeto;
        this.rtimeto = rtimeto;
        this.x = x;
        this.descr = descr;
        this.y = y;
        this.content = content;
        this.fb = fb;
        this.tw = tw;
        this.wb = wb;
        this.himgurl = himgurl;
        this.ig = ig;
        this.type = type;
        this.extra = extra;

    }

    public String getExtra() {
        return extra;
    }

    public String getDescr() {
        return descr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeto() {
        return timeto;
    }

    public void setTimeto(String timeto) {
        this.timeto = timeto;
    }

    public int getRtimeto() {
        return rtimeto;
    }

    public void setRtimeto(int rtimeto) {
        this.rtimeto = rtimeto;
    }

    public double getX() {
        return x;
    }


    public double getY() {
        return y;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFb() {

        return fb;
    }


    public String getTw() {
        return tw;
    }


    public String getWb() {
        return wb;
    }


    public String getIg() {
        return ig;
    }


    public String getHimgurl() {
        return himgurl;
    }


    public float getDistanceto() {
        return distanceto;
    }

    public void setDistanceto(float distanceto) {
        this.distanceto = distanceto;
    }

}
