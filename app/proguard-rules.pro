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



-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*
#models
-keep class seller.in.mesway.response.**{*;}
-keep class seller.in.mesway.models.**{*;}


#metarial
-keep class com.google.android.material.**{*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**

#androidX
-dontwarn androidx.**
-keep class androidx.**{*;}
-keep interface androidx.**{*;}


#lottie
-dontwarn com.airnub.lottie.**
-keep class com.airbnb.lottie.**{*;}

#retrofit
-dontnote retrofit2.Platform

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}



#shimmer
-dontwarn com.facebook.shimmer.**
-keep class com.facebook.shimmer.**{*;}
-keep interface com.facebook.shimmer.** {*;}


#firebase
-dontwarn com.google.firebase.**
-keep class com.google.firebase.**{*;}
-keep interface com.google.firebase.**{*;}

-dontwarn com.google.code.**
-keep class com.google.code.**{*;}
-keep interface com.google.code.**{*;}
#aws
-dontwarn com.amazonaws.**
-keep class com.amazonaws.**{*;}
-keep interface com.amazonaws.**{*;}

-dontwarn com.github.dhaval2404.**
-keep class com.github.dhaval2404.**{*;}
-keep interface com.github.dhaval2404.**{*;}



