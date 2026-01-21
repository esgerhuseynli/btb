#!/bin/bash

# iOS Build Fix Script
# This script fixes the "Command PhaseScriptExecution failed with a nonzero exit code" error

set -e

echo "🧹 Cleaning Flutter build..."
flutter clean

echo "📦 Getting Flutter dependencies..."
flutter pub get

echo "🧹 Cleaning iOS build artifacts..."
cd ios
rm -rf Pods
rm -rf Podfile.lock
rm -rf .symlinks
rm -rf Flutter/Flutter.framework
rm -rf Flutter/Flutter.podspec

echo "📦 Reinstalling CocoaPods dependencies..."
pod deintegrate || true
pod install --repo-update

echo "✅ Done! Now try building again with: flutter build ios"
echo "   Or open ios/Runner.xcworkspace in Xcode and build from there."
