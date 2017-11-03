package com.common.util;


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

public class JSONObjectEx extends JSONObject{
	
	
	//基础类型类名
	static ArrayList arrayList ;
	
	static {
		
		 arrayList = new ArrayList();
		 arrayList.add("java.lang.Integer");
		 arrayList.add("java.lang.String");
		 arrayList.add("java.lang.Float");
		 arrayList.add("java.lang.Long");
		 arrayList.add("java.lang.Double");
	}
	
	
	
	 public JSONObjectEx(Object object, String names[]) {
	        super();
	        Class c = object.getClass();
	        for (int i = 0; i < names.length; i += 1) {
	            String name = names[i];
	            try {
	  	             putOpt(name,c.getMethod("get"+StringUtils.capitalize(name)).invoke(object));
	  	             
	            } catch (Exception ignore) {
	            }
	        }
	    }
	 
	 
	 
	 public JSONObjectEx(java.lang.Object bean) {
		    super();
		    populateMapEx(bean);
	    }
	 
	 
	 public JSONObjectEx(Object[] objs) {
		    super();
		    populateMapExArray(objs);
	    }
	 
	 
	 

	    private void populateMapEx(Object bean) {
	    	
	        Class klass = bean.getClass();

	       // If klass is a System class then set includeSuperClass to false. 

	        boolean includeSuperClass = klass.getClassLoader() != null;

	        Method[] methods = (includeSuperClass) ?
	                klass.getMethods() : klass.getDeclaredMethods();
	        for (int i = 0; i < methods.length; i += 1) {
	            try {
	                Method method = methods[i];

	                String classType = method.getReturnType().getName();
	
	                if (!arrayList.contains(classType)){
	                	continue;
	                }
	                
	                if (Modifier.isPublic(method.getModifiers())) {
	                    String name = method.getName();
	                    String key = "";
	                    if (name.startsWith("get")) {
	                    	if (name.equals("getClass") || 
	                    			name.equals("getDeclaringClass")) {
	                    		key = "";
	                    	} else {
	                    		key = name.substring(3);
	                    	}
	                    } else if (name.startsWith("is")) {
	                        key = name.substring(2);
	                    }
	                    if (key.length() > 0 &&
	                            Character.isUpperCase(key.charAt(0)) &&
	                            method.getParameterTypes().length == 0) {
	                        if (key.length() == 1) {
	                            key = key.toLowerCase();
	                        } else if (!Character.isUpperCase(key.charAt(1))) {
	                            key = key.substring(0, 1).toLowerCase() +
	                                key.substring(1);
	                        }

	                        Object result = method.invoke(bean, (Object[])null);
	                        
	                        put(key, wrapEx(result));
	                    }
	                }
	            } catch (Exception ignore) {
	            	ignore.printStackTrace();
	            }
	        }
	    }
	    
	    
	    
	    private void populateMapExArray(Object[] array) {
	    	
	    	//-------循环-----
	    	
	    	for (int arrayi =0 ; arrayi < array.length;arrayi++){
	    	         
	    		
	    		    Object bean = array[arrayi];
	    		    
	    		    if (bean == null)
	    		    	continue;
	    		
	    		    Class klass = bean.getClass();
			        
			        String className = klass.getName();
			        
			  
			        
			        int index = className.lastIndexOf(".");
			        if(index!=-1){
			        	className=className.substring(index+1);
	
			        	
			        }
		
			        boolean includeSuperClass = klass.getClassLoader() != null;
		
			        Method[] methods = (includeSuperClass) ?
			                klass.getMethods() : klass.getDeclaredMethods();
			        for (int i = 0; i < methods.length; i += 1) {
			            try {
			                Method method = methods[i];
		
			                String classType = method.getReturnType().getName();
			
			                if (!arrayList.contains(classType)){
			                	continue;
			                }
			                
			                if (Modifier.isPublic(method.getModifiers())) {
			                    String name = method.getName();
			                    String key = "";
			                    if (name.startsWith("get")) {
			                    	if (name.equals("getClass") || 
			                    			name.equals("getDeclaringClass")) {
			                    		key = "";
			                    	} else {
			                    		key = name.substring(3);
			                    	}
			                    } else if (name.startsWith("is")) {
			                        key = name.substring(2);
			                    }
			                    if (key.length() > 0 &&
			                            Character.isUpperCase(key.charAt(0)) &&
			                            method.getParameterTypes().length == 0) {
			                        if (key.length() == 1) {
			                            key = key.toLowerCase();
			                        } else if (!Character.isUpperCase(key.charAt(1))) {
			                            key = key.substring(0, 1).toLowerCase() +
			                                key.substring(1);
			                        }
		
			                        Object result = method.invoke(bean, (Object[])null);
			                        
			                        put(className+"_"+key, wrapEx(result));
			                    }
			                }
			            } catch (Exception ignore) {
			            	ignore.printStackTrace();
			            }
			        }
	        
	    	}
	        
	      //-------循环-----
	    }
	    
	    

	 
	    
	    
	    
	    static Object wrapEx(Object object) {
	         try {
	             if (object == null) {
	                 return NULL;
	             }
	             if (object instanceof JSONObject || //object instanceof JSONArray || 
	            		 NULL.equals(object)      || //object instanceof JSONString || 
	            		 object instanceof Byte   || object instanceof Character ||
	                     object instanceof Short  || object instanceof Integer   ||
	                     object instanceof Long   || object instanceof Boolean   || 
	                     object instanceof Float  || object instanceof Double    ||
	                     object instanceof String) {
	                 return object;
	             }
	             
	          
	             Package objectPackage = object.getClass().getPackage();
	             String objectPackageName = ( objectPackage != null ? objectPackage.getName() : "" );
	             if (objectPackageName.startsWith("java.") ||
	            		 objectPackageName.startsWith("javax.") ||
	            		 object.getClass().getClassLoader() == null) {
	                 return object.toString();
	             }
	             return new JSONObject(object);
	         } catch(Exception exception) {
	             return null;
	         }
	     }
	    
	    
	    public String toString() {
	       return super.toString();
	    }


}	
	 
	 
	 
	 
     
  

