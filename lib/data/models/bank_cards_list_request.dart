import 'package:json_annotation/json_annotation.dart';
import 'request_info.dart';

part 'bank_cards_list_request.g.dart';

@JsonSerializable(explicitToJson: true, includeIfNull: false)
class BankCardsListRequest {
  @JsonKey(name: 'requestInfo')
  final RequestInfo requestInfo;

  @JsonKey(name: 'requestParametersValidationMessage', includeIfNull: false)
  final String? requestParametersValidationMessage;

  @JsonKey(name: 'requestParametersValidated', includeIfNull: false)
  final bool? requestParametersValidated;

  BankCardsListRequest({
    required this.requestInfo,
    this.requestParametersValidationMessage,
    this.requestParametersValidated,
  });

  factory BankCardsListRequest.fromJson(Map<String, dynamic> json) =>
      _$BankCardsListRequestFromJson(json);

  Map<String, dynamic> toJson() => _$BankCardsListRequestToJson(this);
}
