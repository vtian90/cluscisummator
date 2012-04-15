/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clusterer;

import java.util.ArrayList;
import weka.associations.ItemSet;
import weka.core.FastVector;
import weka.core.Instances;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class Apriori extends weka.associations.Apriori {
    public Instances instances;

    @Override
    public void buildAssociations(Instances instances) throws Exception {
        super.buildAssociations(instances);
        this.instances = instances;
    }

    public ArrayList<ArrayList<String>> getLs() {
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < m_Ls.size(); i++) {
            for (int j = 0; j < ((FastVector) m_Ls.elementAt(i)).size(); j++) {
                ItemSet thisItemSet = ((ItemSet) ((FastVector) m_Ls.elementAt(i)).elementAt(j));
                int[] m_items = thisItemSet.items();
                ArrayList<String> oneFrequentItemSet = new ArrayList<String>();
                for (int k = 0; k < instances.numAttributes(); k++) {
                    if (m_items[k] != -1) {
                        oneFrequentItemSet.add(instances.attribute(k).name());
                    }
                }
                result.add(oneFrequentItemSet);
            }
        }
        return result;
    } 
}
