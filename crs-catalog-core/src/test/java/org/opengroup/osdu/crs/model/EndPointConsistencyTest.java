// Copyright 2017-2019, Schlumberger
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.opengroup.osdu.crs.model;

import org.opengroup.osdu.crs.model.interfaces.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is to detect any inconsistency between the API class/interface and implementation class
 * in terms of getter methods.
 * 1) Make sure that the API class/interface expose the right set of getter methods defined in the implementation class.
 * 2) Make sure that google endpoints serialization won't serialize more properties than the API class/interface defined.
 */
public class EndPointConsistencyTest {

    @Test
    public void testJsonSerializationConsistency() {
        //<API class/interface, implementation class>
        Map<Class, Class> checkedClasses = new HashMap<>();
        checkedClasses.put(AreaOfUse.class, AreaOfUseImpl.class);
        checkedClasses.put(AreaOfUseDeprecationInfo.class, AreaOfUseDeprecationInfoImpl.class);
        checkedClasses.put(AreaOfUseEssence.class, AreaOfUseEssenceImpl.class);
        checkedClasses.put(AuthorityCode.class, AuthorityCodeImpl.class);
        checkedClasses.put(Base.class, BaseImpl.class);
        checkedClasses.put(CT.class, CTImpl.class);
        checkedClasses.put(Catalog.class, CatalogImpl.class);
        checkedClasses.put(CatalogAttributes.class, CatalogAttributesImpl.class);
        checkedClasses.put(CompoundCT.class, CompoundCTImpl.class);
        checkedClasses.put(CompoundCTEssence.class, CompoundCTEssenceImpl.class);
        checkedClasses.put(CompoundCRS.class, CompoundCRSImpl.class);
        checkedClasses.put(CompoundCRSEssence.class, CompoundCRSEssenceImpl.class);
        checkedClasses.put(CRS.class, CRSImpl.class);
        checkedClasses.put(CRSDeprecationInfo.class, CRSDeprecationInfoImpl.class);
        checkedClasses.put(DeprecationInfo.class, DeprecationInfoImpl.class);
        checkedClasses.put(EarlyBoundCRS.class, EarlyBoundCRSImpl.class);
        checkedClasses.put(EarlyBoundCRSEssence.class, EarlyBoundCRSEssenceImpl.class);
        checkedClasses.put(Essence.class, EssenceImpl.class);
        checkedClasses.put(LateBoundCRS.class, LateBoundCRSImpl.class);
        checkedClasses.put(LateBoundCRSEssence.class, LateBoundCRSEssenceImpl.class);
        checkedClasses.put(Root.class, RootImpl.class);
        checkedClasses.put(Essence.class, EssenceImpl.class);
        checkedClasses.put(SingleCT.class, SingleCTImpl.class);
        checkedClasses.put(SingleCTEssence.class, SingleCTEssenceImpl.class);
        checkedClasses.put(SphericalBoundingBox.class, SphericalBoundingBoxImpl.class);
        checkedClasses.put(CTDeprecationInfo.class, CTDeprecationInfoImpl.class);

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Class, Class> entry: checkedClasses.entrySet()) {
            Class apiClass = entry.getKey();
            Class implClass = entry.getValue();

            List<Method> apiGetters = getGetters(apiClass);
            List<Method> implGetters = getGetters(implClass);
            List<Method> nonAPIGetters = getNonAPIGetters(apiGetters, implGetters);
            for(Method method : nonAPIGetters) {
                try {
                    Annotation annotation = method.getAnnotation(JsonIgnore.class);

                    if(annotation == null) {
                        stringBuilder.append("\n" + getErrorMessage(method.getName(), apiClass.getName(), implClass.getName()));
                    }
                }
                catch (Exception ex) {
                    stringBuilder.append("\n" + getErrorMessage(method.getName(), apiClass.getName(), implClass.getName()) + "\n");
                }
            }
        }

        if(stringBuilder.length() > 0) {
            fail(stringBuilder.toString());
        }
    }

    private static String getErrorMessage(String methodName, String apiClassName, String implClassName) {
        String message = "Method '" + methodName + "' in class '" + implClassName + "' is not defined in API class/interface " + apiClassName;
        message += ". It should be either adding the method to the API class/interface " + apiClassName + " or annotating the method as '@JsonIgnore' in class " + implClassName;
        return message;
    }

    private static boolean isGetter(Method method){
        if(!method.getName().startsWith("get"))      return false;
        if(method.getParameterTypes().length != 0)   return false;
        if(void.class.equals(method.getReturnType())) return false;
        return true;
    }

    private static List<Method> getGetters(Class cls) {
        List<Method> getters = new ArrayList<>();
        for(Method method : cls.getMethods()) {
            if(isGetter(method))
                getters.add(method);
        }
        return getters;
    }

    private static List<Method> getNonAPIGetters(List<Method> apiGetters, List<Method> implGetters) {
        List<Method> nonAPIGetters = new ArrayList<>();
        for(Method method : implGetters) {
            if(method.getName().equals("getClass"))
                continue;

            boolean definedInInterface = false;
            for (Method apiMethod : apiGetters) {
                if(apiMethod.getName().equals(method.getName())) {
                    definedInInterface = true;
                    break;
                }
            }
            if(!definedInInterface)
                nonAPIGetters.add(method);
        }

        return nonAPIGetters;
    }
}
