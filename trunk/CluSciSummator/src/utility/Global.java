/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class Global {

    public static enum listTag {
        TAG_PAPER("paper"),
        TAG_TITLE("title"),
        TAG_AUTHOR("author"),
        TAG_AIM("aim"),
        TAG_NOV_ADV("nov_adv"),
        TAG_CO_GRO("co_gro"),
        TAG_OTHR("othr"),
        TAG_PREV_OWN("prev_own"),
        TAG_OWN_MTHD("own_mthd"),
        TAG_OWN_FAIL("own_fail"),
        TAG_OWN_RES("own_res"),
        TAG_OWN_CONC("own_conc"),
        TAG_CODI("codi"),
        TAG_GAP_WEAK("gap_weak"),
        TAG_ANTISUPP("antisupp"),
        TAG_SUPPORT("support"),
        TAG_USE("use"),
        TAG_FUT("fut");
        private String _tagName;

        private listTag(String input) {
            this._tagName = input;
        }

        @Override
        public String toString() {
            return _tagName;
        }
    }
    
    public final static List<String> rhetoricalStatusList = Arrays.asList(
            "aim"//,"nov_adv"//, "co_gro", "othr", "prev_own",
//            "own_mthd", "own_fail", "own_res", "own_conc", "codi", "gap_weak",
//            "antisupp", "support", "use", "fut"
            );
}
