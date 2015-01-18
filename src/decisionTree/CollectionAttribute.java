package decisionTree;

public class CollectionAttribute extends Attribute{
private String value;
public CollectionAttribute(int featureIndex, String name, String value) {
	super();
	this.featureIndex = featureIndex;
	
	this.name = name;
	this.value = value;
}
public String getValue() {
	return value;
}

public void setValue(String value) {
	this.value = value;
}
	
}
