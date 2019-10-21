package com.jdk.jdk7;

import com.pojo.Person;

import java.io.PrintStream;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

/**
 * Fail-fast
 * This is commonly called "fail early" or "fail-fast".
 */
public class FailFastByRequireNonNull {
    /*
    the true power of this idea unfolds in conjunction with final fields.
    Because now any other code in your class can safely assume that bar isn't null -
    and thus you do not need any if (bar == null) checks in other places!

    alternative:
     @NonNull annotation by lombok
     */
    public static void main(String... args) {
        String str = null;
        str = Objects.requireNonNull(str, "str cannot be null");
        String s = Objects.requireNonNull(str, FailFastByRequireNonNull::getErrorMessageWhenNull);

        String name = "";
        Objects.requireNonNull(name, "every suggestion needs a name");
        PrintStream ps = Objects.requireNonNull(System.out);
        ps.print(name);

        String message = "s must not be null!";
        Objects.requireNonNull(s, () -> message);

    }

    // then its person field is guaranteed be non-null by throwing exception when initlizing the object.
    class Foo {
        private final Person person;

        public Foo(Person person) {
            Objects.requireNonNull(person, "bar must not be null");
            this.person = person;
        }
    }

    private static String getErrorMessageWhenNull() {
        return "a error message when str is null";
    }

    public static Optional<String> getExtension(final URL url) {
        Objects.requireNonNull(url, "url is null");

        final String file = url.getFile();
        if (file.contains(".")) {
            final String sub = file.substring(file.lastIndexOf('.') + 1);
            if (sub.length() == 0) {
                return Optional.empty();
            }
            if (sub.contains("?")) {
                return Optional.of(sub.substring(0, sub.indexOf('?')));
            }
            return Optional.of(sub);
        }
        return Optional.empty();
    }
}