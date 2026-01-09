import 'package:json_annotation/json_annotation.dart';
import 'mobile_user.dart';
import 'device_info.dart';
import 'app_info.dart';

part 'request_info.g.dart';

@JsonSerializable(explicitToJson: true)
class RequestInfo {
  @JsonKey(name: 'MobileUser')
  final MobileUser? mobileUser;

  @JsonKey(name: 'DeviceInfo')
  final DeviceInfo deviceInfo;

  @JsonKey(name: 'AppInfo')
  final AppInfo appInfo;

  @JsonKey(name: 'Language')
  final int language;

  RequestInfo({
    this.mobileUser,
    required this.deviceInfo,
    required this.appInfo,
    required this.language,
  });

  factory RequestInfo.fromJson(Map<String, dynamic> json) =>
      _$RequestInfoFromJson(json);

  Map<String, dynamic> toJson() => _$RequestInfoToJson(this);
}



