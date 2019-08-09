import java.io.*;
import java.util.*;
public class Driver {
    public static void main(String [] args) throws IOException {
        String[][] textData = new String[7][4];
        ArrayList<DataPoint> dataPoints = new ArrayList<>();

        //loads data from data.txt file
        Scanner data = new Scanner(new File("data.txt")).useDelimiter(",|\\n");
        List<String> temps = new ArrayList<String>();
        String token1 = "";
        while (data.hasNext()) {
            token1 = data.next();
            temps.add(token1);
        }
        data.close();

        int in1=0;

        //loads data into 2D array and removes \r on the last character
        for(int i=4;i<=temps.size()-4;i+=4){
            textData[in1][0]=temps.get(i);
            textData[in1][1]=temps.get(i+1);
            textData[in1][2]=temps.get(i+2);
            textData[in1][3]= String.valueOf(temps.get(i + 3).charAt(0));
            in1=in1+1;
        }
        //starts id3 algorithm
        id3(textData);
    }

    public static void id3(String [][] textData){
        //Splits data into training cases and a single test case...only used one test case because the dataset was very small
        ArrayList<String []> trainingData = new ArrayList<>();
        String [] testCase = new String[textData[0].length];
        Random rand = new Random();
        int ind = rand.nextInt(textData.length);
        int inc=0;
        for(int i=0;i<textData.length;i++){
            if(i==ind){
                for(int j=0;j<textData[0].length;j++){
                    testCase[j]=textData[i][j];
                }
            }
            else{
                trainingData.add(textData[i]);
                inc+=1;
            }
        }
        String [][] td = new String[trainingData.size()][];
        for(int i=0;i<trainingData.size();i++){
            td[i]=trainingData.get(i);
        }
        //prints test case
        System.out.println("---------------------");
        System.out.print("TEST CASE: ");
        for(String s:testCase)
            System.out.print(s+" ");
        System.out.println("\n---------------------\n");
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Integer> indexes = new ArrayList<>();
        for(int i=0;i<textData[0].length-1;i++) {
            indexes.add(i);
        }
        display(td);
        //create decision tree object
        DecisionTree d = new DecisionTree(td,indexes);
        String [][] table = td;
        Node n = null;
        //loop that controls how long the algorithm runs
        while(indexes.size()!=0 && table.length!=0){
            //first computes entropy of the valid attributes and then calculates what attribute to split on
            int best = d.findSplit();
            //one the attribute to split on is found, it finds the rule for the split.
            n=d.findRule(best);
            nodes.add(n);
            //create subtable to use for next round of algorithm
            table = makeSubTable(d,n,td);
            display(table);
            //remove the index that has already been used so entropy is only computed for unused attributes
            indexes.remove(indexes.indexOf(best));
            d.setData(table);
            //System.out.println(n.getSplitAttr()+"\n"+n.getSplittingVal()+"\n"+n.getResult());
        }
        n.setTerminalNode(true);
        System.out.println("\n\nCLASSIFYING>>>>>>>>>>>>>>>");
        String result = d.classify(nodes,testCase);
        System.out.println("CLASSIFIED AS: "+result);
        System.out.println("ACTUAL WAS: "+testCase[3]);
        System.out.println("DONE!");
    }

    public static String[][] makeSubTable(DecisionTree d, Node n, String [][] t){
        /////this will make the subtable to be used to find the next split and rule
        String [][] subTab;
        ArrayList<String [][]>subTables = d.getSubTables(n.getSplitAttr());
        int tabSize=0;
        for(int i=0;i<subTables.size();i++){
            int size = subTables.get(i).length;
            if(subTables.get(i)[0][n.getSplitAttr()]==n.getSplittingVal())
                tabSize+=0;
            else
                tabSize+=size;
        }
        subTab=new String[tabSize][t[0].length];
        int ex=0;
        int it=0;
        while(ex<tabSize){
            for(int i=0;i<subTables.size();i++){
                for(int j=0;j<subTables.get(i).length;j++){
                    if(!(subTables.get(i)[j][n.getSplitAttr()].equals(n.getSplittingVal()))){
                        for(int k=0;k<subTables.get(i)[0].length;k++){
                            subTab[it][k]=subTables.get(i)[j][k];
                        }
                        it+=1;
                        ex+=1;
                    }
                }
            }
        }
        return subTab;
    }



    public static void display(String [][] d){
        System.out.println("\n\nDISPLAYING ARRAY");
        System.out.println("_____________________________");
        System.out.println("0|1|2|3\n");

        for(int i=0;i<d.length;i++){
            for(int j=0;j<d[0].length;j++){
                System.out.print(d[i][j]+",");
            }
            System.out.println("");
        }
        System.out.println("_____________________________");
    }


}
