Firebase Kotlin Example
-----------------
Google Login, Database, Storage

Version
--------
1. Android studio build:gradle:3.3.1
2. firebase-core:16.0.7; firebase-auth:16.1.0'; firebase-database:16.0.6'; firebase-storage:16.0.5'
3. org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.21

Before Start
------------
1. Sign In Firebase (https://firebase.google.com/)
2. Make New Project and add package name (You can find app/manifests/AndroidManifest.xml/package="com.example.rhkdg.sharethetrip")
3. Download 'google-services.json' and move to FirebaseLogin-DatabaseExample/app/)
4. Sync firebase json file (Android Studio Tools -> Firebase -> Authentication -> Email and password authentication -> Click 'Connect to Firebase' -> Click 'Sync'
5. Go to your project in Firebase -> Click 'authentication' -> Click 'providers' and activate Google login
6. Go to database(realtime database) -> Click 'rules' -> Change like this (".read": "true", ".write": "true") -> Save
7. Go to storage -> Click 'rules' -> Change like this (allow read, write: if request.auth != null;) -> Save
tip) When you upload post in this app, You should select image if not upload image it will make error!

Screenshots
-----------

<div>
  <img width="150" src="https://user-images.githubusercontent.com/45925992/53954615-180d7100-411a-11e9-9e50-7a94fb5f4328.png">
  <img width="150" src="https://user-images.githubusercontent.com/45925992/53954634-25c2f680-411a-11e9-92e0-f0c4c4fba5ac.png">
  <img width="150" src="https://user-images.githubusercontent.com/45925992/53954638-29567d80-411a-11e9-9930-2dadcf0a5787.png">
  <img width="150" src="https://user-images.githubusercontent.com/45925992/53954649-2e1b3180-411a-11e9-9333-b8b01facef5c.png">
  <img width="150" src="https://user-images.githubusercontent.com/45925992/53954659-32dfe580-411a-11e9-8c85-bb0992649250.png">
  <img width="150" src="https://user-images.githubusercontent.com/45925992/53954662-35dad600-411a-11e9-8ccf-a1a2147bcfe3.png">
</div>
