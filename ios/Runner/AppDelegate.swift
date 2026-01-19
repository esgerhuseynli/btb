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
    // Handle SIMA callback
    if url.scheme == "btbmobile" {
      return simaHandler?.handleUrlCallback(url: url) ?? false
    }
    
    return super.application(app, open: url, options: options)
  }
}
