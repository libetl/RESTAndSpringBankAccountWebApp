package org.toilelibre.libe.bank.ioc.webapp.argresolver;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target ({ ElementType.PARAMETER })
@Retention (RetentionPolicy.RUNTIME)
@Documented
public @interface RequestBodyPath {

    String value ();

}
