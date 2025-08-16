# Lost in Time - Android Game

A complete offline Android platformer game with time manipulation mechanics.

## Game Features

- **3 Complete Levels** with increasing difficulty
- **Time Powers**: Pause, Reverse, and Slow time
- **Smooth Controls**: Touch-based movement and jumping
- **Progressive Difficulty**: Each level introduces new mechanics
- **Local Save System**: Progress is automatically saved
- **Landscape Orientation**: Optimized for mobile gaming

## Time Powers

1. **Pause Time** (⚡): Freezes all moving objects and traps
2. **Reverse Time** (⚡): Rewinds the last 5 seconds of actions
3. **Slow Time** (⚡): Player moves normally while world slows down

## Level Design

- **Level 1**: Basic movement and platforming
- **Level 2**: Introduces Pause Time with moving traps
- **Level 3**: Combines all time powers with complex obstacles

## Controls

- **Left/Right Buttons**: Move character
- **Jump Button**: Jump
- **Time Power Button**: Cycle between time powers
- **Activate Power Button**: Use selected time power

## Technical Details

- **Language**: Java
- **Minimum SDK**: Android 5.0 (API 21)
- **Target SDK**: Android 13 (API 33)
- **Graphics**: Custom rendering with Canvas
- **Audio**: Placeholder system ready for custom sounds
- **Save System**: SharedPreferences for progress

## Building the APK

### Prerequisites

- Android Studio (latest version recommended)
- Java Development Kit (JDK) 8 or higher
- Android SDK

### Build Steps

1. **Open Project in Android Studio**
   ```
   File → Open → Navigate to LostInTime folder → Select
   ```

2. **Sync Project**
   - Wait for Gradle sync to complete
   - Resolve any dependency issues if they arise

3. **Build APK**
   ```
   Build → Build Bundle(s) / APK(s) → Build APK(s)
   ```

4. **Locate APK**
   - APK will be generated in: `app/build/outputs/apk/debug/`
   - File name: `app-debug.apk`

### Alternative: Command Line Build

```bash
cd LostInTime
./gradlew assembleDebug
```

## Installing on Device

### Method 1: Direct Installation

1. **Enable Unknown Sources**
   - Go to Settings → Security → Unknown Sources
   - Enable for your file manager or browser

2. **Transfer APK**
   - Copy `app-debug.apk` to your device
   - Use USB, cloud storage, or email

3. **Install APK**
   - Open the APK file on your device
   - Follow installation prompts

### Method 2: ADB Installation

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Method 3: Android Studio

1. Connect device via USB
2. Enable USB Debugging on device
3. Click "Run" button in Android Studio
4. Select your device and install

## Project Structure

```
LostInTime/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/lostintime/
│   │   │   ├── MainActivity.java          # Main menu
│   │   │   ├── GameActivity.java          # Game interface
│   │   │   ├── GameView.java              # Game rendering
│   │   │   ├── Player.java                # Player character
│   │   │   ├── GameObject.java            # Base game object
│   │   │   ├── Platform.java              # Static platforms
│   │   │   ├── MovingPlatform.java        # Moving platforms
│   │   │   ├── Trap.java                  # Static traps
│   │   │   ├── MovingTrap.java            # Moving traps
│   │   │   ├── Portal.java                # Level completion
│   │   │   ├── LevelManager.java          # Level loading
│   │   │   ├── GameState.java             # Game state
│   │   │   └── HoldTouchListener.java     # Touch handling
│   │   ├── res/
│   │   │   ├── layout/                     # UI layouts
│   │   │   ├── values/                     # Strings, colors, themes
│   │   │   ├── drawable/                   # Graphics
│   │   │   └── mipmap-*/                  # App icons
│   │   └── AndroidManifest.xml            # App configuration
│   ├── build.gradle                       # App build config
│   └── proguard-rules.pro                 # Code obfuscation
├── build.gradle                           # Project build config
├── settings.gradle                        # Project settings
├── gradle.properties                      # Gradle configuration
└── README.md                              # This file
```

## Customization

### Adding New Levels

1. Edit `LevelManager.java`
2. Add new level method (e.g., `loadLevel4`)
3. Update level count in `GameActivity.java`

### Adding Custom Graphics

1. Replace placeholder drawables in `res/drawable/`
2. Update color references in `res/values/colors.xml`
3. Modify drawing methods in game object classes

### Adding Audio

1. Place audio files in `res/raw/`
2. Implement audio playback in game classes
3. Add volume controls and settings

## Troubleshooting

### Build Errors

- **Gradle Sync Failed**: Check internet connection and SDK versions
- **Compilation Errors**: Ensure JDK 8+ is installed
- **Resource Errors**: Verify all XML files are properly formatted

### Runtime Issues

- **App Crashes**: Check logcat for error messages
- **Performance Issues**: Reduce target FPS in `GameView.java`
- **Touch Issues**: Verify touch listener implementations

### Device Compatibility

- **Installation Fails**: Check minimum SDK version
- **Graphics Issues**: Test on different screen densities
- **Orientation Problems**: Ensure landscape mode is enforced

## Future Enhancements

- [ ] Additional levels (4-10)
- [ ] Power-up collectibles
- [ ] Boss battles
- [ ] Multiplayer support
- [ ] Achievement system
- [ ] Custom level editor
- [ ] Particle effects
- [ ] Background music
- [ ] Sound effects
- [ ] Cloud save support

## License

This project is provided as-is for educational and development purposes.

## Support

For issues or questions:
1. Check the troubleshooting section above
2. Review Android Studio logs
3. Verify device compatibility
4. Test on different Android versions

---

**Happy Gaming!** 🎮⏰