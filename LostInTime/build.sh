#!/bin/bash

echo "Building Lost in Time Android Game..."
echo "====================================="

# Check if we're in the right directory
if [ ! -f "build.gradle" ]; then
    echo "Error: Please run this script from the LostInTime directory"
    exit 1
fi

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    echo "Please install Java Development Kit (JDK) 8 or higher"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 8 ]; then
    echo "Error: Java 8 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

echo "Java version: $(java -version 2>&1 | head -n 1)"
echo ""

# Try to build using gradle wrapper
if [ -f "gradlew" ]; then
    echo "Using Gradle wrapper..."
    chmod +x gradlew
    ./gradlew assembleDebug
else
    echo "Gradle wrapper not found. Please use Android Studio to build the project."
    echo ""
    echo "Alternative build methods:"
    echo "1. Open the project in Android Studio"
    echo "2. Wait for Gradle sync to complete"
    echo "3. Build → Build Bundle(s) / APK(s) → Build APK(s)"
    echo ""
    echo "The APK will be generated in: app/build/outputs/apk/debug/app-debug.apk"
    exit 1
fi

echo ""
echo "Build completed!"
echo "Check app/build/outputs/apk/debug/ for the APK file"