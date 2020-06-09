
## Java Versions
- javac --release 13 --enable-preview Example.java    // Compile with preview features of JDK13
- java  --enable-preview Example	                    // Run with preview features of JDK 13

## Java monitor tools

     # jconsole, jvisualvm, jmc

## Java KeyStore

    keytool -list -storepass changeit -keystore "C:\Program Files\Java\jre1.8.0_202\lib\security\cacerts
    perl analyze-ssl.pl -v3 --all-ciphers trackobot.com  (C:\Development\tools\p5-ssl-tools)

    where JAVA stores the private keys?
        Java doesn't store them anywhere.
        You store them in a keystore file, anywhere you want on the file system. Then you tell the "server" where it is.
        Exactly how you do that depends on what the "server" is, e.g. for Tomcat you give the path to the keystore file in the server.xml file.


## Encounter order / Spatial order

        A source has or does not have encounter order.
        List and arrays are sources that have encounter order (arrays can be said to also have a spatial order).
        HashSet is a source that does not have encounter order (another example is PriorityQueue).

        A terminal operation preserves or does not preserve encounter order when producing a result.
        Non-preserving terminal operations are forEach, forEachUntil, findAny, match{Any, None, All}, collect(toHashSet()) and
        collectUnordered.

        An intermediate operation may inject encounter order down-stream.
        The sorted() operation injects encounter order when the natural comparator is used to sort elements.

        An intermediate operation may clear encounter order down-stream.
        There are no such operations implemented.
        (Previously the unordered() operation cleared encounter order.)

        Otherwise an intermediate operation must preserve encounter order if required to do so (see next paragraphs).

        An intermediate operation may choose to apply a different algorithm if encounter order of the elements output from the intermediate operation must be preserved or not.
        The distinct() operation will, when evaluating in parallel, use a ConcurrentHashMap to store unique elements if encounter order does not need to be preserved, otherwise if encounter order needs to be preserved a fold will be performed (equivalent of, in parallel, map each element to a singleton set then associatively reduce the sets to one set).

        An intermediate operation should preserve encounter order of the output elements if:

        a.1) the upstream elements input to the intermediate operation has an encounter order (either because the source has encounter order or because an upstream operation injected encounter order); and
        a.2) the terminal operation preserves encounter order.

        An intermediate operation does not need to preserve encounter order of the output elements if:

        b.1) the upstream elements input to the intermediate operation has no encounter order (either because the source has no encounter order or because an upstream operation cleared encounter order); or
        b.2) the terminal operation does not preserve encounter order *and* the intermediate operation is in a sequence of operations, to be evaluated, where the last operation in the sequence is the terminal operation and all operations in the sequence are evaluated in parallel.

        Rule b.2 above ensures that for the following pipeline encounter order is preserved on the sequential forEach:

         list.parallelStream().distinct().sequential().forEach()

        i.e. the distinct() operation will preserve the encounter order of the list

## countdownlatch

     When should we use CountDownLatch in Java :
     Use CountDownLatch when one of Thread like main thread, require to wait for one or more thread to complete, before its start doing processing.
     try CountDownDemo.java to see details.

## fail-fast strategy by Objects.requireNotNull from jdk7/8/9

    check FailFastByRequireNonNull.java for reference.

## Default constructor is required (Explicitly) in a parent class if the parent class has an argumented constructor.

        http://xahlee.info/java-a-day/inheritance_constructers.html
        If you are explicitly providing an argument-ed constructor, then the default constructor (NOT-argumented | Empty args) is NOT available to the class.

        For Example :
            class A {
              A(int i){
              }
            }

            class B extends A {
            }

            So when you write

            B obj_b = new B();
            It actually calls the implicit constructor provided by java to B, which again calls the super(), which should be ideally A().
            But since you have provided argument-ed constructor to A, the default constructor A() is not available to B().

        Solution
            class B {
              int x;
              //B () {x=300;}
              B (int n) {x=n;}
              int returnMe () {return x;}
            }

            class C extends B {
            }


            the answer to the constructor mystery is that, if one provides any constructor, one must define all constructors.

            Peter Molettiere on Apple's Java forum gave excellent answers:

            Because there is no default constructor available in B, as the compiler error message indicates. Once you define a constructor in a class, the default constructor is not included.
            If you define *any* constructor, then you must define *all* constructors.

            When you try to instantiate C, it has to call super() in order to initialize its super class. You don't have a super(), you only have a super(int n), so C can not be defined with the default constructor C() { super(); }. Either define a no-arg constructor in B, or call super(n) as the first statement in your constructors in C.

            So, the following would work:

            class B {
                int x;
                B() { } // a constructor
                B( int n ) { x = n; } // a constructor
                int returnMe() { return x; }
            }

            class C extends B {
            }
            or this:

            class B {
                int x;
                B( int n ) { x = n; } // a constructor
                int returnMe() { return x; }
            }

            class C extends B {
                C () { super(0); } // a constructor
                C (int n) { super(n); } // a constructor
            }

## final

        When should I use final? **One answer to this is "whenever you possibly can". **
        
        Any field that you never expect to be changed (be that a primitive value, or a reference to an object, whether or not that particular object is itself immutable or not), should generally be declared final. 
        Another way of looking at things is:
        If your object is accessed by multiple threads, and you don't declare its fields final, then you must provide thread-safety by some other means.
        Other means could include declaring the field volatile, using synchronized or an explicit Lock around all accesses to that field.

        A typical case that people overlook is where an object is created by one thread and then later consumed by another thread, 
        e.g. an object via a ThreadPoolExecutor. In this case, the object must still be made properly thread-safe: 
        it doesn't matter that the accesses by different threads aren't concurrent.
        What matters is that the object is accessed by different threads at any point in its lifetime.

## volatile variable

1. Volatile Keyword is applicable to variables.
1. volatile keyword prevents caching of variables.
1. On modern CPUs, even a volatile variable can be shared among distinct CPU caches
1. Volatile is An indication to the VM that multiple threads may try to access/update the field's value at the same time.
1. volatile keyword in Java guarantees that value of the volatile variable will always be read from main memory and not from Thread's local cache.
1. Volatile is Similar to Static(Class) variable, Only one copy of volatile value is cached in main memory,
1. Before doing any ALU Operations each thread has to read the updated value from Main memory after ALU operation it has to write to main memory direclty.
1. Changes to a volatile variable are always visible to other threads.
1. JVM uses CPU architecture to ensure the visibility of 'volatile variable' across ALL threads after the WRITE operation
