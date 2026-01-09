import 'package:json_annotation/json_annotation.dart';
import 'response_info.dart';
import 'bank_card.dart';

part 'bank_cards_response.g.dart';

@JsonSerializable()
class BankCardsResponse {
  @JsonKey(name: 'responceInfo')
  final ResponseInfo responseInfo;

  @JsonKey(name: 'bankCards')
  final List<BankCard>? bankCards;

  BankCardsResponse({
    required this.responseInfo,
    this.bankCards,
  });

  factory BankCardsResponse.fromJson(Map<String, dynamic> json) =>
      _$BankCardsResponseFromJson(json);

  Map<String, dynamic> toJson() => _$BankCardsResponseToJson(this);
}

