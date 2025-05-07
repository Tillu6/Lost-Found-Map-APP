<p align="center">
  <img src="https://github.com/Tillu6/sit708_Task_7.1P/blob/main/app/src/main/ic_launcher-playstore.png"
       alt="Lost & Found App Icon" width="150"/>
</p>

# 🚀 Lost & Found APP

[![Android CI](https://img.shields.io/badge/Android-16.0%2B-green.svg)](https://developer.android.com/)
<img src="https://img.shields.io/badge/SQLite-Embedded-blue.svg" alt="SQLite">

**Lost & Found APP** helps you post and discover lost or found items with ease—online or offline. Built for the SIT708 course, it features Material Design, notch‑safe layouts, and a lightweight SQLite backend.

---

## ✨ Features

* **Post Adverts**: Easily mark items as “Lost” or “Found” and provide essential details like Name, Phone, Description, Date, and Location.
* **Browse Listings**: View all active advertisements in a clean and organized RecyclerView list.
* **View & Manage Items**: Tap on any item to see its detailed information and mark it as recovered when the owner is found.
* **Efficient Bulk Delete**: Quickly clear all entries from the app's overflow menu for easy data management.
* **Completely Offline**: All data is stored locally using SQLite, ensuring full functionality even without an internet connection.

---

## 🛠️ Installation & Setup

1.  **Clone the Repository**:
    ```bash
    git clone https://github.com/Tillu6/sit708_Task_7.1.git
    cd LostFoundAPP
    ```

2.  **Open in Android Studio**:
    * Ensure your project's `minSdkVersion` is set to **16** or higher and `compileSdkVersion` is **36**.
    * Allow Gradle to sync the project dependencies.
    * **Run** the application on your preferred Android emulator or physical device.

3.  **Ready to Go!**
    The app is now installed and ready to help you find or reunite with your lost items. No further configuration is required.

---

## 🔮 Roadmap: Future Enhancements

We're excited about the future of **Lost & Found APP** and plan to implement the following features:

* **🗺️ Map Integration**: Display geo-tagged "Found" posts on a Google Maps interface, allowing users to easily locate nearby items.
* **🔔 Smart Notifications**: Implement a subscription system where users can follow categories and receive push notifications for relevant lost or found items in their vicinity.
* **🖼️ AI-Powered Matching**: Integrate on-device machine learning (TensorFlow Lite) to enable photo-based item matching, suggesting potential matches based on uploaded images.
* **☁️ Cloud Synchronization**: Utilize Firebase Firestore for secure cloud data storage, enabling multi-device access and a potential web dashboard for enhanced community support.

---

## 📂 Project Structure

```
app/
├── java/com/example/lostfoundapp/
│   ├── MainActivity.java
│   ├── AddItemActivity.java
│   ├── ShowItemsActivity.java
│   ├── ItemDetailActivity.java
│   └── LostFoundDBHelper.java
└── res/
    ├── layout/
    ├── drawable/
    └── values/
```

---

## 🙏 Acknowledgements

A heartfelt **thank you** to the **SIT708** course for this engaging and practical assignment. Your guidance and support have been instrumental in bringing this application to life!

---

<p align="center">
Made with ❤️ and ☕ by Saketh Reddy Poreddy.
</p>
