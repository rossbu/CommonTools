package com.jdk.jdk8;

import com.pojo.Person;
import com.pojo.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;


/**
 Optional classes were introduced in order to prevent NullPointerException,
 but the method get() used to retrieve the value inside the Optional might still throw a NoSuchElementException,
 so
 if(myString.isPresent()){
    doSomething(myString.get());
 }
 *
 *
 */
public class OptionalDemo {
    public static void main(String[] args) {
        emptyOptional();
        ifPresent();
        orElse();
        ifPresentOrElse();  // jdk9
        ofNullable();  // turn null -> empty optional object
    }

    private static void ofNullable() {
//        If the specified value is null, then this method returns an empty instance of the Optional class.
        Object o = null;
        Optional op1  = Optional.ofNullable(o);
        System.out.println("Optional 1: " + op1);
    }

    private static void ifPresentOrElse() {
        Optional<Integer> op = Optional.of(131);
        op.ifPresentOrElse(( var e ) -> {
            System.out.println("yes, i have value: " + e);
        }, () -> {
            System.out.println("no, I have nothing here");
        });


        op = Optional.empty();
        op.ifPresentOrElse(( var e ) -> {
            System.out.println("yes, i have value: " + e);
        }, () -> {
            System.out.println("no, I have nothing here");
        });
    }

    private static void orElse() {
        // use ifpresent otherwise like Optional.ofNullable<List>.map(Collection::stream).findfirst().ifpresent ( lllambda)
        Optional<Person> opt = Optional.empty();
        Person x = opt.orElse( new Person() );
        Person y = opt.orElseGet( Person::new ); // lazy loading for heavy resource
    }

    private static void ifPresent() {
        Optional<String> petNameOptional = Optional.of("Bobby");
        String petName = "";
        if(petNameOptional.isPresent()){
            petName = petNameOptional.get();
        }
    }

    private static void emptyOptional() {
        Optional<String> optional =  Optional.empty();
        System.out.println(optional);
        // If you still use the get() functionality on optionals, you're effectively opening youselvies to the same problems as null checks. Don't use Optional get without isPresent
//        System.out.println(optional.get());
        // Do NOT do this , same as null pointer exception ,use ifPresent first, then get if true.
    }

    @RequestMapping(value = {"/article", "/article/{id}}" })
    public User springOptionalTest(@PathVariable Optional<Integer> optionalArticleId) {
        if (optionalArticleId.isPresent()) {
            Integer articleId = optionalArticleId.get();
        } else {
            ;
        }
        return null;
    }
    @RequestMapping(value = {"/user", "/user/{id}"})
    public User getUser(@PathVariable(required = false) Integer userId) {
        if (userId != null) {
            //...
        } else {
            //...
        }
        return new User();
    }
}
