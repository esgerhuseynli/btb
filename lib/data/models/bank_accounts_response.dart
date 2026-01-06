import 'package:json_annotation/json_annotation.dart';
import 'response_info.dart';
import 'bank_account.dart';

part 'bank_accounts_response.g.dart';

@JsonSerializable()
class BankAccountsResponse {
  @JsonKey(name: 'responceInfo')
  final ResponseInfo responseInfo;

  @JsonKey(name: 'bankAccounts')
  final List<BankAccount>? bankAccounts;

  BankAccountsResponse({
    required this.responseInfo,
    this.bankAccounts,
  });

  factory BankAccountsResponse.fromJson(Map<String, dynamic> json) =>
      _$BankAccountsResponseFromJson(json);

  Map<String, dynamic> toJson() => _$BankAccountsResponseToJson(this);
}

