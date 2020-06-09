# Gamiphy SDK

[Gamiphy SDK Jitpack Link](https://jitpack.io/#gamiphy/android-sdk)

## Introduction 

Gamiphy Loyalty & Rewards, is a gamified loyalty program plugin/widget for eCommerce. You will be able to reward users with points for completing pre defined "challenges" within your store. In addition to that users can compete with each other in compeitions reflected on a leaderboard, receive badges and invite their freinds, among other gamified features.



## Requirements

- Android Studio 3.1+
- Java 1.8
- Androidx

## Installation

Gamiphy Loyalty & Rewards is available through [JitPack](https://jitpack.io/#gamiphy/android-sdk). To install
it, simply add the dependency for the Gamiphy SDK in your module (app-level) Gradle file (usually app/build.gradle):

```gradle
       implementation 'com.github.gamiphy:android-sdk:v1.1.4'
```

and make sure you have jitpack in your root-level (project-level) Gradle file (build.gradle), 
```gradle
   allprojects {
    repositories {
        google()
        jcenter()
        // add jitpack if it's not added
        maven { url 'https://jitpack.io' }
    }
}
```

## Getting started

Gamiphy SDK needs to be initialized in Application class, you can do that by calling the init methid as shown below, and pass some required data / parameters that 
you can get after you signup for an account at Gamiphy. Kindly note the initilize method below. 

```kotlin
      GamiBot.getInstance().init(applicationContext, botId, language).setDebug(true)
```
And you can set Debug mode.
```kotlin
      GamiBot.getInstance().init(applicationContext, botId, language).setDebug(true).setDebug(true)
```

## Showing the plugin within your application

Gamiphy Loyalty & Rewards can be triggered and shown in two methods. 

- Simply add the widget button to your xml layout

```xml  
        <com.gamiphy.library.utils.GamiphyBotButton
        android:layout_width="70dp"
        android:layout_height="70dp" />
```

- If you are interested in having your own widget/button that will be repsonsible to open the system, or you want to open the widget after a certin action. You can do so by calling the following method: 

```Kotlin
    GamiphySDK.getInstance().open(context,user,language)
```
note: user and language are optional parameters in case we passed them in login

## Widget visitor flow 

Gamiphy Loyalty & Rewards support the ability for the end users to navigate the different features available, without even being logged in. But whenever 
the users trying to perform actions they will be redirected to either login or signup to the application. 

You need to specify the Activity where the users can login / register in your application. You should implement OnAuthTrigger by doing as the following: 

```Kotlin
GamiphySDK.getInstance().registerGamiphyOnAuthTrigger(object : OnAuthTrigger {
            override fun onAuthTrigger(signUp: Boolean) {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        }
        )
```
OnAuthTrigger method called when click signUp/login in the widget. isSignUp true for signup redirection, isSignUp false for login redirection.

In login activity, after the user logged in, set user name and email and start gamiphy view
```kotlin
 GamiphySDK.getInstance().login(User(email,name,hash))
```

Gamiphy SDK Listeners:

- OnTaskTrigger: this listener has onTaskTrigger method,this method called when click get on custom tasks.
```kotlin
 gamiBot.registerGamiphyOnTaskTrigger(object : OnTaskTrigger {
            override fun onTaskTrigger(actionName: String) {
                Log.d(MainActivity::class.java.simpleName, "here is action name $actionName")
            }
        })
```

- OnAuthTrigger: this listener has OnAuthTrigger method, this method is called when the widget requires login/signup for the user or the login button inside the widget is clicked.
 isSignUp true for signup redirection, isSignUp false for login redirection.
```kotlin
GamiBot.getInstance().registerGamiphyOnAuthTrigger(object : OnAuthTrigger {
            override fun onAuthTrigger(signUp: Boolean) {
             //make your action here, you may start login activity
                if (!signUp) {
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                }
            }
        }
        )
```

- OnRedeemTrigger: this listener has onRedeemTrigger method, this method is called when the redeem button is clicked in the widget. 
redeem is the redeem object want to redeem which contains packageId and pointsToRedeem
```kotlin
 gamiBot.registerGamiphyOnRedeemTrigger(object : OnRedeemTrigger {
            override fun onRedeemTrigger(redeem: Redeem?) {
                Log.d(MainActivity::class.java.simpleName, "here is redeem object  $redeem")
                GamiBot.getInstance().markRedeemDone(redeem?.packageId!!, redeem.pointsToRedeem!!)
            }
        })
```

## Registering the users

You can simply register your users for our SDK by calling this method. 

```kotlin
   GamiBot.getInstance().loginSDK(context, User(email,name,hash))
```

you need to call this method in both cases the login / signup if you do instant login of your users after they login/signup. 


## Creating the challenges: 


You need to send the custom event actions whenever its done using the method markTaskDone shown below.
This method take the event name label and mark it as done.

```kotlin
GamiBot.getInstance().markTaskDone(actionName)
```
note: you can use this method with the widget only, the widget should be on when you use it.

If you need to send the custom event action in general -ignoring widget status - with data object, you need to create new class according data object -class fields names should have same data fields names-.
note: data object is optinoal and depends on your case.
```kotlin
GamiBot.getInstance().markTaskDoneSdk("purchaseCourseEvent",email)
```
e.g: Assume we need to send data object with client details
```
data:{"id:2","name":"testClient"}
```
we will need to create class with same fields names like this: 
```
data class Client(val id: Int, val name: String)
```
then you can send it in markTaskDoneSdk
```kotlin
GamiBot.getInstance().markTaskDoneSdk("purchaseCourseEvent",email,Client(2, "testClient"))
```
to mark redeem, we have markRedeemDone method with packageId and pointsToRedeem parameters, you will need it in onRedeemTrigger event:
```kotlin
GamiBot.getInstance().markRedeemDone(redeem?.packageId!!, redeem.pointsToRedeem!!)
```

## Referral Tracking ##

### Integrate your app with Firebase Dynamic Links
---
Gamiphy Loyalty app uses firebase dynamic links for deep linking. This is why you must integrate Firebase in your project. 
Please follow the [Docs](https://firebase.google.com/docs/android/setup) to integrate with Firebase. 
​
**Note**: You must add **SHA-1** and **SHA-256** signing keys to enable Firebase dynamic links.
### Init localization strings
---
Firebase requires two keys to be integrated with any app:
**uri_prefix**: To set this value, open [Firebase Console](https://console.firebase.google.com/u/0/). Then go to the **Dynamic Links** section and add a new uri_prefix.
**website_uri**: This is a url to the web home page.

Add both values to `res/values/strings.xml` as follows.
```xml
<string name="uri_prefix">{{uri_prefix}}</string>
<string name="website_uri">{{website_url}}</string>
```
### Set up Firebase Dynamic Links SDK
---
After making sure that your app connected to your Firebase project, add the dependency for the Firebase Dynamic Links Android library to your module  (`build.gradle`):  
```gradle
dependencies {  
  ...
  implementation 'com.google.firebase:firebase-dynamic-links:19.1.0'    
  implementation 'com.google.firebase:firebase-analytics:17.2.3'  
}
```
### Create a Dynamic Link
---
Now you can use the following code to create a dynamic link:
```kotlin
dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()  
    .setLink(Uri.parse(getString(R.string.website_uri)))  
        .setDomainUriPrefix(getString(R.string.url_prefix))  
        // Open links with this app on Android  
        .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())  
        // Open links with com.example.ios on iOS 
        .setIosParameters(DynamicLink.IosParameters.Builder("com.example.ios").build())  
        .buildDynamicLink()  
  
val dynamicLinkUri = dynamicLink.uri
```
### Receive Firebase Dynamic Links parameter
---
* **Add intent filter**
To handle the dynamic links you should add a new intent filter to the activity that handles deep links in your app. The intent filter will catch deep links to your domain. 
Use the following code to add the intent filter in `AndroidManifest.xml`
```xml
    <intent-filter>  
     <action android:name="android.intent.action.VIEW" />  
     <category android:name="android.intent.category.DEFAULT" />  
     <category android:name="android.intent.category.BROWSABLE" />  
     <data  
        android:host="url_prefix"  
        android:scheme="https"/>
    </intent-filter>
```
* **Parse the referrer id**
On our side, we will add the referrer to the deepLink query parameter. So, to receive the deep link, call the `getDynamicLink()` method:
```kotlin
     Firebase.dynamicLinks  
         .getDynamicLink(intent)  
         .addOnSuccessListener(this) { pendingDynamicLinkData ->  
                  // Get deep link from result (may be null if no link is found)  
                  var deepLink: Uri? = null  
                  if (pendingDynamicLinkData != null) {  
                   deepLink = pendingDynamicLinkData.link  
                   referrerId = deeplink.getQueryParameter("referrerId")   
               }  
             }  
          .addOnFailureListener(this) { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }
```
### Add parameter to the widget URL
---
Add ShareUrl and referrer ID to Gamiphy’s app url as query string
```kotlin
var builder: Uri.Builder = Uri.Builder().scheme("https")  
    .authority(getString(R.id.gamiphy_bot_url))  
    .appendQueryParameter("referrerId", referrerId)  
    .appendQueryParameter("shareUrl", dynamicLink)  
  
var botUrl: Uri? = builder.build()
```
### Open Gamiphy App
---
Insert these few lines in your onClickListener to open the  app in your mobile browser.
```kotlin 
val intent = Intent(Intent.ACTION_VIEW, botUrl)  
startActivity(intent)
```
