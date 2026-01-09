import 'package:json_annotation/json_annotation.dart';

part 'device_info.g.dart';

@JsonSerializable()
class DeviceInfo {
  @JsonKey(name: 'DeviceID')
  final String deviceID;

  @JsonKey(name: 'Vendor')
  final String vendor;

  @JsonKey(name: 'Model')
  final String model;

  @JsonKey(name: 'OSName')
  final String osName;

  @JsonKey(name: 'OSVersion')
  final String osVersion;

  DeviceInfo({
    required this.deviceID,
    required this.vendor,
    required this.model,
    required this.osName,
    required this.osVersion,
  });

  factory DeviceInfo.fromJson(Map<String, dynamic> json) =>
      _$DeviceInfoFromJson(json);

  Map<String, dynamic> toJson() => _$DeviceInfoToJson(this);
}



