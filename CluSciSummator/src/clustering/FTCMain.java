/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class FTCMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        ArrayList<String> listAttribute = new ArrayList<String>();
        ArrayList<ArrayList<Boolean>> transactions = new ArrayList<ArrayList<Boolean>>();

        listAttribute.add("Ayam");
        listAttribute.add("Bebek");
        listAttribute.add("Cendol");
        listAttribute.add("Dodol");
        listAttribute.add("Emping");
        listAttribute.add("Fanta");

        ArrayList<Boolean> transaction1 = new ArrayList<Boolean>();
        transaction1.add(Boolean.TRUE);
        transaction1.add(Boolean.TRUE);
        transaction1.add(Boolean.TRUE);
        transaction1.add(Boolean.TRUE);
        transaction1.add(Boolean.FALSE);
        transaction1.add(Boolean.FALSE);
        transactions.add(transaction1);

        ArrayList<Boolean> transaction2 = new ArrayList<Boolean>();
        transaction2.add(Boolean.TRUE);
        transaction2.add(Boolean.FALSE);
        transaction2.add(Boolean.FALSE);
        transaction2.add(Boolean.TRUE);
        transaction2.add(Boolean.FALSE);
        transaction2.add(Boolean.FALSE);
        transactions.add(transaction2);

        ArrayList<Boolean> transaction3 = new ArrayList<Boolean>();
        transaction3.add(Boolean.TRUE);
        transaction3.add(Boolean.FALSE);
        transaction3.add(Boolean.FALSE);
        transaction3.add(Boolean.FALSE);
        transaction3.add(Boolean.TRUE);
        transaction3.add(Boolean.FALSE);
        transactions.add(transaction3);

        ArrayList<Boolean> transaction4 = new ArrayList<Boolean>();
        transaction4.add(Boolean.TRUE);
        transaction4.add(Boolean.TRUE);
        transaction4.add(Boolean.FALSE);
        transaction4.add(Boolean.FALSE);
        transaction4.add(Boolean.FALSE);
        transaction4.add(Boolean.TRUE);
        transactions.add(transaction4);

        FTC frequentSearcher = new FTC(listAttribute, transactions, 0.5);
        ArrayList<ArrayList<String>> result = frequentSearcher.searchFrequentTermSet();

        for (int i = 0; i < result.size(); ++i) {
            ArrayList<String> frequentItemSet = result.get(i);
            System.out.println(""+frequentItemSet);
        }

        /* //Tes HashTable:
        Hashtable<ArrayList<String>, ArrayList<String>> ht = new Hashtable<ArrayList<String>, ArrayList<String>>();
        ArrayList<String> a1 = new ArrayList<String>();
        a1.add("akbar");
        a1.add("gumbira");

        ArrayList<String> a2 = new ArrayList<String>();
        a2.add("b");

        ht.put(result.get(0), a1);
        ht.put(result.get(1), a2);
        Enumeration keys = ht.keys();
        while (keys.hasMoreElements()) {
            ArrayList<String> tes = (ArrayList<String>) keys.nextElement();
            System.out.println(tes + ": "
                    + ht.get(tes));
        }
         */
        System.out.println("");
    }
}
