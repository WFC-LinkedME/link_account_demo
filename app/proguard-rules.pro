# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-optimizationpasses 5 #表示proguard对代码进行迭代优化的次数，Android一般为5
-dontoptimize #关闭优化
-ignorewarnings
-verbose
-dontshrink #关闭压缩
#-dontwarn
-keepattributes Signature
-keepattributes EnclosingMethod
-keepattributes InnerClasses

#-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-keepparameternames

-keepattributes *Annotation*
-keepattributes Exceptions
-dontwarn android.support.**
-keep class android.support.** { *; }

# Preserve all View implementations, their special context constructors, and
# their setters.
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
    public void get*(...);
}

# Preserve all fundamental application classes.
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends java.lang.Throwable {*;}
-keep public class * extends java.lang.Exception {*;}

# 联通取号、认证混淆
 -dontwarn com.unicom.xiaowo.**
 -keep class com.unicom.xiaowo.**{*;}

# 移动混淆
 -dontwarn com.cmic.sso.sdk.**
 -keep class com.cmic.sso.sdk.**{*;}

# 电信混淆
 -dontwarn cn.com.chinatelecom.account.**
 -keep class cn.com.chinatelecom.account.**{*;}

# LinkAccount
 -dontwarn cc.lkme.linkaccount.**
 -keep class cc.lkme.linkaccount.**{*;}