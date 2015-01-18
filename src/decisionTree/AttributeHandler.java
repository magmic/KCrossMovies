package decisionTree;

import java.util.ArrayList;
import java.util.Collections;

import java.util.HashMap;



import com.magda.movies.Evaluation;

public class AttributeHandler {
	private static ArrayList<Attribute> commonAttributes;
	public static ArrayList<Attribute> getCommonAttributes() {
		if (commonAttributes == null) {
			int featuresNumber = 18;
			commonAttributes = new ArrayList<Attribute>();
			commonAttributes
					.add(generateBooleanAtribute(0, "Only for adults?"));

			commonAttributes.add(generateContinuousValuedAtribute(2,
					"Is budget below ", false));
			commonAttributes.add(generateContinuousValuedAtribute(4,
					"Is popularity below ", false));
			commonAttributes.add(generateContinuousValuedAtribute(7,
					"Is release date below ", true));
			commonAttributes.add(generateContinuousValuedAtribute(8,
					"Is revenue below ", false));
			commonAttributes.add(generateContinuousValuedAtribute(9,
					"Is runtime below ", false));
			commonAttributes.add(generateContinuousValuedAtribute(11,
					"Is average vote below ", false));
			commonAttributes.add(generateContinuousValuedAtribute(12,
					"Is vote count below ", false));

		}
		return commonAttributes;

	}

	private static BooleanAttribute generateBooleanAtribute(int featureIndex,
			String name) {
		BooleanAttribute attribute = new BooleanAttribute(featureIndex, name);

		attribute.setPossibleOutcomes(new String[] { "true", "false" });
		return attribute;
	}

	private static CollectionAttribute generateCollectionAtribute(
			int featureIndex, String name, String value) {
		CollectionAttribute attribute = new CollectionAttribute(featureIndex,
				name, value);

		attribute.setPossibleOutcomes(new String[] { "true", "false" });
		return attribute;
	}

	private static ContinuousAttribute generateContinuousValuedAtribute(
			int featureIndex, String name, boolean isDate) {
		ContinuousAttribute attribute = new ContinuousAttribute(featureIndex,
				name, isDate);
		attribute.setPossibleOutcomes(new String[] { "true", "false" });
		return attribute;
	}

	public static ArrayList<Attribute> createUserAttribute(
			ArrayList<Evaluation> evaluations, String[][] features) {
		ArrayList<Attribute> userAttribute = new ArrayList<Attribute>(
				getCommonAttributes());
		ArrayList<String> genres = new ArrayList<String>();
		HashMap<String, Integer> languages = new HashMap<String, Integer>();
		HashMap<String, Integer> countries = new HashMap<String, Integer>();
		HashMap<String, Integer> cast = new HashMap<String, Integer>();
		HashMap<String, Integer> crew = new HashMap<String, Integer>();
		HashMap<String, Integer> production = new HashMap<String, Integer>();
		HashMap<String, Integer> keywords = new HashMap<String, Integer>();
		HashMap<String, Integer> lists = new HashMap<String, Integer>();
		
		//14/16
		
		for (Evaluation evaluation : evaluations) {
			addGenre(userAttribute, evaluation, features, genres);
			
			getListWithCounter(evaluation, features, languages, 10);
			getListWithCounter(evaluation, features, countries, 6);
			getListWithCounter(evaluation, features, production, 5);
			getListWithCounter(evaluation, features, cast, 13);
			getListWithCounter(evaluation, features, keywords, 14);
			getListWithCounter(evaluation, features, crew, 15);
			getListWithCounter(evaluation, features, lists, 16);

		}
		getMostCommon(1, languages, userAttribute, 10,evaluations.size());
		getMostCommon(1, countries, userAttribute, 6,evaluations.size());
		getMostCommon(2, cast, userAttribute, 13,evaluations.size());
		getMostCommon(2, crew, userAttribute, 15,evaluations.size());
		getMostCommon(2, production, userAttribute, 5,evaluations.size());
		getMostCommon(2,keywords, userAttribute, 14,evaluations.size());
		getMostCommon(2,lists, userAttribute, 16,evaluations.size());
	
		
		return userAttribute;
	}

	private static void addGenre(ArrayList<Attribute> userAttribute,
			Evaluation evaluation, String[][] features, ArrayList<String> genres) {
		String[] elements;
		
		elements = features[evaluation.getMovieId()][3].split(", ");

		for (String element : elements) {
			if (!genres.contains(element)) {
				genres.add(element);
				String name = "Is it " + element;
				userAttribute.add(generateCollectionAtribute(3, name, element));
			}
		}
	}

	

	private static void getListWithCounter(Evaluation evaluation, String[][] features,
			HashMap<String, Integer> map, int featureIndex) {
		String[] elements;
	
		elements = features[evaluation.getMovieId()][featureIndex].split(", ");

		for (String element : elements) {
			if (featureIndex == 15) {				
				if (!element.contains("Directing|Director|")) {
					break;
				}
			}
			int count = map.containsKey(element) ? map.get(element) : 0;
			count = count + 1;
			map.put(element, count);

		}

	}

	private static void getMostCommon(int results,HashMap<String, Integer> map, ArrayList<Attribute> userAttribute, int featureIndex, int dataSize) {
		ArrayList<String> cast = new ArrayList<String>();
		int maxValues[] = new int[results];
		//String maxStrings[] = { "", "",""};
		for (String key : map.keySet()) {
			Integer value = map.get(key);
			if(value!=dataSize){
			for (int i = 0; i < maxValues.length; i++) {
				if (maxValues[i] < value) {
					maxValues[i] = value;
					
					break;
				}
			}
			}
		}
		for (String key : map.keySet()) {
			Integer value = map.get(key);
			for (int i = 0; i < maxValues.length; i++) {
				if (maxValues[i] == value) {
					
						
					
					userAttribute.add(new CollectionAttribute(featureIndex, key,
							key));
					break;
				}
			}
		}
	

	}

}
