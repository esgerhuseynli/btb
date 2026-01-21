import Flutter
import UIKit

@main
@objc class AppDelegate: FlutterAppDelegate {
  private var simaHandler: SimaHandler?
  
  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
    GeneratedPluginRegistrant.register(with: self)
    
    // Setup SIMA handler
    guard let controller = window?.rootViewController as? FlutterViewController else {
      return super.application(application, didFinishLaunchingWithOptions: launchOptions)
    }
    
    simaHandler = SimaHandler()
    simaHandler?.setupMethodChannel(binaryMessenger: controller.binaryMessenger)
    
    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
  
  override func application(
    _ app: UIApplication,
    open url: URL,
    options: [UIApplication.OpenURLOptionsKey: Any] = [:]
  ) -> Bool {
    // Debug logging
    print("=== AppDelegate: URL callback received ===")
    print("URL scheme: \(url.scheme ?? "nil")")
    print("URL: \(url.absoluteString)")
    print("App state: \(app.applicationState.rawValue)") // 0=active, 1=inactive, 2=background
    
    // Handle SIMA callback URLs (btbmobile://)
    // We use native SimaHandler method channel on iOS instead of sima package
    // because the sima package has a bug where it doesn't complete the Future on iOS
    if url.scheme == "btbmobile" {
      print("Handling SIMA callback with native SimaHandler")
      let handled = simaHandler?.handleUrlCallback(url: url) ?? false
      print("Handled by SimaHandler: \(handled)")
      print("=== AppDelegate: URL callback handling complete ===")
      return handled
    }
    
    // For other URLs, let the parent handle it (for sima package or other handlers)
    let handledByParent = super.application(app, open: url, options: options)
    print("Handled by parent: \(handledByParent)")
    print("=== AppDelegate: URL callback handling complete ===")
    return handledByParent
  }
}
