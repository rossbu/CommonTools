package com.jdk.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import java.lang.annotation.Retention;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import com.jdk.jdk5.annotation.AnnotatedBeanLocator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardMethodMetadata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomAnnotationsTest {

    /** See http://stackoverflow.com/questions/14236424/how-can-i-find-all-beans-with-the-custom-annotation-foo#comment19751414_14236424 */
    @Test
    public void testFindByAnnotation() throws Exception {

        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext( CustomAnnotationsSpringCfg.class );

        Method m = CustomAnnotationsSpringCfg.class.getMethod( "a" );
        assertNotNull( m );
        assertNotNull( m.getAnnotation( Foo.class ) );

        ConfigurableListableBeanFactory factory = appContext.getBeanFactory();
        BeanDefinition bd = factory.getBeanDefinition( "a" );
        System.out.println( Arrays.toString( bd.attributeNames() ) );

        String type = Foo.class.getName();
        if( bd instanceof AnnotatedBeanDefinition ) {
            AnnotatedBeanDefinition abd = (AnnotatedBeanDefinition) bd;

            AnnotationMetadata metadata = abd.getMetadata();
            System.out.println( metadata.getAnnotationTypes() );
            Map<String, Object> attributes = metadata.getAnnotationAttributes( type );
            System.out.println( attributes);
        }
        if( bd instanceof AbstractBeanDefinition ) {
            AbstractBeanDefinition abd = (AbstractBeanDefinition) bd;

            System.out.println( abd.getQualifiers() );
        }

        Object source = ( (BeanMetadataElement) bd ).getSource();
        if( source instanceof StandardMethodMetadata ) {
            StandardMethodMetadata metadata = (StandardMethodMetadata) source;

            System.out.println( metadata.getAnnotationAttributes( type ) );
        }

        // TODO this should work
//        Map<String, Object> beans = appContext.getBeansWithAnnotation( Foo.class );
//        assertEquals( "[a]", beans.keySet().toString() );

        // Workaround
        AnnotatedBeanLocator locator = new AnnotatedBeanLocator( appContext );
        assertEquals( "[a]", locator.getBeansWithAnnotation( Foo.class ).toString() );
    }


    @Retention( RetentionPolicy.RUNTIME )
    @Target( ElementType.METHOD )
    @Qualifier
    public static @interface Foo {
        String value();
    }

    public static class Named {
        private final String name;

        public Named( String name ) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Lazy
    @Configuration
    public static class CustomAnnotationsSpringCfg {

        @Foo( "x" ) @Bean public Named a() { return new Named( "a" ); }
        @Bean public Named b() { return new Named( "b" ); }
    }
}