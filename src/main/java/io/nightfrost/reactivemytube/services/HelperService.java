package io.nightfrost.reactivemytube.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.beans.FeatureDescriptor;
import java.util.stream.Stream;

@Component
public class HelperService {

    /*
     * For updating only changed properties on a put/change request.
     */
    public static Object partialUpdate(Object dbObject, Object partialUpdateObject){
        String[] ignoredProperties = getNullPropertyNames(partialUpdateObject);
        BeanUtils.copyProperties(partialUpdateObject, dbObject, ignoredProperties);
        return dbObject;
    }

    private static String[] getNullPropertyNames(Object object) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(object);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }
}
