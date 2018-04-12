#Trying to create widget for my project, if it will go well - description will be extended
 
 
minSdkVersion - 15

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
```groovy
dependencies {
    compile 'com.github.pokrovskyi-dev:Water-progress-view:v0.0.10'
}
```

[v0.0.10 Javadoc](https://jitpack.io/com/github/sergey-pokrovskyi/Water-progress-view/v0.0.10/javadoc/)

```groovy
<com.mobismarthealth.library.WaterProgressView
        android:id="@+id/waterProgressView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="32dp"
        app:wpv_main_arc_enable="true"
        app:wpv_main_arc_progress="90"
        app:wpv_main_arc_start_angle="270"
        app:wpv_main_arc_width="30"
        app:wpv_main_arc_color="@color/colorPrimary"
        app:wpv_main_arc_bg_enable="true"
        app:wpv_main_arc_bg_width="15"
        app:wpv_main_arc_bg_color="@color/colorPrimaryDark"
        app:wpv_center_text_enable="true"
        app:wpv_center_text_size="18sp"
        app:wpv_center_text_color="#000000"
        app:wpv_wave_enable="true"
        app:wpv_wave_color="#000000"/>
```


#methods:
```groovy
setMainArcEnable(boolean)
setMainArcProgress(int)
setMainArcStartAngle(int)
setMainArcWidth(float)
setMainArcColor(int)
setMainArcBgEnable(boolean)
setMainArcBgWidth(float)
setMainArcBgColor(int)
getCenterTextEnable(boolean)
setCenterTextSize(sp)
setCenterTextColor(int)
```