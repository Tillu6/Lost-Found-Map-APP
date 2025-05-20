<p align="center">
  <img src="https://github.com/Tillu6/Lost-Found-Map-APP/blob/main/app/src/main/ic_launcher-playstore.png"
       alt="Lost & Found App Icon" width="150"/>
</p>

# 🚀 Lost & Found Map App

[![Android SDK 28+](https://img.shields.io/badge/Android-SDK%2028%2B-green.svg)](https://developer.android.com/)  
[![SQLite](https://img.shields.io/badge/SQLite-Embedded-blue.svg)](https://www.sqlite.org/index.html)  
[![Maps SDK](https://img.shields.io/badge/Google%20Maps-SDK%20Installed-brightgreen.svg)](https://developers.google.com/maps/documentation/android-sdk)

**Lost & Found Map App** lets you post, browse and geo-locate lost or found items—even offline. Built for the SIT708 course, it combines Material Design, a lightweight SQLite backend and full Google Maps integration.

---

## ✨ Core Features

- **Post Adverts**  
  Mark an item as **Lost** or **Found**, enter your Name, Phone, Description, Date and Location (typed or “Get Current Location”).  
- **Browse Listings**  
  Scroll through all active adverts in a clean, responsive RecyclerView.  
- **Detail & Remove**  
  Tap any listing to see full details; mark it “recovered” to delete from your local database.  
- **Map View**  
  View all your lost & found pins on a Google Map, auto-zooming to show every marker.  
- **Offline-First**  
  All data lives locally in SQLite—no network needed.  
- **Bulk Clear**  
  Use the overflow menu to delete all entries with a single tap.  

---

## 🛠️ Installation & Setup

1. **Clone the Repository**  
   ```bash
   git clone https://github.com/Tillu6/Lost-Found-Map-APP.git
   cd Lost-Found-Map-APP

---
2. **Acquire a Google Maps API Key**

   * Go to the [Google Cloud Console](https://console.cloud.google.com/).
   * Enable **Maps SDK for Android** and **Places API**.
   * Create an **API key** and restrict it to your app’s package name & SHA-1.
   * In **`app/src/main/res/values/strings.xml`**, replace

     ```xml
     <string name="google_maps_key">YOUR_GOOGLE_MAPS_API_KEY_HERE</string>
     ```

3. **Open in Android Studio**

   * Ensure `minSdkVersion 28`, `compileSdkVersion 35+`.
   * Gradle will sync and download these core deps:

     ```groovy
     implementation "com.google.android.gms:play-services-maps:18.1.0"
     implementation "com.google.android.libraries.places:places:2.6.0"
     implementation "com.google.android.gms:play-services-location:21.0.1"
     implementation "androidx.recyclerview:recyclerview:1.3.0"
     implementation "androidx.constraintlayout:constraintlayout:2.1.4"
     ```
   * Run on an emulator or physical device.

4. **Done!**
   The app will launch and you can start posting and locating items on the map right away.

---

## 📂 Project Structure

```
app/
├── manifests/
│   └── AndroidManifest.xml       ← permissions, map-API meta-data
├── java/com/example/lostfoundapp/
│   ├── MainActivity.java         ← home screen (Create | List | Map)
│   ├── AddItemActivity.java      ← post “Lost”/“Found” adverts
│   ├── ShowItemsActivity.java    ← RecyclerView of all items
│   ├── ItemDetailActivity.java   ← detail view + remove button
│   ├── ShowMapActivity.java      ← Google Map with all markers
│   ├── ItemAdapter.java          ← RecyclerView adapter
│   ├── LostFoundDBHelper.java    ← SQLite helper (CRUD)
│   └── LostFoundItem.java        ← data model
└── res/
    ├── layout/
    │   ├── activity_main.xml
    │   ├── activity_add_item.xml
    │   ├── activity_show_items.xml
    │   ├── activity_item_detail.xml
    │   ├── activity_show_map.xml
    │   └── item_row.xml
    ├── values/
    │   ├── strings.xml
    │   └── styles.xml
    └── drawable/ …  

```

---

## 🔮 Roadmap & Future Enhancements

* **🔔 Push Notifications**
  Notify subscribed users when a new item is posted in their area or category.
* **🖼️ Image Upload & Matching**
  Allow users to attach photos and leverage on-device ML (TensorFlow Lite) for visual matching.
* **☁️ Cloud Sync & Auth**
  Add Firebase/Auth and Firestore so data can sync across devices and users.
* **📍 Geofencing**
  Alert users when they enter zones where a lost item has been reported.

---

## 🙏 Acknowledgements

Big thanks to the **SIT708** teaching team for guiding this hands-on assignment, and to **Google Maps Platform** for their free tier API.

<p align="center">
Made with ❤️ and ☕ by Saketh Reddy Poreddy.
</p>
