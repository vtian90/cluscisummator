/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.clustering;

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
    /**
     * FIELD DARI CLASS:
     * 1. _deltaValue : konfigurasi apriori WEKA
     * 2 _lowerBoundMinSupportValue : minimum support, konfigurasi apriori WEKA 
     * 3. _minMetricValue : konfigurasi apriori WEKA
     * 4. _numRulesValue : konfigurasi apriori WEKA
     * 5. _upperBoundMinSupportValue : konfigurasi apriori WEKA
     * 6. _listAttribute : Daftar attribute WEKA (daftar konsep dalam konteks ini)
     * 7. _transactions : Transaksi dari tiap konsep
     * 8. _documentsCover : cover dari semua dokumen yang ada pada term set hasil Apriori
     * 9. finalCluster : Hasil dari FTC: deskripsi cluster + dokumen cluster
     */
    private final double _deltaValue = 0.05;
    private double _lowerBoundMinSupportValue;
    private final double _minMetricValue = 0.9;
    private final int _numRulesValue = 5;
    private final double _upperBoundMinSupportValue = 1.0;
    private ArrayList<String> _listAttribute;
    private ArrayList<ArrayList<Boolean>> _transactions;
    private ArrayList<Integer> _documentsCover;
    public Hashtable<ArrayList<String>, ArrayList<Integer>> finalCluster; 

    /**
     * KONSTRUKTOR DARI CLASS
     * 
     * @param listAttribute
     * @param transactions
     * @param minimumSupport 
     */
    public FTC(ArrayList<String> listAttribute, ArrayList<ArrayList<Boolean>> transactions, double minimumSupport) {
        this._listAttribute = listAttribute;
        this._transactions = transactions;
        this._lowerBoundMinSupportValue = minimumSupport;
        this._documentsCover = new ArrayList<Integer>();
    }

    /**
     * METHOD DARI CLASS
     * 1. searchFrequentTermSet() : mengidentifikasi semua frequent term set dengan menggunakan algoritma Apriori WEKA
     * 2. clusterFrequentTermSet() : Setelah frequent term set teridentifikasi, dicari daftar makalah yang mengandung frequent term set tersebut
     * 3. filterCluster() : Melakukan filtrasi cluster dengan menghitung EO dari tiap cluster 
     * 4. EO() : menghitung EO dari satu cluster
     */
    
    /**
     * Method searchFrequentTermSet() : mengidentifikasi semua frequent term set dengan menggunakan algoritma Apriori WEKA
     * 
     * @return daftar frequent term set
     * @throws Exception 
     */
    public ArrayList<ArrayList<String>> searchFrequentTermSet() throws Exception {
        ArrayList<ArrayList<String>> result;

        //Bikin atribut sejumlah listAttribute
        FastVector fvAttributes = new FastVector(_listAttribute.size());

        //Bikin detail atribut (nama atribut dan list nominal)
        for (int i = 0; i < _listAttribute.size(); ++i) {
            FastVector fvAttribute = new FastVector(1);
            fvAttribute.addElement("P");
            Attribute attribute = new Attribute(_listAttribute.get(i), fvAttribute);

            fvAttributes.addElement(attribute);
        }

        //Bikin instances
        Instances instances = new Instances("dataset", fvAttributes, _transactions.size());

        //Bikin instance sejumlah transaction yang ada
        for (int i = 0; i < _transactions.size(); ++i) {
            Instance instance = new Instance(_listAttribute.size());
            ArrayList<Boolean> a_transaction = _transactions.get(i);
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
        apriori.setDelta(_deltaValue);
        apriori.setLowerBoundMinSupport(_lowerBoundMinSupportValue);
        apriori.setMinMetric(_minMetricValue);
        apriori.setNumRules(_numRulesValue);
        apriori.setOutputItemSets(true);
        apriori.setUpperBoundMinSupport(_upperBoundMinSupportValue);
        apriori.setVerbose(false);
        apriori.buildAssociations(instances);
        result = apriori.getLs();
        /*
         * PENGUJIAN KOMPONEN CLUSTERING 1
         */
        System.out.println("HASIL IDENTIFIKASI FTS");
        for (int i=0; i<result.size(); ++i) {
            System.out.println(""+result.get(i));
        }
        return result;
    }

    /**
     * Method clusterFrequentTermSet() : Setelah frequent term set teridentifikasi, dicari daftar makalah yang mengandung frequent term set tersebut
     * 
     * @return HashTable<ArrayList<String>, ArrayList<Integer>> (pasangan frequent term set dengan daftar ID dokumen)
     * @throws Exception 
     */
    public Hashtable<ArrayList<String>, ArrayList<Integer>> clusterFrequentTermSet() throws Exception {
        Hashtable<ArrayList<String>, ArrayList<Integer>> result = new Hashtable<ArrayList<String>, ArrayList<Integer>>();

        ArrayList<ArrayList<String>> frequentTermSets = searchFrequentTermSet();
        for (int i = 0; i < frequentTermSets.size(); ++i) {
            ArrayList<String> frequentTermSet = frequentTermSets.get(i);

            ArrayList<Integer> allIndexDocuments = new ArrayList<Integer>();

            for (int j = 0; j < _transactions.size(); ++j) {
                ArrayList<Boolean> transaction = _transactions.get(j);
                
                Boolean addThisDoc = true;
                for (int k = 0; k < frequentTermSet.size(); ++k) {
                    String partFrequent = frequentTermSet.get(k);
                    int indexPartFrequentInTransaction = _listAttribute.indexOf(partFrequent);
                    
                    if (!transaction.get(indexPartFrequentInTransaction)) {
                        addThisDoc = false;
                    } else {
                        if (!_documentsCover.contains(j))
                            _documentsCover.add(j);
                    }
                }
                if (addThisDoc)
                    allIndexDocuments.add(j);
            } 

            result.put(frequentTermSet, allIndexDocuments);
        }
        return result;
    }

    /**
     * Method filterCluster() : Melakukan filtrasi cluster dengan menghitung EO dari tiap cluster
     * 
     * @throws Exception 
     */
    public int filterCluster() throws Exception {
        int statusFilter = 0;
        Hashtable<ArrayList<String>, ArrayList<Integer>> candidateCluster = clusterFrequentTermSet();
        if (candidateCluster.size() == 0) {
            //Tidak ada frequent term set hasil Apriori
            statusFilter = 1;
        } else {
            Hashtable<ArrayList<String>, ArrayList<Integer>> tempCluster = new Hashtable<ArrayList<String>, ArrayList<Integer>>(candidateCluster);
            Hashtable<ArrayList<String>, ArrayList<Integer>> selectedCluster = new Hashtable<ArrayList<String>, ArrayList<Integer>>();
            ArrayList<Integer> coverSelectedTermSets = new ArrayList<Integer>();

            int numCoverageAll = this._documentsCover.size();

            //Bikin tabel jumlah cluster yang dikandung dari tiap makalah
            Hashtable<Integer, Integer> tabelFj = new Hashtable<Integer, Integer>();
            for (int i = 0; i < this._documentsCover.size(); ++i) {
                Integer numberOfClusterInThisDoc = 0;
                Integer thisDocIndex = this._documentsCover.get(i);
                Enumeration clusterDocuments = candidateCluster.elements();
                while (clusterDocuments.hasMoreElements()) {
                    ArrayList<Integer> documentsOnThisCluster = (ArrayList<Integer>) clusterDocuments.nextElement();
                    if (documentsOnThisCluster.contains(thisDocIndex)) {
                        numberOfClusterInThisDoc += 1;
                    }
                }
                tabelFj.put(thisDocIndex, numberOfClusterInThisDoc);
            }

            /**
             * PENGUJIAN 2 KOMPONEN CLUSTERING
             * 2. Nilai EO dari setiap frequent term set dan frequent term set yang terpilih pada setiap iterasi.
             */
            System.out.println("--------EO DARI TIAP CLUSTER------------");
            int iter = 1;
            while (coverSelectedTermSets.size() != numCoverageAll) {
                System.out.println("ITERASI ke-"+iter);
                //Coba print TabelFj:
                System.out.println("-----TABEL FJ------------");
                Enumeration clusterDescription = tabelFj.keys();
                while (clusterDescription.hasMoreElements()) {
                    Integer docIndex = (Integer) clusterDescription.nextElement();
                    System.out.println("jumlah cluster yang dikandung makalah yang-" + docIndex + ": "
                            + tabelFj.get(docIndex));
                }

                //Hitung EO dari tiap termset dari candidateCluster:
                //Ambil EO terkecil dengan |clusterKey| paling panjang
                Enumeration thisClusterKeys = candidateCluster.keys();

                float minimumEO = 999;
                ArrayList<String> clusterKeySelected = new ArrayList<String>();
                ArrayList<Integer> clusterDocumentsSelected = new ArrayList<Integer>();

                while (thisClusterKeys.hasMoreElements()) {
                    ArrayList<String> thisClusterKey = (ArrayList<String>) thisClusterKeys.nextElement();
                    ArrayList<Integer> thisClusterDocIndexes = candidateCluster.get(thisClusterKey);
                    float EOThisCluster = EO(tabelFj, thisClusterDocIndexes);
                    if (EOThisCluster <= minimumEO && clusterKeySelected.size() <= thisClusterKey.size()) {
                        minimumEO = EOThisCluster;
                        clusterKeySelected = thisClusterKey;
                        clusterDocumentsSelected = thisClusterDocIndexes;
                    }
                    System.out.println("" + thisClusterKey + " : " + thisClusterDocIndexes + " = " + EOThisCluster);
                }

                //Print Cluster terpilih;
                System.out.println("CLUSTER TERPILIH:" + clusterKeySelected + " : " + clusterDocumentsSelected + " = " + minimumEO);

                //Masukkin ke selectedCluster:
                //selectedCluster.put(clusterKeySelected, clusterDocumentsSelected);
                selectedCluster.put(clusterKeySelected, tempCluster.get(clusterKeySelected));

                //Tambahin clusterDocumentSelected ke coverSelectedTermSet
                //Update tabelFJ juga sesuai coverSelectedTermSet
                for (int i = 0; i < clusterDocumentsSelected.size(); ++i) {
                    if (!coverSelectedTermSets.contains(clusterDocumentsSelected.get(i))) {
                        coverSelectedTermSets.add(clusterDocumentsSelected.get(i));
                    }

                    Integer numDocumentNew = tabelFj.get(clusterDocumentsSelected.get(i)) - 1;
                    tabelFj.put(clusterDocumentsSelected.get(i), numDocumentNew);
                }


                //Kurangin selectedCluster dari candidateCLuster:
                candidateCluster.remove(clusterKeySelected);

                //Buang semua dokumen di cov(bestCandidate) dari coverage semua cluster sisa
                Enumeration candidateClusterKeys = candidateCluster.keys();
                while (candidateClusterKeys.hasMoreElements()) {
                    ArrayList<String> thisclusterDescription = (ArrayList<String>) candidateClusterKeys.nextElement();
                    ArrayList<Integer> clusterDocuments = candidateCluster.get(thisclusterDescription);
                    ArrayList<Integer> newClusterDocuments = new ArrayList<Integer>();
                    for (int i = 0; i < clusterDocuments.size(); ++i) {
                        if (!coverSelectedTermSets.contains(clusterDocuments.get(i))) {
                            newClusterDocuments.add(clusterDocuments.get(i));
                        }
                    }
                    //Buang semua cluster yang dokumen covernya udah gak ada
                    if (newClusterDocuments.size() == 0) { //artinya udah tercover sama yang kepilih {
                        candidateCluster.remove(thisclusterDescription);
                    } else {
                        candidateCluster.put(thisclusterDescription, newClusterDocuments);
                    }
                }
                ++iter;
            }

            this.finalCluster =  selectedCluster;
        }
        
        return statusFilter;
    }

    /**
     * Method EO() : menghitung EO dari satu cluster
     * 
     * @param tabelFj : tabel daftar dokumen berserta jumlah cluster untuk dokumen tersebut
     * @param docs : daftar ID makalah untuk cluster ini
     * @return 
     */
    private float EO(Hashtable<Integer, Integer> tabelFj, ArrayList<Integer> docs) {
        float result = 0;
        for (int i = 0; i < docs.size(); ++i) {
            Integer thisDoc = docs.get(i);
            Integer fjThisDoc = tabelFj.get(thisDoc);
            float temp = (1 / (float) fjThisDoc);
            float EOThisDoc = (float) ((-temp) * Math.log(temp));
            result += EOThisDoc;
        }

        return result;
    }
}
