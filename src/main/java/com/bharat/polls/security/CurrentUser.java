package com.bharat.polls.security;


import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;


/**
 * To specify which Java elements for the custom annotation can be used to annotate we use @Target annotation.
 * TYPE means any type. A type is either a class, interface, enum or annotation.
 *
 * The @Documented annotation is used to signal to the JavaDoc tool that your custom annotation should be visible
 * in the JavaDoc for classes using your custom annotation.
 *
 * To specify if the custom annotation should be available at runtime, for inspection via reflection we use @Retention .
 *
 * Note: Through reflection API we can invoke methods of objects at runtime irrespective of the
 *       access specifier used with them.
*/
@Target({ElementType.PARAMETER,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {
}
