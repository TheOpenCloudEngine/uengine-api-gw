package org.uengine.kernel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.xml.namespace.QName;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.Order;
import org.metaworks.annotation.Range;
import org.metaworks.annotation.Validator;
import org.metaworks.annotation.ValidatorContext;
import org.metaworks.annotation.ValidatorSet;
import org.uengine.contexts.DatabaseSynchronizationOption;
import org.uengine.contexts.TextContext;

/**
 * @author Jinyoung Jang
 */

public class ProcessVariable implements java.io.Serializable, NeedArrangementToSerialize, Cloneable, ContextAware{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	transient MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
	String name;
	@Id
	@Order(1)
	@ValidatorSet({
		@Validator(name=ValidatorContext.VALIDATE_NOTNULL, message="변수이름을 입력하세요."),
	})
		public String getName() {
			return name;
		}
		public void setName(String value) {
			name = value;
		}

	TextContext displayName = TextContext.createInstance();
	@Order(2)
		public TextContext getDisplayName(){
			if(displayName.getText()==null){
				TextContext result = TextContext.createInstance();
				result.setText(getName());
				return result;
			}
			
			return displayName;
		}	
		public void setDisplayName(TextContext value){
			displayName = value;
		}
		public void setDisplayName(String value) {
			if(getName()==null){
				TextContext textCtx = new TextContext();
				textCtx.setText(value);
				setDisplayName(textCtx);		
			}
			
			getDisplayName().setText(value);
		}
	
	Class type; 
		@Hidden
		public Class getType(){
			if(type==null){
				if(getXmlBindingClassName()!=null){
					try{
						type = GlobalContext.getComponentClass(getXmlBindingClassName());
					}catch(Exception e){
						System.out.println("Warning: Binding class not found at design time.");
					}
				}
				//The other case need to be coded: dynamic compilation of XML binding classes based on the QName
			}
			
			return type;
		}
		public void setType(Class type){
			this.type = type;
		}
		
	transient String typeInputter;
		@Order(3)
		@Range(options={"Text","Date","Complex"}, values={"java.lang.String","java.util.Date","org.uengine.contexts.ComplexType"})
		public String getTypeInputter() {
			return typeInputter;
		}
		public void setTypeInputter(String typeInputter) {
			this.typeInputter = typeInputter;
		}
	
		
	Role openRole;
	@Hidden
		public Role getOpenRole() {
			return openRole;
		}
		public void setOpenRole(Role openRole) {
			this.openRole = openRole;
		}
	
	String xmlBindingClsName;
	@Hidden
		public String getXmlBindingClassName(){
			if(getQName()!=null && xmlBindingClsName==null)
				xmlBindingClsName = org.uengine.util.UEngineUtil.QName2ClassName(getQName());
								
			return xmlBindingClsName;
		}
		public void setXmlBindingClassName(String value){
			xmlBindingClsName = value;
		}

	QName qname;
	@Hidden
		public QName getQName(){
			return qname;
		}
		public void setQName(QName value){
			qname = value;
		}

/*	Serializable defaultValue;
		public Serializable getDefaultValue() {
			return defaultValue;
		}
		public void setDefaultValue(Serializable object) {
			defaultValue = object;
		}*/

/*	InputterAdapter inputter;
		public InputterAdapter getInputter() {
			return inputter;
		}
		public void setInputter(InputterAdapter inputter) {
			this.inputter = inputter;
		}*/

	
/*	Validator[] validators;
		public Validator[] getValidators() {
			return validators;
		}
		public void setValidators(Validator[] validators) {
			this.validators = validators;
		}*/	
	
/*	String bindingClassName;
	
		public String getBindingClassName(){
			if(getQName()!=null && bindingClassName==null)
				bindingClassName = getQName().getLocalPart();
				
			return bindingClassName;
		}
			
		public void setBindingClassName(String value){
			bindingClassName = value;
		}
*/
	
	boolean askWhenInit = false;
	@Hidden
		public boolean isAskWhenInit() {
			return askWhenInit;
		}
		public void setAskWhenInit(boolean b) {
			askWhenInit = b;
		}

	Object defaultValue = null;
	@Order(4)
		public Object getDefaultValue() {
			return defaultValue;
		}
		public void setDefaultValue(Object object) {
			defaultValue = object;
		}
		
	boolean isVolatile;
	@Hidden
		public boolean isVolatile() {
			return isVolatile;
		}
		public void setVolatile(boolean isVolatile) {
			this.isVolatile = isVolatile;
		}
		
		
	public boolean equals(Object obj){
		if(obj !=null && getName()!=null)
			return getName().equals(((ProcessVariable)obj).getName());
			
		return false;
	}
	
	public ProcessVariable(Object[] settings){
		org.uengine.util.UEngineUtil.initializeProperties(this, settings);
	}
	public ProcessVariable(){
		this.setMetaworksContext(new MetaworksContext());
		this.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
		this.setDefaultValue(new String());
	}

	//review: The return object of this method is only for scripting users to indicate certain process variable
	public static ProcessVariable forName(String varName){	
		//review: fly-weight pattern needed
		ProcessVariable pv = new ProcessVariable();
		pv.setName(varName);
		
		return pv;
	}

	public void set(ProcessInstance instance, String scope, String key, Serializable value) throws Exception{
		if(isDatabaseSynchronized()){
			if(getDatabaseSynchronizationOption().set(instance, scope, value));
				return;
		}
		
		if(getType()!=null && (value==null || !value.getClass().isAssignableFrom(getType()))){		
			if(value instanceof String){
				String strValue = (String)value;
				if(getType()==Integer.class){
					try{
System.out.println("ProcessVariable:: converting from String to Integer");
						value = new Integer(Integer.parseInt(strValue));
					}catch(Exception e){
					}
				}//review: there are more cases can be converted from string.
			}
		}
				
		instance.set(scope, getName(), value);
	}
	
	public Serializable get(ProcessInstance instance, String scope, String key) throws Exception{		
		if(isDatabaseSynchronized()){
			Serializable value = getDatabaseSynchronizationOption().get(instance, scope);
			
			if(value instanceof ProcessVariableValue){
				return ((ProcessVariableValue)value).getValue();
			}else{
				return value;
			}
		}
		
		if(instance==null ) return (Serializable)getDefaultValue();
		
		Serializable theValue = instance.get(scope, getName());

		return theValue;
		
	}
	
	public ProcessVariableValue getMultiple(ProcessInstance instance, String scope, String key) throws Exception{		
		if(isDatabaseSynchronized()){
			
			Serializable value = getDatabaseSynchronizationOption().get(instance, scope);
			ProcessVariableValue pvv;
			
			if(value instanceof ProcessVariableValue){
				pvv = (ProcessVariableValue)value;
			}else{
				pvv = new ProcessVariableValue();
				pvv.setValue(value);
			}
			
			pvv.setName(getName());
			return pvv;
		}

		return instance.getMultiple(scope, getName());
	}
	
	public ProcessVariableValue getMultiple(ProcessInstance instance, String scope) throws Exception{
		return getMultiple(instance, scope, null);
	}

	public Serializable get(ProcessInstance instance, String scope) throws Exception{
		return get(instance, scope, null);
	}
	
	public void set(ProcessInstance instance, String scope, Serializable value) throws Exception{
		set(instance, scope, null, value);
	}

	
	public String toString() {
		String dispName = getDisplayName().toString(); 
		
		if(dispName!=null)
			return dispName;
		
		return super.toString();
	}
	
	public void afterDeserialization() {
		setName(getName());
	}

	public void beforeSerialization() {}
	
	public static Object evaluate(Object val, ProcessInstance instance) throws Exception{
		if(val instanceof ProcessVariable){
			val = ((ProcessVariable)val).get(instance, "", null);
		}		
		return val;
	}
	
	boolean isDatabaseSynchronized;
	@Hidden
		public boolean isDatabaseSynchronized() {
			return isDatabaseSynchronized;
		}
		public void setDatabaseSynchronized(boolean isDatabaseSynchronized) {
			this.isDatabaseSynchronized = isDatabaseSynchronized;
		}
			
	DatabaseSynchronizationOption databaseSynchronizationOption;
	@Hidden
		public DatabaseSynchronizationOption getDatabaseSynchronizationOption() {
			return databaseSynchronizationOption;
		}
		public void setDatabaseSynchronizationOption(
				DatabaseSynchronizationOption databaseSynchronizationOption) {
			this.databaseSynchronizationOption = databaseSynchronizationOption;
		}
		
//	String validationScript;
//		public String getValidationScript() {
//			return validationScript;
//		}
//		public void setValidationScript(String validationScript) {
//			this.validationScript = validationScript;
//		}
		
//	public String validateValue(Object value, ProcessInstance instance, ProcessDefinition definition){
//		BSFManager manager = new BSFManager();
//		manager.setClassLoader(this.getClass().getClassLoader());
//	
//		try {
//			manager.declareBean("instance", instance, ProcessInstance.class);
//			manager.declareBean("definition", definition, ProcessDefinition.class);
//			manager.declareBean("value", value, Object.class);
//			
//			BSFEngine engine = manager.loadScriptingEngine("javascript");
//				
////			String result = (String)engine.eval("my_class.my_generated_method",0,0,"function getVal(){\n"+ getValidationScript() + "}\ngetVal();");
////			return result;
//		} catch (BSFException e) {
//			e.printStackTrace();
//			
//			return null;
//		}
//	}
		
	public boolean shouldAccessValueInSpecializedWay(){
		return isDatabaseSynchronized();
	}
		
	public Object clone(){
		//TODO [tuning point]: Object cloning with serialization. it will be called by ProcessManagerBean.getProcessDefintionXX method.
		try{
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			ObjectOutputStream ow = new ObjectOutputStream(bao);
			ow.writeObject(this);
			ByteArrayInputStream bio = new ByteArrayInputStream(bao.toByteArray());			
			ObjectInputStream oi = new ObjectInputStream(bio);
			
			ProcessVariable clonedOne =  (ProcessVariable) oi.readObject();
			clonedOne.setOpenRole(null);

			return clonedOne;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
