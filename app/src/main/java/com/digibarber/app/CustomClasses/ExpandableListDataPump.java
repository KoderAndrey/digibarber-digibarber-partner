package com.digibarber.app.CustomClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DIGIBARBER LTD on 20/9/17.
 */

public class ExpandableListDataPump {

    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> pamPer = new ArrayList<String>();
        pamPer.add("India");
        pamPer.add("Pakistan");


        List<String> beardServices = new ArrayList<String>();
        beardServices.add("Brazil");
        beardServices.add("Spain");
        beardServices.add("Germany");
        beardServices.add("Netherlands");


        List<String> hairCut = new ArrayList<String>();
        hairCut.add("United States");

        List<String> miscellaneous = new ArrayList<String>();
        miscellaneous.add("United States");
        miscellaneous.add("Spain");
        miscellaneous.add("Argentina");

        expandableListDetail.put("Pamper Treatment", pamPer);
        expandableListDetail.put("Beard Services", beardServices);
        expandableListDetail.put("Hair Cut", hairCut);
        expandableListDetail.put("Miscellaneous", miscellaneous);

        return expandableListDetail;
    }

}
