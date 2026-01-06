import 'package:json_annotation/json_annotation.dart';

part 'mobile_device_specifications.g.dart';

@JsonSerializable()
class MobileDeviceSpecifications {
  @JsonKey(name: 'NFC')
  final String nfc;

  @JsonKey(name: 'FaceID')
  final String faceID;

  @JsonKey(name: 'TouchID')
  final String touchID;

  MobileDeviceSpecifications({
    required this.nfc,
    required this.faceID,
    required this.touchID,
  });

  factory MobileDeviceSpecifications.fromJson(Map<String, dynamic> json) =>
      _$MobileDeviceSpecificationsFromJson(json);

  Map<String, dynamic> toJson() => _$MobileDeviceSpecificationsToJson(this);
}








