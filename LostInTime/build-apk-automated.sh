#!/bin/bash

echo "=========================================="
echo "🚀 AUTOMATED APK BUILD - Lost in Time"
echo "=========================================="
echo ""

# Check if we're in the right directory
if [ ! -f "build.gradle" ]; then
    echo "❌ Error: Please run this script from the LostInTime directory"
    exit 1
fi

echo "✅ Project found, starting automated build..."
echo ""

# Create build directories
echo "📁 Creating build directories..."
mkdir -p app/build/outputs/apk/debug
mkdir -p app/build/intermediates
mkdir -p app/build/generated

# Check Java
echo "🔍 Checking Java..."
if ! command -v java &> /dev/null; then
    echo "❌ Java not found. Please install Java 8+ first."
    exit 1
fi

echo "✅ Java found: $(java -version 2>&1 | head -n 1)"
echo ""

# Try to use gradle wrapper
echo "🔨 Attempting to build APK..."
if [ -f "gradlew" ]; then
    echo "Using Gradle wrapper..."
    chmod +x gradlew
    
    # Try to build
    if ./gradlew assembleDebug --no-daemon --stacktrace; then
        echo ""
        echo "🎉 SUCCESS! APK built successfully!"
        echo ""
        echo "📱 APK Location:"
        echo "   $(pwd)/app/build/outputs/apk/debug/app-debug.apk"
        echo ""
        echo "📊 APK Details:"
        if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
            ls -lh "app/build/outputs/apk/debug/app-debug.apk"
            echo ""
            echo "✅ APK is ready for installation!"
            echo "📱 Copy this file to your Android device and install it."
        else
            echo "❌ APK file not found in expected location"
        fi
    else
        echo ""
        echo "⚠️  Gradle build failed. Trying alternative method..."
        echo ""
        createSimpleAPK
    fi
else
    echo "❌ Gradle wrapper not found. Creating simple APK..."
    createSimpleAPK
fi

echo ""
echo "=========================================="
echo "Build process completed!"
echo "=========================================="