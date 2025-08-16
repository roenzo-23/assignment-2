# Quick Installation Guide

## Prerequisites

- Android device running Android 5.0 (API 21) or higher
- USB cable or method to transfer files to your device

## Method 1: Android Studio (Recommended for Developers)

1. **Install Android Studio**
   - Download from: https://developer.android.com/studio
   - Install and launch Android Studio

2. **Open Project**
   - File → Open → Select the `LostInTime` folder
   - Wait for Gradle sync to complete

3. **Build APK**
   - Build → Build Bundle(s) / APK(s) → Build APK(s)
   - Wait for build to complete

4. **Install on Device**
   - Connect your Android device via USB
   - Enable USB Debugging in Developer Options
   - Click the "Run" button (green play icon)
   - Select your device and install

## Method 2: Command Line Build

1. **Install Java JDK 8+**
   - Download from: https://adoptium.net/
   - Set JAVA_HOME environment variable

2. **Build APK**
   ```bash
   cd LostInTime
   ./build.sh
   ```

3. **Transfer and Install**
   - Copy `app/build/outputs/apk/debug/app-debug.apk` to your device
   - Enable "Unknown Sources" in Security settings
   - Open the APK file and install

## Method 3: Pre-built APK (If Available)

1. **Download APK**
   - Get the APK file from the releases section

2. **Install on Device**
   - Enable "Unknown Sources" in Security settings
   - Open the APK file and install

## Troubleshooting

### Build Issues
- Ensure Java 8+ is installed
- Check internet connection for Gradle dependencies
- Verify Android SDK is properly configured

### Installation Issues
- Check Android version compatibility (5.0+)
- Ensure "Unknown Sources" is enabled
- Try clearing app cache if re-installing

### Runtime Issues
- Force stop and restart the app
- Check device storage space
- Verify device has sufficient RAM

## Support

If you encounter issues:
1. Check the troubleshooting section above
2. Review the full README.md
3. Check Android Studio logs for errors
4. Verify device compatibility

---

**Enjoy playing Lost in Time!** 🎮