import java.math.*;

import java.util.ArrayList;

public class DecisionTree {

    private String [][] data;
    private ArrayList<Integer> indexes;


    public DecisionTree(String [][] d, ArrayList<Integer> i){
        data=d;
        indexes=i;
    }

    public void setData(String [][] d){
        data=d;
    }

    //returns array that represents a column of data

    public String[] getDataCols(int j){
        int rows=data.length;
        String [] column = new String[rows];
        for(int i=0; i<rows;i++){
            column[i]=data[i][j];
        }
        return column;
    }

    //method to find what attribute is best to split on from remaining attributes
    public int findSplit(){
        int bestAttr=10;
        double bestEntropy=999999;
        double entropy;
        for(int i=0;i<indexes.size();i++){
            entropy = calculateEntropy(i);
            if(entropy==bestEntropy)
                continue;
            if(entropy<bestEntropy)
                bestAttr=i;
        }
        System.out.println("The best attribute to split on is attribute #"+bestAttr);
        return bestAttr;
    }

    //method that finds rule and will return a node that can be used for classification

    public Node findRule(int attr){
        ArrayList<String [][]> tables = getSubTables(attr);
        for(int i=0;i<tables.size();i++){
            boolean isPos = true;
            boolean isNeg = true;
            for(int j=0;j<tables.get(i).length;j++){
                for(int k = 0;k<tables.get(i).length;k++){
                    if(tables.get(i)[k][data[0].length-1].equals("+"))
                        isNeg=false;
                    if(tables.get(i)[k][data[0].length-1].equals("-"))
                        isPos=false;
                }
            }
            if(isNeg){
                System.out.println("FOUND A RULE: IF COLUMN "+ attr + " IS "+tables.get(i)[0][attr]+" THEN THE RESULT IS ALWAYS -");
                return new Node(attr,tables.get(i)[0][attr],0);
            }
            if(isPos){
                System.out.println("FOUND A RULE: IF COLUMN "+ attr + " IS "+tables.get(i)[0][attr]+" THEN THE RESULT IS ALWAYS +");
                return new Node(attr,tables.get(i)[0][attr],1);
            }

        }

        //if you can't find a final rule, then it randomly assigns one.
        double num=Math.random();
        if(data.length!=0){
            if(num < 0.5) {
                System.out.println("Generated a random rule for attr "+ indexes.get(0)+" result is -");
                return new Node(indexes.get(0),data[0][indexes.get(0)],0);
            } else {
                System.out.println("Generated a random rule for attr "+ indexes.get(0)+" result is +");
                return new Node(indexes.get(0),data[0][indexes.get(0)],1);
            }
        } else {
            return null;
        }
    }

    public ArrayList<String[][]> getSubTables(int attr){
        String [] column = getDataCols(attr);
        ArrayList<String[][]> table = new ArrayList<>();
        ArrayList<String> distinctVals = getDistinctValues(column);
        ArrayList<Integer> count= new ArrayList<>();
        for(int i=0;i<distinctVals.size();i++){
            count.add(0);
            for(int j=0;j<column.length;j++){
                if(column[j].equals(distinctVals.get(i))){
                    count.set(i,count.get(i)+1);
                }
            }
            //System.out.println("COUNT: "+count.get(i));
        }

        ArrayList<Integer> indexes = new ArrayList<>();
        for(int i=0;i<distinctVals.size();i++){
            String [][] subTable;
            indexes.clear();
            for(int j=0;j<column.length;j++){
                if(column[j].equals(distinctVals.get(i))){
                    indexes.add(j);
                }
            }
            subTable=new String[indexes.size()][data[0].length];
            for(int k=0;k<indexes.size();k++){
                for(int l=0;l<data[0].length;l++){
                    subTable[k][l] = data[indexes.get(k)][l];
                }
            }
            table.add(subTable);
        }
        return table;
    }


    //used to calculate log for entropy
    public double calcLogBase2(double d) {
        return Math.log(d)/Math.log(2.0);
    }


    //returns the distinct values of an array set....if a column has values 1,1,2,3,2,1 then it will return 1,2,3
    public ArrayList<String> getDistinctValues(String [] column){
        ArrayList<String> distinctVals = new ArrayList<>();
        for(String s: column){
            boolean inArray = false;
            for(int i=0;i<distinctVals.size();i++){
                if(s.equals(distinctVals.get(i))) {
                    inArray = true;
                    break;
                }
            }
            if(!inArray)
                distinctVals.add(s);
        }
        return distinctVals;
    }

    //function that calculates entropy given the attribute
    public double calculateEntropy(int attr){
        String [] column = getDataCols(attr);
        String [] answers;
        if(data.length==0)
            return 100;
        else
            answers = getDataCols(data[0].length-1);
        ArrayList<String> distinctVals = getDistinctValues(column);
        ArrayList<Integer> count = new ArrayList<>();
        ArrayList<Integer> positiveCases = new ArrayList<>();
        for(int i=0;i<distinctVals.size();i++){
            count.add(0);
            positiveCases.add(0);
            for(int j=0;j<column.length;j++){
                if(column[j].equals(distinctVals.get(i))){
                    count.set(i,count.get(i)+1);
                    if(answers[j].equals("+"))
                        positiveCases.set(i,positiveCases.get(i)+1);
                }
            }
            //System.out.println("Count of "+distinctVals.get(i)+"'s " + count.get(i));
            //System.out.println("Count of "+distinctVals.get(i)+"'s positive Cases:" + positiveCases.get(i));
            //System.out.println("Negative Cases: " + (count.get(i)-positiveCases.get(i))+"\n\n");
        }

        double entropy =0;
        double colSize = column.length;
        for(int i=0; i<distinctVals.size();i++){
            int negCases = count.get(i)-positiveCases.get(i);
            double posCaseFrac = ((double)positiveCases.get(i)/(double)count.get(i));
            double negCaseFrac = ((double)negCases/(double)count.get(i));
            double weight = (count.get(i)/colSize);
            double posPreWeight=((-1.0)*posCaseFrac*calcLogBase2(posCaseFrac));
            double negPreWeight=((-1.0)*negCaseFrac*calcLogBase2(negCaseFrac));
            if(negCases==0 || posCaseFrac==0) {
                entropy+=0;
            } else {
                entropy+=((weight*(posPreWeight+negPreWeight)));
            }
        }
        System.out.print("Entropy For Attr : "+attr+" is "+entropy+"\n");
        return entropy;
    }

    //method that goes thru the list of array nodes and classifies test case

    public String classify(ArrayList<Node> nodes ,String[] testCase){
        int targetAttr=0;
        for(Node n:nodes){
            if(!n.isTerminalNode()){
                targetAttr=n.getSplitAttr();
                if(n.getSplittingVal().equals(testCase[targetAttr])){
                    if (n.getResult() == 0)
                        return "-";
                    else
                        return "+";
                } else {
                    continue;
                }
            } else {
                targetAttr=n.getSplitAttr();
                if(n.getSplittingVal().equals(testCase[targetAttr])){
                    if (n.getResult() == 0)
                        return "-";
                    else
                        return "+";
                } else {
                    if (n.getResult() == 0)
                        return "+";
                    else
                        return "-";
                }

            }
        }
        return "SOMEHOW I GOT HERE";
    }
}
