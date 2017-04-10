# **Dynamic-LockScreen-App**
This application is for android security concern, through this app user able to change their unlock pin **dynamically** based on current time/date/battery status of the device.  
This app is live , one can debug the code and use application.  
 **This application is only for user who use version<android(N)**  
In Nougat, Application leads to force stop, due to new security features included.  

This is just demo , Advance module is left to implement

## How it works?
* At the time of installation of app it will ask for user permission to activate device permission and set master password to login to app.
* In case user, forget master password they can get password by resolving security question that was set up by user during installation.
* Next, user will be provided basically main three options to set up PIN accordingly **Max pin length is 8**
 * **TIME PIN**: This includes options to set pin, lets consider current device time is 1:43 PM
1. **12H** : Pin will be 0143
2. **24H** : Pin will be 1443 
3. **offset(+5)** : Offset is according to 12H format, it will increase 5 hours from current time Pin 0643
4. **offset (-5)** : Offset is according to 12H format, it will deduct 5 hours from current time Pin 0843

 * **DATE PIN** : Modifiers for DATE PIN are as follows, lets consider today date is 05-04-17
1. **DD-MM** : Pin will be 0504
2. **MM-YY** : Pin will be 0417
3. **DD-YY** : Pin will be 0517
4. **YYYY** : Pin will be 2017

 * **BATTERY PIN** : Modifiers for BATTERY PIN are as follows, lets consider current battery status is 54%
1. **Battery Pin** : Pin will be 5454
2. **Offset(+5)** : Add +5 to the current battery status, Pin will be 5959

 * **ADVANCE PIN** : This is challenging module, In which PIN will be according to format user choose. Pin length will not be more than 8. For eg, user can choose (12H-DD-BB),(24H-mm-YY) etc.

## Description of each java activity used
### Splash.java

In this activity first it will check if user permissions allow then only move to next activity otherwise stay on **Permission.java**.
This activity runs 5 ms I had use thread
### **Permission.java**  
This activity is to enable **Device administration policies** , by creating object of "DevicePolicyManager"  

### Master_password.java 
This set up an security question choose by user, Here Spinner is being used to show list of questions . 
   
### **Navigation.java (MAIN MODULE)**

In which, based on user selection of modifiers corresponding service is run in background every second to make password dynamic. Here Foreground service is used in which PIN will change even if app got killed.
