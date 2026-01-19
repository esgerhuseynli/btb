import 'package:json_annotation/json_annotation.dart';
import 'request_info.dart';

part 'bank_accounts_list_request.g.dart';

@JsonSerializable(explicitToJson: true, includeIfNull: false)
class BankAccountsListRequest {
  @JsonKey(name: 'requestInfo')
  final RequestInfo requestInfo;

  @JsonKey(name: 'requestParametersValidationMessage', includeIfNull: false)
  final String? requestParametersValidationMessage;

  @JsonKey(name: 'requestParametersValidated', includeIfNull: false)
  final bool? requestParametersValidated;

  BankAccountsListRequest({
    required this.requestInfo,
    this.requestParametersValidationMessage,
    this.requestParametersValidated,
  });

  factory BankAccountsListRequest.fromJson(Map<String, dynamic> json) =>
      _$BankAccountsListRequestFromJson(json);

  Map<String, dynamic> toJson() => _$BankAccountsListRequestToJson(this);
}
