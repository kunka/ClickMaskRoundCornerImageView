ClickMaskRoundCornerImageView
=============================

Implement android's ClickMaskImageView and a lightweight RoundCornerImageView


Use in xml:
```xml
<com.kk.imageview.ClickMaskRoundCornerImageView
    xmlns:attr="http://schemas.android.com/apk/res/com.kk.demo"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    attr:radiusXY="10dp" | attr:radiusX="10dp" | attr:radiusY="10dp" | attr:isRound="true"
    android:src="@drawable/pic1" />
 ```

normal:

![alt ](http://i.imgur.com/hvhcgTf.png?1)


clicked:

![alt ](http://i.imgur.com/psTAazB.png?1)


RatioImageView
=============================

RatioImageView:

![alt ](http://i.imgur.com/hiVqHm6.jpg?1)


Use in xml:
```xml
 <com.kk.imageview.RatioImageView
     xmlns:attr="http://schemas.android.com/apk/res/com.kk.demo"
     android:layout_width="fill_parent"
     android:layout_height="wrap_content"
     attr:aspectRatio="0.4" | attr:adjustWidth="true"
     android:src="@drawable/g500x200" />
 ```
