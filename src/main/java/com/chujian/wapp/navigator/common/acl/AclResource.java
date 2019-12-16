package com.chujian.wapp.navigator.common.acl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AclResource {
 
    /**
     *  资源的ID
     *
     * @return
     */
    String resourceId() default "";
}