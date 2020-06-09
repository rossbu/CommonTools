# ENUM

JDK 1.5 introduces a new enum type (in addition to the existing top-level constructs class and interface) along with a new keyword enum.

>1. Whenever an enum is defined, a class that extends java.lang.Enum is created
>1. You are not allowed to construct a new instance of enum using new operator, because enum keeps a fixed list of constants.
>1. You can use the “==” operator to compare enum constants effectively, since enum constants are final.
>1. An enum can be used to define a set of enum constants. The constants are *implicitly static final*, which cannot be modified. Since they are static, they can be accessed via EnumName.instanceName.
>1. You cannot extend any other class in enum, enums extend the java.lang.Enum class implicitly.
>1. An enum is a reference type (just like a class, interface and array), which holds a reference to memory in the heap.
>1. enum is a keyword. Enum constructors are always private or default. Therefore, you cannot have public or protected constructors in an enum type.
>1. Enum with abstract method Each of the instances of enum could have its own behaviors.
>1. Enums can implement interfaces. They implicitly implement the Serializable and Comparable interfaces.
>1. You can declare abstract methods within an enum. If you do, all the enum fields must implement the abstract methods.

## Example
>        enum CardSuit { SPADE, DIAMOND, CLUB, HEART }
>        for each enum, the Java compiler automatically generates a static method called values() that returns an array of all the enum constants,
>        4 instances of enum type CardSuit were generated via values(). The instances are created by calling the constructor with the actual argument, when they are first referenced.
>        
>
>        ## Compiled class
>            public static final class EnumExample1$Season extends Enum
>            {
>              private EnumExample1$Season(String s, int i)
>                {
>                    super(s, i);
>                }
>
>                public static EnumExample1$Season[] values()
>                {
>                    return (EnumExample1$Season[])$VALUES.clone();
>                }
>
>                public static EnumExample1$Season valueOf(String s)
>                {
>                    return (EnumExample1$Season)Enum.valueOf(EnumExample1$Season, s);
>                }
>
>                public static final EnumExample1$Season WINTER;
>                public static final EnumExample1$Season SPRING;
>                public static final EnumExample1$Season SUMMER;
>                public static final EnumExample1$Season FALL;
>                private static final EnumExample1$Season $VALUES[];
>
>                static
>                {
>                    WINTER = new EnumExample1$Season("WINTER", 0);
>                    SPRING = new EnumExample1$Season("SPRING", 1);
>                    SUMMER = new EnumExample1$Season("SUMMER", 2);
>                    FALL = new EnumExample1$Season("FALL", 3);
>                    $VALUES = (new EnumExample1$Season[] {
>                        WINTER, SPRING, SUMMER, FALL
>                    });
>                }
>            }
>
>        ## Static
>            enum types that are defined as nested types are always implicitly STATIC, so If you declared an enum like this:
>
>                    enum Suit {SPADES, HEARTS, CLUBS, DIAMONDS}
>
>            The Java compiler would synthetically generate the following class for you:
>            final class Suit extends java.lang.Enum<Suit> {
>              public static final Suit SPADES;
>              public static final Suit HEARTS;
>              public static final Suit CLUBS;
>              public static final Suit DIAMONDS;
>              private static final Suit[] $VALUES;
>              public static Suit[] values();
>              public static Suit valueOf(java.lang.String);
>              private Suit();
>            }
>
>
>        ## SAM ( enum )
>            An enum type can have abstract methods just like a class. Each enum constant needs to implement the abstract method. An example is as follows:
>
>            public enum Animal {
>              Dog { String sound() { return "bark"; } },
>              Cat { String sound() { return "meow"; } },
>              Lion { String sound() { return "roar"; } },
>              Snake { String sound() { return "hiss"; } };
>              abstract String sound();
>            };
>
>            then:
>            String str = "Dog";
>            Animal animal = Animal.valueOf(Animal.class, str);
>            System.out.println(animal + " makes sound: " + animal.sound());
>            // prints
>            Dog makes sound: bark
>
>
>        ## valueOf() to look up an enum by the name.
>
>            The java compiler internally adds the values() method when it creates an enum.
>            The values() method returns an array containing all the values of the enum.
>
>            private enum Day {
>                SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
>                THURSDAY, FRIDAY, SATURDAY
>            };
>            Day day = Day.valueOf(Day.class, "MONDAY");
>            //The method throws an IllegalArgumentException if the name (with the exact case) is not found.  then use Day day = Day.valueOf(Day.class, "Monday");
