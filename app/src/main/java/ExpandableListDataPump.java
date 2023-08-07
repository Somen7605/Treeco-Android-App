import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {

    public static HashMap<String , List<String>> getData() {
        HashMap<String ,List<String>> expandableListDetail =new HashMap<>();

        List<String> classification=new ArrayList<>();
        classification.add("Plant Category");
        classification.add("Plant Order");
        classification.add("Plant Genus");
        classification.add("Plant Sub Species");
        classification.add("Plant Kingdom");
        classification.add("Plant Family");
        classification.add("Plant Species");
        classification.add("Binomial Name");
        classification.add("Common Name");
        classification.add("Synonyms");

        List<String> attributes=new ArrayList<>();
        attributes.add("Leaf Colour");
        attributes.add("Flower Colour");
        attributes.add("Average Height");
        attributes.add("Growth Rate");
        attributes.add("Bloom Time");
        attributes.add("Watering Duration");
        attributes.add("Survival Rate");
        attributes.add("Oxygen Release Rate");
        attributes.add("Medicinal Value");
        attributes.add("Diseases");
        attributes.add("Toxic or Not");

        List<String> needs=new ArrayList<>();
        needs.add("Watering Duration");
        needs.add("Demographic Area");
        needs.add("Weather");
        needs.add("Soil Type");
        needs.add("Fertilizers");

        expandableListDetail.put("Plant Classification",classification);
        expandableListDetail.put("Physical & Biological Attributes",attributes);
        expandableListDetail.put("Needs For Survival",needs);

        return expandableListDetail;


    }
}
