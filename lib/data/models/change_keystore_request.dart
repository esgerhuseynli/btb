import 'package:json_annotation/json_annotation.dart';
import 'request_info.dart';
import 'mobile_device_specifications.dart';

part 'change_keystore_request.g.dart';

@JsonSerializable(explicitToJson: true)
class ChangeKeystoreRequest {
  @JsonKey(name: 'RequestInfo')
  final RequestInfo requestInfo;

  @JsonKey(name: 'KeystoreType')
  final int keystoreType;

  @JsonKey(name: 'MobileDeviceSpecifications')
  final MobileDeviceSpecifications mobileDeviceSpecifications;

  ChangeKeystoreRequest({
    required this.requestInfo,
    required this.keystoreType,
    required this.mobileDeviceSpecifications,
  });

  factory ChangeKeystoreRequest.fromJson(Map<String, dynamic> json) =>
      _$ChangeKeystoreRequestFromJson(json);

  Map<String, dynamic> toJson() => _$ChangeKeystoreRequestToJson(this);
}









