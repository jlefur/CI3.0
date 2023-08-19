package business;

public class C_DescriptorSimpleWithOptionalField extends C_DescriptorSimple {

	/** La description optional du descripteur */
	protected String optionalDescription = null;

	public C_DescriptorSimpleWithOptionalField(String type, String contentsDescriptor,
			String optionalDescription) {
		super(type, contentsDescriptor);
		this.descriptorName = contentsDescriptor;
		this.optionalDescription = optionalDescription;
	}
	
	

	public String getOptionalDescription() {
		return optionalDescription;
	}
	


	public void setOptionalDescription(String optionalDescription) {
		this.optionalDescription = optionalDescription;
	}

	@Override
	public String toString() {
		return "C_DescriptorSimpleWithOptionalField type is: " + this.descriptorType + "   contents is:   "
				+ this.descriptorName + "   optional description is:      " + optionalDescription;
	}

}
