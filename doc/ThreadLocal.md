# ThreadLocal alternatives in Web Application ( Spring )

##  Concept
    Thread Local can be considered as a scope of access, like a request scope or session scope. It’s a thread scope. 


## Scope
	Global access
	Values stored in Thread Local are global to the thread (only), meaning that they can be accessed from anywhere inside that thread.
	If a thread calls methods from several classes, then all the methods can see the Thread Local variable set by other methods (because they are executing in same thread).
	One thread can not access/modify other thread’s Thread Local variables.


	local access
	Values stored in ThreadLocal are local to the thread, meaning that each thread will have it’s own ThreadLocal variable. A thread cannot access/modify other thread’s ThreadLocal variables.

## Use Case
	Consider you have a requirement to generate a unique transaction id for each and every request this servlet process and you need to pass this transaction id to the business methods, 
	for logging purpose. One solution would be passing this transaction id as a parameter to all the business methods. But this is not a good solution as the code is redundant and unnecessary.
	To solve that, you can use Thread Local. You can generate a transaction id (either in servlet or better in a filter) and set it in the Thread Local. After this,
	what ever the business method, that this servlet calls, can access the transaction id from the thread local.


## Pros
	ThreadLocal gives you the opportunity to use the variables without explicitly passing them down through the method invocation chain. Which could be useful on certain occasions.


## Cons
	Introduce a memory leak to the code when using a ThreadLocal Considering that modern Web application servers pool threads (in servers' threadpool) 
    instead of creating a new one on each 	HttpRequest, which have built the foundation to a problem.  
    If one of the application classes stores a value in ThreadLocal variable and doesn’t remove it after the task is completed, 
    a copy of that Object will remain with the Thread (from the application server thread pool). Since lifespan of the pooled Thread surpasses that of the application, 
    it will prevent the object and thus a ClassLoader being responsible for loading the application from being garbage collected


## Solutions/Alternatives

	Organic ThreadLocal
    
    request-scoped beans ( Spring container )
		request-scoped beans are usually a cleaner and more elegant alternative to ThreadLocals when we are dealing with web applications ( no matter it's EE or Spring-DI based web dev)
 		In fact, under the covers, Spring implements request-scoped beans using its own ThreadLocal variables (see RequestContextHolder). 
		Both ThreadLocal and scoped beans give you the same basic advantage - the ability to access the object without having to pass it down manually through the call stack.
		(exception:  JSP taglib. Taglib instances are controlled by the servlet container, NOT Spring, so JSP Taglib cannot participate in Spring's IoC framework)
		So For a Spring web application, if you want to have access to objects in layers that are specific to the current request thread, then use request-scoped beans. 
	
	SimpleThreadScope
		Spring also provide a non-default thread scope using class SimpleThreadScope. To use this scope, you need to register it to container using CustomScopeConfigurer class.
		but it does not clean up any objects associated with it. ( you need to provide customized overwrite method ). 
		It is therefore typically preferable to use a request-bound scope implementation such as org.springframework.web.context.request.RequestScope in web environments,
		Implementing the full lifecycle for scoped attributes (including reliable destruction).
		SourceCode:
			@Override
			public void registerDestructionCallback(String name, Runnable callback) {
				logger.warn("SimpleThreadScope does not support destruction callbacks. " +
				"Consider using RequestScope in a web environment.");
				// unless write  code to destroy the retention objects
			}


	ThreadLocalTargetSource 
		This approach would still use a ThreadLocal reference, but delegates setting it, injecting it and clearing it, to the Spring framework.
		for example:  clear inside of spring IOC container
		@Bean(destroyMethod = "destroy")
  		public ThreadLocalTargetSource threadLocalContext() {
    			ThreadLocalTargetSource result = new ThreadLocalTargetSource();
    			result.setTargetBeanName("extrinsicsContext");
    			return result;
  		}


##  Reference
	https://tech.asimio.net/2017/11/28/An-alternative-to-ThreadLocal-using-Spring.html
	https://github.com/spring-projects/spring-framework/blob/master/spring-context/src/main/java/org/springframework/context/support/SimpleThreadScope.java
	https://bitbucket.org/asimio/threadlocaltargetsource-demo/src/master/
	https://howtodoinjava.com/spring-core/spring-bean-scopes/#thread
	https://plumbr.io/blog/locked-threads/how-to-shoot-yourself-in-foot-with-threadlocals


