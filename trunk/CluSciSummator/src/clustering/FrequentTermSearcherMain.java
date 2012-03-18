/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import java.util.ArrayList;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class FrequentTermSearcherMain {

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
        
        FrequentTermSetSearcher frequentSearcher = new FrequentTermSetSearcher(listAttribute, transactions);
        ArrayList<ArrayList<String>> result = frequentSearcher.searchFrequentTermSet();
        
        for (int i = 0; i < result.size(); ++i) {
            ArrayList<String> frequentItemSet = result.get(i);
            System.out.print("[");
            for (int j = 0; j < frequentItemSet.size(); ++j) {
                System.out.print(frequentItemSet.get(j));
                if (j!=frequentItemSet.size()-1)
                    System.out.print(",");
            }
            System.out.println("]");
        }
        
    }
}
