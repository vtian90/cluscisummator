/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import java.util.ArrayList;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class FrequentTermSetSearcher {
    private final double deltaValue = 0.05;
    private final double lowerBoundMinSupportValue = 0.5;
    private final double minMetricValue = 0.9;
    private final int numRulesValue = 1;
    private final double upperBoundMinSupportValue = 1.0;
    
    ArrayList<String> listAttribute;
    ArrayList<ArrayList<Boolean>> transactions;
    
    public FrequentTermSetSearcher(ArrayList<String> listAttribute, ArrayList<ArrayList<Boolean>> transactions){
        this.listAttribute = listAttribute;
        this.transactions = transactions;
    }
    
    public ArrayList<ArrayList<String>> searchFrequentTermSet() throws Exception{
        ArrayList<ArrayList<String>> result;
        
        //Bikin atribut sejumlah listAttribute
        FastVector fvAttributes = new FastVector(listAttribute.size());

        //Bikin detail atribut (nama atribut dan list nominal)
        for (int i = 0; i < listAttribute.size(); ++i) {
            FastVector fvAttribute = new FastVector(1);
            fvAttribute.addElement("P");
            Attribute attribute = new Attribute(listAttribute.get(i), fvAttribute);

            fvAttributes.addElement(attribute);
        }

        //Bikin instances
        Instances instances = new Instances("dataset", fvAttributes, transactions.size());

        //Bikin instance sejumlah transaction yang ada
        for (int i=0; i<transactions.size(); ++i) {
            Instance instance = new Instance(listAttribute.size());
            ArrayList<Boolean> a_transaction = transactions.get(i);
            for (int j=0; j<a_transaction.size(); ++j) {
                if (a_transaction.get(j))
                    instance.setValue((Attribute) fvAttributes.elementAt(j), "P");
            }
            instances.add(instance);
        }
        
        System.out.println("HALOOO");
        Apriori apriori = new Apriori();
        apriori.setCar(false);
        apriori.setClassIndex(-1);
        apriori.setDelta(deltaValue);
        apriori.setLowerBoundMinSupport(lowerBoundMinSupportValue);
        apriori.setMinMetric(minMetricValue);
        apriori.setNumRules(numRulesValue);
        apriori.setOutputItemSets(true);
        apriori.setUpperBoundMinSupport(upperBoundMinSupportValue);
        apriori.setVerbose(false);
        apriori.buildAssociations(instances);

        System.out.println("HAIIIIII");
        result = apriori.getLs();
        
        return result;
    }
}
