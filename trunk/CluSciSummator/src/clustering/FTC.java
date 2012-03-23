/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class FTC {

    private final double deltaValue = 0.05;
    private double lowerBoundMinSupportValue;
    private final double minMetricValue = 0.9;
    private final int numRulesValue = 5;
    private final double upperBoundMinSupportValue = 1.0;
    ArrayList<String> listAttribute;
    ArrayList<ArrayList<Boolean>> transactions;
    ArrayList<Integer> documentsCover; //cover dari semua dokumen yang ada pada term set hasil Apriori

    public FTC(ArrayList<String> listAttribute, ArrayList<ArrayList<Boolean>> transactions, double minimumSupport) {
        this.listAttribute = listAttribute;
        this.transactions = transactions;
        this.lowerBoundMinSupportValue = minimumSupport;
        this.documentsCover = new ArrayList<Integer>();
    }

    public ArrayList<ArrayList<String>> searchFrequentTermSet() throws Exception {
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
        for (int i = 0; i < transactions.size(); ++i) {
            Instance instance = new Instance(listAttribute.size());
            ArrayList<Boolean> a_transaction = transactions.get(i);
            for (int j = 0; j < a_transaction.size(); ++j) {
                if (a_transaction.get(j)) {
                    instance.setValue((Attribute) fvAttributes.elementAt(j), "P");
                }
            }
            instances.add(instance);
        }

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

        result = apriori.getLs();

        return result;
    }

    public Hashtable<ArrayList<String>, ArrayList<Integer>> clusterFrequentTermSet() throws Exception {
        Hashtable<ArrayList<String>, ArrayList<Integer>> result = new Hashtable<ArrayList<String>, ArrayList<Integer>>();

        ArrayList<ArrayList<String>> frequentTermSets = searchFrequentTermSet();
        for (int i = 0; i < frequentTermSets.size(); ++i) {
            ArrayList<String> frequentTermSet = frequentTermSets.get(i);

            ArrayList<Integer> allIndexDocuments = new ArrayList<Integer>();

            for (int j = 0; j < frequentTermSet.size(); ++j) {
                String partFrequent = frequentTermSet.get(j);
                int indexPartFrequentInConcepts = listAttribute.indexOf(partFrequent);

                for (int k = 0; k < transactions.size(); ++k) {
                    ArrayList<Boolean> transaction = transactions.get(k);
                    if (transaction.get(indexPartFrequentInConcepts) && !allIndexDocuments.contains((Integer) k)) {
                        allIndexDocuments.add(k);
                        if (!documentsCover.contains(k)) {
                            documentsCover.add(k);
                        }
                    }
                }
            }

            result.put(frequentTermSet, allIndexDocuments);
        }

        return result;
    }

    public Hashtable<ArrayList<String>, ArrayList<Integer>> filterCluster() throws Exception {
        Hashtable<ArrayList<String>, ArrayList<Integer>> candidateCluster = clusterFrequentTermSet();
        Hashtable<ArrayList<String>, ArrayList<Integer>> selectedCluster = new Hashtable<ArrayList<String>, ArrayList<Integer>>();
        ArrayList<Integer> coverSelectedTermSets = new ArrayList<Integer>();

        int numCoverageAll = this.documentsCover.size();

        //Bikin tabel jumlah cluster yang dikandung dari tiap makalah
        Hashtable<Integer, Integer> tabelFj = new Hashtable<Integer, Integer>();
        for (int i = 0; i < this.documentsCover.size(); ++i) {
            Integer numberOfClusterInThisDoc = 0;
            Integer thisDocIndex = this.documentsCover.get(i);
            Enumeration clusterDocuments = candidateCluster.elements();
            while (clusterDocuments.hasMoreElements()) {
                ArrayList<Integer> documentsOnThisCluster = (ArrayList<Integer>) clusterDocuments.nextElement();
                if (documentsOnThisCluster.contains(thisDocIndex)) {
                    numberOfClusterInThisDoc += 1;
                }
            }
            tabelFj.put(thisDocIndex, numberOfClusterInThisDoc);
        }

        //Coba print TabelFj:
        System.out.println("-----TABEL FJ------------");
        Enumeration clusterDescription = tabelFj.keys();
        while (clusterDescription.hasMoreElements()) {
            Integer docIndex = (Integer) clusterDescription.nextElement();
            System.out.println("jumlah cluster yang dikandung makalah yang-" + docIndex + ": "
                    + tabelFj.get(docIndex));
        }


        while (coverSelectedTermSets.size() != numCoverageAll) {
            //Hitung EO dari tiap termset dari candidateCluster:
            Enumeration thisClusterKeys = candidateCluster.keys();
            while (thisClusterKeys.hasMoreElements()) {
                ArrayList<String> thisClusterKey = (ArrayList<String>) thisClusterKeys.nextElement();
                System.out.println("Cluster Description: "+thisClusterKey);
                ArrayList<Integer> thisClusterDocIndexes = candidateCluster.get(thisClusterKey);
                System.out.println("Indeks dokumennya "+thisClusterDocIndexes);
                float EOThisCluster = EO(tabelFj, thisClusterDocIndexes);
                System.out.println("EO dari cluster "+thisClusterKey+" = "+EOThisCluster);
            }
            break;
        }

        return selectedCluster;
    }

    //Ngitung EO dari satu cluster (teknisnya sih dari satu list dokumen)
    private float EO(Hashtable<Integer, Integer> tabelFj, ArrayList<Integer> docs) {
        float result = 0;
        for (int i = 0; i < docs.size(); ++i) {
            Integer thisDoc = docs.get(i);
            Integer fjThisDoc = tabelFj.get(thisDoc);
            float temp = (1/(float)fjThisDoc);
            float EOThisDoc = (float) ((-temp)*Math.log(temp));
            result += EOThisDoc;
        }

        return result;
    }
}
