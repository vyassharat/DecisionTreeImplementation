import java.util.ArrayList;

public class Node {
    private ArrayList<Node>  childNodes = new ArrayList<>();
    private Node parent;
    private boolean isTerminalNode;
    private int result;
    private String splittingVal;
    private int splitAttr;

    //attrNum is the col num and result..0=-,1=+
    public Node(int attrNum,String spl, int res){
        splitAttr=attrNum;
        splittingVal=spl;
        result=res;
    }

    public void setTerminalNode(boolean b){isTerminalNode=b;}

    public boolean isTerminalNode() {
        return isTerminalNode;
    }

    public int getResult(){ return result; }

    public int getSplitAttr() {
        return splitAttr;
    }

    public String getSplittingVal() {
        return splittingVal;
    }
}
