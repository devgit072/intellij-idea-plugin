Plugin on Intellij-Idea platform is utility which comes with certain features and it can help boosting
productivity while doing coding. For-example: Auto-completion, generating code etc.

In this tutorials we will learn how to develop a plugin on Intellij Idea. A plugin on intellij-idea can
be developed using Java or Kotlin(new kid in the JVM block!!).

We will develop plugin for 
* Action : Select the given keyword on search on google.com
* Auto-Completion in *.yaml/*.yml file.
* Generate Code/Boilerplate snippet in java file.

We will put plenty of code to make the code self-documentation.

To implement auto-completion, there are two ways:
* Use JsonSchema which is simple to implement, effective and easy way. 
However, it has less control over the cursor and hence less powerful.
* Implement code completion using code. It is tedious(for beginners) to implement, but a more engaging auto-completion 
can be implemented. We can have more control over the cursor.

To implement generation of boiler-plate code generation, we can use live-template xml file.

Anyway it is easy and fun to develop a plugin on Intellij-Idea platform and once you learn 
how to develop a plugin, you can develop a plugin in future which suits your requirements.

Action implemention:  
Action class is implemented in SearchGoogleAction.java. You also have to register 
the class name in plugin.xml

Boiler Code generation using live template is implemented in TemplateProvider.java

Code-Completion using code is implemented in autoCompletion package. Code is pretty self documented and can be understood.

Other miscellaneous details:

How to implement notifier:  
Here is sample code snippet:


    public class Notifier implements StartupActivity {
    private String title, message;
    NotificationType notificationType;
    Notifier(String title, String message, NotificationType notificationType) {
        this.title = title;
        this.message = message;
        this.notificationType = notificationType;
    }
    @Override
    public void runActivity(@NotNull Project project) {
        final Notification notification = new Notification("JsonSchemaNotification", title,
                message, notificationType);
        notification.notify(project);
    }
    }


Call above Class function:  
  ``new Notifier("blaTitle", "Mesg" , NotificationType.INFO).runActivity(thiProject)``
  
 
How to implement background activity:
This is helpful. Sometime, your UI may freeze if Action takes long time to perform.

`ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
                @Override
                public void run() {
                    ApplicationManager.getApplication().runReadAction(new TriggerScriptsInBackgound(parameters, project));
                }
            });
`  

`public class TriggerGoScriptsInBackgound implements Runnable {
    @Override
    public void run() {
    }
}`

