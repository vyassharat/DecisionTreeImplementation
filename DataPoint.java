import java.util.Objects;

public class DataPoint {
    private String a;
    private String b;
    private String c;
    private String d;

    public DataPoint(String i1,String i2, String i3, String i4){
        a=i1;
        b=i2;
        c=i3;
        d=i4;
    }

    public String getA() {
        return a;
    }

    public String getB() {
        return b;
    }

    public String getC() {
        return c;
    }

    public String getD() {
        return d;
    }

    public void print(){
        System.out.println("A:"+a);
        System.out.println("B:"+b);
        System.out.println("C:"+c);
        System.out.println("D:"+d);
    }
}
